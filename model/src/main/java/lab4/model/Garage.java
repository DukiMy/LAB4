package lab4.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.isTrue;

public final class Garage<T extends Vehicle> extends GameObject {

  private final int maxCapacity;
  private final List<T> parkingLot = new ArrayList<>();
  private final Class<T> type;

  public Garage(Class<T> type, Point2D point, int maxCapacity) {
    super(point);
    this.type = requireNonNull(type);
    isTrue(1 <= maxCapacity && maxCapacity <= 127);
    this.maxCapacity = maxCapacity;
  }

  public int getFreeSlots() { return maxCapacity - parkingLot.size(); }
  public int getCapacity() { return maxCapacity; }
  public boolean hasSpace() { return getFreeSlots() > 0; }

  public boolean accepts(Vehicle v) {
    return type.isInstance(v) && !parkingLot.contains(type.cast(v));
  }

  public void accept(Vehicle v) {
    T t = type.cast(requireNonNull(v));
    isTrue(hasSpace(), "Garage is full");
    isTrue(!parkingLot.contains(t), "Already parked");
    parkingLot.add(t);
    t.mutatePoint(getX(), getY());
  }

  public boolean contains(Vehicle v) { return parkingLot.contains(v); }

  public void release(Vehicle v) { parkingLot.remove(v); }

  public List<T> getParked() { return List.copyOf(parkingLot); }
}
