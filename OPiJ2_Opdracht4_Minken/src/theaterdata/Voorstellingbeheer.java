package theaterdata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import theater.Klant;
import theater.Voorstelling;

/**
 * Klasse die de voorstellingen beheert. Op elke datum is er maar één
 * voorstelling. Voorstellingen worden ingelezen uit de database.
 */
public class Voorstellingbeheer {

  private static PreparedStatement pselectvoorstellingsdata = null;
  private static PreparedStatement pselectvoorstelling = null;
  private static PreparedStatement pselectDatabasePlaatsen = null;
  private static PreparedStatement pselectDatabaseKlanten = null;
  private static PreparedStatement pinsertSchrijfBezetting = null;

  /**
   * Roept funcie aan om de prepared statements te initialiseren
   * 
   * @throws TheaterException
   */
  public static void init() throws TheaterException {
    Voorstellingbeheer.initialiseerPrepStatements();
  }

  /**
   * Initialiseert PreparedStatements voor de SQL-opdrachten om alle
   * voorstellingsdata uit te lezen bij een gegeven datum
   * 
   * @throws TheaterException
   *           als de SQL-opdracht een fout bevat of niet gecompileerd kan
   *           worden.
   */
  private static void initialiseerPrepStatements() throws TheaterException {

    try {
      pselectvoorstellingsdata = Connectiebeheer.connectie
          .prepareStatement("SELECT voorstelling.datum FROM theater.voorstelling");
      pselectvoorstelling = Connectiebeheer.connectie
          .prepareStatement("SELECT voorstelling.datum, voorstelling.naam FROM theater.voorstelling");
      pselectDatabasePlaatsen = Connectiebeheer.connectie
          .prepareStatement("SELECT bezetting.klant, bezetting.rijnummer, bezetting.stoelnummer FROM theater.bezetting WHERE voorstelling = ?");
      pselectDatabaseKlanten = Connectiebeheer.connectie
          .prepareStatement("SELECT  klant.naam, klant.telefoon FROM theater.klant WHERE klantnummer =  ?");        
      pinsertSchrijfBezetting = Connectiebeheer.connectie
          .prepareStatement("INSERT INTO theater.bezetting (voorstelling, rijnummer, stoelnummer, klant) VALUES (?, ?, ?, ?)");
    
    }
    catch (SQLException e) {
      throw new TheaterException(
          "Er is een fout in de formulering van een sql-opdracht.");
    }

  }

  /**
   * Levert alle data op waarop voorstellingen plaatsvinden na de datum van vandaag.
   * 
   * @return ArrayList data van voorstellingen
   * @throws TheaterException
   * @throws SQLException
   */
  public static ArrayList<GregorianCalendar> geefVoorstellingsData()
      throws TheaterException {
    
    GregorianCalendar nu = new GregorianCalendar();
    ArrayList<GregorianCalendar> data = new ArrayList<GregorianCalendar>();
    try {
      ResultSet res = pselectvoorstellingsdata.executeQuery();
      if (res.next() ) {
        do   {
          GregorianCalendar datum = new GregorianCalendar();
          java.sql.Date sqlDatum = res.getDate(1);
          datum.setTimeInMillis(sqlDatum.getTime());
            if (datum.after(nu)) {
            data.add(datum);
            }
        }  while (res.next());
      } else {
          throw new TheaterException();
      }
    }
    catch (SQLException e) {
      Connectiebeheer.closeDB();
      throw new TheaterException(
          "SQL exception bij het lezen van de data van de voorstellingen, database connectie is gesloten ");
    }
    catch (TheaterException e) {
      throw new TheaterException("De database is leeg");
    }
    return data;

  }

  /**
   * Zoekt een voorstelling op de gegeven datum.
   * 
   * @param datum de datum van de voorstelling
   * @return een voorstelling op de gegeven datum of null wanneer die
   *         voorstelling er niet is.
   * @throws TheaterException
   */

  @SuppressWarnings("finally")
  public static Voorstelling geefVoorstelling(GregorianCalendar datum)
      throws TheaterException {
    
    Voorstelling voorstelling = null;
    java.sql.Date sqlDatum = new java.sql.Date(datum.getTimeInMillis());

//      De voorstelling wordt opgehaald uit de database
    try {
        ResultSet res = pselectvoorstelling.executeQuery();
        if (!res.next() ) {
          throw new TheaterException();
        } else {
          do {
          if (res.getDate(1).equals(sqlDatum)) {
            GregorianCalendar datumGregoriaans = new GregorianCalendar();
            datumGregoriaans.setTimeInMillis(sqlDatum.getTime());
            voorstelling = new Voorstelling(res.getString(2), datumGregoriaans);
            }
          } while (res.next());
        }      
    }
    catch (SQLException e) {
      Connectiebeheer.closeDB();
      throw new TheaterException ("Fout  bij het lezen van een voorstelling ");
    }
    catch (TheaterException e) {
      throw new TheaterException("Er zijn geen voorstellingen in de database");
    }
    
//      De bezetting met klanten wordt opgehaald uit de database voor deze voorstelling
    try {
      pselectDatabasePlaatsen.setDate(1, sqlDatum);
      ResultSet resBezetting = pselectDatabasePlaatsen.executeQuery();
      
      if (!resBezetting.next() ) {
        throw new TheaterException();
      } else {
          do {
          int bezettingKlant = resBezetting.getInt("klant");
          int rijNummer = resBezetting.getInt("rijnummer");
          int stoelNummer = resBezetting.getInt("stoelnummer");

//      Lees de klant met dit klantnummer voor deze plaats uit de database
          pselectDatabaseKlanten.setInt(1, bezettingKlant);
          ResultSet resKlant = pselectDatabaseKlanten.executeQuery();
          resKlant.next();
          Klant klant = Klantbeheer.geefKlant(resKlant.getString("naam"), resKlant.getString("telefoon"));

//      plaatsen worden eerst gereserveerd en direct erna wordt de klant
//      geplaatst
          voorstelling.reserveer(rijNummer, stoelNummer);
          voorstelling.plaatsKlant(rijNummer, stoelNummer, klant);      
        }  while (resBezetting.next());
      }
    }
    catch (SQLException e) {
      Connectiebeheer.closeDB();
      throw new TheaterException(
          "SQL exception bij het lezen van de data van de voorstellingen, databaseconnectie is gesloten ");
    }
    catch (TheaterException e) {
      throw new TheaterException("De klantendatabase is leeg");
    }
    finally {return voorstelling;}
  }

  /**
   * Schrijft de bezette plaats weg naar de database
   * 
   * @param datum de datum van de voorstelling
   * @param rijnummer nummer van de rij
   * @param stoelnummer nummer van de stoel
   * @param klantnummer het klantnummer
   * @throws TheaterException
   */
  public static void schrijfPlaats(GregorianCalendar datum, int rijnummer,
      int stoelnummer, int klantnummer) throws TheaterException {

    java.sql.Date sqlDatum = new java.sql.Date(datum.getTimeInMillis());

    try {
      pinsertSchrijfBezetting.setDate(1, sqlDatum);
      pinsertSchrijfBezetting.setInt(2, rijnummer);
      pinsertSchrijfBezetting.setInt(3, stoelnummer);
      pinsertSchrijfBezetting.setInt(4, klantnummer);
      
      pinsertSchrijfBezetting.executeUpdate();
    }  
    catch (SQLException e) {
      Connectiebeheer.closeDB();
      throw new TheaterException(
          "Er is een fout opgetreden bij het schrijven van een plaats naar de database, connectie is gesloten ");
    }
  }

}