package lab4;

import lab4.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GarageTests {

  private static final Point2D.Double ORIGO = new Point2D.Double(0.0d, 0.0d);
  private final List<GameObject> created = new ArrayList<>();

  private <T extends GameObject> T track(T obj) {
    created.add(obj);
    return obj;
  }

  @AfterEach
  void cleanup() {
    for (GameObject o : created) o.destroy();
    created.clear();
  }

  @Test
  void ctorRejectsInvalidCapacity() {
    assertThrows(IllegalArgumentException.class,
      () -> track(new Garage<>(Vehicle.class, ORIGO, 0)));

    assertThrows(IllegalArgumentException.class,
      () -> track(new Garage<>(Vehicle.class, ORIGO, 128)));
  }

  @Test
  void freeSlotsDecreasesWhenLoadedUntilFull() {
    World w = new World(700, 400);

    Garage<Vehicle> g = track(new Garage<>(Vehicle.class, ORIGO, 2));
    w.addGarage(g);

    Vehicle v1 = track(new Volvo240());
    Vehicle v2 = track(new Saab95());
    Vehicle v3 = track(new Scania());
    v1.mutatePoint(0.1, 0.1);
    v2.mutatePoint(0.2, 0.2);
    v3.mutatePoint(0.3, 0.3);

    w.addVehicle(v1);
    w.addVehicle(v2);
    w.addVehicle(v3);

    assertEquals(2, g.getFreeSlots());

    assertTrue(w.loadNearestInto(g, 10.0));
    assertEquals(1, g.getFreeSlots());

    assertTrue(w.loadNearestInto(g, 10.0));
    assertEquals(0, g.getFreeSlots());

    Point2D before = v3.getPoint();
    assertFalse(w.loadNearestInto(g, 10.0));
    assertEquals(before, v3.getPoint(), "Tredje Vehicle får inte lastas i ett fullt garage.");
  }

  @Test
  void loadsClosestVehicleInRange() {
    World w = new World(700, 400);

    Garage<Vehicle> g = track(new Garage<>(Vehicle.class, ORIGO, 5));
    w.addGarage(g);

    Vehicle near = track(new Volvo240());
    Vehicle far  = track(new Saab95());
    near.mutatePoint(1.0, 1.0);
    far.mutatePoint(9.0, 0.0);

    w.addVehicle(near);
    w.addVehicle(far);

    assertTrue(w.loadNearestInto(g, 5.0));

    assertEquals(g.getPoint(), near.getPoint(), "Närmaste Vehicle borde laddas.");
    assertNotEquals(g.getPoint(), far.getPoint(), "Bortre Vehicle borde inte lastas.");
  }

  @Test
  void doesNotLoadOutOfRange() {
    World w = new World(700, 400);

    Garage<Vehicle> g = track(new Garage<>(Vehicle.class, ORIGO, 5));
    w.addGarage(g);

    Vehicle outOfRange = track(new Volvo240());
    outOfRange.mutatePoint(100.0, 100.0);
    Point2D before = outOfRange.getPoint();

    w.addVehicle(outOfRange);

    assertFalse(w.loadNearestInto(g, 5.0));
    assertEquals(before, outOfRange.getPoint(), "Vehicle utanför radie får inte laddas.");
    assertEquals(5, g.getFreeSlots(), "Fria platser får inte upptas om inget laddas.");
  }

  @Test
  void unloadMovesVehicleToLeftOfGarage() {
    World w = new World(700, 400);

    Garage<Vehicle> g = track(new Garage<>(
      Vehicle.class,
      new Point2D.Double(10.0d, 20.0d),
      2
    ));
    w.addGarage(g);

    Vehicle v = track(new Volvo240());
    v.mutatePoint(10.1, 20.1);
    w.addVehicle(v);

    assertTrue(w.loadNearestInto(g, 5.0));
    assertEquals(g.getPoint(), v.getPoint(), "Vehicle borde lastas.");

    assertTrue(w.unloadFrom(g, v));
    assertEquals(9.0, v.getX(), 1e-12);
    assertEquals(20.0, v.getY(), 1e-12);
    assertEquals(2, g.getFreeSlots(), "Unload borde skapa fri plats.");
  }
}
