package theater;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

import theaterdata.*;

/**
 * Representeert (de kassa van) een theater.
 * @author Open Universiteit
 */
public class Theater {
  public static final int AANTALPERRIJ = 10;
  public static final int AANTALRIJEN = 15;
  private String naam = null;
  private Voorstelling huidigeVoorstelling = null;
  
  /**
   * Creeert een theater.
   * @param naam theaternaam
   * @throws TheaterException 
   * @throws ParseException 
   */
  public Theater(String naam) throws TheaterException, ParseException {
    this.naam = naam;
    Connectiebeheer.openDB();
    Klantbeheer.init();
    Voorstellingbeheer.init();
  }

  /**
   * Geeft de naam van het theater.
   * @return naam van het theater
   */
  public String getNaam() {
    return naam;
  }
  
  /**
   * Geeft een lijst van data waarop voorstellingen zijn gepland.
   * @return lijst met data.
   * @throws ParseException 
   * @throws TheaterException 
   */
  public ArrayList<GregorianCalendar> geefVoorstellingsData() throws TheaterException {
    return Voorstellingbeheer.geefVoorstellingsData();
  }
  
  /**
   * Wisselt de huidige voorstelling naar voorstelling met gegeven datum.
   * @param datum datum van gevraagde voorstelling
   * @throws TheaterException 
   */
  public void wisselVoorstelling(GregorianCalendar datum) throws TheaterException {

    huidigeVoorstelling = Voorstellingbeheer.geefVoorstelling(datum);
  }

  /**
   * Geeft de huidige voorstelling.
   * @return de huidige voorstelling
   */
  public Voorstelling getHuidigeVoorstelling() {
    return huidigeVoorstelling;
  }
  
  /**
   * Plaatst een klant op alle gereserveerde stoelen van de huidige uitvoering.
   * Wanneer een klant nog niet bestaat, dan wordt deze eerst gecreeerd.
   * @param naam naam van klant
   * @param telefoon telefoonnummer van klant
   * @throws TheaterException 
   * @throws SQLException 
   */
  public void plaatsKlant(String naam, String telefoon) throws TheaterException{

//    naam met hoofdletter en zonder spaties vooraan
    if(naam.equals("") || telefoon.equals("")) {
      throw new TheaterException("De naam en/ of het telefoonnummer is leeg");
    }
//    Validatie op correcte invoer
    String naamZonderSpaties = naam.trim();
    naam = naamZonderSpaties.substring(0, 1).toUpperCase() + naamZonderSpaties.substring(1).toLowerCase();
    if (naam.length() < 3) {
      throw new TheaterException("De naam dient uit minimaal 2 letters te bestaan");
    }
    if (!naam.chars().allMatch(Character::isLetter)) {
      throw new TheaterException("De naam kan niet uit cijfers bestaan");
    }
    if (telefoon.matches("[0-9]+") && telefoon.length() == 10) {
      Klant klant = Klantbeheer.geefKlant(naam, telefoon);
      huidigeVoorstelling.plaatsKlant(klant);
    } else {
      throw new TheaterException("Het telefoonnummer dient uit 10 cijfers te bestaan");
    }
  }


  /**
   * Verandert de reserveringsstatus (VRIJ<=>GERESERVEERD) van een plaats
   * in de huidige voorstelling.
   * @param rijnummer rijnummer van plaats
   * @param stoelnummer stoelnummer van plaats
   * @return true als verandering is gelukt, anders false
   */
  public boolean veranderReservering(int rijnummer, int stoelnummer) {
    return huidigeVoorstelling.veranderReservering(rijnummer, stoelnummer);
    
  }

  /**
   * Geeft informatie over een plaats in de huidige voorstelling.
   * @param rijnummer rijnummer van plaats
   * @param stoelnummer stoelnummer van plaats
   * @return informatie over plaats
   */
  public String geefPlaatsInfo(int rijnummer, int stoelnummer) {
    Plaats plaats = huidigeVoorstelling.getPlaats(rijnummer, stoelnummer);
    return plaats.toString();
  }
}
