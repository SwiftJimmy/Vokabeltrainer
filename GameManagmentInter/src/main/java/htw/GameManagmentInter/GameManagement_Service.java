package htw.GameManagmentInter;

import java.util.List;
import htw.PlayerManagementInter.Player;

/**
 * Die GameManagement_Service Schnittstelle realisiert die Erstellung, die Bearbeitung, die Löschung und die Abfrage der Game Objekte. 
 */
public interface GameManagement_Service {
	/**
	 * Die Anzahl der zu spielenden Runden.
	 */
	final int NUMBER_OF_ROUNDS = 3;
	
	/**
	 * Die Anzahl an Falschen Antworten in einer Frage. 
	 */
	final int NUMBER_OF_WRONG_ANSWERS = 3;
	
	/**
	 * Diese Methode erstellt ein neues Game, speichert es in der Datenbank und 
	 * gibt das erstellte Game Objekt zurückt. 
	 * 
	 * @param player1 : Erster Spieler
	 * @param player2 : Zweiter Spieler
	 * @param from : Ausgangssprache für das Spiel (Frage)
	 * @param to : Übersetzungssprache für das Spiel (Antwort)
	 * @param vocabularyListId : ID der ausgewählten VocabularyListe
	 * @return Game Objekt : Falls ein Game erfolgreich erstellt wurde
	 * @throws GameNotCreatableException : Falls das Spiel aufgrund einer VocabularyManagementException (NoVocabularyFoundException | NoWordFoundException | NoVocabularyListFoundException | GameManagementServiceException) nicht erstellt werden kann. 
	 * @throws OnePlayerTwoTimesInGameException : Falls player1 und player2 der selbe Spieler ist.
	 * @throws GameManagementServiceException : Falls ein technischer Fehler aufgetreten ist.
	 */
	public Game createGame(Player player1, Player player2, String from,String to, int vocabularyListId)throws OnePlayerTwoTimesInGameException, GameNotCreatableException, GameManagementServiceException;
	
	/**
	 * Die Methode löscht ein Game Objekt anhand der Game-Id. Dafür wird vorab überprüft, ob das Game Objekt noch in der Datenbank existiert.
	 * 
	 * @param gameId : Die Id des Games, welches gelöscht werden soll.
	 * @return True : Wenn das Löschen erfolgreich war.
	 * @throws GameNotFoundException : Falls kein Spiel anhand der ID gefunden werden kann.
	 * @throws GameManagementServiceException : Falls ein technischer Fehler aufgetreten ist.
	 */
	public boolean deleteGame(int gameId) throws GameNotFoundException, GameManagementServiceException;
	
	
	/**
	 * Die Methode aktualisiert ein Game Objekt. Dafür wird vorab überprüft, ob das Game Objekt noch in der Datenbank existiert.
	 * 
	 * @param game : Das Game, welches aktualisiert werden soll.
	 * @return True : Wenn die Aktualisierung erfolgreich war.
	 * @throws GameNotFoundException : Falls das Spiel in der Datenbank nicht mehr existiert.
	 * @throws GameManagementServiceException : Falls ein technischer Fehler aufgetreten ist.
	 */
	public boolean updateGame(Game game) throws GameNotFoundException, GameManagementServiceException;
	
	
	/**
	 * Die Methode gibt alle Game Objekte als Liste zurück, an welchen ein Spieler mit der übergebenen ID beteiligt ist. 
	 * 
	 * @param playerID : Die PlayerID eines Spielers.
	 * @return 	List<Game> : Liste mit allen Game Objekten an denen der Spieler beteiligt ist
	 * 		 	leere Liste : Falls keine Games mit dem gewünschten Spieler vorhanden sind.
	 * @throws GameManagementServiceException : Falls ein technischer Fehler aufgetreten ist.
	 */
	public List<Game> getAllGamesByPlayer(int playerID) throws GameManagementServiceException;
	
	/**
	 * Die Methode löschte alle Spiele, an denen ein Spieler beteiligt ist 
	 * 
	 * @param playerID : Die ID des Spielers, wessen Spiele gelöscht werden sollen. 
	 * @return true : Wenn die Löschung der Spiele erfolgreich, bzw aufgrund von keiner Spielbeteiligung nicht nötig war.
	 * @throws GameManagementServiceException : Falls ein technischer Fehler aufgetreten ist.
	 */
	public boolean deleteAllGamesByPlayer(int playerID) throws GameManagementServiceException;
	
}
