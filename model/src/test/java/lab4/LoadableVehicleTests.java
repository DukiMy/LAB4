package lab4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class LoadableVehicleTests {
  private final List<GameObject> created = new ArrayList<>();

  private <T extends GameObject> T track(T obj) {
    created.add(obj);
    return obj;
  }

  @AfterEach
  void cleanup() {
    for (GameObject o : created) {
      o.destroy();
    }
    created.clear();
  }

  @Test
  void rampStateChanges() {
    VolvoFH16 carrier = track(new VolvoFH16());

    carrier.lowerRamp();
    assertTrue(carrier.canLoad(), "Trailer bör kunna lastas.");
    assertTrue(carrier.isRampLowered(), "Rampen bör vara nedfälld.");

    carrier.raiseRamp();
    assertFalse(carrier.canLoad(), "Trailer bör inte kunna lastas.");
    assertFalse(carrier.isRampLowered(), "Rampen bör vara uppfälld.");
  }

  @Test
  void canNotMoveWhileRampLowered() {
    VolvoFH16 carrier = track(new VolvoFH16());

    carrier.lowerRamp();
    Point2D before = carrier.getPoint();

    carrier.startEngine();
    carrier.gas(0.5);
    carrier.move();

    Point2D after = carrier.getPoint();
    assertEquals(
      before,
      after,
      "Trailer bör inte byta pos när rampen är nere."
    );
  }

  @Test
  void canNotLowerRampWhileMoving() {
    VolvoFH16 carrier = track(new VolvoFH16());

    carrier.startEngine();
    carrier.gas(0.5);
    carrier.move();

    carrier.lowerRamp();
    assertFalse(
      carrier.isRampLowered(),
      "Rampen bör inte sänkas om bilen rör sig."
    );
  }

  @Test
  void canNotLoadWhileRampIsRaised() {
    VolvoFH16 carrier = track(new VolvoFH16());
    Saab95 car = track(new Saab95());

    car.mutatePoint(0.1d, 0.1d);
    Point2D carBefore = car.getPoint();

    carrier.raiseRamp();
    carrier.load();

    assertEquals(
      carBefore,
      car.getPoint(),
      "Bil får inte lastas på när rampen är uppe."
    );

    carrier.startEngine();
    carrier.gas(0.5);
    carrier.move();
    assertEquals(
      carBefore,
      car.getPoint(),
      "Bil får inte följa trailer om den inte lastats."
    );
  }

  @Test
  void canNotUnloadWhileRampIsRaised() {
    VolvoFH16 carrier = track(new VolvoFH16());
    Saab95 car = track(new Saab95());

    car.mutatePoint(0.1d, 0.1d);

    carrier.lowerRamp();
    carrier.load();
    assertEquals(
      carrier.getPoint(),
      car.getPoint(),
      "Bil borde lastas."
    );

    carrier.raiseRamp();
    Point2D carBefore = car.getPoint();
    carrier.unLoad();

    assertEquals(
      carBefore,
      car.getPoint(),
      "Bil får inte lastas av när rampen är uppe."
    );
  }

  @Test
  void canNotLoadVehicleOutOfRange() {
    VolvoFH16 carrier = track(new VolvoFH16());
    Saab95 car = track(new Saab95());

    car.mutatePoint(100.0d, 100.0d);
    Point2D carBefore = car.getPoint();

    carrier.lowerRamp();
    carrier.load();

    assertEquals(
      carBefore,
      car.getPoint(),
      "Bil får inte lastas om den är utanför radien."
    );
  }

  @Test
  void canLoadVehicle() {
    VolvoFH16 carrier = track(new VolvoFH16());
    Saab95 car = track(new Saab95());

    car.mutatePoint(0.1d, 0.1d);

    carrier.lowerRamp();
    carrier.load();

    assertEquals(
      carrier.getPoint(),
      car.getPoint(),
      "Position på lastad bil borde muteras till samma pos som trailer."
    );

    carrier.raiseRamp();
    carrier.startEngine();
    carrier.gas(1.0);
    carrier.move();

    assertEquals(
      carrier.getPoint(),
      car.getPoint(),
      "Lastad bil borde ha samma position som trailern."
    );
  }

  @Test
  void canUnloadVehicle() {
    VolvoFH16 carrier = track(new VolvoFH16());
    Saab95 car = track(new Saab95());

    car.mutatePoint(0.1d, 0.1d);
    carrier.lowerRamp();
    carrier.load();
    assertEquals(
      carrier.getPoint(),
      car.getPoint(),
      "Bil borde inte lastas."
    );

    Point2D carrierPos = carrier.getPoint();
    carrier.unLoad();

    assertEquals(carrierPos.getX() - 1.0, car.getX(), 1e-12);
    assertEquals(carrierPos.getY(), car.getY(), 1e-12);
  }

  @Test
  void canNotLoadWhenCarrierIsFull() {
    VolvoFH16 carrier = track(new VolvoFH16());
    carrier.lowerRamp();

    Saab95[] loaded = new Saab95[6];
    for (int i = 0; i < 6; i++) {
      loaded[i] = track(new Saab95());
      loaded[i].mutatePoint(0.1 + i * 0.01, 0.1 + i * 0.01);
      carrier.load();
      assertEquals(
        carrier.getPoint(),
        loaded[i].getPoint(),
        "Bil borde inte lastas."
      );
    }

    Saab95 seventh = track(new Saab95());
    seventh.mutatePoint(0.2, 0.2);
    Point2D before = seventh.getPoint();

    carrier.load();

    assertEquals(
      before,
      seventh.getPoint(),
      "En sjunde bil får inta lastas på en full trailer"
    );
  }

  @Test
  void canNotUnloadWhenCargoIsEmpty() {
    VolvoFH16 carrier = track(new VolvoFH16());
    carrier.lowerRamp();

    Point2D carrierBefore = carrier.getPoint();
    assertDoesNotThrow(carrier::unLoad);

    assertEquals(carrierBefore, carrier.getPoint());
  }
}
