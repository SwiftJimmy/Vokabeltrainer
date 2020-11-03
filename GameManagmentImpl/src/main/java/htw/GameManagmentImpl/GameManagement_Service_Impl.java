package htw.GameManagmentImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import htw.GameManagmentInter.Answer;
import htw.GameManagmentInter.Game;
import htw.GameManagmentInter.GameManagementDao;
import htw.GameManagmentInter.GameManagementServiceException;
import htw.GameManagmentInter.GameNotFoundException;
import htw.GameManagmentInter.OnePlayerTwoTimesInGameException;
import htw.GameManagmentInter.QuizQuestion;
import htw.GameManagmentInter.Round;
import htw.GameManagmentInter.GameManagement_Service;
import htw.GameManagmentInter.GameNotCreatableException;
import htw.PlayerManagementInter.Player;
import htw.VocabularyManagmentInter.NoVocabularyListFoundException;
import htw.VocabularyManagmentInter.NoWordFoundException;
import htw.VocabularyManagmentInter.VocabManagementServiceException;
import htw.VocabularyManagmentInter.Vocabulary;
import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.VocabularySupply_Service;
import htw.VocabularyManagmentInter.Word;

public class GameManagement_Service_Impl implements GameManagement_Service {
	
	private GameManagementDao gmDao;
	private VocabularySupply_Service vocabSupplyService;
	
	public GameManagement_Service_Impl(GameManagementDao gmDao, 
			VocabularySupply_Service vocabSupplyService) {
		super();
		this.gmDao = gmDao;
		this.vocabSupplyService = vocabSupplyService;
	}
		
	@Override
	public boolean updateGame(Game game) throws GameNotFoundException {
		gmDao.begin();
		Game dbGame = gmDao.getGame(game.getGameID());
		
		if(dbGame == null) {
			gmDao.commit();
			gmDao.close();
			throw new GameNotFoundException(game.getGameID());
		}
			
		
		boolean result = gmDao.updateGame(game);
		gmDao.commit();
		gmDao.close();
		return result;
	}

	@Override
	public List<Game> getAllGamesByPlayer(int playerID)  {
		gmDao.begin();
		List<Game>  resultList = gmDao.getAllGamesByPlayer(playerID);
		gmDao.commit();
		gmDao.close();
		return resultList;
	}

	@Override	
	public Game createGame(Player player1, Player player2, String from, String to, int vocabularyListId) throws GameNotCreatableException, OnePlayerTwoTimesInGameException  {
		Game game = null;
		gmDao.begin();
		if (player1.equals(player2)) {
			gmDao.commit();
			gmDao.close();
			throw new OnePlayerTwoTimesInGameException(player2.getName());
		}
			
		try {
			VocabularyList vocabularyList = vocabSupplyService.getVocabularyList(vocabularyListId);
			List<Round> rounds = generateRounds(from, to, vocabularyList, player1, player2);
			
			game = new Game(player1, player2, from, to, rounds);
			
			gmDao.saveGame(game);
			
		} catch (NoWordFoundException | NoVocabularyListFoundException | VocabManagementServiceException e) {
			throw new GameNotCreatableException(e);
		} finally {
			gmDao.commit();
			gmDao.close();
		}
		return game;
	}
	
	@Override
	public boolean deleteGame(int gameId) throws  GameNotFoundException  {
		gmDao.begin();
		
		Game game = gmDao.getGame(gameId);
		if(game == null) {
			gmDao.commit();
			gmDao.close();
			throw new GameNotFoundException(gameId);
		}
			
		
		boolean result = gmDao.deleteGame(game);
		gmDao.commit();
		gmDao.close();
		return result;
	}
	
	@Override
	public boolean deleteAllGamesByPlayer(int playerID) throws GameManagementServiceException {
		gmDao.begin();
		List<Game> games = gmDao.getAllGamesByPlayer(playerID);
		boolean result = true; 
		
		for(Game game : games) {
			
			result = gmDao.deleteGame(game) && result;
		}
		
		if(result) {
			gmDao.commit();
		} else {
			gmDao.rollback();
		}
		gmDao.close();
		return result;
	}

	/**
	 * Die Methode erstellt n (gemäß NUMBER_OF_ROUNDS) Runden
	 * 
	 * @param from : Ausgangssprache
	 * @param to : Übersetzungssprache
	 * @param vocabularyList : Liste an potenziellen Vokabeln
	 * @return Liste an n Runden
	 * @throws NoVocabularyFoundException : Falls keine Vocabulary gefunden werden kann. 
	 * @throws NoWordFoundException : Falls kein Wort gefunden werden kann. 
	 * @throws NoVocabularyListFoundException : Falls keine VocabularyList gefunden werden kann. 
	 * @throws VocabManagementServiceException : Falls ein technischer Fehler aufgetreten ist. 
	 */
	private List<Round> generateRounds(String from, String to, VocabularyList vocabularyList, Player player1, Player player2) throws  NoWordFoundException, NoVocabularyListFoundException, VocabManagementServiceException  {
		
		List<Word> words = this.vocabSupplyService.getWords(to);
		List<Round> roundList = new ArrayList<Round>(); 
		
		for(int i = 0; i < NUMBER_OF_ROUNDS; i++) {
			int roundNumber = i + 1;
			Vocabulary vocabulary = getRandomVocabulary(vocabularyList, from, to);
			Word questionWord = getWord(vocabularyList ,vocabulary, from);
			Word answerWord = getWord(vocabularyList, vocabulary, to);
			
			List<Word> wrongAnswers = getWrongAnswers(vocabularyList, vocabulary, to, words);
			QuizQuestion quizQuestion = new QuizQuestion(questionWord, answerWord, wrongAnswers);
			
			Answer player1Answer = new Answer(player1);
			Answer player2Answer = new Answer(player2);
			
			Round round = new Round(roundNumber, quizQuestion,player1Answer,player2Answer);
			
			roundList.add(round);
		}
		
		return roundList;
	}
	
	/**
	 * Die Methode extrahiert eine zufällige Vocabulary aus einer vocabularList Objekt
	 * 
	 * @param vocabularyList
	 * @param from
	 * @param to
	 * @return zufällige Vocabulary
	 */
	private Vocabulary getRandomVocabulary(VocabularyList vocabularyList, String from, String to) {
		
		List<Vocabulary> vocabularies = new ArrayList<Vocabulary>();
		Random random = new Random();
		for(Vocabulary vocabulary : vocabularyList.getVocabularies()) {
			if ((from.equals(vocabularyList.getLanguage1()) && to.equals(vocabularyList.getLanguage2()) ) || 
					(from.equals(vocabularyList.getLanguage2())  && to.equals(vocabularyList.getLanguage1())) ) {
				vocabularies.add(vocabulary);
			}
		}
		int randomIndex = random.nextInt(vocabularies.size());
		return vocabularies.get(randomIndex);
	}
	
	
	/**
	 * Die Methode gibt ein zufälliges Wort in der gewünschten Sprache aus der Vokabel zurück.
	 * 
	 * @param vocabulary : die Vokabel
	 * @param language : die Sprache des gewünschten Wortes
	 * @return Word
	 */
	private Word getWord(VocabularyList vocabularyList,Vocabulary vocabulary,String language)  {
		Word word;
		int amountWords;
		Random random = new Random();

		if(vocabularyList.getLanguage1().equals(language)) {
			amountWords = vocabulary.getWord_language_one().size();
			word = vocabulary.getWord_language_one().get(random.nextInt(amountWords));
		} else {
			amountWords = vocabulary.getWord_language_two().size();
			word = vocabulary.getWord_language_two().get(random.nextInt(amountWords));
		}
		
		return word;
	}
	
	/**
	 * Aus allen verfügbaren falschen Antwortwörtern, werden n (gemäß NUMBER_OF_WRONG_ANSWERS)
	 * Wörter als Liste zurückgegeben.  
	 * 
	 * @param vocabulary die Vokabel mit der Frage und Antwort
	 * @param to Die Strache in welche die Übersetzung erstelltwerden soll
	 * @param words alle verfügbaren Wörter in der Übersetzungssprache
	 * @return List<Word> eine Liste an n falschen Antwortwörtern 
	 */
	private List<Word> getWrongAnswers(VocabularyList vocabularyList, Vocabulary vocabulary,String to,List<Word> words) {
		List<Word> wordsClone = new ArrayList<Word>(words);
		List<Word> wrongAnswers = new ArrayList<Word>();
		List<Word> answerWords;
		int amountWords;
		Random random = new Random();
		
		if(vocabularyList.getLanguage1().equals(to)) {
			answerWords = vocabulary.getWord_language_one();
			
		} else {
			answerWords = vocabulary.getWord_language_two();
		}
		
		
		wordsClone.removeAll(answerWords); 
		for(int i = 0; i < NUMBER_OF_WRONG_ANSWERS; i++) {
			amountWords =  wordsClone.size();
			Word wrongAnswer = wordsClone.get(random.nextInt(amountWords));
			
			if(!wrongAnswer.equals(null)) {
				wrongAnswers.add(wrongAnswer);
				wordsClone.remove(wrongAnswer);
			}
		}
		
		return wrongAnswers;
	}
}
