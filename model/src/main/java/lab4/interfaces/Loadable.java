/**
 *  Utfärdat av Durim Miziraj
 *  Kontakt: gusmizdu@student.gu.se
 *
 *  Gränssnittet avgränsar beteenden hos objekt som kan lasta in andra objekt.
 */

package lab4.interfaces;

public interface Loadable {
  void load();
  void unLoad();
  void printLoad();
  boolean canLoad();
}
