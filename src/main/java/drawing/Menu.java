package drawing;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    List<MenuBox> menuBoxList = new ArrayList<>();

    public Menu(Sketch sketch, List<MenuBox> menuBoxList) {
        this.menuBoxList = menuBoxList;
    }

    public void draw() {
        for(MenuBox menuBox : menuBoxList) {
            menuBox.draw();
        }
    }

    public void handleClick() {
        for(MenuBox menuBox : menuBoxList) {
            if(menuBox.clicked()) {
                menuBox.handleClick();
            }
        }
    }
}
