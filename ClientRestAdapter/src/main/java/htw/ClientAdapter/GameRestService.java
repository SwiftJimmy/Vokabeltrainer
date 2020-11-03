package htw.ClientAdapter;

import java.util.List;
import java.util.Map;

import htw.GameManagmentInter.Game;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GameRestService {
	
	@PUT("/game_service/create")
	Call<Game> createGame(	@Body Map<String,Object> map);
	
	@GET("/game_service/get")
	Call<List<Game>> getAllGamesByPlayer(@Query("playerID")  int playerId);
	
	@DELETE("/game_service/delete/{id}")
	Call<Boolean> deleteGame(@Path("id") int gameID);
	
	@DELETE("/game_service/delete/byPlayer/{id}")
	Call<Boolean> deleteAllGamesByPlayer(@Path("id") int playerId);
	
	@PUT("/game_service/update")
	Call<Boolean> updateGame(@Body Game game);
}
