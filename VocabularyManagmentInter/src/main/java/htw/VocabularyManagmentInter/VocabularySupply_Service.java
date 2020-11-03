package htw.VocabularyManagmentInter;

import java.util.List;

/**
 * Die VocabularySupply_Service Schnittstelle realisiert die Abfrage von Vocabulary- und Word Objekten. 
 */
public interface VocabularySupply_Service {
	
	/**
	 * Die Methode gibt eine Lister aller, in der Datenbank verfügbaren, Wörter eine Sprache zurück.
	 * 
	 * @param language : Die Sprache, für welche alle Wörter benötigt werden.
	 * @return List<Word> : Die Liste aller verfügbaren Wörter einer Sprache.
	 * @throws NoWordFoundException : Falls kein Wort gefunden wird.
	 * @throws VocabManagementServiceException : Falls ein technischer Fehler auftritt.
	 */
	public List<Word> getWords( String language) throws NoWordFoundException, VocabManagementServiceException;
	
	/**
	 * Die Methode gibt ein VocabularyList Objekt anhand der übergebenen Id zurück.
	 * 
	 * @param vocabularyListId : ID der gesuchten VocabularyListe
	 * @return VocabularyList : Die gefundene VokabularyList.
	 * @throws NoVocabularyListFoundException : Falls kein VocabularyListObjekt  mit der ID existiert.
	 * @throws VocabManagementServiceException : Falls ein technischer Fehler auftritt.
	 */
	public VocabularyList getVocabularyList(int vocabularyListId) throws NoVocabularyListFoundException, VocabManagementServiceException;
}
