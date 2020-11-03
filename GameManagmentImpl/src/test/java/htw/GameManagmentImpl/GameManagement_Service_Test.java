package htw.GameManagmentImpl;


import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import htw.GameManagmentInter.Game;
import htw.GameManagmentInter.GameManagementDao;
import htw.GameManagmentInter.GameManagementServiceException;
import htw.GameManagmentInter.GameNotCreatableException;
import htw.GameManagmentInter.GameNotFoundException;
import htw.GameManagmentInter.OnePlayerTwoTimesInGameException;
import htw.PlayerManagementInter.Player;
import htw.VocabularyManagmentInter.VocabularySupply_Service;
import htw.VocabularyManagmentInter.NoVocabularyListFoundException;
import htw.VocabularyManagmentInter.NoWordFoundException;
import htw.VocabularyManagmentInter.VocabManagementServiceException;
import htw.VocabularyManagmentInter.Vocabulary;
import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.Word;

public class GameManagement_Service_Test {
	
	private GameManagement_Service_Impl service;
	private VocabularySupply_Service vocabSupplyService;
	private GameManagementDao gmDao;
	private String fromLanguage = "English";
	private String toLanguage = "Deutsch";
	private Player player1 = new Player("Martin");
	private Player player2 = new Player("Ulf");
	private Player player3 = new Player("Marion");
	private String[] englishWords =  {"Apple", "Bird", "Car", "Dinosaur"};
	private String[] germanWords =  {"Apfel", "Vogel", "Auto", "Dinosaurier"};	

	
	@Before
	public void setUp() {
		vocabSupplyService = Mockito.mock(VocabularySupply_Service.class);
		gmDao = Mockito.mock(GameManagementDao.class);
		service = new GameManagement_Service_Impl(gmDao,vocabSupplyService);
	}
	
	private VocabularyList generateVocabularyList() {
		
		List<Vocabulary> vocabularys = new ArrayList<Vocabulary>();
			
		for(int i = 0; i < englishWords.length; i++) {
								
			Vocabulary vocabel = generateVocabulary(englishWords[i], germanWords[i]);
			vocabularys.add(vocabel);
		}
		return new VocabularyList("Chapter", "Book", vocabularys,fromLanguage,toLanguage);
	}
	
	private Vocabulary generateVocabulary(String englishWord, String germanWord) {
		
				
		Word englishword = new Word(englishWord, fromLanguage);
		Word germanword = new Word(germanWord, toLanguage);
				
		List<Word> englishWordList = new ArrayList<Word>();
		List<Word> germanWordList = new ArrayList<Word>();
				
		englishWordList.add(englishword);
		germanWordList.add(germanword);
				
		return new Vocabulary(englishWordList, germanWordList);
	
	}
	
	private List<Word> generateWordList(String language){
		String[] germanWords =  {"Vogel", "Auto", "Dinosaurier"};
		List<Word> wordList = new ArrayList<Word>();
		
		for(String name : germanWords) {
			Word word = new Word(name, language);
			wordList.add(word);
		}
		return wordList;
	}
	
	private Game generateGame(Player playerOne, Player playerTwo) throws NoVocabularyListFoundException, VocabManagementServiceException, NoWordFoundException, GameNotCreatableException, OnePlayerTwoTimesInGameException {
		VocabularyList vocabularyList = generateVocabularyList();
		Mockito.when(vocabSupplyService.getVocabularyList(vocabularyList.getVocabularyListId())).thenReturn(vocabularyList);
		Mockito.when(vocabSupplyService.getWords(toLanguage)).thenReturn(generateWordList(toLanguage));	
		Mockito.when(gmDao.saveGame(Mockito.any(Game.class))).thenReturn(true);
		return service.createGame(playerOne, playerTwo, fromLanguage, toLanguage, vocabularyList.getVocabularyListId());
	}
	
	/**
	 * Die Methode testet, ob ein Game Objekt erstellt wird.
	 * Dafür wird eine Liste an VocabularyList Objekten erstellt.
	 * Ausserdem werden 2 Player Objekte erstellt und als Erwartungswerte der getPlayer Methode des playerManagmentService als Mockups übergeben. 
	 * Zusätzlich wird ein QuizQuestion Objekt erstellt und als Erwartungswert der getQuizQuestion Methode des quizQuestionManagmentService als
	 * Mockup übergeben.
	 * @throws NoVocabularyFoundException 
	 * @throws NoWordFoundException 
	 * @throws NoVocabularyListFoundException 
	 * @throws OnePlayerTwoTimesInGameException 
	 * @throws GameNotCreatableException 
	 * @throws GameManagementServiceException 
	 * @throws VocabManagementServiceException 
	 */
	@Test
	public void testCreateGameSuccess() throws  NoWordFoundException, OnePlayerTwoTimesInGameException, NoVocabularyListFoundException, GameNotCreatableException, GameManagementServiceException, VocabManagementServiceException {

		Game game = generateGame(player1, player2);

		Assert.assertEquals(game.getFromLanguage(), fromLanguage);
		Assert.assertEquals(game.getToLanguage(), toLanguage);
		Assert.assertEquals(game.getPlayer1(), player1);
		Assert.assertEquals(game.getPlayer2(), player2);
		
	}
	
	/**
	 * Die Methode testet, ob eine OnePlayerTwoTimesInGameException geworfen wird,
	 * wenn bei der Erstellung eines Game Objekt zweimal der selbe Player anhand der ID eingebunden wird.
	 * @throws NoVocabularyFoundException 
	 * @throws NoWordFoundException 
	 * @throws NoVocabularyListFoundException 
	 * @throws OnePlayerTwoTimesInGameException 
	 * @throws GameNotCreatableException 
	 * @throws GameManagementServiceException 
	 * @throws VocabManagementServiceException 
	 * 
	 */
	@Test(expected = OnePlayerTwoTimesInGameException.class)
	public void testCreateGameFails2() throws  NoWordFoundException, NoVocabularyListFoundException, OnePlayerTwoTimesInGameException, GameNotCreatableException, GameManagementServiceException, VocabManagementServiceException {
		
		VocabularyList vocabularyList = generateVocabularyList();
		Mockito.when(vocabSupplyService.getVocabularyList(vocabularyList.getVocabularyListId())).thenReturn(vocabularyList);
		Mockito.when(vocabSupplyService.getWords(toLanguage)).thenReturn(generateWordList(toLanguage));

		service.createGame(player1, player1, fromLanguage, toLanguage, vocabularyList.getVocabularyListId());	
	}
	
	/**
	 * Die Methode testet, ob alle Games des beteiligten Spielers
	 * durch die Methode getAllGamesByPlayer gefunden werden. Dafür werden 3 Games erstellt, 
	 * wobei bei nur einem Game Spieler 1 mitspielt und bei 2 Spielen Spieler 3
	 * 
	 * Ergebnis Bei der Suche nach Spieler 3 Spielen werden 2 zurückgegeben. 
	 * 			Bei der Suche nach Spieler 1 Splien wird 1 Spiel zurückgegeben
	 * @throws NoVocabularyFoundException 
	 * @throws NoWordFoundException 
	 * @throws OnePlayerTwoTimesInGameException 
	 * @throws NoVocabularyListFoundException 
	 * @throws GameNotCreatableException 
	 * @throws GameManagementServiceException 
	 * @throws VocabManagementServiceException 
	 */
	@Test
	public void testGetAllGamesByPlayerSuccess() throws  OnePlayerTwoTimesInGameException, NoWordFoundException, NoVocabularyListFoundException, GameNotCreatableException, GameManagementServiceException, VocabManagementServiceException {
		List<Game> allGamesWithPlayer3Expected = new ArrayList<Game>();
		List<Game> allGamesWithPlayer1Expected = new ArrayList<Game>();
		
		Game runningGame1 = generateGame(player3, player2);
		Game runningGame2 = generateGame(player3, player2);
		Game runningGame3 = generateGame(player1, player2);
		
		allGamesWithPlayer3Expected.add(runningGame1);
		allGamesWithPlayer3Expected.add(runningGame2);
		allGamesWithPlayer1Expected.add(runningGame3);
		
		Mockito.when(gmDao.getAllGamesByPlayer(3)).thenReturn(allGamesWithPlayer3Expected);
		Mockito.when(gmDao.getAllGamesByPlayer(1)).thenReturn(allGamesWithPlayer1Expected);
		
		
		List<Game> allGamesPlayer1 = service.getAllGamesByPlayer(1);
		List<Game> allGamesPlayer3 = service.getAllGamesByPlayer(3);
		

		Assert.assertTrue(allGamesPlayer3.equals(allGamesWithPlayer3Expected));
		Assert.assertTrue(allGamesPlayer1.equals(allGamesWithPlayer1Expected));
		
	}
	
	/**
	 * Die Methode testet, ob keine Games durch die Methode getAllGamesByPlayer gefunden wird, wenn zuvor kein Game objekt erstellt wurde. 
	 * 
	 * Ergebnis Status true Games = null, Status false Games = null
	 * @throws GameManagementServiceException 
	 */
	@Test
	public void testGetAllGamesByPlayerFails() throws GameManagementServiceException {
		Mockito.when(gmDao.getAllGamesByPlayer(1)).thenReturn(null);
		Mockito.when(gmDao.getAllGamesByPlayer(2)).thenReturn(null);
		List<Game> allGamesTrue = service.getAllGamesByPlayer(1);
		List<Game> allGamesFalse = service.getAllGamesByPlayer(2);
		
		Assert.assertEquals(allGamesTrue, null);
		Assert.assertEquals(allGamesFalse, null);
	}
	
	
	/**
	 * Die Methode testet, ob eine GameNotFoundException gworfen wird,
	 * wenn ein Game beim Aufruf der deleteGame Methode nicht vorhanden ist.
	 * @throws GameManagementServiceException 
	 * @throws GameNotFoundException 
	 */
	@Test(expected = GameNotFoundException.class)
	public void testDeleteGameFails() throws GameManagementServiceException, GameNotFoundException  {
		Mockito.when(gmDao.getGame(0)).thenReturn(null);
		service.deleteGame(0);
	}
	
	/**
	 * Die Methode testet, ob das Löschen eines Spieles erfolgreich ist.
	 * 
	 * @throws GameManagementServiceException 
	 * @throws GameNotFoundException 
	 * @throws NoVocabularyListFoundException 
	 * @throws NoVocabularyFoundException 
	 * @throws VocabManagementServiceException 
	 * @throws NoWordFoundException 
	 * @throws OnePlayerTwoTimesInGameException 
	 * @throws GameNotCreatableException 
	 */
	@Test
	public void testDeleteGameSuccess() throws GameManagementServiceException, GameNotFoundException, VocabManagementServiceException, NoVocabularyListFoundException, NoWordFoundException, GameNotCreatableException, OnePlayerTwoTimesInGameException  {

		Game game = generateGame(player1, player2);
		Mockito.when(gmDao.getGame(game.getGameID())).thenReturn(game);
		Mockito.when(gmDao.deleteGame(game)).thenReturn(true);
		
		boolean result = service.deleteGame(game.getGameID());
		Assert.assertTrue(result);
	}
	
	/**
	 * Die Methode testet, ob false zurückgegeben wird,
	 * wenn ein Game beim Aufruf der updateGame Methode das Update nicht erfolgreich war.
	 * @throws GameManagementServiceException 
	 * @throws GameNotFoundException 
	 * @throws NoVocabularyListFoundException 
	 * @throws NoVocabularyFoundException 
	 * @throws VocabManagementServiceException 
	 * @throws NoWordFoundException 
	 * @throws OnePlayerTwoTimesInGameException 
	 * @throws GameNotCreatableException 
	 */
	@Test()
	public void testGameUpdateFails() throws GameManagementServiceException, GameNotFoundException, VocabManagementServiceException, NoVocabularyListFoundException, NoWordFoundException, GameNotCreatableException, OnePlayerTwoTimesInGameException  {
		Game runningGame = generateGame(player3, player2);
		
		Mockito.when(gmDao.getGame(runningGame.getGameID())).thenReturn(runningGame);
		
		Mockito.when(gmDao.updateGame(runningGame)).thenReturn(false);
		boolean result = service.updateGame(runningGame);
		
		Assert.assertFalse(result);
	}
	
	/**
	 * Die Methode testet, ob eine GameNotFoundException geworfen wird,
	 * wenn ein Game beim Aufruf der updateGame Methode nicht mehr vorhanden ist.
	 * @throws GameManagementServiceException 
	 * @throws GameNotFoundException 
	 * @throws NoVocabularyListFoundException 
	 * @throws NoVocabularyFoundException 
	 * @throws VocabManagementServiceException 
	 * @throws NoWordFoundException 
	 * @throws OnePlayerTwoTimesInGameException 
	 * @throws GameNotCreatableException 
	 */
	@Test(expected =  GameNotFoundException.class)
	public void testGameUpdateFails2() throws GameManagementServiceException, GameNotFoundException, VocabManagementServiceException, NoVocabularyListFoundException, NoWordFoundException, GameNotCreatableException, OnePlayerTwoTimesInGameException  {
				
		Game runningGame = generateGame(player3, player2);
		
		Mockito.when(gmDao.getGame(runningGame.getGameID())).thenReturn(null);
		service.updateGame(runningGame);
	}
	
	/**
	 * Die Methode prüft, ob alle Spiele eines Spielers erfolgreich gelöscht werden.
	 * 
	 * @throws GameManagementServiceException
	 * @throws NoVocabularyListFoundException 
	 * @throws NoVocabularyFoundException 
	 * @throws VocabManagementServiceException 
	 * @throws NoWordFoundException 
	 * @throws OnePlayerTwoTimesInGameException 
	 * @throws GameNotCreatableException 
	 */
	@Test
	public void testDeleteAllGamesByPlayerSuccess() throws GameManagementServiceException, VocabManagementServiceException, NoVocabularyListFoundException, NoWordFoundException, GameNotCreatableException, OnePlayerTwoTimesInGameException {
		
		List<Game> gameList = new ArrayList<Game>();
		
		gameList.add(generateGame(player3, player2));
		gameList.add(generateGame(player3, player2));

		Mockito.when(gmDao.getAllGamesByPlayer(player3.getId())).thenReturn(gameList);
		Mockito.when(gmDao.deleteGame(Mockito.any())).thenReturn(true);
		
		boolean result = service.deleteAllGamesByPlayer(player3.getId());
		
		Assert.assertTrue(result);
	}
	
	/**
	 *  Die Methode prüft, ob die Methode deleteAllGamesByPlayer fals zurück gibt, 
	 *  falls das Löschen eines Spieles nicht erfolgreich war
	 * 
	 * @throws GameManagementServiceException
	 * @throws NoVocabularyListFoundException 
	 * @throws NoVocabularyFoundException 
	 * @throws VocabManagementServiceException 
	 * @throws NoWordFoundException 
	 * @throws OnePlayerTwoTimesInGameException 
	 * @throws GameNotCreatableException 
	 */
	@Test
	public void testDeleteAllGamesByPlayerFails() throws GameManagementServiceException, VocabManagementServiceException,  NoVocabularyListFoundException, NoWordFoundException, GameNotCreatableException, OnePlayerTwoTimesInGameException {
		
		List<Game> gameList = new ArrayList<Game>();
				
		gameList.add(generateGame(player3, player2));
		gameList.add(generateGame(player3, player2));

		
		Mockito.when(gmDao.getAllGamesByPlayer(player3.getId())).thenReturn(gameList);
		Mockito.when(gmDao.deleteGame(gameList.get(0))).thenReturn(true);
		Mockito.when(gmDao.deleteGame(gameList.get(1))).thenReturn(false);
		
		boolean result = service.deleteAllGamesByPlayer(player3.getId());
		
		Assert.assertFalse(result);
	}	
}
