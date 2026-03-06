/**
 * CarController.java
 */

package lab4.app;

import lab4.model.*;
import lab4.model.interfaces.VehicleFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.Random;

public final class CarController {

  private final World world;
  private final Timer timer;
  private final Random rng = new Random();

  private final VehicleFactory vehicleFactory = new RandomVehicleFactory();
  private final Garage<Vehicle> garage = new Garage<>(Vehicle.class, new Point2D.Double(100, 100), 5);
  private final VolvoFH16 carrier = new VolvoFH16();

  public CarController(World world) {
    this.world = world;
    this.timer = new Timer(40, e -> world.step());

    world.addGarage(garage);

    carrier.mutatePoint(200, 200);
    world.addVehicle(carrier);
  }

  public void bind(lab4.ui.Toolbar tb) {
    tb.setStartAction(new AbstractAction("Start") {
      @Override public void actionPerformed(ActionEvent e) { timer.start(); }
    });

    tb.setStopAction(new AbstractAction("Stop") {
      @Override public void actionPerformed(ActionEvent e) { timer.stop(); }
    });

    tb.setTurnLeftAction(new AbstractAction("Turn left") {
      @Override
      public void actionPerformed(ActionEvent e) {
        world.turnAllLeft();
      }
    });

    tb.setTurnRightAction(new AbstractAction("Turn right") {
      @Override
      public void actionPerformed(ActionEvent e) {
        world.turnAllRight();
      }
    });

    tb.setAddVolvo240Action(new AbstractAction("Add Volvo240") {
      @Override public void actionPerformed(ActionEvent e) { addRandom(new Volvo240()); }
    });

    tb.setAddSaab95Action(new AbstractAction("Add Saab95") {
      @Override public void actionPerformed(ActionEvent e) { addRandom(new Saab95()); }
    });

    tb.setAddScaniaAction(new AbstractAction("Add Scania") {
      @Override public void actionPerformed(ActionEvent e) { addRandom(new Scania()); }
    });

    tb.setAddVolvoFH16Action(new AbstractAction("Add VolvoFH16") {
      @Override public void actionPerformed(ActionEvent e) { addRandom(new VolvoFH16()); }
    });

    tb.setBrakeAction(new AbstractAction("Brake") {
      @Override
      public void actionPerformed(ActionEvent e) {
        double amount = tb.getGasAmount() / 100.0;
        world.brakeAll(amount);
      }
    });

    tb.setAddCarAction(new AbstractAction("Add car") {
      @Override
      public void actionPerformed(ActionEvent e) {
        Vehicle v = vehicleFactory.createRandom();
        addRandom(v);
      }
    });

    tb.setRemoveCarAction(new AbstractAction("Remove car") {
      @Override
      public void actionPerformed(ActionEvent e) {
        world.removeLastVehicle();
      }
    });

    tb.setGasAction(new AbstractAction("Gas") {
      @Override
      public void actionPerformed(ActionEvent e) {
        double amount = tb.getGasAmount() / 100.0;
        world.gasAll(amount);
      }
    });
  }



  private void addRandom(Vehicle v) {
    if (!world.canAddVehicle()) return;

    double x = 20 + rng.nextInt(Math.max(1, world.getWidth() - 40));
    double y = 20 + rng.nextInt(Math.max(1, world.getHeight() - 40));
    v.mutatePoint(x, y);
    world.addVehicle(v);
  }
}
