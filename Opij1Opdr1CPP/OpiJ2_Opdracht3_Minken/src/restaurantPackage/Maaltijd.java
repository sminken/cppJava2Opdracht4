package restaurantPackage;

/**
 * Verantwoordelijk voor het beheer van een maaltijd. Bevat een omschrijving van
 * de maaltijd en het tafelnummer waar de maaltijd geserveert dient te worden.
 * 
 * @author Steven Minken
 *
 */
public class Maaltijd {

  private String omschrijving = null;
  private int tafelnummer = 0;

  /**
   * constructor voor het aanmaken van een Maaltijd object
   * 
   * @param omschrijving
   *          de omschrijving van de maaltijd
   * @param tafelnummer
   *          het tafelnummer waarvoor de maaltijd bestemd is
   */
  public Maaltijd(String omschrijving, int tafelnummer) {
    this.omschrijving = omschrijving;
    this.tafelnummer = tafelnummer;
  }

  /**
   * Retourneert het tafelnummer
   * 
   * @return int het tafelnummer
   */
  public int getTafelnummer() {
    return tafelnummer;
  }

  /**
   * Geeft een stringrepresentatie van een maaltijdobject. Bevat het gerecht en
   * het tafelnummer
   */
  @Override
  public String toString() {
    return omschrijving + " voor tafel " + tafelnummer;
  }

}
