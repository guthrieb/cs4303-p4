package drawing;


import collisiondetection.shapes.Shape;
import collisiondetection.shapes.Vector;
import collisionresponse.PhysicsLoop;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import drawing.menu.InvalidPageException;
import drawing.menu.Menu;
import drawing.menu.MenuFactory;
import gameobjects.GameObject;
import helpers.Timer;
import movement.TetherDirection;
import playercontrols.*;
import processing.core.PApplet;
import processing.core.PImage;
import world.GameMap;
import world.HealthPowerUp;
import world.Maps;
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
    private static final Colour WEAPON_FILL_COLOUR = new Colour(255, 0, 255);
    private static final Colour WEAPON_LINE_COLOUR = new Colour(255, 0, 0);
    public static final String REGULARLASER_KEY = "regularlaser";
    public static final String MEGALASER_KEY = "megalaser";
    public static final String SHOTGUN_KEY = "shotgun";
    public static final String MACHINE_GUN_KEY = "machine_gun";
    public static final String DESTROYED_KEY = "destroyed";
    private static final int POWERUP_LIMIT = 3;
    private static final int POWERUP_PROBABILITY = 700;
    private PImage background;
    private static int healthPackNo = 0;
    private static int weaponPackNo = 0;
    public static int noOfPowerups = 0;

    private List<FadingText> weaponInfos = new ArrayList<>();

    private Timer countDownTimer = new Timer(1000);
    private int countDown = 3;

    private List<Colour> colours = new ArrayList<>(Arrays.asList(
            new Colour(200, 0, 0),
            new Colour(0, 200, 0)
    ));


    private static final int NUMPAD_6 = 227;
    private static final int NUMPAD_4 = 226;
    private static final int NUMPAD_8 = 224;

    private GameUI gameUI;
    private Menu menu;
    private Player winningPlayer;
    private GameState state = GameState.MENU;

    public HashMap<String, AudioPlayer> playerHashMap = new HashMap<>();
    private List<Vector> spawnLocations = new ArrayList<>();
    private PhysicsLoop physicsLoop;
    private List<Player> players = new ArrayList<>();
    public List<Laser> lasers = new ArrayList<>();

    private List<String> maps = new ArrayList<>(Arrays.asList(Maps.MAP_1_NAME, Maps.MAP_2_NAME, Maps.MAP_3_NAME, Maps.MAP_4_NAME));
    private List<String> floors = new ArrayList<>(Arrays.asList(Maps.FLOOR_1_NAME, Maps.FLOOR_2_NAME, Maps.FLOOR_3_NAME, Maps.FLOOR_4_NAME));
    private int currentMap = 0;
    private int currentFloor = 0;
    private List<Trail> trails = new ArrayList<>();

    public static void main(String[] args) {
        PApplet.main("drawing.Sketch");
    }

    public void nextMap() {
        currentMap++;
        if (currentMap >= maps.size()) {
            currentMap = 0;
        }
        initialiseMap(maps.get(currentMap), floors.get(currentFloor), false);
    }

    public void updateMapText() {
        menu.updateMenuEntry(maps.get(currentMap), "main_menu", "select_map");
    }

    public void changeFloor() {
        currentFloor++;
        if (currentFloor >= floors.size()) {
            currentFloor = 0;
        }
        initialiseMap(maps.get(currentMap), floors.get(currentFloor), false);
    }

    public void updateFloorText() {
        menu.updateMenuEntry("Map Type: " + floors.get(currentFloor), "main_menu", "floored_select");
    }

    public void beginPlay() {
        initialiseMap(maps.get(currentMap), floors.get(currentFloor), true);
        countDownTimer.reset();
        state = GameState.COUNTDOWN;
    }

    public void addTrail(Trail trail) {
        this.trails.add(trail);
    }

    public enum GameState {
        MENU, GAME_OVER, PLAYING, COUNTDOWN
    }


    @Override
    public void setup() {

        Minim minim = new Minim(this);
        playerHashMap.put(MEGALASER_KEY, minim.loadFile("audio/megalaser.wav"));
        playerHashMap.put(SHOTGUN_KEY, minim.loadFile("audio/shotgun_laser.wav"));
        playerHashMap.put(REGULARLASER_KEY, minim.loadFile("audio/regular_laser.wav"));
        playerHashMap.put(MACHINE_GUN_KEY, minim.loadFile("audio/machine_gun_zap.mp3"));
        playerHashMap.put(DESTROYED_KEY, minim.loadFile("audio/destroyed.mp3"));
        this.background = loadImage("img/background.jpg");
        this.background.resize(width, height);
        AudioPlayer music = minim.loadFile("audio/funk_floor.mp3");
        music.loop();
        super.setup();

        physicsLoop = new PhysicsLoop(new ArrayList<>(), 10, DT, this);
        this.gameUI = new GameUI(this);
        this.menu = new MenuFactory(this).getMenu();
        initialiseMap(maps.get(currentMap), floors.get(currentFloor), false);
    }

    public void settings() {
        fullScreen();
    }

    private void initialiseMap(String mapName, String floorName, boolean spawnPlayers) {
        GameMap gameMap = Maps.getLayout(floorName, mapName);
        spawnLocations = gameMap.getPowerUpSpawns();

        players = new ArrayList<>();

        physicsLoop.objects = new ArrayList<>();
        physicsLoop.objects = gameMap.getWorldObjects();

        if (spawnPlayers) {
            int playerNo = 0;
            List<Vector> playerSpawns = gameMap.getPlayerSpawns();
            for (int i = 0; i < playerSpawns.size(); i++) {
                Vector playersSpawn = playerSpawns.get(i);
                Shape playerShape1 = new Shape(PlayerShapes.BOOSTING_VERTICES);
                playerNo++;
                Player newPlayer = new Player("P" + playerNo, this, playerShape1, playersSpawn.copy(), PLAYER_MASS,
                        PLAYER_MOMENT_INERTIA, colours.get(i), new Colour(0, 0, 0));

                newPlayer.shape.rotate(Math.PI * 3 / 2);
                newPlayer.physicsObject.orientation = Math.PI * 3 / 2;
                players.add(newPlayer);
                physicsLoop.objects.add(newPlayer);
            }
        }
    }

    public void keyPressed() {
        if (state == GameState.PLAYING) {
            player1Controls();

            player2Controls();
        } else if (state == GameState.GAME_OVER) {
            gameOverControls();
        }
    }

    private void gameOverControls() {
        if (key == 'r') {
            resetGame();
            initialiseMap(maps.get(currentMap), floors.get(currentFloor), true);
        } else if (key == 'm') {
            state = GameState.MENU;
        } else if (key == 'q') {
            exit();
        }
    }

    private void resetGame() {
        players = new ArrayList<>();
        winningPlayer = null;
        physicsLoop.objects = new ArrayList<>();
        state = GameState.COUNTDOWN;
        countDownTimer.reset();
        lasers = new ArrayList<>();
        trails = new ArrayList<>();
    }

    public void mousePressed() {
        if (state == GameState.MENU) {
            menu.handleClick();
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
            if (keyCode == ENTER) {
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
            if (key == ' ') {
                player.setFiring(true);
            }
        } else if (playerMode == Player.Mode.TETHER) {
            if (key == 'a') {
                player.addTether(TetherDirection.LEFT, physicsLoop.objects);
            } else if (key == 'd') {
                player.addTether(TetherDirection.RIGHT, physicsLoop.objects);
            } else if (key == 'w') {
                player.addTether(TetherDirection.UP, physicsLoop.objects);
            } else if (key == 's') {
                player.addTether(TetherDirection.DOWN, physicsLoop.objects);
            }
        }

        if (key == 'c') {
            player.changeMode(Player.Mode.MOVEMENT);
        }
        if (key == 'v') {
            player.changeMode(Player.Mode.TETHER);
        }
        if (key == 'b') {
            player.changeMode(Player.Mode.DROPPER);
        }
    }

    public void keyReleased() {
        if (state == GameState.PLAYING) {
            if (key == 'w') {
                players.get(0).setBoosting(false);
            }
            if (key == 'a' || key == 'd') {
                players.get(0).setRotating(Player.RotationDirection.NO_ROTATION);
            }

            if (key == ' ') {
                players.get(0).setFiring(false);
            }

            if (keyCode == ENTER) {
                players.get(1).setFiring(false);
            }


            if (keyCode == NUMPAD_8) {
                players.get(1).setBoosting(false);

            }
            if (keyCode == NUMPAD_6 || keyCode == NUMPAD_4) {
                players.get(1).setRotating(Player.RotationDirection.NO_ROTATION);
            }
        }
    }

    public void draw() {
        background(200, 200, 200);
        background(background);
        ListIterator<Trail> trailListIterator = trails.listIterator();
        while (trailListIterator.hasNext()) {
            Trail trail = trailListIterator.next();
            trail.update();
            trail.draw(SCALE);
            if (trail.complete()) {
                trailListIterator.remove();
            }
        }

        if (state == GameState.COUNTDOWN) {
            for (GameObject object : physicsLoop.objects) {
                object.draw(this, 0.5);
            }

            if (countDownTimer.completed()) {
                countDown--;
                countDownTimer.reset();
                if (countDown < 0) {
                    state = GameState.PLAYING;
                    countDown = 3;
                }
            }

            fill(255, 255, 255);
            if (countDown > 0) {
                textSize(100);
                text(countDown, width / 2f, height / 2f);
                textSize(20);
            } else {
                textSize(100);
                text("GO", width / 2f, height / 2f);
                textSize(20);
            }

        } else if (state == GameState.MENU) {
            for (GameObject object : physicsLoop.objects) {
                object.draw(this, SCALE);
            }
            drawMenu();
        } else if (state == GameState.GAME_OVER) {
            for (GameObject object : physicsLoop.objects) {
                object.draw(this, SCALE);
            }

            textSize(100);
            Colour playerColour = winningPlayer.playerColour;
            stroke(playerColour.r, playerColour.g, playerColour.b);
            fill(playerColour.r, playerColour.g, playerColour.b);
            text(winningPlayer.id.toUpperCase() + " WINS", width / 2f, height / 2f);
            textSize(20);
            text("Press \"r\" to play again, \"m\" to return to the main menu and \"q\" to quit!", width / 2f, height * 3 / 5f);
            textSize(10);
            physicsLoop.step();
        } else {
            fill(255, 255, 255);

            spawnPowerups();


            for (GameObject object : physicsLoop.objects) {
                if (!(object instanceof Player)) {
                    object.draw(this, SCALE);
                }
            }
            for (Player player : players) {
                player.update(physicsLoop.objects);
                player.draw(this, SCALE);
            }

            ListIterator<Laser> iterator = lasers.listIterator();
            while (iterator.hasNext()) {
                Laser laser = iterator.next();

                laser.draw(this, SCALE);
                if (laser.isFaded()) {
                    iterator.remove();
                }
            }

            ListIterator<FadingText> fadingTextListIterator = weaponInfos.listIterator();
            while (fadingTextListIterator.hasNext()) {
                FadingText text = fadingTextListIterator.next();
                text.draw(SCALE);
                if (text.complete()) {
                    fadingTextListIterator.remove();
                }
            }

            gameUI.draw(players);
            physicsLoop.step();

            if (gameOver()) {
                state = GameState.GAME_OVER;
            }
        }
    }

    private boolean gameOver() {
        int alivePlayers = 0;
        Player alivePlayer = null;
        for (Player player : players) {
            if (!player.dead()) {
                alivePlayers++;
                alivePlayer = player;
            }
            if (alivePlayers > 1) {
                return false;
            }
        }
        this.winningPlayer = alivePlayer;
        return true;
    }

    private void drawMenu() {
        try {
            this.menu.draw();
        } catch (InvalidPageException e) {
            e.printStackTrace();
        }
    }

    private void spawnPowerups() {
        Random random = new Random();

        int i = random.nextInt(POWERUP_PROBABILITY);

        if (noOfPowerups < POWERUP_LIMIT && i == 0) {
            noOfPowerups++;
            int spawnLocationIndex = random.nextInt(spawnLocations.size());
            Vector vector = spawnLocations.get(spawnLocationIndex);
            int powerUpType = random.nextInt(2);


            if (powerUpType == 0) {
                addWeaponPowerup(random, vector);
            } else {
                physicsLoop.objects.add(new HealthPowerUp("health_" + healthPackNo++, vector.copy(), POWERUP_MASS, POWERUP_MOMENT_INERTIA,
                        100, HEALTH_FILL_COLOUR, HEALTH_LINE_COLOUR));
            }
        }

    }

    private void addWeaponPowerup(Random random, Vector vector) {
        int laserType = random.nextInt(4);

        if (laserType == 0) {
            physicsLoop.objects.add(new WeaponPowerUp("powerup_" + weaponPackNo++, "Laser Rifle", vector.copy(), POWERUP_MASS, POWERUP_MOMENT_INERTIA,
                    LaserMode.standardLaserMode(), WEAPON_FILL_COLOUR, WEAPON_LINE_COLOUR));
        } else if (laserType == 1) {
            physicsLoop.objects.add(new WeaponPowerUp("powerup_" + weaponPackNo++, "Laser Shotgun", vector.copy(), POWERUP_MASS, POWERUP_MOMENT_INERTIA,
                    LaserMode.shotgunLasers(), WEAPON_FILL_COLOUR, WEAPON_LINE_COLOUR));
        } else if (laserType == 2) {
            physicsLoop.objects.add(new WeaponPowerUp("powerup_" + weaponPackNo++, "M E G A L A S E R", vector.copy(), POWERUP_MASS, POWERUP_MOMENT_INERTIA,
                    LaserMode.megaLaserMode(), WEAPON_FILL_COLOUR, WEAPON_LINE_COLOUR));
        } else if (laserType == 3) {
            physicsLoop.objects.add(new WeaponPowerUp("powerup_" + weaponPackNo++, "Machine Gun Laser", vector.copy(), POWERUP_MASS, POWERUP_MOMENT_INERTIA,
                    LaserMode.machineGunLasers(), WEAPON_FILL_COLOUR, WEAPON_LINE_COLOUR));
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
        weaponInfos.add(new FadingText(this, powerUp.name, player.physicsObject.position, 1000));

    }

    public void addPowerUpInteractions(Player player, HealthPowerUp powerUp) {
        player.collect(powerUp);
    }
}
