package htw.VocabularyManagmentInter;

import java.util.List;
import java.util.Map;

/**
 * Die VocabularyManagement_Service Schnittstelle realisiert die Erstellung und Abfrage der VokabularyList Objekte. 
 */
public interface VocabularyManagement_Service {
		
	/**
	 * Die Methode erstellt ein VocabularyList Objekt. Um sicherzustellen, dass noch kein VocabularyList Objekt 
	 * mit demselben Buch- und Kapiteltitel existiert, wird vorab überprüft, ob diese (in Kombination) bereits 
	 * in der Datenbank vorkommen.
	 * 
	 * @param book : Der Buchtitel.
	 * @param chapter : Der Titel des Kapitels.
	 * @param vocabularys : Die Liste an Vokabeln des Buch-Kapitels.
	 * @param language1 ; Eine Übersetzungssprache des Buch-Kapitels.
	 * @param language2 ; Eine wetere Übersetzungssprache des Buch-Kapitels.
	 * @return VocabularyList Objekt : Das erstellte VocabularyList Objekt.
	 * @throws DouplicateVocabularyListException : Falls es bereits eine VocabularyList mit demselben Buch- und Kapiteltitel existiert. 
	 * @throws VocabManagementServiceException : Falls ein technischer Fehler auftritt.
	 */
	public VocabularyList createVocabularyList(String book,String chapter, List<Vocabulary> vocabularys, String language1, String language2) throws DouplicateVocabularyListException, VocabManagementServiceException;
	
	/**
	 * Die Methode gibt eine Liste aller VocabularyList Objekte zurück.
	 * 
	 * @return List<VocabularyList> : Eine Liste aller VokabularyList Objekte.
	 * 		   eine leere Liste : Falls kein VocabularyList Objekt existieren.
	 * @throws VocabManagementServiceException : Falls ein technischer Fehler auftritt.
	 */
	public List<VocabularyList> getAllVocabularyLists() throws VocabManagementServiceException;
	
	/**
	 * Die Methode gibt eine Liste der Hauptinformationen aller VocabularyList Objekte zurück. Diese Informationen werden jeweils in einem 
	 * VocabularyListDTO Objekt gespeichert und bestehen aus dem Buch- und Kapiteltitel, der Id sowie den beiden Sprachen einer VocabularyList. 
	 * 
	 * @return List<VocabularyListDTO> : Eine Liste aller VocabularyListDTO Objekte.
	 * 		   eine leere Liste : Falls kein VocabularyList Objekt existieren.
	 * @throws VocabManagementServiceException : Falls ein technischer Fehler auftritt.
	 */
	public List<VocabularyListDTO> getAllVocabularyListsMainInformation() throws VocabManagementServiceException;
	
}
