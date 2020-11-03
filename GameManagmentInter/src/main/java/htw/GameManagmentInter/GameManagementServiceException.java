package htw.GameManagmentInter;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

import org.hibernate.QueryTimeoutException;
import org.hibernate.StaleStateException;

public class GameManagementServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public GameManagementServiceException(RuntimeException ex) {
		String reloadMessage = " Aktualisieren Sie Ihre Spieloberfläche und wiederholen Sie den Vorgang. "
							 + "Wenden Sie sich an den Spiele Suppport falls der Fehler weiterhin auftritt.";
		if(ex instanceof NoResultException) {
			this.errorMessage = "Das Spiel ist in der Datenbank nicht mehr vorhanden." + reloadMessage;
		} else if (ex instanceof NonUniqueResultException) {
			this.errorMessage = "Das Spiel ist in der Datenbank mehrfach vorhanden." + reloadMessage;
		} else if (ex instanceof EntityExistsException) {
			this.errorMessage = "Das Spiel ist in der Datenbank bereits angelegt." + reloadMessage;
		} else if (ex instanceof IllegalArgumentException) {
			this.errorMessage = "Es ist ein Fehler beim Speichern/Löschen/Aufrufen des Spieles aufgetreten." + reloadMessage;
		} else if (ex instanceof QueryTimeoutException) {
			this.errorMessage = "Das Spiel konnte aufgrund eines Timeout Fehlers nicht gespeichert/aufgerufen werden." + reloadMessage;
		} else if (ex instanceof IllegalStateException) {
			this.errorMessage = "Die Transaktion konnte in der Datenbank nicht beendet werden. Dadurch wurde das Spiel nicht gespeichert/aufgerufen." + reloadMessage;
		} else if (ex instanceof RollbackException || ex instanceof  StaleStateException || ex instanceof OptimisticLockException ) { 
			this.errorMessage = "Die Speicherung/Löschung des Spiels wurde revidiert." + reloadMessage;
		} else {
			this.errorMessage = "Das Spiel konnte nicht gespeichert/aufgerufen werden." + reloadMessage;
		}
	}
	
	public GameManagementServiceException(String message) {
		super(message);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
