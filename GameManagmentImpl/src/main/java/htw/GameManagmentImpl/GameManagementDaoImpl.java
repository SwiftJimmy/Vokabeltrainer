package htw.GameManagmentImpl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import htw.GameManagmentInter.Game;
import htw.GameManagmentInter.GameManagementDao;
import htw.GameManagmentInter.GameManagementServiceException;

public class GameManagementDaoImpl implements GameManagementDao {
	
	private EntityManager entityManager;
	
	public GameManagementDaoImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public boolean saveGame(Game game)   {
		try {
			entityManager.persist(game);
			return true;
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new GameManagementServiceException(exp);
		} 
	}

	@Override
	public Game getGame(int gameID)  {
		try {
			return entityManager.find(Game.class, gameID);
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new GameManagementServiceException(exp);
		} 
	}
	
	@Override
	public boolean updateGame(Game game) {
		try {
			this.entityManager.merge(game);
			this.entityManager.flush();
			return true;
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new GameManagementServiceException(exp);
		} 
	}

	@Override
	public List<Game> getAllGamesByPlayer(int playerId) {
		try {
			TypedQuery<Game> query = entityManager.createNamedQuery("Game.findAllByPlayer", Game.class);
			return query.setParameter("playerId", playerId).getResultList(); 
		} catch (RuntimeException  exp) {
			System.err.println(exp.getMessage());
			close();
			throw new GameManagementServiceException(exp);
		}
	}
	
	@Override
	public boolean deleteGame(Game game)  {
		try {
			entityManager.remove(game);
			return true;
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new GameManagementServiceException(exp);
		}
	}

	@Override
	public void begin()  {
		try {
			if (!this.entityManager.getTransaction().isActive())
				this.entityManager.getTransaction().begin();
		} catch (RuntimeException exp ) {
			System.err.println(exp.getMessage());
			close();
			throw new GameManagementServiceException(exp);
		}
	}
	
	@Override
	public void commit() {
		try {
			this.entityManager.getTransaction().commit();
		} catch (RuntimeException exp ) {
			System.err.println(exp.getMessage());
			close();
			throw new GameManagementServiceException(exp);
		}
	}

	@Override
	public void rollback() {
		try {
			this.entityManager.getTransaction().rollback();
		} catch (RuntimeException exp ) {
			close();
			throw new GameManagementServiceException(exp);
		}
	}
	
	@Override
	public void close() {
		try {
			if(entityManager.isOpen()) {
				entityManager.close();
			}
		} catch (RuntimeException exp ) {
			System.err.println(exp.getMessage());
			throw new GameManagementServiceException(exp);
		}
	}
}
