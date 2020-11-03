package htw.PlayerManagementInter;

public class DuplicatePlayerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicatePlayerException(String name) {
        super("Es existiert bereits ein Spieler mit dem Namen " + name + ". Wählen Sie einen anderen Namen");
    }
}
