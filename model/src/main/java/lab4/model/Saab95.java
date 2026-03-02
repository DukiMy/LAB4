/**
 * File: Saab95.java
 *
 * API:
 *  setTurbo(boolean state)
 *
*/

package lab4.model;

import lab4.model.interfaces.Car;
import lab4.model.interfaces.TurboChargable;

import java.awt.geom.Point2D;
import static java.awt.Color.RED;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


public final class Saab95 extends Vehicle implements Car, TurboChargable{

	private boolean turboState;

	public Saab95() {
		super(
      /* Number of doors */ 2,
      /* Engine power    */ 125.0d,
      /* Vehicle Color   */ RED,
      /* Vehicle model   */ "Saab95",
      /* Point           */ new Point2D.Double(0.0d, 0.0d)
    );

    turboState = false;
	}

	public void setTurbo(final boolean state) { turboState = state; }

	private double speedFactor() {
		return getEnginePower() * 0.01d * (turboState ? 1.3d : 1.0d);
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
