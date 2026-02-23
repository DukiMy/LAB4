/**
 *  Utfärdat av Durim Miziraj
 *  Kontakt: gusmizdu@student.gu.se
 */

package lab4;

import lab4.interfaces.Tippable;

import java.awt.geom.Point2D;
import static java.awt.Color.BLUE;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.validState;



public final class Scania extends ConditionallyMovableVehicle implements Tippable {
  private byte tipBedAngle = 0;

  public Scania() {
    super(
      /* Number of doors */ 2,
      /* Engine power    */ 200.0d,
      /* Vehicle color   */ BLUE,
      /* Vehicle model   */ "Scania",
      /* X position      */ new Point2D.Double(0.0d, 0.0d)
     );

    tipBedAngle = 0;
  }

  public void setTipBedAngle(final byte angle) {
    double speed = getCurrentSpeed();

    isTrue(0 <= angle && angle <= 70);
    validState(speed == 0.0d);

    tipBedAngle = angle;
  }

  public byte getTipBedAngle() { return tipBedAngle; }

  @Override
  public boolean canMove() { return tipBedAngle == 0; }

  @Override
  protected double speedFactor() { return getEnginePower() * 0.01d; }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
  }
}
