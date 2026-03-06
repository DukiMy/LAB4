/**
 * RandomVehicleFactory.java
 */

package lab4.model;

import lab4.model.interfaces.VehicleFactory;

import java.util.Random;

public final class RandomVehicleFactory implements VehicleFactory {
  private final Random rng = new Random();

  @Override
  public Vehicle createRandom() {
    return switch (rng.nextInt(4)) {
      case 0 -> new Volvo240();
      case 1 -> new Saab95();
      case 2 -> new Scania();
      default -> new VolvoFH16();
    };
  }
}
