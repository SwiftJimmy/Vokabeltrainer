package htw.GameManagmentInter;

public class OnePlayerTwoTimesInGameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1534241971647446196L;
	
	public OnePlayerTwoTimesInGameException(String playerName) {
		super("Der Spieler " + playerName + " kann nicht gegen sich selbst spielen.");
	}
}
