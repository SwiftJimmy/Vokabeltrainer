package htw.VocabularyManagmentInter;

import java.util.List;

public interface VocabularyManagementDao {
	
	public List<Word> getAllWordsByLanguage(String language);
	public List<VocabularyList> getAllVocabularyLists();
	public List<VocabularyListDTO> getAllVocabularyListMainInformation();
	public VocabularyList getVocabularyList(int vocabularyListId);
	public VocabularyList getVocabularyListByBookAndChapter(String book, String chapter);
	public Word saveWord(Word word);
	public Word getWordByWordAndLanguage(String word, String language);
	public VocabularyList saveVocabularyList(VocabularyList vocabularyList);
	public void begin();
	public void commit();
	public void close();	
}
