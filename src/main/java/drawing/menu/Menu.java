package drawing.menu;

import drawing.Sketch;

import java.util.*;

public class Menu {
    private final Sketch sketch;
    List<MenuBox> menuBoxList = new ArrayList<>();
    private final Map<String, ArrayList<MenuBox>> pages = new HashMap<>();
    private String currentPage = "main_menu";

    public Menu(Sketch sketch) {
        this.sketch = sketch;
    }

    public void addMenuBox(String page, MenuBox menuBox) {
        if(pages.containsKey(page)) {
            pages.get(page).add(menuBox);
        } else {
            pages.put(page, new ArrayList<>(Collections.singletonList(menuBox)));
        }
    }

    public void draw() throws InvalidPageException {
        drawPage(currentPage);
    }

    private void drawPage(String page) throws InvalidPageException {
        List<MenuBox> boxes = pages.get(page);

        if(boxes != null){
            for(MenuBox menuBox : boxes) {
                menuBox.draw();
            }
        } else {
            throw new InvalidPageException("Page not found: " + page);
        }
    }

    public void handleClick() {
        List<MenuBox> menuBoxes = pages.get(currentPage);
        System.out.println(menuBoxes);
        for (MenuBox menuBox : menuBoxes) {
            if (menuBox.clickable()) {
                System.out.println("SUCCESSFUL CLICK");
                menuBox.handleClick();
            }
        }
    }

    public void changePage(String page) throws InvalidPageException {
        if(pages.containsKey(page)) {
            currentPage = page;
        } else {
            throw new InvalidPageException("Page not found: " + page);
        }
    }

    public void updateMenuEntry(String newText, String page, String id) {
        if(pages.containsKey(page)) {
            for(MenuBox menuBox : pages.get(page)) {
                if(menuBox.getId().equals(id)) {
                    menuBox.update(newText);
                }
            }
        }
    }
}
