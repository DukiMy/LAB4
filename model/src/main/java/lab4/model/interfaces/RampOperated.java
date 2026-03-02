/**
 *  Utfärdat av Durim Miziraj
 *  Kontakt: gusmizdu@student.gu.se
 *
 *  Gränssnittet avgränsar beteendet hos objekt med ned- och uppfällbara
 *  rampar.
 */

package lab4.model.interfaces;

public interface RampOperated extends Loadable {
  void lowerRamp();
  void raiseRamp();
  boolean isRampLowered();
}
