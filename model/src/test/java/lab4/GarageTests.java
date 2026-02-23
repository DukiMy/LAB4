package lab4;

import java.util.ArrayList;
import java.util.List;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


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
    Garage<Vehicle> g = track(new Garage<>(
      Vehicle.class,
      ORIGO,
      2)
    );

    Vehicle v1 = track(new Volvo240());
    Vehicle v2 = track(new Saab95());
    Vehicle v3 = track(new Scania());
    v1.mutatePoint(0.1, 0.1);
    v2.mutatePoint(0.2, 0.2);
    v3.mutatePoint(0.3, 0.3);

    assertEquals(2, g.getFreeSlots());

    g.load();
    assertEquals(1, g.getFreeSlots());

    g.load();
    assertEquals(0, g.getFreeSlots());

    Point2D before = v3.getPoint();
    g.load();
    assertEquals(0, g.getFreeSlots());
    assertEquals(
      before,
      v3.getPoint(),
      "Tredje Vehicle får inte lastas i ett fullt garage."
    );
  }

  @Test
  void loadsClosestVehicleInRange() {
    Garage<Vehicle> g = track(new Garage<Vehicle>(
      Vehicle.class,
      ORIGO,
      5)
    );

    Vehicle near = track(new Volvo240());
    Vehicle far  = track(new Saab95());
    near.mutatePoint(1.0, 1.0);
    far.mutatePoint(9.0, 0.0);

    g.load();

    assertEquals(
      g.getPoint(),
      near.getPoint(),
      "Närmaste Vehicle borde laddas."
    );

    assertNotEquals(
      g.getPoint(),
      far.getPoint(),
      "Bortre Vehicle borde inte lastas."
    );
  }

  @Test
  void doesNotLoadOutOfRange() {
    Garage<Vehicle> g = track(new Garage<Vehicle>(
      Vehicle.class,
      ORIGO,
      5)
    );

    Vehicle outOfRange = track(new Volvo240());
    outOfRange.mutatePoint(100.0, 100.0);
    Point2D before = outOfRange.getPoint();

    g.load();

    assertEquals(
      before,
      outOfRange.getPoint(),
      "Vehicle utanför radie får inte laddas."
    );

    assertEquals(
      5,
      g.getFreeSlots(),
      "Fria platser får inte upptas om inget laddas."
    );
  }

  @Test
  void doesNotLoadSameVehicleTwice() {
    Garage<Vehicle> g = track(new Garage<>(
      Vehicle.class,
      ORIGO,
      2)
    );

    Vehicle v = track(new Saab95());
    v.mutatePoint(0.1, 0.1);
    g.load();
    assertEquals(g.getPoint(), v.getPoint());
    assertEquals(1, g.getFreeSlots());

    g.load();
    assertEquals(
      1,
      g.getFreeSlots(),
      "Kapacitet bör inte förbrukas om samma Vehicle lastas två ggr"
    );
  }

  @Test
  void unloadMovesVehicleToLeftOfGarage() {
    Garage<Vehicle> g = track(new Garage<>(
      Vehicle.class,
      new Point2D.Double(10.0d, 20.0d),
      2)
    );

    Vehicle v = track(new Volvo240());
    v.mutatePoint(10.1, 20.1);
    g.load();
    assertEquals(g.getPoint(), v.getPoint(), "Vehicle borde inte lastas.");

    g.unLoad(v);
    assertEquals(9.0, v.getX(), 1e-12);
    assertEquals(20.0, v.getY(), 1e-12);
    assertEquals(
      2,
      g.getFreeSlots(),
      "Unload borde skapa fri plats."
    );
  }

  @Test
  void unloadOfVehicleNotInGarageDoesNothing() {
    Garage<Vehicle> g = track(new Garage<>(
      Vehicle.class,
      ORIGO,
      2)
    );

    Vehicle v = track(new Volvo240());
    v.mutatePoint(3.0, 3.0);
    Point2D before = v.getPoint();

    g.unLoad(v);
    assertEquals(
      before,
      v.getPoint(),
      "Vehicle som inte är i Garage får inte byta pos när den lastas av"
    );

    assertEquals(
      2,
      g.getFreeSlots(),
      "Kapaciteten får inte ändras"
    );
  }

  @Test
  void typeFilterLoadsOnlyVehiclesOfT() {
    Garage<Saab95> saabGarage = track(new Garage<>(
      Saab95.class,
      ORIGO,
      2)
    );

    Saab95 saab = track(new Saab95());
    Volvo240 volvo = track(new Volvo240());
    saab.mutatePoint(0.2, 0.2);
    volvo.mutatePoint(0.1, 0.1);

    saabGarage.load();

    assertEquals(
      saabGarage.getPoint(),
      saab.getPoint(),
      "Måste ladda Saab95, inte Volvo, även om Volvo står närmare."
    );

    assertNotEquals(
      saabGarage.getPoint(),
      volvo.getPoint(),
      "Fel typ får aldrig lastas."
    );
  }

  @Test
  void toStringIsOverridden() throws NoSuchMethodException {
    track(new Garage<>(
      Vehicle.class,
      new Point2D.Double(1.0d, 1.0d),
      10)
    );

    for (GameObject gObj : created) {
      String toString = gObj.toString();
      assertFalse(toString.isBlank());
      assertEquals(
        gObj.getClass(),
        gObj.getClass()
          .getMethod("toString")
          .getDeclaringClass()
      );

      out.println(gObj.toString());
    }
  }
}
