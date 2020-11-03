package htw.PlayerManagementImpl;

import java.util.List;
import htw.PlayerManagementInter.DuplicatePlayerException;
import htw.PlayerManagementInter.Player;
import htw.PlayerManagementInter.PlayerManagementDao;
import htw.PlayerManagementInter.PlayerNotFoundException;
import htw.PlayerManagementInter.PlayerManagement_Service;

public class PlayerManagement_Service_impl implements PlayerManagement_Service {
	
	private PlayerManagementDao pmDao;
		
	public PlayerManagement_Service_impl(PlayerManagementDao pmDao) {
		super();
		this.pmDao = pmDao;
	}

	@Override
	public Player createPlayer(String name) throws DuplicatePlayerException {
		Player player = null;
		pmDao.begin();

		Player dbPlayer = pmDao.getPlayerByName(name);
	
		if(dbPlayer != null) {
			pmDao.close();
			throw new DuplicatePlayerException(name);
		}

		player = new Player(name);
		pmDao.savePlayer(player);
		pmDao.commit();
		pmDao.close();
		return player;
	}
	
	@Override
	public Player editPlayerName(Player player, String name) throws  DuplicatePlayerException, PlayerNotFoundException {
		pmDao.begin();
		Player dbPlayerName = pmDao.getPlayerByName(name);
		
		if(dbPlayerName != null) {
			pmDao.commit();
			pmDao.close();
			throw new DuplicatePlayerException(name);
		}
			
		Player dbPlayerId = pmDao.getPlayer(player.getId());
		
		if(dbPlayerId == null) {
			pmDao.commit();
			pmDao.close();
			throw new PlayerNotFoundException(player.getId());
		}
			
		player.setName(name);
		pmDao.updatePlayer(player);
		
		pmDao.commit();
		pmDao.close();
		return player;
	}
	
	@Override
	public boolean deletePlayer(int playerID) throws PlayerNotFoundException {
		pmDao.begin();		
		Player player = pmDao.getPlayer(playerID);
		
		if(player == null) {
			pmDao.close();
			throw new PlayerNotFoundException(playerID);
		}
			
		
		boolean result = pmDao.deletePlayer(player);
		pmDao.commit();
		pmDao.close();
		return result;
	}
	
	@Override
	public List<Player> getAllPlayer() {
		pmDao.begin();	
		List<Player> playerList = pmDao.getAllPlayers();
		pmDao.commit();
		pmDao.close();
		return playerList;
	}
}
