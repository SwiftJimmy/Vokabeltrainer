package model;

import java.util.List;
import htw.ClientAdapter.VocabularyClientAdapter;
import htw.VocabularyManagmentInter.VocabularyList;
import javafx.concurrent.Task;

public class GetAllVocabularyListWorker extends Task<List<VocabularyList>> {

	private VocabularyClientAdapter vca;
	private List<VocabularyList> vocabLists;
	private String errorMessage;
	
	public GetAllVocabularyListWorker() {
		super();
		this.vca = new VocabularyClientAdapter();
	}

	@Override
	protected List<VocabularyList> call() {
		try {
			this.vocabLists =  vca.getAllVocabularyLists();
		} catch (RuntimeException e) {
			this.errorMessage = e.getMessage();
		}
		return vocabLists;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public List<VocabularyList> getVocabularyLists() {
		return vocabLists;
	}
}
