package lab4;

import lab4.model.interfaces.*;
import lab4.model.*;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehicleTest {
  private final List<Vehicle> created = new ArrayList<>();

  private <T extends Vehicle> T track(T obj) {
    created.add(obj);
    return obj;
  }

  @AfterEach
  void cleanup() {
    for (GameObject o : created) o.destroy();
    created.clear();
  }

  @Test
  void vehicleMovesForward() {
    double startX;
    double startY;

    track(new Volvo240());
    track(new Saab95());
    track(new VolvoFH16());

    for (Vehicle v : created) {
      startX = v.getX();
      startY = v.getY();

      v.startEngine();
      v.gas(0.3);
      v.move();

      assertTrue(
        v.getX() != startX || v.getY() != startY,
        String.format(
          "%s flyttade inte när 'move()' anropades.",
          v.getModelName()
        )
      );
    }
  }

  @Test
  void turningChangesDirection() {
    double dirBeforeTurn;
    double dirAfterTurn;
    boolean hasLeftTurn;
    boolean hasRightTurn;

    track(new Volvo240());
    track(new Saab95());
    track(new VolvoFH16());

    for (Vehicle v : created) {
      dirBeforeTurn = v.getDirection();
      v.turnLeft();
      dirAfterTurn = v.getDirection();
      hasLeftTurn = (dirBeforeTurn != dirAfterTurn);

      dirBeforeTurn = v.getDirection();
      v.turnRight();
      dirAfterTurn = v.getDirection();
      hasRightTurn = (dirBeforeTurn != dirAfterTurn);

      assertTrue(
        hasLeftTurn && hasRightTurn,
        String.format(
          "%s ändrade inte riktning.",
          v.getModelName()
        )
      );
    }
  }

  @Test
  void brakeReducesSpeed() {
    double speedBeforeBraking;
    double speedAfterBreaking;

    track(new Volvo240());
    track(new Saab95());
    track(new VolvoFH16());
    track(new Scania());

    for (Vehicle v : created) {
      v.startEngine();
      v.gas(0.5);
      v.move();
      speedBeforeBraking = v.getCurrentSpeed();

      v.brake(0.5);
      speedAfterBreaking = v.getCurrentSpeed();

      assertTrue(
        speedAfterBreaking < speedBeforeBraking,
        String.format(
          "Farten sänktes inte när %s bromsade in.",
          v.getModelName()
        )
      );
    }
  }

  @Test
  void gasRejectsOutOfRange() {
    track(new Volvo240());
    track(new Saab95());
    track(new VolvoFH16());
    track(new Scania());

    for (Vehicle v : created) {
      assertThrows(IllegalArgumentException.class, () -> v.gas(-0.0001));
      assertThrows(IllegalArgumentException.class, () -> v.gas( 1.0001));
    }
  }

  @Test
  void brakeRejectsOutOfRange() {
    track(new Volvo240());
    track(new Saab95());
    track(new VolvoFH16());
    track(new Scania());

    for (Vehicle v : created) {
      assertThrows(IllegalArgumentException.class, () -> v.brake(-0.0001d));
      assertThrows(IllegalArgumentException.class, () -> v.brake( 1.0001d));
    }
  }

  @Test
  void turboIncreasesSpeed() {
    double speedIncrease = 0.3d;

    Vehicle v = track(new Saab95());
    double speedBefore;

    v.startEngine();
    v.gas(speedIncrease);
    speedBefore = v.getCurrentSpeed();
    v.stopEngine();

    TurboChargable turboV = (TurboChargable) v;
    turboV.setTurbo(true);

    double speedAfter;
    v = (Saab95)turboV;
    v.startEngine();
    v.gas(speedIncrease);
    speedAfter = v.getCurrentSpeed();
    v.stopEngine();

    assertTrue(
      speedBefore < speedAfter,
      "Turbon ökade inte farten."
    );
  }

  @Test
  void setCurrentSpeedBoundsWork() {
    track(new Volvo240());
    track(new Saab95());
    track(new VolvoFH16());
    track(new Scania());

    for (Vehicle v : created) {
      v.startEngine();
      for (int i = 0; i < 150; i++) {
        v.gas(1.0d);
      }

      assertTrue(
        v.getCurrentSpeed() == v.getEnginePower(),
         "'setCurrentSpeed' håller inte övre fartgräns"
      );

      v.stopEngine();

      for (int i = 0; i < 100; i++) {
        v.brake(1.0d);
      }

      assertTrue(
        v.getCurrentSpeed() == 0.0d,
      "'setCurrentSpeed' håller inte nedre fartgräns"
      );
    }
  }

  @Test
  void colorIsMutatedAndAccessed() {
    Color c;

    track(new Volvo240());
    track(new Saab95());
    track(new VolvoFH16());
    track(new Scania());

    for (Vehicle v : created) {
      c = new Color(0);
      v.setColor(c.getRGB());

      assertTrue(
        v.getColor() == c.getRGB(),
        "Färg kunnde inte muteras eller hämtas"
      );
    }
  }

  @Test
  void vehicleHasDoors() {
    track(new Volvo240());
    track(new Saab95());
    track(new VolvoFH16());
    track(new Scania());

    for (Vehicle v : created) {
      assertTrue(
        0 < v.getNrDoors(),
        "Vehicle " + v.getModelName() + " har inga dörrar."
      );
    }
  }
}
