/**
 * WorldListnener.java
 */

package lab4.model;

import lab4.model.interfaces.WorldView;

@FunctionalInterface
public interface WorldListener {
  void worldChanged(WorldView view);
}
