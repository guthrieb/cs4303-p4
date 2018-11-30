package drawing;


import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import collisionresponse.PhysicsLoop;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import gameobjects.GameObject;
import playercontrols.Laser;
import playercontrols.LaserMode;
import playercontrols.Player;
import playercontrols.PlayerShapes;
import world.GameMap;
import world.HealthPowerUp;
import world.Maps;
import movement.TetherDirection;
import processing.core.PApplet;
import world.WeaponPowerUp;

import java.util.*;

public class Sketch extends PApplet {
    private static final double DT = 1 / 60.0;
    public static final double SCALE = 0.5;
    private static final int PLAYER_MASS = 2132;
    private static final int PLAYER_MOMENT_INERTIA = 838101;
    private static final double POWERUP_MOMENT_INERTIA = 838101;
    private static final double POWERUP_MASS = 2000;
    private static final Colour HEALTH_FILL_COLOUR = new Colour(255, 255, 255);
    private static final Colour HEALTH_LINE_COLOUR = new Colour(255, 0, 0);
    private static final Colour WEAPON_FILL_COLOUR = new Colour(0, 0, 0);
    private static final Colour WEAPON_LINE_COLOUR = new Colour(255, 0, 0);
    public static final String REGULARLASER_KEY = "regularlaser";
    public static final String MEGALASER_KEY = "megalaser";
    public static final String SHOTGUN_KEY = "shotgun";
    public static final String MACHINE_GUN_KEY = "machine_gun";
    private static int healthPackNo = 0;
    private static int weaponPackNo = 0;

    private List<Colour> colours = new ArrayList<>(Arrays.asList(
            new Colour(200, 0, 0),
            new Colour(0, 200, 0)
    ));

    private PhysicsLoop physicsLoop;
    private List<Player> players = new ArrayList<>();
    public List<Laser> lasers = new ArrayList<>();

    private static final int NUMPAD_6 = 227;
    private static final int NUMPAD_4 = 226;
    private static final int NUMPAD_8 = 224;

    private GameUI gameUI;

    public static void main(String[] args) {
        PApplet.main("drawing.Sketch");
    }

    public HashMap<String, AudioPlayer> playerHashMap = new HashMap<>();
    private List<Vector> spawnLocations = new ArrayList<>();


    @Override
    public void setup() {

        Minim minim = new Minim(this);
        playerHashMap.put(MEGALASER_KEY, minim.loadFile("audio/megalaser.wav"));
        playerHashMap.put(SHOTGUN_KEY, minim.loadFile("audio/shotgun_laser.wav"));
        playerHashMap.put(REGULARLASER_KEY, minim.loadFile("audio/regular_laser.wav"));
        playerHashMap.put(MACHINE_GUN_KEY, minim.loadFile("audio/machine_gun_zap.mp3"));
        AudioPlayer music = minim.loadFile("audio/funk_floor.mp3");
        music.loop();
        super.setup();
    }

    public void settings() {
        fullScreen();
        buildShapes();

    }

    private void buildShapes() {
        physicsLoop = new PhysicsLoop(new ArrayList<>(), 10, DT, this);
        this.gameUI = new GameUI(this);

        initialiseMap("map2", true);
    }

    private void initialiseMap(String mapName, boolean floored) {
        GameMap gameMap = Maps.getLayout(floored, mapName);
        spawnLocations = gameMap.getPowerUpSpawns();

        physicsLoop.objects = gameMap.getWorldObjects();
        int playerNo = 0;
        List<Vector> playerSpawns = gameMap.getPlayerSpawns();
        for (int i = 0; i < playerSpawns.size(); i++) {
            Vector playersSpawn = playerSpawns.get(i);
            Shape playerShape1 = new Shape(PlayerShapes.BOOSTING_VERTICES);
            playerNo++;
            Player newPlayer = new Player("p" + playerNo, this, playerShape1, playersSpawn, PLAYER_MASS,
                    PLAYER_MOMENT_INERTIA, colours.get(i), new Colour(0, 0, 0));
            players.add(newPlayer);
            physicsLoop.objects.add(newPlayer);
        }
    }

    public void keyPressed() {
        player1Controls();

        player2Controls();
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

        if(key == ' ') {
            players.get(0).setFiring(false);
        }

        if(keyCode == ENTER) {
            players.get(1).setFiring(false);
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

        spawnPowerups();

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

    private void spawnPowerups() {
        Random random = new Random();
        int i = random.nextInt(300);

        System.out.println(i);
        if(i == 0) {
            int spawnLocationIndex = random.nextInt(spawnLocations.size());
            Vector vector = spawnLocations.get(spawnLocationIndex);
            int powerUpType = random.nextInt(2);



            if(powerUpType == 0) {
                System.out.println("SPAWNING POWERUP");
                physicsLoop.objects.add(new WeaponPowerUp("powerup_" + weaponPackNo++, vector.copy(), POWERUP_MASS, POWERUP_MOMENT_INERTIA,
                        LaserMode.standardLaserMode(), WEAPON_FILL_COLOUR, WEAPON_LINE_COLOUR));
            } else {
                System.out.println("SPAWNING HEALTH");
                physicsLoop.objects.add(new HealthPowerUp("health_" + healthPackNo++, vector.copy(), POWERUP_MASS, POWERUP_MOMENT_INERTIA,
                        100, HEALTH_FILL_COLOUR, HEALTH_LINE_COLOUR));
            }
        }

    }

    public void point(Vector centerPoint) {
        point(centerPoint.x, centerPoint.y);
    }


    private void point(double x, double y) {
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

    public void addPowerUpInteractions(Player player, WeaponPowerUp powerUp) {
        player.collect(powerUp);
    }

    public void addPowerUpInteractions(Player player, HealthPowerUp powerUp) {
        player.collect(powerUp);
    }
}
