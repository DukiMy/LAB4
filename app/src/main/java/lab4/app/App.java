/**
 * App.java
 */

package lab4.app;

import lab4.model.World;
import lab4.ui.*;

import javax.swing.SwingUtilities;

public final class App {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {

      World world = new World(700, 700);

      Toolbar toolbar = new Toolbar();
      Canvas canvas = new Canvas(700, 700);
      Frame frame = new Frame(toolbar, canvas);

      WorldSnapshotPublisher publisher = new WorldSnapshotPublisher();
      publisher.addListener(canvas);
      world.addListener(publisher);

      CarController controller = new CarController(world);
      controller.bind(toolbar);

      frame.setVisible(true);
    });
  }
}
