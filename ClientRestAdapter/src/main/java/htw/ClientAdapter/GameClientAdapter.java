package htw.ClientAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import htw.GameManagmentInter.Game;
import htw.GameManagmentInter.GameManagementServiceException;
import htw.GameManagmentInter.GameManagement_Service;
import htw.GameManagmentInter.GameNotCreatableException;
import htw.GameManagmentInter.GameNotFoundException;
import htw.GameManagmentInter.OnePlayerTwoTimesInGameException;
import htw.PlayerManagementInter.Player;
import model.ErrorMessage;
import model.GuiIOException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GameClientAdapter implements GameManagement_Service{
	
	private Retrofit rf = new Retrofit.Builder().baseUrl("http://localhost:8080")
			.addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create())
			.build();
	private GameRestService restService = rf.create(GameRestService.class);
	Gson gson = new Gson();
	
	@Override
	public Game createGame(Player player1, Player player2, String from, String to, int vocabularyListId)
			throws OnePlayerTwoTimesInGameException, GameNotCreatableException{
		Game result = null;
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("player1", player1);
		map.put("player2", player2);
		map.put("from", from);
		map.put("to", to);
		map.put("vocabularyListId", vocabularyListId);
		
		Call<Game> call = restService.createGame(map);
		Response<Game> response = null;
		try {
			response = call.execute();
			if(response.isSuccessful()) {
				result = response.body();
			} else {
				ErrorMessage message = gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				if (response.code() == 422) {
				    throw new GameNotCreatableException(message.getMessage());
				} else if (response.code() == 409) {
					throw new OnePlayerTwoTimesInGameException(player1.getName());
				} else if (response.code() == 500) {
					throw new GameManagementServiceException(message.getMessage());
				}
			}
		} catch (IOException e) {
			throw new GuiIOException(e);
		}
		return result;
	}

	@Override
	public boolean updateGame(Game game) throws GameNotFoundException{
		Call<Boolean> call = restService.updateGame(game);
		Response<Boolean> response = null;
		boolean result = false;
		try {
			response = call.execute();
			if(response.isSuccessful()) {
				result = response.body();
			}  else {
				ErrorMessage message=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				if (response.code() == 404) {
					throw new GameNotFoundException(message.getMessage());
				} else if (response.code() == 500) {
					throw new GameManagementServiceException(message.getMessage());
				}
			}
			
		} catch (IOException e) {
			throw new GuiIOException(e);
		}

		return result;
		
	}

	@Override
	public boolean deleteGame(int gameID) throws GameNotFoundException {
		Call<Boolean> call = restService.deleteGame(gameID);
		Response<Boolean> response = null;
		boolean result = false;
		try {
			response = call.execute();
			if (response.isSuccessful()) {
				result = response.body();
			} else {
				ErrorMessage message=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				if (response.code() == 404) {
					throw new GameNotFoundException(message.getMessage());
				} else if (response.code() == 500) {
					throw new GameManagementServiceException(message.getMessage());
				}
			}
			
		} catch (IOException e) {
			throw new GuiIOException(e);
		}
		return result;
	}
	
	@Override
	public List<Game> getAllGamesByPlayer(int playerID)  {
		Call<List<Game>> call = restService.getAllGamesByPlayer(playerID);
		Response<List<Game>> response = null;
		List<Game> result = null;
		try {
			response = call.execute();
			if(response.isSuccessful()) {
				result = response.body();
			} else if (response.code() == 500) {
				ErrorMessage message=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				throw new GameManagementServiceException(message.getMessage());
			}
			
		} catch (IOException e) {
			throw new GuiIOException(e);
		}
		return result;
	}

	@Override
	public boolean deleteAllGamesByPlayer(int playerID) throws GameManagementServiceException {
		Call<Boolean> call = restService.deleteAllGamesByPlayer(playerID);
		Response<Boolean> response = null;
		boolean result = false;
		try {
			response = call.execute();
			if(response.isSuccessful()) {
				result = response.body();
			} else if (response.code() == 500) {
				ErrorMessage message=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				throw new GameManagementServiceException(message.getMessage());
			}
			
		} catch (IOException e) {
			throw new GuiIOException(e);
		}
		return result;
	}
}
