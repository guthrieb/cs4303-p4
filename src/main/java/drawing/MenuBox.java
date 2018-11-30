package drawing;

import collisiondetection.shapes.Vector;
import processing.core.PConstants;


import java.util.function.Consumer;

public class MenuBox {
    private final Sketch sketch;
    private String content;
    private final Vector screenPos;
    private final float width;
    private final float height;
    private final Runnable consumer;

    public MenuBox(Sketch sketch, String content, Vector screenPos, float width, float height, Consumer<T> consumer) {
        this.sketch = sketch;
        this.content = content;
        this.screenPos = screenPos;
        this.width = width;
        this.height = height;
        this.consumer = consumer;
    }

    public void handleClick() {
        consumer.run();
    }

    public void draw() {
        sketch.rectMode(PConstants.CENTER);
        sketch.rect((float)screenPos.x, (float)screenPos.y, width, height);
        sketch.text(content, (float)screenPos.x, (float)screenPos.y);
    }

    public void update(String content) {
        this.content = content;
    }

    public boolean clicked() {
        return sketch.mouseY > (screenPos.y - height/2)
                && sketch.mouseY > (screenPos.y + height/2)
                && sketch.mouseX > (screenPos.x - height/2)
                && sketch.mouseX < (screenPos.x + height/2);
    }
}
