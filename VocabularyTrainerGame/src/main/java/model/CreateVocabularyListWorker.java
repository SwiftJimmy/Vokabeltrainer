package model;

import java.util.List;

import htw.ClientAdapter.VocabularyClientAdapter;
import htw.VocabularyManagmentInter.DouplicateVocabularyListException;
import htw.VocabularyManagmentInter.Vocabulary;
import htw.VocabularyManagmentInter.VocabularyList;
import javafx.concurrent.Task;

public class CreateVocabularyListWorker extends Task<VocabularyList> {
	
	private List<Vocabulary> vocabularys;
	private String book;
	private String chapter;
	private String language1;
	private String language2;
	private VocabularyClientAdapter vca;
	private VocabularyList vocabularyList;
	private String errorMessage;
	
	public CreateVocabularyListWorker(String book,String chapter,List<Vocabulary> vocabularys, String language1, String language2) {
		super();
		this.language1 = language1;
		this.language2 = language2;
		this.book = book;
		this.chapter = chapter;
		this.vocabularys = vocabularys;
		this.vca = new VocabularyClientAdapter();
	}

	@Override
	protected VocabularyList call()   {
		
		try {
			this.vocabularyList =  vca.createVocabularyList(book, chapter, vocabularys, language1, language2);
		} catch (RuntimeException | DouplicateVocabularyListException e) {
			errorMessage = e.getMessage();
		}
		
		return vocabularyList;
	}

	public VocabularyList getVocabularyList() {
		return vocabularyList;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
