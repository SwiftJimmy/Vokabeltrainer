package htw.VocabularyManagmentInter;

public class DouplicateVocabularyListException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DouplicateVocabularyListException(String book, String chapter) {
		super("Es existiert bereits ein Kapitel " + chapter + " in dem Buch " + book);
	}
	
	public DouplicateVocabularyListException(String message) {
		super(message);
	}

}
