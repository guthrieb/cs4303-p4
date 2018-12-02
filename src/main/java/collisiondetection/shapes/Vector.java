package collisiondetection.shapes;

import java.util.Objects;

public class Vector {
    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void negate() {
        this.x = -x;
        this.y = -y;
    }

    public Vector subtract(Vector toSubtract) {
        this.x = this.x - toSubtract.x;
        this.y = this.y - toSubtract.y;
        return this;
    }

    public void add(Vector toAdd) {
        this.x = this.x + toAdd.x;
        this.y = this.y + toAdd.y;
    }

    public static double dot(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public Vector negateN() {
        return new Vector(-x, -y);
    }

    public Vector copy() {
        return new Vector(x, y);
    }

    public Vector subtractN(Vector toSubtract) {
        return new Vector(x - toSubtract.x, y - toSubtract.y);
    }

    public static Vector tripleProduct(Vector v1, Vector v2, Vector v3) {
        Vector vector = v2.multiplyN(dot(v3, v1));
        Vector vector2 = v1.multiplyN(dot(v3, v2));
        return vector.subtractN(vector2);
    }

    public Vector multiplyN(double scalar) {
        return new Vector(x*scalar, y*scalar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;

        return Math.abs(vector.x - x) < 0.0000001 &&  Math.abs(vector.y - y) < 0.0000001;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public void normalize() {
        double mag = mag();
        x /= mag;
        y /= mag;
    }

    public double mag() {
        return Math.sqrt(x*x + y*y);
    }

    public void multiply(double u) {
        x*=u;
        y*=u;
    }

    public Vector cross(double scalar) {
        return new Vector(y*scalar, x*scalar*-1);
    }

    public void add(Vector v, double s)
    {
        this.x = x + v.x * s;
        this.y = y + v.y * s;
    }

    public static double cross( Vector a, Vector b )
    {
        return a.x * b.y - a.y * b.x;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector addN(Vector vectpr) {
        return new Vector(x + vectpr.x, y + vectpr.y);
    }

    public void addMultScalar(Vector v, double s) {
        this.x = x + v.x *s;
        this.y = y + v.y *s;
    }

    public static Vector cross(double a, Vector v) {
        return new Vector(v.y * -a, v.x * a);
    }

    double squaredLength() {
        return x*x + y*y;
    }
}
