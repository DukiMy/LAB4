package lab4;

import lab4.model.GameObject;
import lab4.model.Scania;
import lab4.model.interfaces.RampOperated;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadableVehicleTests {

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
  void rampStateChanges() {
    RampOperated v = track(new Scania());

    v.lowerRamp();
    assertTrue(v.canLoad(), "Fordon bör kunna lastas när rampen är nere och hastigheten är 0.");
    assertTrue(v.isRampLowered(), "Rampen bör vara nedfälld.");

    v.raiseRamp();
    assertFalse(v.canLoad(), "Fordon bör inte kunna lastas när rampen är uppe.");
    assertFalse(v.isRampLowered(), "Rampen bör vara uppfälld.");
  }

  @Test
  void canNotTipOrRampWhileMovingContractHolds() {
    Scania scania = track(new Scania());

    scania.startEngine();
    scania.gas(0.5);
    scania.move();

    // Lowering ramp while moving should do nothing
    scania.lowerRamp();
    assertFalse(scania.isRampLowered());

    // Tip while moving should throw by your Scania implementation
    assertThrows(IllegalStateException.class, () -> scania.setTipBedAngle((byte) 1));
  }

  @Test
  void rampPreventsMovement() {
    Scania scania = track(new Scania());
    scania.lowerRamp();

    Point2D before = scania.getPoint();

    scania.startEngine();
    scania.gas(0.5);
    scania.move();

    Point2D after = scania.getPoint();
    assertEquals(before, after, "Scania bör inte röra sig när rampen är nere.");
  }
}
