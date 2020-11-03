package htw.GameUi;


import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import start.App;

public class MainController {

    @FXML
    private Tab tab_Game;

    @FXML
    private Pane gamePane;

    @FXML
    private Pane playerPane;

    @FXML
    private Tab tab_Player;

    @FXML
    private Tab tab_Vocabulary;
    
    @FXML
    private Pane vocabPane;

    @FXML
    void onPlayerSelect(Event event) throws IOException  {
    	if(tab_Player.isSelected()){
    		this.playerPane = FXMLLoader.load(App.class.getResource("/PlayerPane.fxml"));
        	tab_Player.setContent(playerPane);
    	}
    }

    @FXML
    void gameSelected(Event event) throws IOException {
    	if(tab_Game.isSelected()){
	    	this.gamePane = FXMLLoader.load(App.class.getResource("/GamePane.fxml"));
	    	tab_Game.setContent(gamePane);
    	}
    }
    
    @FXML
    void onVocabSelected(Event event) throws IOException {
    	if(tab_Vocabulary.isSelected()){
	    	this.vocabPane = FXMLLoader.load(App.class.getResource("/VocabPane.fxml"));
	    	tab_Vocabulary.setContent(vocabPane);
    	}
    }

}
