package lab4.model;

import lab4.model.interfaces.Car;
import lab4.model.interfaces.WorldView;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class World implements WorldView {

  private static final int MAX_VEHICLES = 10;

  private final int width;
  private final int height;

  private final List<Vehicle> vehicles = new ArrayList<>();
  private final List<Garage<?>> garages = new ArrayList<>();

  private final CopyOnWriteArrayList<WorldListener> listeners = new CopyOnWriteArrayList<>();

  public World(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void addListener(WorldListener l) { listeners.add(requireNonNull(l)); }
  public void removeListener(WorldListener l) { listeners.remove(l); }

  private void notifyChanged() {
    for (WorldListener l : listeners) l.worldChanged(this);
  }

  @Override public int getWidth() { return width; }
  @Override public int getHeight() { return height; }
  @Override public List<Vehicle> getVehicles() { return Collections.unmodifiableList(vehicles); }
  @Override public List<Garage<?>> getGarages() { return Collections.unmodifiableList(garages); }

  public int getVehicleCount() {
    return vehicles.size();
  }

  public boolean canAddVehicle() {
    return vehicles.size() < MAX_VEHICLES;
  }

  public boolean addVehicle(Vehicle v) {
    requireNonNull(v);
    if (!canAddVehicle()) return false;
    vehicles.add(v);
    notifyChanged();
    return true;
  }

  public void addGarage(Garage<?> g) {
    garages.add(requireNonNull(g));
    notifyChanged();
  }

  public boolean removeLastVehicle() {
    if (vehicles.isEmpty()) return false;
    vehicles.remove(vehicles.size() - 1);
    notifyChanged();
    return true;
  }

  public void turnAllLeft() {
    for (Vehicle v : vehicles) v.turnLeft();
    notifyChanged();
  }

  public void turnAllRight() {
    for (Vehicle v : vehicles) v.turnRight();
    notifyChanged();
  }

  public void gasAll(double amount) {
    for (Vehicle v : vehicles) {
      v.gas(amount);
    }
    notifyChanged();
  }

  public void brakeAll(double amount) {
    for (Vehicle v : vehicles) {
      v.brake(amount);
    }
    notifyChanged();
  }

  public void step() {
    for (Vehicle v : vehicles) {
      v.move();
      clampToWorld(v);
    }
    notifyChanged();
  }

  public void changed() {
    notifyChanged();
  }

  private void clampToWorld(Vehicle v) {
    double x = v.getX();
    double y = v.getY();

    double nx = x;
    double ny = y;
    boolean hitBoundary = false;

    if (x < 0.0) {
      nx = 0.0;
      hitBoundary = true;
    } else if (x > width) {
      nx = width;
      hitBoundary = true;
    }

    if (y < 0.0) {
      ny = 0.0;
      hitBoundary = true;
    } else if (y > height) {
      ny = height;
      hitBoundary = true;
    }

    if (hitBoundary) {
      v.mutatePoint(nx, ny);
      v.invertDirection();
    }
  }

  public boolean loadNearestInto(Garage<?> garage, double radius) {
    requireNonNull(garage);
    if (!garage.hasSpace()) return false;

    Vehicle best = closestVehicleInRange(
      garage.getPoint(),
      radius,
      v -> garage.accepts(v)
    );

    if (best == null) return false;

    vehicles.remove(best);
    garage.accept(best);
    notifyChanged();
    return true;
  }

  public boolean unloadFrom(Garage<?> garage, Vehicle v) {
    requireNonNull(garage);
    requireNonNull(v);

    if (!garage.contains(v)) return false;

    garage.release(v);
    vehicles.add(v);
    v.mutatePoint(garage.getX() - 1.0, garage.getY());
    notifyChanged();
    return true;
  }

  public boolean loadNearestOnto(VolvoFH16 carrier, double radius) {
    requireNonNull(carrier);
    if (!carrier.canLoad()) return false;
    if (!carrier.hasSpace()) return false;

    Vehicle best = closestVehicleInRange(
      carrier.getPoint(),
      radius,
      v -> (v instanceof Car) && carrier.accepts(v)
    );

    if (best == null) return false;

    vehicles.remove(best);
    carrier.accept(best);
    notifyChanged();
    return true;
  }

  public boolean unloadFrom(VolvoFH16 carrier) {
    requireNonNull(carrier);
    if (!carrier.canLoad()) return false;

    Vehicle v = carrier.releaseTop();
    if (v == null) return false;

    vehicles.add(v);
    v.mutatePoint(carrier.getX() - 1.0, carrier.getY());
    notifyChanged();
    return true;
  }

  private Vehicle closestVehicleInRange(Point2D from, double radius, Predicate<Vehicle> allowed) {
    requireNonNull(from);
    requireNonNull(allowed);

    double r2 = radius * radius;
    Vehicle best = null;
    double bestD2 = Double.POSITIVE_INFINITY;

    for (Vehicle v : vehicles) {
      if (!allowed.test(v)) continue;
      double d2 = from.distanceSq(v.getPoint());
      if (d2 <= r2 && d2 < bestD2) {
        bestD2 = d2;
        best = v;
      }
    }
    return best;
  }
}
