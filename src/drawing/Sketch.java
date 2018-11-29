package drawing;


import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import collisiondetection.shapes.VectorConstants;
import collisionresponse.PhysicsLoop;
import gameobjects.GameObject;
import playercontrols.Laser;
import playercontrols.Player;
import playercontrols.PlayerShapes;
import world.GameMap;
import world.MapConstants;
import world.MapShapes;
import movement.TetherDirection;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Sketch extends PApplet {
    private static final double DT = 1 / 60.0;
    public static final double SCALE = 0.5;
    private static final int MAP_WIDTH = 3850;

    public PhysicsLoop physicsLoop;
    private List<Player> players = new ArrayList<>();
    public List<Laser> lasers = new ArrayList<>();
    private static double worldScale = 1;

    private static final int NUMPAD_6 = 227;
    private static final int NUMPAD_4 = 226;
    private static final int NUMPAD_5 = 65368;
    private static final int NUMPAD_8 = 224;

    private GameUI gameUI;

    public static void main(String[] args) {
        PApplet.main("drawing.Sketch");
    }

    public void settings() {
        fullScreen();
        buildShapes();

    }

    private void buildShapes() {
        physicsLoop = new PhysicsLoop(new ArrayList<>(), 10, DT, this);
        this.gameUI = new GameUI(this);

        initialiseMap("map1", true);
    }

    public void initialiseMap(String mapName, boolean floored) {
        GameMap gameMap = MapConstants.getLayout(floored, mapName);

        physicsLoop.objects = gameMap.getWorldObjects();
        int playerNo = 0;
        for(Vector playersSpawn : gameMap.getPlayerSpawns()) {
            Shape playerShape1 = new Shape(PlayerShapes.BOOSTING_VERTICES);
            playerNo++;
            Player newPlayer = new Player("p" + playerNo, this, playerShape1, playersSpawn, 2132, 838101, new Colour(0, 0, 255), new Colour(0, 0, 0));
            players.add(newPlayer);
            physicsLoop.objects.add(newPlayer);
        }
    }

//    private void addWorldShapes() {
//        Shape wall1 = new Shape(MapShapes.WALL_SHAPE_1);
//        Shape wall2 = new Shape(MapShapes.WALL_SHAPE_2);
//        Shape wedge = new Shape(MapShapes.WEDGE);
//
//        physicsLoop.objects.add(new GameObject("left_wall",wall1, new Vector(0, 1000), 0, 0, false));
//        physicsLoop.objects.add(new GameObject("right_wall",wall1, new Vector(MAP_WIDTH, 1000), 0, 0, false));
//        physicsLoop.objects.add(new GameObject("top_wall", wall2, new Vector(MAP_WIDTH /2.0, 0), 0, 0, false));
//        physicsLoop.objects.add(new GameObject("bottom_wall", wall2, new Vector(MAP_WIDTH /2.0, 2150), 0, 0, false));
//        physicsLoop.objects.add(new GameObject("", wedge, new Vector(MAP_WIDTH /2.0, 2050), 0, 0, false));
//        physicsLoop.objects.add(new GameObject("", wedge, new Vector(3* MAP_WIDTH /4.0, 2050), 0, 0, false));
//        physicsLoop.objects.add(new GameObject("", wedge, new Vector(MAP_WIDTH /4.0, 2050), 0, 0, false));
//    }

    public void mousePressed() {
        if (mouseButton == LEFT) {
            Vector vector = new Vector(mouseX, mouseY);
            players.get(0).attachTether(vector);
        } else {
            players.get(0).untether();
        }
    }

    public void keyPressed() {
        player1Controls();

        player2Controls();

        if (key == '[') {
            worldScale -= 0.05;
        }

        if (key == ']') {
            worldScale += 0.05;
        }
    }

    private void player2Controls() {
        Player player = players.get(1);
        if (player.getMode() == Player.Mode.MOVEMENT) {
            if (keyCode == NUMPAD_8) {
                player.setBoosting(true);

            }
            if (keyCode == NUMPAD_6) {
                player.setRotating(Player.RotationDirection.RIGHT);

            }
            if (keyCode == NUMPAD_4) {
                player.setRotating(Player.RotationDirection.LEFT);
            }
            if(keyCode == ENTER) {
                player.setFiring(true);
            }
        } else if (player.getMode() == Player.Mode.TETHER) {
            if (keyCode == NUMPAD_4) {
                player.addTether(TetherDirection.LEFT, physicsLoop.objects);
            } else if (keyCode == NUMPAD_6) {
                player.addTether(TetherDirection.RIGHT, physicsLoop.objects);
            }
        }


        if (key == 'p') {
            player.changeMode(Player.Mode.MOVEMENT);
        }
        if (key == '[') {
            player.changeMode(Player.Mode.TETHER);
        }
        if (key == ']') {
            player.changeMode(Player.Mode.DROPPER);
        }
    }

    private void player1Controls() {
        Player player = players.get(0);
        Player.Mode playerMode = player.getMode();
        if (playerMode == Player.Mode.MOVEMENT) {
            if (key == 'w') {
                player.setBoosting(true);

            }
            if (key == 'd') {
                player.setRotating(Player.RotationDirection.RIGHT);

            }
            if (key == 'a') {
                player.setRotating(Player.RotationDirection.LEFT);
            }
            if(key == ' ') {
                player.setFiring(true);
            }
        } else if (playerMode == Player.Mode.TETHER) {
            if (key == 'a') {
                player.addTether(TetherDirection.LEFT, physicsLoop.objects);
            } else if (key == 'd') {
                player.addTether(TetherDirection.RIGHT, physicsLoop.objects);
            }
        }

        if (key == '1') {
            player.changeMode(Player.Mode.MOVEMENT);
        }
        if (key == '2') {
            player.changeMode(Player.Mode.TETHER);
        }
        if (key == '3') {
            player.changeMode(Player.Mode.DROPPER);
        }
    }

    public void keyReleased() {
        if (key == 'w') {
            players.get(0).setBoosting(false);
        }
        if (key == 'a' || key == 'd') {
            players.get(0).setRotating(Player.RotationDirection.NO_ROTATION);
        }

        if (keyCode == NUMPAD_8) {
            players.get(1).setBoosting(false);

        }
        if (keyCode == NUMPAD_6 || keyCode == NUMPAD_4) {
            players.get(1).setRotating(Player.RotationDirection.NO_ROTATION);
        }
    }

    public void draw() {

        background(200, 200, 200);

        text(Float.toString(frameRate), width/2f, height/2f);

        for (Player player : players) {
            player.update(physicsLoop.objects);
        }

        for (GameObject object : physicsLoop.objects) {
            object.draw(this, SCALE);
        }

        ListIterator<Laser> iterator = lasers.listIterator();
        while(iterator.hasNext()) {
            Laser laser = iterator.next();

            laser.draw(this, SCALE);
            if(laser.isFaded()) {
                iterator.remove();
            }
        }

        gameUI.draw(players);
        physicsLoop.step();
    }

    public void point(Vector centerPoint) {
        point(centerPoint.x, centerPoint.y);
    }


    public void point(double x, double y) {
        strokeWeight(5);
        point((float) x, (float) y);
        strokeWeight(1);
    }

    public void line(Vector centerPoint, Vector orientationPoint) {
        line(centerPoint.x, centerPoint.y, orientationPoint.x, orientationPoint.y);
    }

    private void line(double x, double y, double x1, double y1) {
        line((float) x, (float) y, (float) x1, (float) y1);
    }

    public void ellipse(double x, double y, double radius, double radius1) {
        ellipse((float) x, (float) y, (float) radius, (float) radius1);
    }
}
