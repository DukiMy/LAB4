package lab4.model;

import java.awt.geom.Point2D;

public abstract class GameObject {
  private final Point2D.Double pos;

  protected GameObject(Point2D point) {
    this.pos = new Point2D.Double(point.getX(), point.getY());
  }

  public final double getX() { return pos.getX(); }
  public final double getY() { return pos.getY(); }

  public final Point2D getPoint() {
    return (Point2D) pos.clone();
  }

  public final void mutatePoint(double x, double y) {
    pos.setLocation(x, y);
  }

  public void destroy() {}
}
