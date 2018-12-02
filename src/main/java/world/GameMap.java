package world;

import collisiondetection.shapes.Vector;
import gameobjects.GameObject;

import java.util.List;

public class GameMap {
    private final List<GameObject> worldObjects;
    private final List<Vector> playerSpawns;
    private final List<Vector> powerUpSpawns;

    public GameMap(List<GameObject> worldObjects, List<collisiondetection.shapes.Vector> playerSpawns, List<Vector> powerUpSpawns) {
        this.worldObjects = worldObjects;
        this.playerSpawns = playerSpawns;
        this.powerUpSpawns = powerUpSpawns;
    }

    public List<GameObject> getWorldObjects() {
        return worldObjects;
    }

    public List<Vector> getPlayerSpawns() {
        return playerSpawns;
    }

    public List<Vector> getPowerUpSpawns() {
        return powerUpSpawns;
    }
}
