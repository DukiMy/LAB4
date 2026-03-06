/**
 * WorldSnapshotPublisher.java
 */

package lab4.app;

import lab4.model.interfaces.WorldView;
import lab4.model.WorldListener;
import lab4.ui.render.*;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class WorldSnapshotPublisher implements WorldListener {

  private final CopyOnWriteArrayList<SnapshotListener> listeners = new CopyOnWriteArrayList<>();

  public void addListener(SnapshotListener l) { listeners.add(l); }

  @Override
  public void worldChanged(WorldView view) {
    WorldSnapshot snap = toSnapshot(view);
    if (SwingUtilities.isEventDispatchThread()) publish(snap);
    else SwingUtilities.invokeLater(() -> publish(snap));
  }

  private void publish(WorldSnapshot snap) {
    for (SnapshotListener l : listeners) l.snapshotChanged(snap);
  }

  private static WorldSnapshot toSnapshot(WorldView w) {
    List<VehicleSnapshot> vs = w.getVehicles().stream()
      .map(v -> new VehicleSnapshot(
        v.getClass().getSimpleName(),
        v.getX(), v.getY(), v.getDirection()
      ))
      .toList();

    List<GarageSnapshot> gs = w.getGarages().stream()
      .map(g -> new GarageSnapshot(g.getClass().getSimpleName(),
      g.getX(), g.getY(), g.getFreeSlots(), g.getCapacity()))
      .toList();

    return new WorldSnapshot(w.getWidth(), w.getHeight(), vs, gs);
  }
}
