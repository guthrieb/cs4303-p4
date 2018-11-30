package playercontrols;

import collisiondetection.shapes.Vector;
import drawing.Colour;
import drawing.Sketch;
import helpers.Timer;
import processing.core.PConstants;

public class Trail {
    private final Sketch sketch;
    private final Colour fillColour;
    Timer timer = new Timer(500);
    Vector position;
    double radius = 10;
    double maxRadius = 10;

    public Trail(Sketch sketch, Vector position, Colour fillColour) {
        this.position = position;
        this.sketch = sketch;
        this.fillColour = fillColour;
    }

    public void update(){
        radius = (timer.percentageRemaining())*maxRadius;
    }

    public void draw(double scale) {
        sketch.ellipseMode(PConstants.CENTER);
        sketch.fill(fillColour.r, fillColour.g, fillColour.b, 200);
        sketch.ellipse((float)position.multiplyN(scale).x, (float)position.multiplyN(scale).y, (float)radius, (float)radius);
    }

    public boolean complete() {
        return timer.completed();
    }
}
