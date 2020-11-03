package htw.VocabularyManagmentInter;

public class NoVocabularyListFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3962858984696601101L;
	
	public NoVocabularyListFoundException(int id) {
		super("Die Vocabulary Liste mit der ID " +id +" wurde nicht gefunden.");
	}
	
	public NoVocabularyListFoundException(String message) {
		super(message);
	}
}
