package htw.ClientAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;
import htw.VocabularyManagmentInter.DouplicateVocabularyListException;
import htw.VocabularyManagmentInter.VocabManagementServiceException;
import htw.VocabularyManagmentInter.Vocabulary;
import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.VocabularyListDTO;
import htw.VocabularyManagmentInter.VocabularyManagement_Service;
import model.ErrorMessage;
import model.GuiIOException;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VocabularyClientAdapter implements VocabularyManagement_Service {
	private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(1000,TimeUnit.SECONDS).build();
	private Retrofit rf = new Retrofit.Builder().baseUrl("http://localhost:8080").client(client)
			.addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create())
			.build();
	private Gson gson = new Gson();
	private VocabRestService service = rf.create(VocabRestService.class);
	

	@Override
	public List<VocabularyList> getAllVocabularyLists() {
		Call<List<VocabularyList>> call = service.getAllVocabularyLists();
		Response<List<VocabularyList>> response = null;
		List<VocabularyList> result = null;
		try {
			response = call.execute();
			
			if(response.isSuccessful()) {
				result =  response.body();
			} else if (response.code() == 500) {
				ErrorMessage message = gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				throw new VocabManagementServiceException(message.getMessage());
			}
		} catch (IOException e) {
			throw new GuiIOException(e);
		}
		return result;
	}
	
	@Override
	public VocabularyList createVocabularyList(String book, String chapter, List<Vocabulary> vocabularys, String language1, String language2) throws DouplicateVocabularyListException {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("language1", language1);
		map.put("language2", language2);
		map.put("book", book);
		map.put("chapter", chapter);
		map.put("vocabularys", vocabularys);

		Call<VocabularyList> call = service.createVocabularyList(map);
		Response<VocabularyList> response = null;
		VocabularyList result = null;
		try {
			response = call.execute();
			
			if(response.isSuccessful()) {
				result =  response.body();
			} else {
				ErrorMessage message = gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				if(response.code() == 409) {
					throw new DouplicateVocabularyListException(message.getMessage());
				} else if (response.code() == 500) {
					throw new VocabManagementServiceException(message.getMessage());
				}
			}
		} catch (IOException e) {
			throw new GuiIOException(e);
		}
		return result;
	}

	@Override
	public List<VocabularyListDTO> getAllVocabularyListsMainInformation() throws VocabManagementServiceException {
		Call<List<VocabularyListDTO>> call = service.getAllVocabularyListsMainInformation();
		Response<List<VocabularyListDTO>> response = null;
		List<VocabularyListDTO> result = null;
		try {
			response = call.execute();
			
			if(response.isSuccessful()) {
				result =  response.body();
			} else if (response.code() == 500) {
				ErrorMessage message = gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				throw new VocabManagementServiceException(message.getMessage());
			}
		} catch (IOException e) {
			throw new GuiIOException(e);
		}
		return result;
	}
}
