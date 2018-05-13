package restaurantPackage;

/**
 * Klasse verantwoordelijk voor het beheer van de kok objecten
 * @author Steven Minken
 *
 */

import java.util.Random;

public class Kok extends Thread {

  private static long BEREIDINGSTIJD = 4000;
  private String naam = null;
  private Uitgiftebalie uitgiftebalie = null;
  private volatile boolean stoppen = false;

  /**
   * constructor voor het creeeren van een kok object en een referentie naar de
   * uitgiftebalie die private is
   * 
   * @param naam
   *          naam van de kok
   * @param balie
   *          de uitgiftebalie
   */
  public Kok(String naam, Uitgiftebalie uitgiftebalie) {
    this.naam = naam;
    this.uitgiftebalie = uitgiftebalie;
  }

  /**
   * @override overschrijft de run methode van Thread
   */
  @Override
  public void run() {

    System.out.println(naam + " is gestart");
    try {
      while (!stoppen) {
        Maaltijd maaltijd = kook();
        uitgiftebalie.plaatsMaaltijd(maaltijd);
        System.out.println(naam + " plaatst " + maaltijd.toString());
      }
      System.out.println(naam + " is gestopt");
    }
    catch (Exception e) {
      System.out.println(e.getMessage() + " " + naam + " is onderbroken");
    }
  }

  /**
   * Retourneert een nieuwe maaltijd
   * 
   * @return Maaltijd de nieuwe maaltijd
   * @throws InterruptedException
   */
  private Maaltijd kook() throws InterruptedException {
    try {
      System.out.println(naam + " is begonnen met koken van de maaltijd");
      Thread.sleep(BEREIDINGSTIJD);
      System.out.println(naam + " is klaar met koken.");
      return new Maaltijd(kiesGerecht(), kiesTafel());
    }
    catch (InterruptedException e) {
      throw new InterruptedException(
          e.getMessage() + " " + naam + " is onderbroken");
    }
  }

  /**
   * Retourneert een willekeurig tafelnummer waarvoor een maaltijd bereid gaat
   * worden.
   * 
   * @return int tafelnummer
   */
  public int kiesTafel() {
    return new Random().nextInt(Restaurant.AANTALTAFELS) + 1;
  }

  /**
   * Retourneert een willekeurig gerecht uit de gerechten van het menu
   * 
   * @return String naam van het gerecht
   */
  private String kiesGerecht() {
    int keuze = new Random().nextInt(3) + 1;
    switch (keuze) {
      case 1:
        return "Spaghetti bolognese";
      case 2:
        return "Thaise groene curry";
      case 3:
        return "Wortelstampot met gehaktbal";
      default:
        return "Kipsate";
    }
  }

  /**
   * Stopt deze kok thread
   */
  public void stoppen() {
    stoppen = true;
  }

}
