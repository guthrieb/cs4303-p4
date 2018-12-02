package drawing.menu;

import collisiondetection.shapes.Vector;
import drawing.Colour;
import drawing.Sketch;
import processing.core.PConstants;

class MenuBox {
    private final Sketch sketch;
    private final boolean runnable;
    private String content;
    private final Vector screenPos;
    private final float width;
    private final float height;
    private final Runnable consumer;
    private final String id;
    private final Colour highlightColour = new Colour(255, 0, 0, 100);
    public Colour fillColour = new Colour(255, 255, 255, 100);

    public MenuBox(Sketch sketch, String id, String content, Vector screenPos, float width, float height, Runnable consumer) {
        this.sketch = sketch;
        this.content = content;
        this.screenPos = screenPos;
        this.width = width;
        this.height = height;
        this.consumer = consumer;
        this.id = id;
        this.runnable = consumer != null;
    }

    public void handleClick() {
        consumer.run();
    }

    public void draw() {
        sketch.rectMode(PConstants.CENTER);
        if(clickable()) {
            sketch.fill(highlightColour.r, highlightColour.g, highlightColour.b, highlightColour.alpha);
        } else {
            sketch.fill(fillColour.r, fillColour.b, fillColour.g, fillColour.alpha);
        }



        sketch.rect((float)screenPos.x, (float)screenPos.y, width, height);

        sketch.stroke(0, 0, 0);
        sketch.fill(0, 0, 0);
        sketch.textSize(20);
        sketch.textAlign(PConstants.CENTER, PConstants.CENTER);
        sketch.text(content, (float)screenPos.x, (float)screenPos.y);
        sketch.stroke(0, 0, 0);
    }

    public void update(String content) {
        this.content = content;
    }

    boolean clickable() {
        if(!runnable) {
            return false;
        }

        int x = sketch.mouseX;
        int y = sketch.mouseY;

        double xPos = screenPos.x;
        double yPos = screenPos.y;
        double xDim = width;
        double yDim = height;
        return x > xPos - xDim/2
                && x < xPos + xDim/2
                && y > yPos - yDim/2
                && y < yPos + yDim/2;
    }

    public String getId() {
        return this.id;
    }
}
