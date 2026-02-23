package lab4;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import lab4.interfaces.Tippable;

public class TippableVehiclesTest {
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
  void tipBedChangesAngle() {
    Tippable tippable = track(new Scania());
    byte angle = 70;
    double before = tippable.getTipBedAngle();
    double after;

    tippable.setTipBedAngle(angle);
    after = tippable.getTipBedAngle();

    assertTrue(
      before != after,
      "Flaket borde byta vinkel."
    );
  }

  @Test
  void outsideTipBedAngleBoundsThrowsException() {
    Tippable tippable = track(new Scania());
    byte lowerAngleBound = -1;
    byte upperAngleBound = 71;

    assertThrows(
      IllegalArgumentException.class,
        () -> tippable.setTipBedAngle(lowerAngleBound),
        "IllegalArgumentException borde kastas."
    );

    assertThrows(
      IllegalArgumentException.class,
        () -> tippable.setTipBedAngle(upperAngleBound),
        "IllegalArgumentException borde kastas."
    );
  }

  @Test
  void cantMoveWhileTipped() {
    ConditionallyMovableVehicle scania = track(new Scania());
    Tippable tipBed = (Tippable) scania;
    byte lowestTipAngle = 1;
    byte highestTipAngle = 70;
    Point2D beforeMoveAttempt;
    Point2D afterMoveAttempt;

    tipBed.setTipBedAngle(lowestTipAngle);
    beforeMoveAttempt = scania.getPoint();

    scania.startEngine();
    scania.gas(0.5d);
    scania.move();
    afterMoveAttempt = scania.getPoint();

    assertEquals(
      beforeMoveAttempt,
      afterMoveAttempt,
      "Bilen borde inte röra sig när flaket är tippat till 1 vinkelgrad"
    );

    scania.stopEngine();
    tipBed.setTipBedAngle(highestTipAngle);

    beforeMoveAttempt = scania.getPoint();

    scania.startEngine();
    scania.gas(0.5d);
    scania.move();

    afterMoveAttempt = scania.getPoint();

    assertEquals(
      beforeMoveAttempt,
      afterMoveAttempt,
      "Bilen borde inte röra sig när flaket är tippat till max vinkelgrad."
    );
  }

  @Test
  void cantTipWhileMoving() {
    ConditionallyMovableVehicle scania = track(new Scania());
    Tippable tipBed = (Tippable) scania;
    byte lowestTipAngle = 1;
    byte highestTipAngle = 70;

    scania.startEngine();
    scania.gas(0.5d);
    scania.move();

    assertThrows(
      IllegalStateException.class,
      () -> tipBed.setTipBedAngle(lowestTipAngle),
      "IllegalStateException bör kastas."
    );

    assertThrows(
      IllegalStateException.class,
      () -> tipBed.setTipBedAngle(highestTipAngle),
      "IllegalStateException bör kastas."
    );
  }
}
