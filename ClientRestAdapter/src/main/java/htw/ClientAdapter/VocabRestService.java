package htw.ClientAdapter;

import java.util.List;
import java.util.Map;

import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.VocabularyListDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface VocabRestService {
	@GET("/vocab_service/get")
	public Call<List<VocabularyList>> getAllVocabularyLists();
	
	@GET("/vocab_service/get/maininfo")
	public Call<List<VocabularyListDTO>> getAllVocabularyListsMainInformation();
	
	@PUT("/vocab_service/create")
	public Call<VocabularyList> createVocabularyList(@Body Map<String,Object> map);
	

}
