package htw.RestAdapter;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.injectors.ConstructorInjection;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import htw.PlayerManagementImpl.PlayerManagementDaoImpl;
import htw.PlayerManagementImpl.PlayerManagement_Service_impl;
import htw.PlayerManagementInter.DuplicatePlayerException;
import htw.PlayerManagementInter.Player;
import htw.PlayerManagementInter.PlayerManagement_Service;
import htw.PlayerManagementInter.PlayerNotFoundException;

@RestController
@RequestMapping("/player_service")
public class PlayerManagement_Service_Controller {

	@PutMapping("/create")
	public Player createPlayer(@RequestParam String name) throws DuplicatePlayerException {
		PlayerManagement_Service pms = registerComponents();
		Player player;	
		player = pms.createPlayer(name);		
		return player;
	}
	
	@GetMapping("/get")
	public List<Player> getAllPlayer() {
		PlayerManagement_Service pms = registerComponents();
		List<Player> player = pms.getAllPlayer();
		return player;
	}
	
	@DeleteMapping("/delete/{id}")
	public boolean deletePlayer(@PathVariable("id") int PlayerID) throws PlayerNotFoundException {
		PlayerManagement_Service pms = registerComponents();
		boolean result = pms.deletePlayer(PlayerID);
		return result;
	}

	@PatchMapping("/edit")
	public Player editPlayerName(@RequestBody Map<String, Object> payload) throws DuplicatePlayerException, PlayerNotFoundException {
		ObjectMapper objectMapper = new ObjectMapper();
		PlayerManagement_Service pms = registerComponents();
		Player player = objectMapper.convertValue(payload.get("player"), Player.class);
		String name = objectMapper.convertValue(payload.get("name"), String.class);
		
		Player result = pms.editPlayerName(player, name);
		return result;
	}
		
	private PlayerManagement_Service registerComponents() {
		MutablePicoContainer container = new DefaultPicoContainer(new ConstructorInjection());	
		EntityManager em = EntityManagerService.getInstance().getEntityManagerFactory().createEntityManager();
		container.addComponent(PlayerManagementDaoImpl.class);
		container.addComponent(PlayerManagement_Service_impl.class);
		container.addComponent(em);
		return container.getComponent(PlayerManagement_Service_impl.class);
	}
}
