/*
 * File: Volvo240.java
 */

package lab4.model;

import static java.awt.Color.BLACK;

import lab4.model.interfaces.Car;

import java.awt.geom.Point2D;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


public final class Volvo240 extends Vehicle implements Car{

	private static final double TRIM_FACTOR = 1.25d;

	public Volvo240() {
		super(
      /* Number of doors */ 4,
      /* Engine power    */ 100.0d,
      /* Vehicle color   */ BLACK,
      /* vehicle model   */ "Volvo240",
      /* Point           */ new Point2D.Double(0.0d, 0.0d)
     );
	}

	private double speedFactor() {
		return getEnginePower() * 0.01d * TRIM_FACTOR;
	}

  @Override
	protected double increasedSpeedFactor(final double speedIncrease) {
    return Math.min(getCurrentSpeed() + speedFactor() * speedIncrease, getEnginePower());
	}

	@Override
	protected double decreasedSpeedFactor(final double speedDecrease) {
    return Math.max(getCurrentSpeed() - speedFactor() * speedDecrease, 0.0d);
	}

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
  }
}
