package drawing;

import collisiondetection.shapes.Vector;
import helpers.Timer;
import processing.core.PConstants;

class FadingText {
    private final String text;
    private final Vector position;
    private final Sketch sketch;
    private final Timer timer;
    private final Colour fill = new Colour(255, 255, 255);

    FadingText(Sketch sketch, String text, Vector position, long timeToFade) {
        this.sketch = sketch;
        this.text = text;
        this.position = position;
        this.timer = new Timer(timeToFade);
    }

    public void draw(double scale) {
        sketch.textAlign(PConstants.CENTER);
        sketch.fill(fill.r, fill.g, fill.b, (float) (255*timer.percentageRemaining()));
        Vector vector = position.multiplyN(scale);
        sketch.text(this.text, (float)vector.x, (float)vector.y);
    }

    boolean complete() {
        return timer.completed();
    }
}
