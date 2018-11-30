package drawing;

public class Colour {
    public int r;
    public int g;
    public int b;
    public int alpha = 255;
    public Colour(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Colour(int r, int g, int b, int alpha) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
    }

}
