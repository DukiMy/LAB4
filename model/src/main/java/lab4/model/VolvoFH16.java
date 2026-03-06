package lab4.model;

import lab4.model.interfaces.Car;
import lab4.model.interfaces.Loadable;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Deque;

import static java.awt.Color.YELLOW;
import static java.util.Objects.requireNonNull;

public final class VolvoFH16 extends ConditionallyMovableVehicle implements Loadable {

  public static final double DEFAULT_LOAD_RADIUS = 5.0d;
  private static final int MAX_CAPACITY = 6;

  private final Deque<Vehicle> cargo = new ArrayDeque<>();

  private boolean loadingEnabled;

  public VolvoFH16() {
    super(2, 200.0d, YELLOW, "VolvoFH16", new Point2D.Double(0.0d, 0.0d));
    loadingEnabled = false;
  }

  @Override
  public boolean canLoad() {
    return getCurrentSpeed() == 0.0d && loadingEnabled;
  }

  public void setLoadingEnabled(boolean enabled) {
    if (getCurrentSpeed() != 0.0d) return;
    this.loadingEnabled = enabled;
  }

  @Override
  public void load() {}

  @Override
  public void unLoad() {}

  @Override
  public void printLoad() {
    System.out.println(cargo);
  }

  public boolean hasSpace() { return cargo.size() < MAX_CAPACITY; }

  public boolean accepts(Vehicle v) {
    return (v instanceof Car) && !cargo.contains(v);
  }

  public void accept(Vehicle v) {
    requireNonNull(v);
    cargo.push(v);
    v.mutatePoint(getX(), getY());
  }

  public Vehicle releaseTop() {
    return cargo.isEmpty() ? null : cargo.pop();
  }

  @Override
  public void move() {
    if (!canMove()) return;
    super.move();
    for (Vehicle v : cargo) v.mutatePoint(getX(), getY());
  }

  @Override protected boolean canMove() { return true; }
  @Override protected double speedFactor() { return getEnginePower() * 0.01d; }
}
