/**
 * File: VolvoFH16.java
*/

package lab4.model;

import lab4.model.interfaces.Car;
import lab4.model.interfaces.RampOperated;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.lang.System.out;

import java.awt.geom.Point2D;
import static java.awt.Color.YELLOW;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public final class VolvoFH16 extends ConditionallyMovableVehicle implements RampOperated {

  private static final double LOAD_RADIUS = 5.0d;
  private static final byte MAX_CAPACITY = 6;
  private final Deque<Vehicle> cargo = new ArrayDeque<>();
  private boolean isRampLowered;

  public VolvoFH16() {
    super(
      /* Number of doors */ 2,
      /* Engine power    */ 200.0d,
      /* Vehicle color   */ YELLOW,
      /* Vehicle model   */ "VolvoFH16",
      /* X position      */ new Point2D.Double(0.0d, 0.0d)
    );

    raiseRamp();
  }

  public void lowerRamp() {
    if (getCurrentSpeed() > 0) return;
    isRampLowered = true;
  }

  public void raiseRamp() {
    isRampLowered = false;
  }

  public boolean isRampLowered() {
    return isRampLowered;
  }

  public boolean canLoad() {
    return (getCurrentSpeed() == 0 && isRampLowered);
  }

  public void load() {
    Vehicle v;

    if (!canLoad()) {
      out.println("Can not load, ramp is not lowered!");
      return;
    }

    if (cargo.size() >= MAX_CAPACITY) {
      out.println("Can not load, " + getModelName() +" is full!");
      return;
    }

    v = getClosestInRange(
      Vehicle.class,
      LOAD_RADIUS,
      car -> (car instanceof Car) && !cargo.contains(car)
    );

    if (v == null) return;

    cargo.push(v);
    v.mutatePoint(getX(), getY());
  }

  public void unLoad() {
    if (!canLoad()) {
      out.println("Can not unload, ramp is not lowered!");
      return;
    }

    if (cargo.isEmpty()) {
      out.println("There is nothing to unload!");
      return;
    }

    Vehicle v = cargo.pop();
    v.mutatePoint(getX() - 1.0, getY());
  }

  public void printLoad() {
    StringBuilder sb = new StringBuilder();

    if (cargo.isEmpty()) sb.append("Cargohold is empty.");
    for (Vehicle v : cargo) sb.append(v).append('\n');
    out.println(sb);
  }

	protected double speedFactor() { return getEnginePower() * 0.01d; }

  @Override
  public void move() {
    super.move();
    for (Vehicle v : cargo) {
      v.mutatePoint(getX(), getY());
    }
  }

  @Override
  protected boolean canMove() { return !isRampLowered; }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
  }
}
