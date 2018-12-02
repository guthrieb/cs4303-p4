package drawing.menu;

import collisiondetection.shapes.Vector;
import drawing.Colour;
import drawing.Sketch;

public class MenuFactory {
    public static final String MAIN_MENU_KEY = "main_menu";
    private final Sketch sketch;

    public MenuFactory(Sketch sketch) {
        this.sketch = sketch;
    }

    public Menu getMenu() {
        Menu menu = new Menu();

        MenuBox title = new MenuBox(sketch, "title", "M O R P H Z O N E",
                new Vector(sketch.width / 2f, sketch.height / 8f),
                sketch.width / 4f, sketch.height / 10f,
                null);
        title.fillColour = new Colour(255, 255, 255, 200);


        MenuBox playButton = new MenuBox(sketch, "play_game", "Play!",
                new Vector(sketch.width / 2f, sketch.height * 2 / 6f),
                sketch.width / 6f, sketch.height / 10f,
                sketch::beginPlay);

        MenuBox mapSelect = new MenuBox(sketch, "select_map", "Map 1",
                new Vector(sketch.width / 2f, sketch.height * 3 / 6f),
                sketch.width / 6f, sketch.height / 10f,
                () -> {
                    sketch.nextMap();
                    sketch.updateMapText();
                });

        MenuBox flooredSelect = new MenuBox(sketch, "floored_select", "Map Type: Floored",
                new Vector(sketch.width / 2f, sketch.height * 4 / 6f),
                sketch.width / 6f, sketch.height / 10f,
                () -> {
                    sketch.changeFloor();
                    sketch.updateFloorText();
                });

        MenuBox quit = new MenuBox(sketch, "quit", "Quit ",
                new Vector(sketch.width / 2f, sketch.height * 7 / 8f),
                sketch.width / 6f, sketch.height / 10f,
                sketch::exit);

        menu.addMenuBox(MAIN_MENU_KEY, title);
        menu.addMenuBox(MAIN_MENU_KEY, playButton);
        menu.addMenuBox(MAIN_MENU_KEY, mapSelect);
        menu.addMenuBox(MAIN_MENU_KEY, flooredSelect);
        menu.addMenuBox(MAIN_MENU_KEY, quit);

        return menu;
    }

}
