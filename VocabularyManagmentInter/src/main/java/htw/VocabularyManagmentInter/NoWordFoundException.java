package htw.VocabularyManagmentInter;
 
public class NoWordFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5630172835913283199L;

	public NoWordFoundException(String language) {
		super("Es wurde ein Wort in der Sprache " +language +" gefunden.");
	}
}
