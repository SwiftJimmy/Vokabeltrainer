package htw.PlayerManagementInter;

import java.util.List;

public interface PlayerManagementDao {

		public boolean savePlayer(Player entity) throws PlayerManagementServiceException;
		public Player getPlayer(int primaryKey) throws PlayerManagementServiceException;
		public boolean deletePlayer(Player player) throws PlayerManagementServiceException;
		public List<Player> getAllPlayers() throws PlayerManagementServiceException;
		public Player getPlayerByName(String name) throws PlayerManagementServiceException;
		public boolean updatePlayer(Player player) throws PlayerManagementServiceException;
		public void begin() throws PlayerManagementServiceException;
		public void commit() throws PlayerManagementServiceException;
		public void close();
}
