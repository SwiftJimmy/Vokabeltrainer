package model;

import java.io.IOException;
import java.net.ConnectException;

public class GuiIOException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public GuiIOException(IOException e) {
		if(e instanceof ConnectException){
			this.errorMessage = "Es konnte keine Verbindung zum Server aufgebaut werden";
		} else {
			this.errorMessage = "Es ist ein Fehler aufgetreten. Starten Sie das Programm ggf neu.";
		}
	}

	public String getMessage() {
		return errorMessage;
	}
}
