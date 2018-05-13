package theaterdata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import theater.Klant;

/**
 * De klasse die klanten beheert. De klasse leest de klanten uit de database
 */
public class Klantbeheer {

  private static PreparedStatement pselecthoogsteklantnummer = null;
  private static PreparedStatement pselectzoekklant = null;

  private static PreparedStatement pupdatenieuweklant = null;

  /**
   * Roept de functie aan die de prepared statements initialiseert
   * 
   * @throws TheaterException
   */
  public static void init() throws TheaterException {
    Klantbeheer.initialiseerPrepStatements();
  }

  /**
   * Initialiseert PreparedStatements voor de SQL-opdrachten om klanten te
   * beheren in de database
   * 
   * @throws TheaterException als de SQL-opdracht een fout bevat.
   */
  private static void initialiseerPrepStatements() throws TheaterException {

    try {
      pselecthoogsteklantnummer = Connectiebeheer.connectie.prepareStatement(
          "SELECT MAX(klantnummer) AS hoogstenummer FROM klant");
      pselectzoekklant = Connectiebeheer.connectie
          .prepareStatement("SELECT klant.klantnummer, klant.naam, klant.telefoon FROM theater.klant WHERE naam = ? AND telefoon = ?");

      pupdatenieuweklant = Connectiebeheer.connectie
          .prepareStatement("INSERT INTO theater.klant(klantnummer, naam, telefoon) VALUES (?, ?, ?)");

    }
    catch (SQLException e) {
      throw new TheaterException(
          "Fout bij het initializeren van de prepared statements in klantbeheer ");
    }
    
  }

  /**
   * Genereert het volgende beschikbare klantnummer.
   * 
   * @return int nieuw klantnummer
   * @throws TheaterException
   */
  public static int getVolgendKlantNummer() throws TheaterException {
    
    int volgendNummer = 0;
    ResultSet res;
    try {
        res = pselecthoogsteklantnummer.executeQuery();
        if (!res.next() ) {
          throw new TheaterException();
        } else {
          do {
        volgendNummer = res.getInt("hoogstenummer");
        volgendNummer++;
        } while (res.next()); 
      }
    }
    catch (SQLException e) {
      Connectiebeheer.closeDB();
      throw new TheaterException(
          "Fout bij het verbinden met de database, deze is gesloten ");
    }
    catch (TheaterException e) {
      throw new TheaterException("De database is leeg");
    }

    return volgendNummer;
  }

  /**
   * Geeft een klant met de gegeven naam en het gegeven telefoonnummer Als de
   * klant al in de lijst zat, wordt die teruggegeven, anders wordt er een
   * nieuwe klant aangemaakt teruggegeven.
   * 
   * @param naam naam van de klant
   * @param telefoon telefoonnummer van de klant
   * @return Klant een klant me de ingevoerde naam en telefoon.
   * @throws TheaterException
   */
  public static Klant geefKlant(String naam, String telefoon)
      throws TheaterException {
    
      Klant klant = zoekKlant(naam, telefoon);
      if (klant != null) {
        return klant;
      } else
        return nieuweKlant(naam, telefoon);
  }

  /**
   * Retourneert klant met gegeven naam en telefoonnummer. 
   * Wanneer deze nog niet voorkomt in de database wordt de klant toegevoegd.
   * 
   * @param naam naam van te zoeken klant
   * @param telefoon telefoonnummer van te zoeken klant
   * @return Klant de klant of null als de klant niet voorkomt in de database
   * @throws TheaterException
   */
  private static Klant zoekKlant(String naam, String telefoon)
      throws TheaterException {

    try {
      pselectzoekklant.setString(1, naam);
      pselectzoekklant.setString(2, telefoon);
      ResultSet res = pselectzoekklant.executeQuery();
        if (res.next() ) {
            return new Klant(res.getInt(1), naam, telefoon);
        }
    }
    catch (SQLException e) {
      Connectiebeheer.closeDB();
      throw new TheaterException(
          "Fout bij het verbinden met de database, deze is gesloten ");
    }
    return null;
  }

  /**
   * Voegt een nieuwe klant toe aan de database en maakt een nieuw klantobject aan
   * 
   * @param naam naam van de nieuwe klant
   * @param telefoon telefoonnummer van de nieuwe klant
   * @throws TheaterException
   */
  private static Klant nieuweKlant(String naam, String telefoon)
      throws TheaterException {

    int volgendKlantnummer = Klantbeheer.getVolgendKlantNummer();
    try {
      pupdatenieuweklant.setInt(1, volgendKlantnummer);
      pupdatenieuweklant.setString(2, naam);
      pupdatenieuweklant.setString(3, telefoon);

      pupdatenieuweklant.executeUpdate();
    }
    catch (SQLException e) {
      Connectiebeheer.closeDB();
      throw new TheaterException(
          "Fout bij het schrijven van klant naar de database, deze is gesloten ");
    }
    return new Klant(volgendKlantnummer, naam, telefoon);
  }

}
