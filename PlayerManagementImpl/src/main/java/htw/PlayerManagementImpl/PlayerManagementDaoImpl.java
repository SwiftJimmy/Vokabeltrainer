package htw.PlayerManagementImpl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import htw.PlayerManagementInter.Player;
import htw.PlayerManagementInter.PlayerManagementDao;
import htw.PlayerManagementInter.PlayerManagementServiceException;

public class PlayerManagementDaoImpl implements PlayerManagementDao {

	private EntityManager entityManager;
	
	public PlayerManagementDaoImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public boolean savePlayer(Player entity) {
		try {
			this.entityManager.persist(entity);
			return true;
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new PlayerManagementServiceException(exp);
		}
		
	}

	@Override
	public Player getPlayer(int id)  {
		try {
			Player player = entityManager.find(Player.class, id);
			return player;
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new PlayerManagementServiceException(exp);
		}
	}

	@Override
	public boolean deletePlayer(Player player) {
		try {
			entityManager.remove(player);
			return true;
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new PlayerManagementServiceException(exp);
		}
	}

	@Override
	public List<Player> getAllPlayers() {
		try {
			TypedQuery<Player> query = entityManager.createNamedQuery("Player.getAllPlayers", Player.class);
			return query.getResultList(); 
		}	catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new PlayerManagementServiceException(exp);
		}
	}

	@Override
	public Player getPlayerByName(String name) {
		try {
			TypedQuery<Player> query = entityManager.createNamedQuery("Player.getPlayerByName", Player.class);
			return query.setParameter("name", name).getSingleResult(); 
		}	catch (NoResultException exp) {
			return null;
		}	catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new PlayerManagementServiceException(exp);
		}
	}

	@Override
	public boolean updatePlayer(Player player)  {
		try {
			this.entityManager.merge(player);
			this.entityManager.flush();
			return true;
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new PlayerManagementServiceException(exp);
		}
	}
	
	@Override
	public void begin() {
		try {
			if (!this.entityManager.getTransaction().isActive())
				this.entityManager.getTransaction().begin();
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new PlayerManagementServiceException(exp);
		}
	}
	
	@Override
	public void commit() {
		try {
			this.entityManager.getTransaction().commit();
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new PlayerManagementServiceException(exp);
		}
	}
	
	@Override
	public void close() {
		try {
			if(this.entityManager.isOpen()) {
				this.entityManager.close();
			};
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			throw new PlayerManagementServiceException(exp);
		}
	}
}
