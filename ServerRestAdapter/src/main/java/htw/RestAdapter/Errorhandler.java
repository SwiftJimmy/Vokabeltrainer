package htw.RestAdapter;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import htw.GameManagmentInter.GameManagementServiceException;
import htw.GameManagmentInter.GameNotCreatableException;
import htw.GameManagmentInter.GameNotFoundException;
import htw.GameManagmentInter.OnePlayerTwoTimesInGameException;
import htw.PlayerManagementInter.DuplicatePlayerException;
import htw.PlayerManagementInter.PlayerManagementServiceException;
import htw.PlayerManagementInter.PlayerNotFoundException;
import htw.VocabularyManagmentInter.DouplicateVocabularyListException;
import htw.VocabularyManagmentInter.NoVocabularyListFoundException;
import htw.VocabularyManagmentInter.NoWordFoundException;
import htw.VocabularyManagmentInter.VocabManagementServiceException;

@ControllerAdvice
public class Errorhandler {
	
	    @ExceptionHandler({	PlayerNotFoundException.class, NoVocabularyListFoundException.class,
	    					NoVocabularyListFoundException.class, NoWordFoundException.class, 
	    					GameNotFoundException.class})
	    public void handleNotFound(HttpServletResponse response, Exception e) throws IOException {
	 		response.sendError(HttpStatus.NOT_FOUND.value(),e.getMessage());
	 	}
	    
	    @ExceptionHandler({	GameNotCreatableException.class})
		public void handleNotCreatable(HttpServletResponse response, GameNotCreatableException e) throws IOException {
	    	response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(),e.getMessage());
		}
	    
	    @ExceptionHandler({DuplicatePlayerException.class, DouplicateVocabularyListException.class,
	    					OnePlayerTwoTimesInGameException.class})
	    public void handleConflict(HttpServletResponse response,Exception e) throws IOException {
	    	response.sendError(HttpStatus.CONFLICT.value(),e.getMessage());
	    }
	    
	    @ExceptionHandler({ GameManagementServiceException.class, PlayerManagementServiceException.class,
	    					VocabManagementServiceException.class})
	    public void handle(HttpServletResponse response, RuntimeException  e) throws IOException {
	    	
	    	if(e instanceof GameManagementServiceException )
	    		response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ((GameManagementServiceException) e).getErrorMessage());
	    	
	    	if(e instanceof PlayerManagementServiceException )
	    		response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ((PlayerManagementServiceException) e).getErrorMessage());
	    	
	    	if(e instanceof VocabManagementServiceException )
	    		response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ((VocabManagementServiceException) e).getErrorMessage());
	    }
	    
	    @ExceptionHandler({ IllegalArgumentException.class})
	    public void handle(HttpServletResponse response, IllegalArgumentException  e) throws IOException {
	    	response.sendError(HttpStatus.BAD_REQUEST.value(),"Die Anfrage kann aufgrund einer invaliden Syntax nicht verarbeitet werden.");
	    }
}
