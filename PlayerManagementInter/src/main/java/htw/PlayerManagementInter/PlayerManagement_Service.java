package htw.PlayerManagementInter;

import java.util.List;

/**
 * Die PlayerManagement_Service Schnittstelle realisiert die Erstellung, die Bearbeitung, die Löschung und die Abfrage der Player Objekte. 
 */
public interface PlayerManagement_Service {
	
	/**
	 * Die Methode erstellt ein neues Player Objekt, gibt es zurück und speichert es in der Datenbank ab. 
	 * Um sicherzustellen, dass kein Name doppelt vorkommt, wird überprüft, ob der übergebene Name bereits
	 * von einem anderen Spieler belegt ist. 
	 * 
	 * @param name : Der Name des neuen Players als String.
	 * @return Player Objekt : Falls ein Player mit demselben Namen noch nicht vorhanden ist.
	 * @throws DuplicatePlayerException : Falls bereits ein Player mit demselben Namen existiert.
	 * @throws PlayerManagementServiceException : Falls ein technischer Fehler auftritt.
	 */
	public Player createPlayer(String name) throws DuplicatePlayerException, PlayerManagementServiceException;
	
	/**
	 * Die Methode ändert den Namen eines Players und gibt das aktualisierte Player Objekt zurück. 
	 * Um sicherzustellen, dass kein Name doppelt vorkommt, wird überprüft, ob der übergebene Name bereits 
	 * von einem anderen Spieler belegt ist.
	 * 
	 * @param player : Der Player, dessen Name geändert werden soll.
	 * @param name : Der neue Name für den Player.
	 * @return aktuallisiertes Player Objekt : Falls ein Player mit demselben Namen noch nicht vorhanden ist.
	 * @throws DuplicatePlayerException : Falls bereits ein Player mit demselben Namen existiert.
	 * @throws PlayerManagementServiceException : Falls ein technischer Fehler auftritt.
	 * @throws PlayerNotFoundException : Falls der Player in der Datenbank nicht mehr existiert.
	 */
	public Player editPlayerName(Player player, String name) throws  DuplicatePlayerException, PlayerManagementServiceException, PlayerNotFoundException;
	
	/**
	 * Ein Player wird anhand seiner ID gelöscht. Dafür wird vorab überprüft, ob das Player Objekt noch in der Datenbank existiert.
	 * 
	 * @param playerID : Die ID des Players, welcher gelöscht werden soll.
	 * @return true -> Falls der Player erfolgreich gelöscht wurde.
	 * @throws PlayerNotFoundException : Falls der Player in der Datenbank nicht mehr existiert.
	 * @throws PlayerManagementServiceException : Falls ein technischer Fehler auftritt.
	 */
	public boolean deletePlayer(int playerID) throws PlayerNotFoundException, PlayerManagementServiceException;
	
	/**
	 * Die Methode gibt eine Liste aller Player Objekte zurück. 
	 * 
	 * @return List<Player> : Eine Liste aller Player Objekte.
	 * 		   leere Liste  : Falls kein Player vorhanden ist.
	 * @throws PlayerManagementServiceException : Falls ein technischer Fehler auftritt. 
	 */
	public List<Player> getAllPlayer() throws PlayerManagementServiceException;
		
}
