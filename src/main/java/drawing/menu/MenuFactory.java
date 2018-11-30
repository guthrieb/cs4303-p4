package drawing.menu;

import collisiondetection.shapes.Vector;
import drawing.Sketch;

public class MenuFactory {
    private final Sketch sketch;

    public MenuFactory(Sketch sketch) {
        this.sketch = sketch;
    }

    public Menu getMenu() {
        Menu menu = new Menu(sketch);

        MenuBox playButton = new MenuBox(sketch, "play_game", "Play!",
                new Vector(sketch.width / 2f, sketch.height / 4f),
                sketch.width / 6f, sketch.height / 10f,
                () -> {
                    sketch.beginPlay();
                });

        MenuBox mapSelect = new MenuBox(sketch, "select_map", "Map 1",
                new Vector(sketch.width / 2f, sketch.height * 2 / 4f),
                sketch.width / 6, sketch.height / 10f,
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

        menu.addMenuBox("main_menu", playButton);
        menu.addMenuBox("main_menu", mapSelect);
        menu.addMenuBox("main_menu", flooredSelect);

        return menu;
    }

}
