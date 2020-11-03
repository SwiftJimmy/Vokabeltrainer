package htw.VocabularyManagmentImpl;

import java.util.List;

import htw.VocabularyManagmentInter.NoVocabularyListFoundException;
import htw.VocabularyManagmentInter.NoWordFoundException;
import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.VocabularySupply_Service;
import htw.VocabularyManagmentInter.Word;
import htw.VocabularyManagmentInter.VocabularyManagementDao;

public class VocabularySupply_Service_Impl implements VocabularySupply_Service  {

	VocabularyManagementDao vmDao;
	
	public VocabularySupply_Service_Impl(VocabularyManagementDao vmDao) {
		super();
		this.vmDao = vmDao;
	}
	
	@Override
	public List<Word> getWords(String language) throws NoWordFoundException {
		List<Word> wordsList = vmDao.getAllWordsByLanguage(language);
		if(wordsList.isEmpty())
			throw new NoWordFoundException(language);
		return wordsList;
	}
	
	@Override
	public VocabularyList getVocabularyList(int vocabularyListId) throws NoVocabularyListFoundException {
		VocabularyList vocabularyList = vmDao.getVocabularyList(vocabularyListId);
		if(vocabularyList == null) {
			throw new NoVocabularyListFoundException(vocabularyListId);
		}
		return vocabularyList;
	}
}
