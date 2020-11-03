package htw.RestAdapter;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerService {
	
	private EntityManagerFactory emf;
	private static EntityManagerService entityManager = null;
	
	private EntityManagerService() 
    { 
		this.emf = Persistence.createEntityManagerFactory("managerMysql");		
    } 
	
	public static EntityManagerService getInstance() 
    { 
        if (entityManager == null) 
        	entityManager = new EntityManagerService(); 

        return entityManager; 
    }
		
	public EntityManagerFactory getEntityManagerFactory() {
		return this.emf;
	}
}
