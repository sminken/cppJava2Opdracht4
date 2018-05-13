package restaurantPackage;

/**
 * Klasse Restaurant beheert het restaurant. Verantwoordelijk voor het beheer
 * van de uitgiftebalie en het beheer van het aantal tafels.
 * 
 * @author Steven Minken
 *
 */
public class Restaurant {

  protected static final int AANTALTAFELS = 10;
  private static final int SIMULATIETIJD = 120000;
  private Uitgiftebalie uitgiftebalie = null;

  /**
   * Constructor voor het creeeren van een restaurant met een uitgiftebalie
   */
  public Restaurant() {
    uitgiftebalie = new Uitgiftebalie();
  }

  /**
   * De main methode waarin het restaurant, de koks en ober gecreeert, gestart
   * en gestopt worden.
   * 
   * @param args
   */
  public static void main(String[] args) {

    Restaurant restaurant = new Restaurant();

    // Kok extends Thread
    Kok kok1 = new Kok("Kok 1", restaurant.uitgiftebalie);
    Kok kok2 = new Kok("Kok 2", restaurant.uitgiftebalie);

    // De threads van de koks worden gestart
    kok1.start();
    kok2.start();

    // Ober implements Runnable
    Ober ober1 = new Ober("Ober 1", restaurant.uitgiftebalie);
    Ober ober2 = new Ober("Ober 2", restaurant.uitgiftebalie);
    Ober ober3 = new Ober("Ober 3", restaurant.uitgiftebalie);

    // De threads van de obers worden gestart
    Thread ober1Thread = new Thread(ober1);
    ober1Thread.start();
    Thread ober2Thread = new Thread(ober2);
    ober2Thread.start();
    Thread ober3Thread = new Thread(ober3);
    ober3Thread.start();

    // Nadat de simulatietijd is verstreken worden de laatste handelingen van de
    // koks en obers afgerond en de threads gestopt
    try {
      Thread.sleep(Restaurant.SIMULATIETIJD);
      // Roept stoppen() aan op de koks om de instanties te laten stoppen
      kok1.stoppen();
      kok2.stoppen();
      // Roept stoppen() aan op de obers om de instanties te laten stoppen
      ober1.stoppen();
      ober2.stoppen();
      ober3.stoppen();
      Thread.sleep(14000);
      System.out.println("Programma succesvol gestopt, er staan nog "
          + restaurant.uitgiftebalie.aantalMaaltijdenOpDeBalie()
          + " maaltijden op de balie.");
    }
    catch (InterruptedException e) {
      System.out.println(e.getMessage() + ", het programma is onderbroken");
    }

  }

}
