package model;

import java.util.List;

import htw.ClientAdapter.VocabularyClientAdapter;
import htw.VocabularyManagmentInter.VocabularyListDTO;
import javafx.concurrent.Task;

public class GetAllVocabularyListDTOWorker extends Task<List<VocabularyListDTO>> {

	private VocabularyClientAdapter vca;
	private List<VocabularyListDTO> vocabLists;
	private String errorMessage;
	
	public GetAllVocabularyListDTOWorker() {
		super();
		this.vca = new VocabularyClientAdapter();
	}

	@Override
	protected List<VocabularyListDTO> call() {
		try {
			this.vocabLists =  vca.getAllVocabularyListsMainInformation();
		} catch (RuntimeException e) {
			this.errorMessage = e.getMessage();
		}
		return vocabLists;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public List<VocabularyListDTO> getVocabularyLists() {
		return vocabLists;
	}
}
