package htw.GameUi;


import java.io.IOException;
import java.util.List;
import htw.ClientAdapter.PlayerClientAdapter;
import htw.GameManagmentInter.Game;
import htw.PlayerManagementInter.Player;
import htw.VocabularyManagmentInter.VocabularyListDTO;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.CreateGameWorker;
import model.GetAllVocabularyListDTOWorker;

public class NewGameDialogController {

    @FXML
    private ComboBox<Player> player2Dropdown;

    @FXML
    private Button cancelGameButton;

    @FXML
    private ComboBox<VocabularyListDTO> vocabListDropdown;

    @FXML
    private ComboBox<String> languageToDropdown;

    @FXML
    private ComboBox<String> languageFromDropdown;

    @FXML
    private Button startGameButton;
    
    @FXML
    private Button updateButton;
    
    private GameController gc;
    private PlayerClientAdapter pca;
    private Stage dialogStage;
    private Player mainPlayer;
    private Game game;
    private boolean gameCreatet;
    
    public void initialize() throws IOException {
    	pca = new PlayerClientAdapter();
    	fillPlayerComboBox();
    	fillVocabComboBox();
    }
    
    @FXML
    void languageFromSelected(ActionEvent event) {
    	removeSelectedLanguage(languageFromDropdown,languageToDropdown);
    }
      
    @FXML
    void vocabularyListSelected(ActionEvent event) {
    	fillLanguageComboBox(languageFromDropdown);
    	fillLanguageComboBox(languageToDropdown);
    }

    @FXML
    void startGameButton_OnClick(ActionEvent event) {
    	if(playerIsNotSelected()) {
    		new Alert(AlertType.INFORMATION, "Wählen Sie einen Gegenspieler aus." ,ButtonType.OK).show();
    	}else if(languagesAreNotSelected()) {
    		new Alert(AlertType.INFORMATION, "Wählen Sie die Sprachen aus" ,ButtonType.OK).show();
    	} else {
    		gc.setSpinningUpdateButton(true, "Das Spiel wird erstellt ... ");
    		Player player2 = player2Dropdown.getValue();
    		String from = languageFromDropdown.getValue();
    		String to = languageToDropdown.getValue();
    		VocabularyListDTO vocabularyList = vocabListDropdown.getValue();
    		CreateGameWorker worker = new CreateGameWorker(mainPlayer, player2, from, to, vocabularyList.getId());
    		setOnWorkerDone(worker);
    		Thread workerThread = new Thread(worker);
    		workerThread.start();
    		this.dialogStage.close();
    	}
    }
    
    @FXML
    void cancelGameButton_OnClick(ActionEvent event) {
    	gameCreatet = false;
    	this.dialogStage.close();
    }
    
    private boolean languagesAreNotSelected() {
    	return languageFromDropdown.getSelectionModel().isEmpty() || languageToDropdown.getSelectionModel().isEmpty();
    }
    
    private boolean playerIsNotSelected() {
    	return player2Dropdown.getSelectionModel().isEmpty();
    }
    
    private void setOnWorkerDone(CreateGameWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		        Game game = worker.getGame();
		        
		        if(game == null || worker.getErrorMessage() != null ) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        } else{
		        	gc.getList_ActiveGame().getItems().add(0, game);
		        	new Alert(AlertType.INFORMATION, "Spiel wurde erstellt" ,ButtonType.OK).show();
		        }
		        gc.setSpinningUpdateButton(false, null);
		        
		    }
		});
    }

  


	public void setMainPlayer(Player mainPlayer) {
		this.mainPlayer = mainPlayer;
		player2Dropdown.getItems().remove(mainPlayer);
		if(!player2Dropdown.getItems().isEmpty()) 
    		player2Dropdown.getSelectionModel().select(0);
	}
	
	
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	


	public boolean isGameCreatet() {
		return gameCreatet;
	}



	public Game getGame() {
		return game;
	}



	private void fillPlayerComboBox() throws IOException {
			try {
				
				List<Player> allPlayer = pca.getAllPlayer();
				player2Dropdown.getItems().addAll(allPlayer);
				
			} catch (RuntimeException e) {
				new Alert(AlertType.ERROR, e.getMessage() ,ButtonType.OK).show();
			} 
	    	
	}
	    
	private void fillVocabComboBox() {
		setSpinningUpdateButton(true);
		GetAllVocabularyListDTOWorker worker = new GetAllVocabularyListDTOWorker();
		setOnVocabularysLoadedWorkerDone(worker);
		Thread workerThread = new Thread(worker);
		workerThread.start(); 		
	}
	
	private void setOnVocabularysLoadedWorkerDone(GetAllVocabularyListDTOWorker worker) {
		worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        List<VocabularyListDTO> list = worker.getVocabularyLists();
		        if(list != null) {
		        	vocabListDropdown.getItems().addAll(list);
		        	if(!list.isEmpty()) 
		        		vocabListDropdown.getSelectionModel().select(0);
		        	fillLanguageComboBox(languageFromDropdown);
		        	fillLanguageComboBox(languageToDropdown);
		        }  else if (worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        	
		        } 
		    setSpinningUpdateButton(false);
		    }
		});
	}
	    
	private void fillLanguageComboBox(ComboBox<String> languageCombobox) {
	    	if (this.vocabListDropdown.getItems().isEmpty())
	    		return;
	    	
	    	VocabularyListDTO vocabularyList = vocabListDropdown.getSelectionModel().getSelectedItem();
	    	
	    	List<String> languages = vocabularyList.getLanguages();
	    	languageCombobox.getItems().clear();
	    	languageCombobox.getItems().addAll(languages);
	}
	
	private void removeSelectedLanguage(ComboBox<String> selectedCombobox,ComboBox<String> otherCombobox) {
    	String selectedLanguage = selectedCombobox.getSelectionModel().getSelectedItem();
    	fillLanguageComboBox(otherCombobox);
    	otherCombobox.getItems().remove(selectedLanguage);
    }
	

	public GameController getGc() {
		return gc;
	}



	public void setGc(GameController gc) {
		this.gc = gc;
	}
	
	
	private void setSpinningUpdateButton(boolean spinning) {
	    String spinnerImage;
	    			
	    if (spinning) {
	    	spinnerImage = getClass().getResource("/spinning.gif").toExternalForm();
	    }else {
	    	spinnerImage = getClass().getResource("/update.png").toExternalForm();
	    }

	    	Image img = new Image(spinnerImage);
	     	ImageView view = new ImageView(img);
	     	view.setFitHeight(30); 
	     	view.setFitWidth(30); 
	     	updateButton.setPrefSize(30, 30);
	     	updateButton.setGraphic(view);
	        
	   }
    
}