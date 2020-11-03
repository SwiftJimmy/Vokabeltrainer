package htw.PlayerManagementInter;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.StaleStateException;

public class PlayerManagementServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public PlayerManagementServiceException(RuntimeException ex) {
		String reloadMessage = " Aktualisieren Sie Ihre Spieoberfläche und wiederholen Sie den Vorgang. "
							 + "Wenden Sie sich an den Spiele Suppport falls der Fehler weiterhin auftritt.";
		if(ex instanceof NoResultException) {
			this.errorMessage = "Der Spieler ist in der Datenbank nicht mehr vorhanden." + reloadMessage;
		} else if (ex instanceof NonUniqueResultException) {
			this.errorMessage = "Der Spieler ist in der Datenbank mehrfach vorhanden." + reloadMessage;
		} else if (ex instanceof EntityExistsException) {
			this.errorMessage = "Der Spieler wurde in der Datenbank bereits angelegt." + reloadMessage;
		} else if (ex instanceof IllegalArgumentException) {
			this.errorMessage = "Es ist ein Fehler beim Speichern/Löschen des Spielers aufgetreten." + reloadMessage;
		} else if (ex instanceof QueryTimeoutException) {
			this.errorMessage = "Der Spieler konnte aufgrund eines Timeout Fehlers nicht gespeichert werden." + reloadMessage;
		} else if (ex instanceof IllegalStateException) {
			this.errorMessage = "Die Transaktion konnte in der Datenbank nicht beendet werden. Dadurch wurde der Spieler nicht gespeichert." + reloadMessage;
		} else if (ex instanceof RollbackException || ex instanceof  StaleStateException || ex instanceof OptimisticLockException ) { 
			this.errorMessage = "Die Speicherung/Löschung des Spielers wurde revidiert." + reloadMessage;
		} else {
			this.errorMessage = "Der Spieler konnte nicht gespeichert werden." + reloadMessage;
		}
	}
	
	public PlayerManagementServiceException(String message) {
		super(message);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
