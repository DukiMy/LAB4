/**
 *  Utfärdat av Durim Miziraj
 *  Kontakt: gusmizdu@student.gu.se
 *
 *  Gränssnittet avgränsar beteenden hos objekt med avstängbar turbo.
 */

package lab4.model.interfaces;

public interface TurboChargable extends Movable {
  public void setTurbo(boolean state);
}
