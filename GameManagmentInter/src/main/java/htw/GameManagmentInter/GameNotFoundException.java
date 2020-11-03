package htw.GameManagmentInter;

public class GameNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7571258991054414071L;
	
	public GameNotFoundException(int id) {
		super("Das Spiel mit der ID "+id+" wurde nicht gefunden. Aktualisieren Sie die Spieloberfläche.");
	}
	
	public GameNotFoundException(String message) {
		super(message);
	}
}
