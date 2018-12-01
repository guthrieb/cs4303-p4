package drawing.menu;

import collisiondetection.shapes.Vector;
import drawing.Colour;
import drawing.Sketch;

public class MenuFactory {
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
                new Vector(sketch.width / 2f, sketch.height / 4f),
                sketch.width / 6f, sketch.height / 10f,
                sketch::beginPlay);

        MenuBox mapSelect = new MenuBox(sketch, "select_map", "Map 1",
                new Vector(sketch.width / 2f, sketch.height * 2 / 4f),
                sketch.width / 6f, sketch.height / 10f,
                () -> {
                    sketch.nextMap();
                    sketch.updateMapText();
                });

        MenuBox flooredSelect = new MenuBox(sketch, "floored_select", "Floor: On",
                new Vector(sketch.width / 2f, sketch.height * 3 / 4f),
                sketch.width / 6f, sketch.height / 10f,
                () -> {
                    sketch.changeFloor();
                    sketch.updateFloorText();
                });

        menu.addMenuBox("main_menu", title);
        menu.addMenuBox("main_menu", playButton);
        menu.addMenuBox("main_menu", mapSelect);
        menu.addMenuBox("main_menu", flooredSelect);
//        menu.addMenuBox("main_menu", quit);

        return menu;
    }

}
