package htw.ClientAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import htw.PlayerManagementInter.DuplicatePlayerException;
import htw.PlayerManagementInter.Player;
import htw.PlayerManagementInter.PlayerManagementServiceException;
import htw.PlayerManagementInter.PlayerManagement_Service;
import htw.PlayerManagementInter.PlayerNotFoundException;
import model.ErrorMessage;
import model.GuiIOException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PlayerClientAdapter implements PlayerManagement_Service {

	private Retrofit rf = new Retrofit.Builder().baseUrl("http://localhost:8080")
			.addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create())
			.build();
	
	private PlayerRestService restService = rf.create(PlayerRestService.class);
	private Gson gson = new Gson();

	@Override
	public Player createPlayer(String name) throws DuplicatePlayerException {
		Call<Player> call = restService.createPlayer(name);
		Response<Player> response = null;
		Player player = null;
		
		try {
			response = call.execute();
			
			if(response.isSuccessful()) {
				player =  response.body();
			} else if(response.code() == 409) {
				throw new DuplicatePlayerException(name);
			} else if(response.code() == 500) {
				ErrorMessage message = gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				throw new PlayerManagementServiceException(message.getMessage());
			} 
			
		} catch (IOException e) {
			throw new GuiIOException(e);
		}
		return player;
	}
	
	@Override
	public Player editPlayerName(Player player, String name) throws  DuplicatePlayerException, PlayerNotFoundException {
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("player", player);
		map.put("name", name);
		
		Call<Player> call = restService.editPlayerName(map);
		Response<Player> response = null;
		Player result = null;
		
		try {
			response = call.execute();
			
			if(response.isSuccessful()) {
				result =  response.body();
			}  else {
				ErrorMessage message=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				if(response.code() == 404) {
					throw new PlayerNotFoundException(message.getMessage());
				} else if(response.code() == 409) {
					throw new DuplicatePlayerException(name);
				} else if(response.code() == 500) {
					throw new PlayerManagementServiceException(message.getMessage());
				}
			}
		} catch (IOException e) {
			throw new GuiIOException(e);
		}	
		
		return result;
	}
	
	@Override
	public boolean deletePlayer(int playerID) throws PlayerNotFoundException {
		Call<Boolean> call = restService.deletePlayer(playerID);
		Response<Boolean> response = null;
		boolean result = false;
		try {
			response = call.execute();
			if(response.isSuccessful()) {
				result =  response.body();
			} else {
				ErrorMessage message=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				if(response.code() == 404) {
					throw new PlayerNotFoundException(message.getMessage());
				} else if(response.code() == 500) {
					throw new PlayerManagementServiceException(message.getMessage());
				}
			}
			
		} catch (IOException e) {
			throw new GuiIOException(e);
		}
		return result;
	}

	@Override
	public List<Player> getAllPlayer() {
		Call<List<Player>> call = restService.getAllPlayer();
		Response<List<Player>> response = null;
		List<Player> result = null;
		try {
			response = call.execute();
			
			if(response.isSuccessful()) {
				result =  response.body();
			} else if(response.code() == 500) {
				ErrorMessage message=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
				throw new PlayerManagementServiceException(message.getMessage());
			}
		} catch (IOException e) {
			throw new GuiIOException(e);
		}

		return result;
	}	
}
