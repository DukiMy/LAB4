/**
 *  Utfärdat av Durim Miziraj
 *  Kontakt: gusmizdu@student.gu.se
 */

package lab4;

import lab4.interfaces.Car;
import lab4.interfaces.TurboChargable;

import java.awt.geom.Point2D;
import static java.awt.Color.RED;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


public final class Saab95 extends Vehicle implements Car, TurboChargable{

	private boolean turboOn;

	public Saab95() {
		super(
      /* Number of doors */ 2,
      /* Engine power    */ 125.0d,
      /* Vehicle Color   */ RED,
      /* Vehicle model   */ "Saab95",
      /* Point           */ new Point2D.Double(0.0d, 0.0d)
    );

    turboOn = false;
	}

	public void setTurbo(final boolean state) { turboOn = state; }

	private double speedFactor() {
		return getEnginePower() * 0.01d * (turboOn ? 1.3d : 1.0d);
	}

  @Override
	protected double increasedSpeedFactor(final double speedIncrease) {
    return getCurrentSpeed() + speedFactor() * speedIncrease;
	}

	@Override
	protected double decreasedSpeedFactor(final double speedDecrease) {
    return getCurrentSpeed() - speedFactor() * speedDecrease;
	}

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
  }
}
