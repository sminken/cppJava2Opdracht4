package restaurantPackage;

/**
 * Representeert een ober die gereedstaande maaltijden uitserveert.
 * 
 * @author Steven Minken
 *
 */
public class Ober implements Runnable {

  private static int LOOPTIJD = 1000;
  private static int SERVEERTIJD = 1500;
  private static int WACHTTIJD = 1000;
  private String naam = null;
  private Uitgiftebalie uitgiftebalie = null;
  private volatile boolean stoppen = false;

  /**
   * constructor voor het aanmaken van een ober met naam en een referentie naar
   * de uitgiftebalie die private is.
   * 
   * @param naam
   *          naam van de ober
   * @param uitgiftebalie
   *          de uitgiftebalie
   */
  public Ober(String naam, Uitgiftebalie uitgiftebalie) {
    this.naam = naam;
    this.uitgiftebalie = uitgiftebalie;
  }

  /**
   * Beheert de thread van een instantie van een ober
   * 
   * @override overschrijft de run methode van Thread
   */
  @Override
  public void run() {

    try {
      System.out.println(naam + " is gestart");
      while (!stoppen) {
        Maaltijd maaltijd = uitgiftebalie.pakMaaltijd();
        if (maaltijd != null) {
          serveer(maaltijd);
        }
        else {
          Thread.sleep(WACHTTIJD);
        }
      }
      System.out.println(naam + " is gestopt");
    }
    catch (InterruptedException e) {
      System.out.println(e.getMessage() + " " + naam + " is onderbroken");
    }

  }

  /**
   * Methode verantwoordelijk voor het serveren van een maaltijd
   * 
   * @param maaltijd
   *          de maaltijd
   * @throws InterruptedException
   */
  private void serveer(Maaltijd maaltijd) throws InterruptedException {

    try {
      System.out.println(
          naam + " pakt " + maaltijd.toString() + " van de uitgiftebalie");
      System.out
          .println(naam + " loopt naar tafel " + maaltijd.getTafelnummer());
      Thread.sleep(LOOPTIJD * maaltijd.getTafelnummer());
      System.out.println(naam + " serveert " + maaltijd.toString());
      Thread.sleep(SERVEERTIJD);
      System.out.println(naam + " loopt terug naar de uitgiftebalie");
      Thread.sleep(LOOPTIJD * maaltijd.getTafelnummer());
      System.out.println(naam + " is weer bij de uitgiftebalie");
    }
    catch (InterruptedException e) {
      throw new InterruptedException(
          e.getMessage() + " " + naam + " is onderbroken");
    }

  }

  /**
   * Stopt deze ober thread
   */
  public void stoppen() {
    stoppen = true;
  }

}
