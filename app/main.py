from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routers import auth, users, discovery, connections, websockets
from app.database import Base, engine
# Import models to ensure they are registered with Base.metadata
from app.models.user import User
from app.models.connection import Connection

app = FastAPI(title="TapConnect API", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(auth.router)
app.include_router(users.router)
app.include_router(discovery.router)
app.include_router(connections.router)
app.include_router(websockets.router)

@app.on_event("startup")
async def startup_event():
    # Automatically create tables on startup if they don't exist
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)

@app.get("/")
async def root():
    return {"message": "TapConnect Backend is running! Phase 6 (Real-Time) active."}
