package htw.GameManagmentInter;

import java.util.List;

public interface GameManagementDao {
	public boolean saveGame(Game entity) ;
	public Game getGame(int primaryKey) ;
	public boolean deleteGame(Game game) ;
	public List<Game> getAllGamesByPlayer(int playerId) ;
	public void begin() ;
	public void commit() ;
	public void rollback() ;
	public boolean updateGame(Game game);
	public void close();
}
