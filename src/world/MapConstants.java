package world;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Colour;
import gameobjects.GameObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapConstants {
    private static final double MAP_WIDTH_1 = 3850;
    private static final int MAP_HEIGHT_1 = 2150;
    private static final Colour RED = new Colour(255, 0, 0);
    private static final Colour GREY = new Colour(100, 100, 100);
    private static List<GameObject> flooredMap = new ArrayList<>(
            Arrays.asList(
                    new GameObject("left_wall", new Shape(MapShapes.WALL_SHAPE_1), new Vector(0, 1000), 0, 0, false, GREY, GREY),
                    new GameObject("right_wall", new Shape(MapShapes.WALL_SHAPE_1), new Vector(MAP_WIDTH_1, 1000), 0, 0, false, GREY, GREY),
                    new GameObject("top_wall", new Shape(MapShapes.WALL_SHAPE_2), new Vector(MAP_WIDTH_1 / 2.0, 0), 0, 0, false, GREY, GREY),
                    new GameObject("bottom_wall", new Shape(MapShapes.WALL_SHAPE_2), new Vector(MAP_WIDTH_1 / 2.0, MapConstants.MAP_HEIGHT_1), 0, 0, false, GREY, GREY),
                    new GameObject("", new Shape(MapShapes.WEDGE), new Vector(MAP_WIDTH_1 / 2.0, 2050), 0, 0, false, GREY, GREY),
                    new GameObject("", new Shape(MapShapes.WEDGE), new Vector(3 * MAP_WIDTH_1 / 4.0, 2050), 0, 0, false, GREY, GREY),
                    new GameObject("", new Shape(MapShapes.WEDGE), new Vector(MAP_WIDTH_1 / 4.0, 2050), 0, 0, false, GREY, GREY),
                    new GameObject("", new Shape(MapShapes.CORNER_WEDGE_1), new Vector(MAP_WIDTH_1 / 39.0, 2050), 0, 0, false, GREY, GREY),
                    new GameObject("", new Shape(MapShapes.CORNER_WEDGE_2), new Vector(MAP_WIDTH_1 * 38 / 39.0, 2050), 0, 0, false, GREY, GREY)
            )
    );

    private static List<GameObject> obstacleLayout1 = new ArrayList<>(
            Arrays.asList(
                    new GameObject("object1", new Shape(MapShapes.SQUARE), new Vector(MAP_WIDTH_1/2, MAP_HEIGHT_1/2), 0, 0, false, GREY, GREY)
//                    new Game
            )
    );

    private static List<GameObject> bottomlessMap = new ArrayList<>(
            Arrays.asList(
                    new GameObject("left_wall", new Shape(MapShapes.WALL_SHAPE_1), new Vector(0, 1000), 0, 0, false, GREY, GREY),
                    new GameObject("right_wall", new Shape(MapShapes.WALL_SHAPE_1), new Vector(MAP_WIDTH_1, 1000), 0, 0, false, GREY, GREY),
                    new GameObject("top_wall", new Shape(MapShapes.WALL_SHAPE_2), new Vector(MAP_WIDTH_1 / 2.0, 0), 0, 0, false, GREY, GREY)
            )
    );
    //

    public static GameMap gameMap1 = new GameMap(flooredMap,
            new ArrayList<>(Arrays.asList(new Vector(MAP_WIDTH_1 / 4, MAP_HEIGHT_1 / 2f), new Vector(MAP_WIDTH_1 * 3 / 4, MAP_HEIGHT_1 / 2f))),
            new ArrayList<>());

    public static GameMap gameMap2 = new GameMap(bottomlessMap,
            new ArrayList<>(Arrays.asList(new Vector(MAP_WIDTH_1 / 4, MAP_HEIGHT_1 / 2f), new Vector(MAP_WIDTH_1 * 3 / 4, MAP_HEIGHT_1 / 2f))),
            new ArrayList<>());

    static final HashMap<String, List<GameObject>> obstacleLayouts = new HashMap<>();
    static {
        obstacleLayouts.put("map1", obstacleLayout1);
    }

    public static GameMap getLayout(boolean floored, String mapName) {
        List<GameObject> gameObjects = obstacleLayouts.get(mapName);
        GameMap map;
        if(floored) {
            map = gameMap1;
        } else {
            map = gameMap2;
        }

        List<GameObject> objects = map.getWorldObjects();
        objects.addAll(gameObjects);
        return map;
    }
}
