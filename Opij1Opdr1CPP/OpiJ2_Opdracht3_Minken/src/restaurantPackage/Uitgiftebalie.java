package restaurantPackage;

import java.util.ArrayList;

/**
 * Verantwoordelijk voor het beheer van de uitgiftebalie. Beheert de
 * first-in-first-out que van maaltijden.
 * 
 * @author Steven Minken
 *
 */
public class Uitgiftebalie {

  private ArrayList<Maaltijd> maaltijdLijst = new ArrayList<Maaltijd>();

  /**
   * constructor voor het aanmaken van een uitgiftebalie
   */
  public Uitgiftebalie() {

  }

  /**
   * Plaatst een maaltijd aan het einde van de ArrayList
   * 
   * @param maaltijd
   *          de maaltijd
   */
  public synchronized void plaatsMaaltijd(Maaltijd maaltijd) {
    maaltijdLijst.add(maaltijd);
  }

  /**
   * Retourneert de eerste maaltijd van de maaltijdlijst
   * 
   * @return Maaltijd de maaltijd
   */
  public synchronized Maaltijd pakMaaltijd() {

    if (!maaltijdLijst.isEmpty()) {
      return maaltijdLijst.remove(0);
    }
    else {
      return null;
    }

  }

  /**
   * Retourneert het aantal maaltijden op de balie
   * 
   * @return int het aantal maaltijden op de balie
   */
  public int aantalMaaltijdenOpDeBalie() {
    return maaltijdLijst.size();
  }

}
