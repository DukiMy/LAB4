/**
 *  Utfärdat av Durim Miziraj
 *  Kontakt: gusmizdu@student.gu.se
 *
 *  Klassen är en konkret representation av ett Garage.
 *  Objekt av denna klass har...
 *    - Position
 *    - Datastruktur för hållande av bilar.
 *    - Ett maxantal bilar
 *  Objekt av denna klass skall kunna...
 *    - Lasta och lossa bilar
 *    -
 */

package lab4;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

import static java.lang.System.out;
import static org.apache.commons.lang3.Validate.isTrue;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public final class Garage<T extends Vehicle> extends GameObject {

  private static final double LOAD_RADIUS = 10.0d;

  private final int maxCapacity;
  private final List<T> parkingLot = new ArrayList<>();
  private final Class<T> type;

  public Garage(
    final Class<T> type,
    final Point2D point,
    final int maxCapacity) {
    super(point);

    requireNonNull(type);
    isTrue(1 <= maxCapacity && maxCapacity <= 127);

    this.type = type;
    this.maxCapacity = maxCapacity;
  }

  public int getFreeSlots() {
    return maxCapacity - parkingLot.size();
  }

  public boolean load() {
    if (getFreeSlots() <= 0) {
      out.println("The garage is full!");
      return false;
    }

    T v = getClosestInRange(type, LOAD_RADIUS, x -> !parkingLot.contains(x));
    if (v == null) {
      return false;
    }

    parkingLot.add(v);
    v.mutatePoint(getX(), getY());
    return true;
  }

  public void unLoad(final T vehicle) {
    requireNonNull(vehicle);

    if (parkingLot.remove(vehicle)) {
      vehicle.mutatePoint(getX() - 1.0, getY());
    } else {
      out.println("That vehicle is not in the garage!");
    }
  }

  public void printLoad() {
    StringBuilder sb = new StringBuilder();

    for (T t : parkingLot) sb.append(t).append('\n');
    if (sb.length() == 0) sb.append("Parkinglot is empty.");

    out.println(sb);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
  }
}
