package world;

import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import drawing.Colour;
import gameobjects.DestroyerBeam;
import gameobjects.GameObject;

import java.util.*;

public class Maps {
    public static final String FLOOR_1_NAME = "Floored";
    public static final String FLOOR_2_NAME = "Bottomless";
    public static final String FLOOR_3_NAME = "Bottomless/Topless";
    public static final String FLOOR_4_NAME = "?!?!?!";
    private static final double MAP_WIDTH_1 = 3850;
    private static final int MAP_HEIGHT_1 = 2150;
    private static final Colour RED = new Colour(255, 0, 0);
    private static final Colour GREY = new Colour(100, 100, 100);
    private static final List<GameObject> flooredMap = new ArrayList<>(
            Arrays.asList(
                    new GameObject("left_wall", new Shape(MapComponentVertices.WALL_SHAPE_1), new Vector(0, 1000), 0, 0, GREY, GREY),
                    new GameObject("right_wall", new Shape(MapComponentVertices.WALL_SHAPE_1), new Vector(MAP_WIDTH_1, 1000), 0, 0, GREY, GREY),
                    new GameObject("top_wall", new Shape(MapComponentVertices.WALL_SHAPE_2), new Vector(MAP_WIDTH_1 / 2.0, 0), 0, 0, GREY, GREY),
                    new GameObject("bottom_wall", new Shape(MapComponentVertices.WALL_SHAPE_2), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1), 0, 0, GREY, GREY),
                    new GameObject("", new Shape(MapComponentVertices.OCTAGON), new Vector(MAP_WIDTH_1 / 2.0, 2060), 0, 0, GREY, GREY),
                    new GameObject("", new Shape(MapComponentVertices.OCTAGON), new Vector(3 * MAP_WIDTH_1 / 4.0, 2060), 0, 0, GREY, GREY),
                    new GameObject("", new Shape(MapComponentVertices.OCTAGON), new Vector(MAP_WIDTH_1 / 4.0, 2060), 0, 0, GREY, GREY),
                    new GameObject("", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 4)), new Vector(MAP_WIDTH_1 / 39.0, 2100), 0, 0, GREY, GREY),
                    new GameObject("", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 4)), new Vector(MAP_WIDTH_1 * 38 / 39.0, 2100), 0, 0, GREY, GREY)
            )
    );

    private static final List<GameObject> obstacleLayout1 = new ArrayList<>(
            Arrays.asList(
                    new GameObject("object1", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 8)),
                            new Vector(MAP_WIDTH_1 / 2f, MAP_HEIGHT_1 / 2f), 0, 0, GREY, GREY),
                    new GameObject("object2", new Shape(MapComponentVertices.scale(MapComponentVertices.SQUARE, 4)),
                            new Vector(MAP_WIDTH_1 / 4f, MAP_HEIGHT_1 / 2f), 0, 0, GREY, GREY),
                    new GameObject("object3", new Shape(MapComponentVertices.scale(MapComponentVertices.SQUARE, 4)),
                            new Vector(MAP_WIDTH_1 * 3 / 4f, MAP_HEIGHT_1 / 2f), 0, 0, GREY, GREY),
                    new GameObject("object4", new Shape(MapComponentVertices.scale(MapComponentVertices.SQUARE, 4)),
                            new Vector(MAP_WIDTH_1 * 1 / 6f, MAP_HEIGHT_1 * 4 / 6f), 0, 0, GREY, GREY),
                    new GameObject("object5", new Shape(MapComponentVertices.scale(MapComponentVertices.SQUARE, 4)),
                            new Vector(MAP_WIDTH_1 * 5 / 6f, MAP_HEIGHT_1 * 4 / 6f), 0, 0, GREY, GREY),
                    new GameObject("object6", new Shape(MapComponentVertices.scale(MapComponentVertices.SQUARE, 4)),
                            new Vector(MAP_WIDTH_1 * 1 / 6f, MAP_HEIGHT_1 * 2 / 6f), 0, 0, GREY, GREY),
                    new GameObject("object7", new Shape(MapComponentVertices.scale(MapComponentVertices.SQUARE, 4)),
                            new Vector(MAP_WIDTH_1 * 5 / 6f, MAP_HEIGHT_1 * 2 / 6f), 0, 0, GREY, GREY)
            )
    );

    private static final List<GameObject> obstacleLayout2 = new ArrayList<>(
            Arrays.asList(
                    new GameObject("object1", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 4)),
                            new Vector(MAP_WIDTH_1 / 4f, MAP_HEIGHT_1 / 4f), 0, 0, GREY, GREY),
                    new GameObject("object2", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 4)),
                            new Vector(MAP_WIDTH_1 * 3 / 4f, MAP_HEIGHT_1 * 3 / 4f), 0, 0, GREY, GREY),
                    new GameObject("object3", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 4)),
                            new Vector(MAP_WIDTH_1 / 4f, MAP_HEIGHT_1 * 3 / 4f), 0, 0, GREY, GREY),
                    new GameObject("object4", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 4)),
                            new Vector(MAP_WIDTH_1 * 3 / 4f, MAP_HEIGHT_1 / 4f), 0, 0, GREY, GREY),
                    new GameObject("object5", new Shape(MapComponentVertices.scale(MapComponentVertices.RECTANGLE, 4)),
                            new Vector(MAP_WIDTH_1 / 2f, MAP_HEIGHT_1 / 2f), 0, 0, GREY, GREY)

            )
    );

    private static final List<GameObject> obstacleLayout4 = new ArrayList<>(
            Arrays.asList(
                    new GameObject("object1", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 4)),
                            new Vector(MAP_WIDTH_1 * 3 / 20f, MAP_HEIGHT_1 / 4f), 0, 0, GREY, GREY),
                    new GameObject("object2", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 2)),
                            new Vector(MAP_WIDTH_1 * 6 / 20f, MAP_HEIGHT_1 * 14 / 30f), 0, 0, GREY, GREY),
                    new GameObject("object3", new Shape(MapComponentVertices.scale(MapComponentVertices.RECTANGLE, 5)),
                            new Vector(MAP_WIDTH_1 * 15 / 20f, MAP_HEIGHT_1 * 26 / 30f), 0, 0, GREY, GREY),
                    new GameObject("object3", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 5)),
                            new Vector(MAP_WIDTH_1 * 15 / 20f, MAP_HEIGHT_1 * 49 / 50f), 0, 0, GREY, GREY),
                    new GameObject("object4", new Shape(MapComponentVertices.scale(MapComponentVertices.SQUARE, 5)),
                            new Vector(MAP_WIDTH_1 * 14 / 20f, MAP_HEIGHT_1 * 5 / 30f), 0, 0, GREY, GREY),
                    new GameObject("object5", new Shape(MapComponentVertices.scale(MapComponentVertices.WEDGE, 1)),
                            new Vector(MAP_WIDTH_1 * 8 / 20f, MAP_HEIGHT_1 * 12 / 30f), 0, 0, GREY, GREY),
                    new GameObject("object5", new Shape(MapComponentVertices.scale(MapComponentVertices.RECTANGLE, 8)),
                            new Vector(MAP_WIDTH_1 * 8 / 20f, MAP_HEIGHT_1 * 12 / 30f), 0, 0, GREY, GREY)
            )
    );

    private static final List<GameObject> obstacleLayout3 = new ArrayList<>(
            Collections.singletonList(
                    new GameObject("object1", new Shape(MapComponentVertices.scale(MapComponentVertices.OCTAGON, 10)),
                            new Vector(MAP_WIDTH_1 / 2f, MAP_HEIGHT_1 / 2f), 0, 0, GREY, GREY)

            )
    );

    private static final List<GameObject> bottomlessMap = new ArrayList<>(
            Arrays.asList(
                    new DestroyerBeam("beam1", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1)),
                    new DestroyerBeam("beam2", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1 - 40)),
                    new DestroyerBeam("beam3", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1 - 80)),
                    new GameObject("left_wall", new Shape(MapComponentVertices.WALL_SHAPE_1), new Vector(0, 1000), 0, 0, GREY, GREY),
                    new GameObject("right_wall", new Shape(MapComponentVertices.WALL_SHAPE_1), new Vector(MAP_WIDTH_1, 1000), 0, 0, GREY, GREY),
                    new GameObject("top_wall", new Shape(MapComponentVertices.WALL_SHAPE_2), new Vector(MAP_WIDTH_1 / 2.0, 0), 0, 0, GREY, GREY)
            )
    );

    private static final List<GameObject> bottomlessAndToplessMap = new ArrayList<>(
            Arrays.asList(
                    new DestroyerBeam("beam1", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1)),
                    new DestroyerBeam("beam2", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1 - 40)),
                    new DestroyerBeam("beam3", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1 - 80)),
                    new DestroyerBeam("beam4", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, 0)),
                    new DestroyerBeam("beam5", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, 40)),
                    new DestroyerBeam("beam6", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, 80)),
                    new GameObject("left_wall", new Shape(MapComponentVertices.WALL_SHAPE_1), new Vector(0, 1000), 0, 0, GREY, GREY),
                    new GameObject("right_wall", new Shape(MapComponentVertices.WALL_SHAPE_1), new Vector(MAP_WIDTH_1, 1000), 0, 0, GREY, GREY)
//                    new GameObject("top_wall", new Shape(MapComponentVertices.WALL_SHAPE_2), new Vector(MAP_WIDTH_1 / 2.0, 0), 0, 0, false, GREY, GREY)
            )
    );

    private static final List<GameObject> questionMarkMap = new ArrayList<>(
            Arrays.asList(
                    new DestroyerBeam("bottom_beam1", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1)),
                    new DestroyerBeam("bottom_beam2", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1 - 40)),
                    new DestroyerBeam("bottom_beam3", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, Maps.MAP_HEIGHT_1 - 80)),
                    new DestroyerBeam("top_beam1", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, 0)),
                    new DestroyerBeam("top_beam2", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, 40)),
                    new DestroyerBeam("top_beam3", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_HORIZONTAL, 1)), new Vector(MAP_WIDTH_1 / 2.0, 80)),
                    new DestroyerBeam("right_beam1", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_VERTICAL, 1)), new Vector(MAP_WIDTH_1 - 40, Maps.MAP_HEIGHT_1 / 2f)),
                    new DestroyerBeam("right_beam2", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_VERTICAL, 1)), new Vector(MAP_WIDTH_1 - 80, Maps.MAP_HEIGHT_1 / 2f)),
                    new DestroyerBeam("right_beam3", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_VERTICAL, 1)), new Vector(MAP_WIDTH_1 - 120, Maps.MAP_HEIGHT_1 / 2f)),
                    new DestroyerBeam("left_beam1", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_VERTICAL, 1)), new Vector( 40, Maps.MAP_HEIGHT_1 / 2f)),
                    new DestroyerBeam("left_beam2", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_VERTICAL, 1)), new Vector(80, Maps.MAP_HEIGHT_1 / 2f)),
                    new DestroyerBeam("left_beam3", new Shape(MapComponentVertices.scale(MapComponentVertices.DESTROYER_BEAM_VERTICAL, 1)), new Vector(120, Maps.MAP_HEIGHT_1 / 2f))
            )
    );

    private static final GameMap gameMap1 = new GameMap(obstacleLayout1,
            new ArrayList<>(Arrays.asList(new Vector(MAP_WIDTH_1 / 4, MAP_HEIGHT_1 / 4f), new Vector(MAP_WIDTH_1 * 3 / 4, MAP_HEIGHT_1 / 4f))),
            new ArrayList<>(
                    Arrays.asList(
                            new Vector(MAP_WIDTH_1, 200),
                            new Vector(MAP_WIDTH_1 * 2 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 3 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 4 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 5 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 6 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 2 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 5 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 6 / 7, MAP_HEIGHT_1 * 2 / 4f)
                    )
            ));
    private static final GameMap gameMap2 = new GameMap(obstacleLayout2,
            new ArrayList<>(Arrays.asList(new Vector(MAP_WIDTH_1 / 4, MAP_HEIGHT_1 / 2f), new Vector(MAP_WIDTH_1 * 3 / 4, MAP_HEIGHT_1 * 2 / 4f))),
            new ArrayList<>(
                    Arrays.asList(
                            new Vector(MAP_WIDTH_1, 200),
                            new Vector(MAP_WIDTH_1 * 2 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 3 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 4 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 5 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 6 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 2 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 3 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 4 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 5 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 6 / 7, MAP_HEIGHT_1 * 2 / 4f)
                    )
            ));

    private static final GameMap gameMap3 = new GameMap(obstacleLayout3,
            new ArrayList<>(Arrays.asList(new Vector(MAP_WIDTH_1 / 4, MAP_HEIGHT_1 / 4f), new Vector(MAP_WIDTH_1 * 3 / 4, MAP_HEIGHT_1 / 4f))),
            new ArrayList<>(
                    Arrays.asList(
                            new Vector(MAP_WIDTH_1, 200),
                            new Vector(MAP_WIDTH_1 * 2 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 3 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 4 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 5 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 6 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 2 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 2.5 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 4.5 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 5 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 6 / 7, MAP_HEIGHT_1 * 2 / 4f)
                    )
            ));

    private static final GameMap gameMap4 = new GameMap(obstacleLayout4,
            new ArrayList<>(Arrays.asList(new Vector(MAP_WIDTH_1 / 4, MAP_HEIGHT_1 / 4f), new Vector(MAP_WIDTH_1 * 3 / 4, MAP_HEIGHT_1 / 4f))),
            new ArrayList<>(
                    Arrays.asList(
                            new Vector(MAP_WIDTH_1, 200),
                            new Vector(MAP_WIDTH_1 * 2 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 3 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 4 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 5 / 7, 200),
                            new Vector(MAP_WIDTH_1 * 6 / 7, 200),
                            new Vector(MAP_WIDTH_1 / 2, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 2 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 2.5 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 4.5 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 5 / 7, MAP_HEIGHT_1 * 2 / 4f),
                            new Vector(MAP_WIDTH_1 * 6 / 7, MAP_HEIGHT_1 * 2 / 4f)
                    )
            ));
//    public static GameMap gameMap2 = new GameMap(,
//            new ArrayList<>(Arrays.asList(new Vector(MAP_WIDTH_1 / 4, MAP_HEIGHT_1 / 2f), new Vector(MAP_WIDTH_1 * 3 / 4, MAP_HEIGHT_1 / 2f))),
//            new ArrayList<>());

    private static final HashMap<String, GameMap> obstacleLayouts = new HashMap<>();

    public static final String MAP_1_NAME = "Map 1";

    public static final String MAP_2_NAME = "Map 2";

    public static final String MAP_3_NAME = "Map 3";

    public static final String MAP_4_NAME = "Map 4";
    private static final Map<String, List<GameObject>> floorLayouts = new HashMap<>();

    static {
        obstacleLayouts.put(MAP_1_NAME, gameMap1);
        obstacleLayouts.put(MAP_2_NAME, gameMap2);
        obstacleLayouts.put(MAP_3_NAME, gameMap3);
        obstacleLayouts.put(MAP_4_NAME, gameMap4);
    }

    static {
        floorLayouts.put(FLOOR_1_NAME, flooredMap);
        floorLayouts.put(FLOOR_2_NAME, bottomlessMap);
        floorLayouts.put(FLOOR_3_NAME, bottomlessAndToplessMap);
        floorLayouts.put(FLOOR_4_NAME, questionMarkMap);
    }

    public static GameMap getLayout(String floorName, String mapName) {
        GameMap gameMap = obstacleLayouts.get(mapName);

        List<GameObject> objects = new ArrayList<>(gameMap.getWorldObjects());

        List<GameObject> floorMap = floorLayouts.get(floorName);
        objects.addAll(floorMap);
        return new GameMap(objects, gameMap.getPlayerSpawns(), gameMap.getPowerUpSpawns());
    }
}
