package htw.ClientAdapter;

import java.util.List;
import java.util.Map;

import htw.PlayerManagementInter.Player;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlayerRestService {
	@GET("/player_service/get/{id}")
	Call<Player> getPlayer(@Path("id") int id);

	@PUT("/player_service/create")
	Call<Player> createPlayer(@Query("name") String name );
	
	@DELETE("/player_service/delete/{id}")
	Call<Boolean>  deletePlayer(@Path("id") int id);
	
	@PATCH("/player_service/edit")
	Call<Player> editPlayerName(@Body Map<String,Object> map);
	
	@GET("/player_service/get")
	Call<List<Player>> getAllPlayer();
}
