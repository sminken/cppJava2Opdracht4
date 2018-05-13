package theaterdata;

public class TheaterException extends Exception {

  /**
   * Verantwoordelijk voor het beheer van de theater excepties
   */
  private static final long serialVersionUID = 1L;

  public TheaterException() {
    super();
  }

  public TheaterException(String s) {
    super(s);
  }

}
