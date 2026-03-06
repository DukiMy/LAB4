/**
 * WorldView.java
 */

package lab4.model.interfaces;

import lab4.model.Vehicle;
import lab4.model.Garage;

import java.util.List;

public interface WorldView {
  List<Vehicle> getVehicles();
  List<Garage<?>> getGarages();
  int getWidth();
  int getHeight();
}
