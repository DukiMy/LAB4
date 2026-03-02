/**
 *  Utfärdat av Durim Miziraj
 *  Kontakt: gusmizdu@student.gu.se
 *
 *  Gränssnittet avgränsar beteenden hos objekt med tippbara flak.
 */

package lab4.model.interfaces;

public interface Tippable extends Movable{

  void setTipBedAngle(byte angle);
  byte getTipBedAngle();
}
