package htw.GameManagmentInter;

public class GameNotCreatableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 462665117820909804L;

	public GameNotCreatableException(Exception x) {
		super("Das Spiel konnte aufgrund des folgenden Fehlers nicht erstellt werden: " + x.getMessage(),x);
	}
	
	public GameNotCreatableException(String message) {
		super(message);
	}
}
