package htw.RestAdapter;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.injectors.ConstructorInjection;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import htw.GameManagmentImpl.GameManagementDaoImpl;
import htw.GameManagmentImpl.GameManagement_Service_Impl;
import htw.GameManagmentInter.Game;
import htw.GameManagmentInter.GameManagement_Service;
import htw.GameManagmentInter.GameNotCreatableException;
import htw.GameManagmentInter.GameNotFoundException;
import htw.GameManagmentInter.OnePlayerTwoTimesInGameException;
import htw.PlayerManagementImpl.PlayerManagementDaoImpl;
import htw.PlayerManagementInter.Player;
import htw.VocabularyManagmentImpl.VocabularyManagementDaoImpl;
import htw.VocabularyManagmentImpl.VocabularySupply_Service_Impl;

@RestController
@RequestMapping("/game_service")
public class GameManagement_Service_Controller  {
	
	@PutMapping("/create")
	public Game createGame(	@RequestBody Map<String,Object> map) throws   OnePlayerTwoTimesInGameException, GameNotCreatableException {
		GameManagement_Service gms =  registerComponents();
		ObjectMapper objectMapper = new ObjectMapper();
				
		Player player1 =  objectMapper.convertValue(map.get("player1"), Player.class);
		Player player2 = objectMapper.convertValue(map.get("player2"), Player.class);
		int vocabularyListId = objectMapper.convertValue(map.get("vocabularyListId"), Integer.class);
		String from = (String) map.get("from");
		String to = (String) map.get("to");
		
		Game game = gms.createGame(player1, player2, from, to, vocabularyListId);
	
		return game;
	}

	@GetMapping("/get")
	public List<Game> getAllGamesByPlayerID(@RequestParam  int playerID) {
		GameManagement_Service gms =  registerComponents();
		List<Game> games = gms.getAllGamesByPlayer(playerID);
		return games;
	}
		
	@DeleteMapping("/delete/{id}")
	public boolean deleteGame(@PathVariable("id") int gameID) throws GameNotFoundException {
		GameManagement_Service gms =  registerComponents();
		boolean result =  gms.deleteGame(gameID);
		return result;
	}
	
	@DeleteMapping("/delete/byPlayer/{id}")
	public boolean deleteAllGamesByPlayer(@PathVariable("id") int playerId){
		GameManagement_Service gms =  registerComponents();
		boolean result =  gms.deleteAllGamesByPlayer(playerId);
		return result;
	}
	
	@PutMapping("/update")
	public boolean updateGame(@RequestBody Map<String, Object> payload) throws GameNotFoundException {
		ObjectMapper objectMapper = new ObjectMapper();
		GameManagement_Service gms =  registerComponents();
		Game game = objectMapper.convertValue(payload, Game.class);
		boolean result = gms.updateGame(game);
		return result;
	}
	
	private GameManagement_Service registerComponents() {
		MutablePicoContainer container = new DefaultPicoContainer(new ConstructorInjection());	
		EntityManager em = EntityManagerService.getInstance().getEntityManagerFactory().createEntityManager();
		container.addComponent(PlayerManagementDaoImpl.class);
		container.addComponent(GameManagementDaoImpl.class);
		container.addComponent(GameManagement_Service_Impl.class);
		container.addComponent(VocabularyManagementDaoImpl.class);
		container.addComponent(VocabularySupply_Service_Impl.class);
		container.addComponent(em);
		return container.getComponent(GameManagement_Service_Impl.class);
	}
}
