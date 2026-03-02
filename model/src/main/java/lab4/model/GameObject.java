/**
 * File: GameObject.java
 *
 * API:
 *  getX()
 *  getY()
 *  getPoint()
 *  mutatePoint(final double x, final double y)
 *  T getClosestInRange(Class<T> type, double loadRadius, Predicate<? super T> allowed)
 *  destroy()
 */

package lab4.model;

import java.util.Set;
import java.util.HashSet;
import java.util.function.Predicate;

import java.awt.geom.Point2D;

import static java.util.Objects.requireNonNull;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.validState;
import static org.apache.commons.lang3.Validate.finite;

public abstract class GameObject {
  private static final Set<GameObject> gameObjects = new HashSet<>();
  private final Point2D pos;

  protected GameObject(Point2D point) {
    Double x = point.getX();
    Double y = point.getY();
    isTrue((x >= 0.0d) && (y >= 0.0d));
    finite(x);
    finite(y);

    pos = new Point2D.Double(x, y);
    gameObjects.add(this);
  }

  public double getX() { return pos.getX(); }

  public double getY() { return pos.getY(); }

  public Point2D getPoint() {
    return new Point2D.Double(pos.getX(), pos.getY());
  }

  public void mutatePoint(final double x, final double y) {
    finite(x);
    finite(y);

    pos.setLocation(x, y);
  }

  public <T extends Vehicle> T getClosestInRange(
    final Class<T> type,
    final double loadRadius,
    final Predicate<? super T> allowed) {

    requireNonNull(type);
    isTrue(loadRadius > 1);
    requireNonNull(allowed);

    double maxDistSq = loadRadius * loadRadius;

    T best = null;
    double bestD2 = Double.POSITIVE_INFINITY;

    for (GameObject other : gameObjects) {
      if (other.equals(this)) continue;
      if (!type.isInstance(other)) continue;

      T candidate = type.cast(other);
      if (!allowed.test(candidate)) continue;

      double d2 = pos.distanceSq(other.pos);
      if (d2 <= maxDistSq && d2 < bestD2) {
        bestD2 = d2;
        best = candidate;
      }
    }

    return best;
  }

  public void destroy() {
    validState(gameObjects.contains(this));
    gameObjects.remove(this);
  }
}
