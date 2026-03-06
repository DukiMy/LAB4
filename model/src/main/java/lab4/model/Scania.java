package lab4.model;

import lab4.model.interfaces.RampOperated;
import lab4.model.interfaces.Tippable;

import java.awt.geom.Point2D;

import static java.awt.Color.BLUE;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.validState;

public final class Scania extends ConditionallyMovableVehicle implements RampOperated, Tippable {

  private byte tipBedAngle = 0;
  private boolean rampLowered = false;

  public Scania() {
    super(2, 200.0d, BLUE, "Scania", new Point2D.Double(0.0d, 0.0d));
  }

  @Override
  public void setTipBedAngle(final byte angle) {
    isTrue(0 <= angle && angle <= 70);
    validState(getCurrentSpeed() == 0.0d);
    tipBedAngle = angle;
  }

  @Override
  public byte getTipBedAngle() { return tipBedAngle; }

  @Override
  protected boolean canMove() {
    return tipBedAngle == 0 && !rampLowered;
  }

  @Override
  protected double speedFactor() { return getEnginePower() * 0.01d; }

  @Override
  public void lowerRamp() {
    if (getCurrentSpeed() != 0.0d) return;
    rampLowered = true;
  }

  @Override
  public void raiseRamp() { rampLowered = false; }

  @Override
  public boolean isRampLowered() { return rampLowered; }

  @Override
  public boolean canLoad() {
    return getCurrentSpeed() == 0.0d && rampLowered;
  }

  @Override public void load() {}
  @Override public void unLoad() {}
  @Override public void printLoad() {}
}
