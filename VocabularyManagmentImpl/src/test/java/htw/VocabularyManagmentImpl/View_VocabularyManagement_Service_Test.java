package htw.VocabularyManagmentImpl;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import htw.VocabularyManagmentInter.DouplicateVocabularyListException;
import htw.VocabularyManagmentInter.NoVocabularyListFoundException;
import htw.VocabularyManagmentInter.NoWordFoundException;
import htw.VocabularyManagmentInter.VocabManagementServiceException;
import htw.VocabularyManagmentInter.Vocabulary;
import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.VocabularyManagementDao;
import htw.VocabularyManagmentInter.Word;

public class View_VocabularyManagement_Service_Test {
	
	private VocabularyManagementDao vmDao;
	private VocabularySupply_Service_Impl gameService;
	private VocabularyManagement_Service_Impl vocabularyService;
	private String englishLanguage = "English";
	private String germanLanguage = "Deutsch";
	private String spanishLanguage = "Spainisch";
	private String chapter = "Chapter";
	private String book = "Book";
	
	
	@Before
	public void setUp() {
		vmDao = Mockito.mock(VocabularyManagementDao.class);
		vocabularyService = new VocabularyManagement_Service_Impl(vmDao);
		gameService = new VocabularySupply_Service_Impl(vmDao);
	}
	
	private List<Word> generateWordList(String[] words, String language){
		List<Word> wordList = new ArrayList<Word>();
		for(String wordName: words) {
			wordList.add(new Word(wordName, language));
		}
		return wordList;
	}
	
	private VocabularyList generateVocabularyList(String[] englishWords, String[] germanWords) {
		
		List<Vocabulary> vocabularys = new ArrayList<Vocabulary>();
		
		for(int i = 0; i < englishWords.length; i++) {
			
			Word englishword = new Word(englishWords[i], englishLanguage);
			Word germanword = new Word(germanWords[i], germanLanguage);
			
			List<Word> englishWordList = new ArrayList<Word>();
			List<Word> germanWordList = new ArrayList<Word>();
			
			englishWordList.add(englishword);
			germanWordList.add(germanword);
			
			Vocabulary vocabel = new Vocabulary(englishWordList,germanWordList);
			vocabularys.add(vocabel);
		}
		return new VocabularyList(chapter, book, vocabularys,englishLanguage,germanLanguage);
	}
	
		
	/**
	 * Die Methode testet, ob durch den Aufruf der getAllVocabularyLists Methode alle vorab erstellten VocabularyLists zurückgegeben werden.
	 * 
	 *  Dafür werden 2 VocabularyLists erstellt.
	 * @throws VocabManagementServiceException 
	 */
	@Test
	public void testGetAllVocabularyListsSuccess() throws VocabManagementServiceException {
		List<VocabularyList> expectedVocabularyLists = new ArrayList<VocabularyList>();
		String[] englishWords =  {"Apple", "Bird", "Car", "Dinosaur"};
		String[] germanWords =  {"Apfel", "Vogel", "Auto", "Dinosaurier"};
		VocabularyList vocabularyList1 = generateVocabularyList(englishWords,germanWords);
		VocabularyList vocabularyList2 = generateVocabularyList(englishWords,germanWords);
		expectedVocabularyLists.add(vocabularyList1);
		expectedVocabularyLists.add(vocabularyList2);
		
		Mockito.when(vmDao.getAllVocabularyLists()).thenReturn(expectedVocabularyLists);
		
		List<VocabularyList> vocabularyList = vocabularyService.getAllVocabularyLists();
		
		Assert.assertEquals(expectedVocabularyLists, vocabularyList);
	}

	/**
	 * Die Methode testet, ob keine VocabularyListe wird, wenn
	 * beim Aufruf der getAllVocabularyLists Methode noch keine VocabularyList erstellt wurde
	 * @throws VocabManagementServiceException 
	 */
	@Test
	public void testGetAllVocabularyListsFailes() throws VocabManagementServiceException {
		Mockito.when(vmDao.getAllVocabularyLists()).thenReturn(null);
		List<VocabularyList> list = vocabularyService.getAllVocabularyLists();
		Assert.assertEquals(null, list);
	}
	
	/**
	 * Die Methode testet, ob durch den Aufruf der getWords Methode alle vorab erstellten Words einer Sprache zurückgegeben werden.
	 * @throws NoWordFoundException 
	 * @throws VocabManagementServiceException 
	 */
	@Test
	public void testGetWordsSuccess() throws NoWordFoundException, VocabManagementServiceException {
		String[] englishWords =  {"Apple", "Bird", "Car", "Dinosaur"};
		String[] germanWords =  {"Apfel", "Vogel", "Auto", "Dinosaurier"};
		generateVocabularyList(englishWords,germanWords);
		
		List<Word> expectedEnglishWordList = generateWordList(englishWords, englishLanguage);
		List<Word> expectedGermanWordList = generateWordList(germanWords, germanLanguage);
		
		Mockito.when(vmDao.getAllWordsByLanguage(englishLanguage)).thenReturn(expectedEnglishWordList);
		Mockito.when(vmDao.getAllWordsByLanguage(germanLanguage)).thenReturn(expectedGermanWordList);
		
		List<Word> allEnglishWords = gameService.getWords(englishLanguage);
		List<Word> allGermanWords = gameService.getWords(germanLanguage);
		
		Assert.assertEquals(allEnglishWords, expectedEnglishWordList);
		Assert.assertEquals(allGermanWords, expectedGermanWordList);
	}
	
	/**
	 * Die Methode testet, ob eine NoWordFoundException geworfen wird, wenn
	 * beim Aufruf der getWords Methode noch keine Word Objekte erstellt wurden
	 * @throws NoWordFoundException 
	 * @throws VocabManagementServiceException 
	 */
	@Test(expected = NoWordFoundException.class)
	public void testGetWordsFailes() throws NoWordFoundException, VocabManagementServiceException {
		List<Word> wordList = new ArrayList<Word>();
		Mockito.when(vmDao.getAllWordsByLanguage(englishLanguage)).thenReturn(wordList);
		gameService.getWords(englishLanguage);
	}
	
	/**
	 * Die Methode testet, ob beim Aufruf der createVocabularyList ein VocabularyList Objekt erfolgreich erstellt wird. 
	 * @throws DouplicateVocabularyListException
	 * @throws VocabManagementServiceException
	 */
	@Test
	public void createVocabularyListSuccess() throws DouplicateVocabularyListException, VocabManagementServiceException  {
		String[] englishWords =  {"Apple", "Bird", "Car", "Dinosaur"};
		String[] germanWords =  {"Apfel", "Vogel", "Auto", "Dinosaurier"};
		VocabularyList vocabularyListExp = generateVocabularyList(englishWords,germanWords);
		
		Mockito.when(vmDao.getVocabularyListByBookAndChapter(book, chapter)).thenReturn(null);
		Mockito.when(vmDao.saveVocabularyList(Mockito.any())).thenReturn(vocabularyListExp);
		
		VocabularyList vocabularyListActual = vocabularyService.createVocabularyList(book, chapter, vocabularyListExp.getVocabularies(),englishLanguage,germanLanguage);
		

		Assert.assertEquals(vocabularyListActual, vocabularyListExp);
	}
	
	/**
	 * Die Methode testet, ob eine DouplicateVocabularyListException geworfen wird, wenn
	 * beim Aufruf der createVocabularyList Methode bereits eine VocabularyList mit dem Buch und Kapitelnamen existiert 
	 * 
	 * @throws DouplicateVocabularyListException
	 * @throws VocabManagementServiceException
	 */
	@Test(expected = DouplicateVocabularyListException.class)
	public void createVocabularyListFails() throws DouplicateVocabularyListException, VocabManagementServiceException  {
		String[] englishWords =  {"Apple", "Bird", "Car", "Dinosaur"};
		String[] germanWords =  {"Apfel", "Vogel", "Auto", "Dinosaurier"};
		VocabularyList vocabularyListExp = generateVocabularyList(englishWords,germanWords);
		Mockito.when(vmDao.getVocabularyListByBookAndChapter(book, chapter)).thenReturn(vocabularyListExp);
		
		vocabularyService.createVocabularyList(book, chapter, vocabularyListExp.getVocabularies(),englishLanguage,germanLanguage );
	}
	
	/**
	 * Die Methode testet, ob eine VocabularyList anhand der ID zurückgegebn wird
	 * @throws NoVocabularyListFoundException 
	 */
	@Test
	public void getVocabularyListSuccess() throws NoVocabularyListFoundException {
		String[] englishWords =  {"Apple", "Bird", "Car", "Dinosaur"};
		String[] germanWords =  {"Apfel", "Vogel", "Auto", "Dinosaurier"};
		VocabularyList expect =  generateVocabularyList(englishWords,germanWords);
		Mockito.when(vmDao.getVocabularyList(9)).thenReturn(expect);
		VocabularyList result = gameService.getVocabularyList(9);
		
		Assert.assertEquals(expect, result);
	}
	
	/**
	 * Die Methode testet, ob eine NoVocabularyListFoundException geworfen wird, wenn
	 * beim Aufruf der getVocabularyList Methode eine unbekannte ID übergeben wird
	 * @throws NoVocabularyListFoundException 
	 */
	@Test(expected = NoVocabularyListFoundException.class)
	public void getVocabularyListFails() throws NoVocabularyListFoundException {
		Mockito.when(vmDao.getVocabularyList(9)).thenReturn(null);
		gameService.getVocabularyList(9);
	}	
}
