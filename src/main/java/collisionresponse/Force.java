package collisionresponse;

import collisiondetection.shapes.Vector;

import java.util.Objects;

public class Force {
    public final Vector directions;
    public final Vector contactPoint;
    private final String forceId;
    public boolean angular = true;

    public Force(String id, Vector directions, Vector contactPoint) {
        this.forceId = id;
        this.directions = directions;
        this.contactPoint = contactPoint;
    }
    public Force(String id, Vector directions, Vector contactPoint, boolean angular) {
        this.forceId = id;
        this.directions = directions;
        this.contactPoint = contactPoint;
        this.angular = angular;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Force force = (Force) o;
        return Objects.equals(forceId, force.forceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forceId);
    }

    @Override
    public String toString() {
        return "Force{" +
                "directions=" + directions +
                ", contactPoint=" + contactPoint +
                '}';
    }
}
