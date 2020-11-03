package htw.PlayerManagementInter;

public class PlayerNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerNotFoundException(int playerId) {
		super("Der Spieler mit der ID "+playerId+" wurde nicht gefunden. Aktualisieren Sie Ihre Spieloberfläche.");
	}
	
	public PlayerNotFoundException(String message) {
		super(message);
	}
}
