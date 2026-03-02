/**
 * File: Vehile.java
 *
 *  getDirection()
 *  getNrDoors()
 *  getEnginePower()
 *  getCurrentSpeed()
 *  getColor()
 *  setColor(int)
 *  getModelName()
 *  startEngine()
 *  stopEngine()
 *  turnLeft()
 *  turnRight()
 *
 */

package lab4.model;

import lab4.model.interfaces.Movable;

import java.awt.Color;
import java.awt.geom.Point2D;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Objects.requireNonNull;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.finite;
import static org.apache.commons.lang3.Validate.notBlank;

public abstract class Vehicle extends GameObject implements Movable {

  private static final double TURN_RATE = 0.1d;
	private final int nrDoors;
  private double direction;
	private double enginePower;
	private double currentSpeed;
	private Color color;
	private String modelName;

	protected Vehicle(
    final int nrDoors,
    final double enginePower,
    final Color color,
    final String modelName,
    final Point2D point) {
    super(point);

    inclusiveBetween(1, 5, nrDoors);
    isTrue(enginePower > 0.0d);
    finite(enginePower);
    requireNonNull(color);
    notBlank(modelName);
    finite(point.getX());
    finite(point.getY());

    this.direction = 0.0d;
		this.nrDoors = nrDoors;
		this.enginePower = enginePower;
		this.color = color;
		this.modelName = modelName;
	  stopEngine();
  }

  public double getDirection() { return direction; }

	public int getNrDoors() { return nrDoors; }

	public double getEnginePower() { return enginePower; }

	public double getCurrentSpeed() { return currentSpeed; }

	public int getColor() { return color.getRGB(); }

  public String getModelName() { return modelName; }

  public void setColor(final int c) { color = new Color(c); }

	public void startEngine() { setCurrentSpeed(0.1d); }

	public void stopEngine() { setCurrentSpeed(0.0d); }

  public void turnLeft() { direction += TURN_RATE; }

  public void turnRight() { direction -= TURN_RATE; }

  private static final void validate(
    final double lowBound,
    final double highBound,
    final double arg) {

    finite(arg);
    isTrue(lowBound <= arg && arg <= highBound);
  }

  public void brake(final double speedDecrease) {
    validate(0.0d, 1.0d, speedDecrease);
    decreaseSpeed(speedDecrease);
	}

  public void gas(final double speedIncrease) {
    validate(0.0d, 1.0d, speedIncrease);
    increaseSpeed(speedIncrease);
	}

  public void move() {
    super.mutatePoint(
      super.getX() + sin(direction) * getCurrentSpeed(),
      super.getY() + cos(direction) * getCurrentSpeed()
    );
  }

  private void setCurrentSpeed(final double setSpeed) {
    if (setSpeed > getEnginePower()) {
      currentSpeed = getEnginePower();
      return;

    } else if (setSpeed < 0.0d) {
      currentSpeed = 0.0d;

    } else {
      currentSpeed = setSpeed;
    }
  }

	private void increaseSpeed(final double speedIncrease) {
		setCurrentSpeed(
      increasedSpeedFactor(speedIncrease)
    );
	}

	private void decreaseSpeed(final double speedDecrease) {
		setCurrentSpeed(
      decreasedSpeedFactor(speedDecrease)
    );
	}

  public void invertDirection() {
  direction = direction + Math.PI;
  direction = direction % (2.0d * Math.PI);
  if (direction < 0.0d) direction += 2.0d * Math.PI;
}


  protected abstract double increasedSpeedFactor(final double speedIncrease);
  protected abstract double decreasedSpeedFactor(final double speedDecrease);
}
