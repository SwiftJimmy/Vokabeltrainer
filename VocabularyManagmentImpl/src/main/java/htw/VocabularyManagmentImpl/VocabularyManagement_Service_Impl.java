package htw.VocabularyManagmentImpl;

import java.util.ArrayList;
import java.util.List;
import htw.VocabularyManagmentInter.VocabularyManagement_Service;
import htw.VocabularyManagmentInter.DouplicateVocabularyListException;
import htw.VocabularyManagmentInter.VocabManagementServiceException;
import htw.VocabularyManagmentInter.Vocabulary;
import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.VocabularyListDTO;
import htw.VocabularyManagmentInter.VocabularyManagementDao;
import htw.VocabularyManagmentInter.Word;

public class VocabularyManagement_Service_Impl implements VocabularyManagement_Service {
	
	VocabularyManagementDao vmDao;
			
	public VocabularyManagement_Service_Impl(VocabularyManagementDao vmDao) {
		super();
		this.vmDao = vmDao;
	}

	@Override
	public VocabularyList createVocabularyList(String book, String chapter, List<Vocabulary> vocabularys, String language1, String language2)
			throws DouplicateVocabularyListException {
		vmDao.begin();
		VocabularyList vocabularyList = vmDao.getVocabularyListByBookAndChapter(book, chapter);
		
		if (vocabularyList != null) {
			vmDao.commit();
			vmDao.close();
			throw new DouplicateVocabularyListException(book,chapter);
		}
			
		
		for(Vocabulary vocab : vocabularys) {
			vocab.setWord_language_one(generateWordsforVocabulary(vocab.getWord_language_one()));
			vocab.setWord_language_two(generateWordsforVocabulary(vocab.getWord_language_two()));
		}
		
		vocabularyList = vmDao.saveVocabularyList(new VocabularyList(chapter, book, vocabularys,language1,language2));
		vmDao.commit();
		vmDao.close();
		return vocabularyList;
	}
	
	@Override
	public List<VocabularyList> getAllVocabularyLists(){
		vmDao.begin();
		List<VocabularyList> vocabularyLists = vmDao.getAllVocabularyLists();
		vmDao.commit();
		vmDao.close();
		return vocabularyLists;
	}
	
	@Override
	public List<VocabularyListDTO> getAllVocabularyListsMainInformation() throws VocabManagementServiceException {
		vmDao.begin();
		List<VocabularyListDTO> vocabularyLists = vmDao.getAllVocabularyListMainInformation();
		vmDao.commit();
		vmDao.close();
		return vocabularyLists;
	}
	
	private List<Word> generateWordsforVocabulary(List<Word> wordlist) {
		List<Word> newWordList = new ArrayList<Word>();
		for (Word word : wordlist) {
			newWordList.add(createWord(word.getLanguage(),word.getWord())); 
		}
		
		return newWordList;
	}
	
	private Word createWord(String language, String wordString) {
		Word word = vmDao.getWordByWordAndLanguage(wordString, language);
		
		if(word == null) 
			word = vmDao.saveWord(new Word(wordString,language));
		
		return word;
	}
}
