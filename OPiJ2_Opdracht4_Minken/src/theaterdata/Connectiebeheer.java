package theaterdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Beheert de connectie met de database. Bevat methoden voor openen en sluiten
 * van connectie met database, en voor opvragen van de connectie.
 * 
 * @author Open Universiteit
 */
public class Connectiebeheer {

  public static Connection connectie = null;

  /**
   * Maakt een connectie met de database en initialiseert Klantbeheer en
   * VoostellingBeheer.
   * 
   * @throws TheaterException als de initialisatie mislukt.
   */
  public static void openDB() throws TheaterException {

    try {
      connectie = DriverManager.getConnection(DBConst.URL,
          DBConst.GEBRUIKERSNAAM, DBConst.WACHTWOORD);
    }
    catch (SQLException e) {
      throw new TheaterException(
          "Probleem bij verbinden met de SQL database. De combinatie van de url, gebruikersnaam of wachtwoord is onjuist ");
    }

  }

  /**
   * Sluit de connectie met de database
   * 
   * @throws TheaterException
   */
  public static void closeDB() throws TheaterException {

    if (connectie != null) {
      try {
        connectie.close();
      }
      catch (SQLException e) {
        throw new TheaterException(
            "Probleem bij afsluiten van de SQL database: ");
      }
    }
  }

}
