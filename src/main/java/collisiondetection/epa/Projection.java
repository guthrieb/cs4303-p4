package collisiondetection.epa;

class Projection {
    private final double min;
    private final double max;

    public Projection(double minVal, double maxVal) {
        this.min = minVal;
        this.max = maxVal;
    }

    public boolean overlaps(Projection p2) {
        return !(this.min > p2.max || p2.min > this.max);
    }

    public double getOverlap(Projection p2) {
        if(this.overlaps(p2)) {
            return Math.min(p2.max, this.max) - Math.max(this.min, p2.min);
        } else {
            return 0;
        }
    }

    public boolean contains(Projection p2) {
        return p2.min > this.min && p2.max < this.max;
    }

    @Override
    public String toString() {
        return "Projection{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
