from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select, or_, and_
from typing import List
from app.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.connection import Connection, ConnectionStatus, ConnectionType
from app.schemas.connection import ConnectionRequest, ConnectionResponse, ConnectionAction

router = APIRouter(prefix="/connections", tags=["connections"])

@router.post("/request", response_model=ConnectionResponse, status_code=status.HTTP_201_CREATED)
async def send_connection_request(
    body: ConnectionRequest,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """Send a connection request to another user."""
    if body.peer_id == current_user.user_id:
        raise HTTPException(status_code=400, detail="You cannot connect with yourself")

    # Check if user exists
    result = await db.execute(select(User).where(User.user_id == body.peer_id))
    if not result.scalar_one_or_none():
        raise HTTPException(status_code=404, detail="Peer user not found")

    # Check if existing connection
    result = await db.execute(
        select(Connection).where(
            or_(
                and_(Connection.user_id == current_user.user_id, Connection.peer_id == body.peer_id),
                and_(Connection.user_id == body.peer_id, Connection.peer_id == current_user.user_id)
            )
        )
    )
    existing = result.scalar_one_or_none()
    if existing:
        return existing

    new_conn = Connection(
        user_id=current_user.user_id,
        peer_id=body.peer_id,
        status=ConnectionStatus.PENDING,
        connection_type=body.connection_type
    )
    db.add(new_conn)
    await db.commit()
    await db.refresh(new_conn)
    return new_conn

@router.post("/accept", response_model=ConnectionResponse)
async def accept_connection(
    body: ConnectionAction,
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """Accept a pending connection request."""
    result = await db.execute(
        select(Connection).where(
            Connection.connection_id == body.connection_id,
            Connection.peer_id == current_user.user_id,
            Connection.status == ConnectionStatus.PENDING
        )
    )
    conn = result.scalar_one_or_none()
    if not conn:
        raise HTTPException(status_code=404, detail="Pending connection request not found")

    conn.status = ConnectionStatus.ACCEPTED
    await db.commit()
    await db.refresh(conn)
    return conn

@router.get("/", response_model=List[ConnectionResponse])
async def list_connections(
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """List all connections (friends) for the current user."""
    result = await db.execute(
        select(Connection).where(
            or_(Connection.user_id == current_user.user_id, Connection.peer_id == current_user.user_id),
            Connection.status == ConnectionStatus.ACCEPTED
        )
    )
    return result.scalars().all()

@router.get("/pending", response_model=List[ConnectionResponse])
async def list_pending_requests(
    current_user: User = Depends(get_current_user),
    db: AsyncSession = Depends(get_db)
):
    """List all incoming pending connection requests."""
    result = await db.execute(
        select(Connection).where(
            Connection.peer_id == current_user.user_id,
            Connection.status == ConnectionStatus.PENDING
        )
    )
    return result.scalars().all()
