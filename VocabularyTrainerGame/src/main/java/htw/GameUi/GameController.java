package htw.GameUi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import htw.GameManagmentInter.Answer;
import htw.GameManagmentInter.Game;
import htw.GameManagmentInter.Round;
import htw.PlayerManagementInter.Player;
import htw.PlayerManagementInter.ScoreResult;
import htw.VocabularyManagmentInter.Word;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ResultTableEntry;
import model.UpdateGameWorker;
import model.DeleteGameWorker;
import model.GetAllGameWorker;
import model.GetAllPlayerWorker;

public class GameController {
	
	@FXML
    private TableColumn<ResultTableEntry, String> answerColumnPlayer2;

    @FXML
    private TableColumn<ResultTableEntry, String> answerColumnPlayer1;
    
    @FXML
    private TableColumn<ResultTableEntry, String> resultColumnPlayer2;
    
    @FXML
    private TableColumn<ResultTableEntry, String> resultColumnPlayer1;

    @FXML
    private TableColumn<ResultTableEntry, String> questColumn;
      
	@FXML
    private TableColumn<ResultTableEntry, String> player1Column;
	
	@FXML
    private TableColumn<ResultTableEntry, String> player2Column;
	
	@FXML
    private TableColumn<ResultTableEntry, String> roundColumn;
	
	@FXML
    private TableView<ResultTableEntry> resultTable;
	
	@FXML
	private Label roundNumber;
	
    @FXML
    private Pane gamePane;
    
    @FXML
    private Pane roundOtherPlayer_Pane;
    
    @FXML
    private Pane questionPane;

    @FXML
    private Label roundOtherPlayer_Label;

    @FXML
    private Button btn_Answer2;

    @FXML
    private Button btn_Answer1;

    @FXML
    private Button btn_DeleteGame;

    @FXML
    private Button btn_Answer4;

    @FXML
    private ComboBox<Player> cb_ChoosePlayer;

    @FXML
    private Button btn_Answer3;

    @FXML
    private Label question_Label;
    
    @FXML
    private Button btn_AddGame;

    @FXML
    private ListView<Game> list_ActiveGame;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Label informationLabel;
        
    public void initialize() throws IOException {
    	setSpinningUpdateButton(true,"Alle Spieler und Spiele werden geladen ... ");
    	setCellStyle();
    	updateView();
    	resultColumnPlayer1.setCellValueFactory(new PropertyValueFactory<>("player1Result"));
    	resultColumnPlayer2.setCellValueFactory(new PropertyValueFactory<>("player2Result"));
    	questColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
    	answerColumnPlayer1.setCellValueFactory(new PropertyValueFactory<>("ansewrPlayer1"));
    	answerColumnPlayer2.setCellValueFactory(new PropertyValueFactory<>("ansewrPlayer2"));
    	roundColumn.setCellValueFactory(new PropertyValueFactory<>("round"));
    }
    
    @FXML
    void updateGameView(ActionEvent event) {
    	setSpinningUpdateButton(true, "Spiele werden aktualisiert ... ");
    	updateView();
    }
    
    @FXML
    void onSelectPlayer(ActionEvent event) {
    	setSpinningUpdateButton(true, "Die Spiele des Spielers werden geladen ... ");
    	roundOtherPlayer_Label.setText("Erstelle oder wähle ein Spiel.");
    	roundOtherPlayer_Pane.setVisible(true);
        questionPane.setVisible(false);
        resultTable.setVisible(false);
    	list_ActiveGame.getItems().clear();
    	Player player = cb_ChoosePlayer.getValue();
    	GetAllGameWorker worker = new GetAllGameWorker(player);
    	setOnWorkerDoneOnPlayerSelected(worker);
		Thread workerThread = new Thread(worker);
		workerThread.start();
    }
  
    
    @FXML
    void onClick_AddGame(ActionEvent event) {
    	
    	if(mainPlayerIsNotSelected()) {
    		new Alert(AlertType.INFORMATION, "Wählen Sie erst einen Spieler aus." ,ButtonType.OK).show();
    		return;
    	}
    		
    	try {
    	FXMLLoader loader = new FXMLLoader();
    	
    	Stage dialogStage = loadNewGameDialog(loader);
    	NewGameDialogController controller = loader.getController();
  
	    controller.setMainPlayer(cb_ChoosePlayer.getValue());
	    controller.setGc(this);
	    controller.setDialogStage(dialogStage);

	    dialogStage.showAndWait();
    	} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    @FXML
    void onClick_DeleteGame(ActionEvent event)  {
    	
    	Game game = this.list_ActiveGame.getSelectionModel().getSelectedItem();
    	if(game != null) {
    		setSpinningUpdateButton(true, "Das Spiel wird gelöscht ... ");
    		DeleteGameWorker worker = new DeleteGameWorker(game);
    		setOnGameDeleteWorkerDone(worker);
    		Thread workerThread = new Thread(worker);
    		workerThread.start();

    	} else {
    		new Alert(AlertType.INFORMATION, "Erstelle oder wähle ein Spiel." ,ButtonType.OK).show();
    	}
    }
    
    @FXML
    void onClick_Answer(ActionEvent event) {
    	Button clickedButton = (Button) event.getSource();
    	Game game = this.list_ActiveGame.getSelectionModel().getSelectedItem();
    	Round currentRound = findActiveRound(game);
    	
    	updateAnswer(currentRound,clickedButton);
    	updateGame(game,currentRound);
    	updatePlayer(game);
    	setSpinningUpdateButton(true, "Die Antwort wird überprüft ... ");
    	answerButtonsSelectable(false);
    	UpdateGameWorker worker = new UpdateGameWorker(game);
    	setOnUpdateGameWorkerDone(worker);
		Thread workerThread = new Thread(worker);
		workerThread.start(); 
    }
    
    @FXML
    void gameSelected(MouseEvent event) {
    	Game game = this.list_ActiveGame.getSelectionModel().getSelectedItem();
    	
    	if(game == null) {
 
    		this.questionPane.setVisible(false);
    		return;
    	} 
    	
    	Round currentRound = findActiveRound(game);
 	
    	if(currentRound != null) {
    		setUpGameQA(currentRound);
    	} else {
    		setResultsView(game);
    	}
    }
    	
    private void setOnGameDeleteWorkerDone(DeleteGameWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        boolean result = worker.getResult();
		        if(result) {
	    			list_ActiveGame.getItems().remove(worker.getGame());
	    			if(list_ActiveGame.getItems().isEmpty()) {
	    				roundOtherPlayer_Label.setText("Erstelle oder wähle ein Spiel.");
	    		    	roundOtherPlayer_Pane.setVisible(true);
	    		        questionPane.setVisible(false);
	    		        resultTable.setVisible(false);
	    			} else {
	    				gameSelected(null);
	    			}
	    			
		        } else if (worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        }
		     	
		        setSpinningUpdateButton(false,null);
		    }
		});
    }

    
    
    private void setOnUpdateGameWorkerDone(UpdateGameWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
			    boolean result = worker.getResult();
			    	
			    if(result )	{
			    	Player selectedPlayer = cb_ChoosePlayer.getValue();
			    	
			    	GetAllGameWorker worker = new GetAllGameWorker(selectedPlayer);
					setOnWorkerDone(worker);
					Thread workerThread = new Thread(worker);
					workerThread.start();   
				} else if (worker.getErrorMessage() != null) {
					setSpinningUpdateButton(false,null);
					answerButtonsSelectable(true);
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        } 
		    }
		});
    }
    
    private void updatePlayer(Game game) {
    	if(allRoundsFinished(game)) {
    		if(game.getPointPlayer1() == game.getPointPlayer2()) {
    			game.getPlayer1().getScore().addDrawn();
    			game.getPlayer2().getScore().addDrawn();
    		} else if(game.getPointPlayer1() < game.getPointPlayer2()) {
    			game.getPlayer1().getScore().addLost();;
    			game.getPlayer2().getScore().addWon();
    		} else {
    			game.getPlayer1().getScore().addWon();;
    			game.getPlayer2().getScore().addLost();
    		}        	
    	}
    }
    
    private void setUpGameQA(Round round) {
    	roundOtherPlayer_Pane.setVisible(false);
    	questionPane.setVisible(true);
    	List<Word> possibleAnswers = new ArrayList<Word>(round.getQuizQuestion().getWrongAnswers());
    	possibleAnswers.add(round.getQuizQuestion().getAnswerWord());
    	Collections.shuffle(possibleAnswers);
    	
    	roundNumber.setText(String.valueOf(round.getNumber()));
    	btn_Answer1.setText(possibleAnswers.get(0).getWord());
    	btn_Answer2.setText(possibleAnswers.get(1).getWord());
    	btn_Answer3.setText(possibleAnswers.get(2).getWord());
    	btn_Answer4.setText(possibleAnswers.get(3).getWord());
    	question_Label.setText(round.getQuizQuestion().getQuestionWord().getWord());
    	
    }
    
    private void setResultsView(Game game) {
    	roundOtherPlayer_Pane.setVisible(true);
    	questionPane.setVisible(false);
    	resultTable.getItems().clear();
    	String nameotherPlayer1 = game.getRounds().get(0).getAnswerPlayer1().getPlayer().getName();
    	String nameotherPlayer2 = game.getRounds().get(0).getAnswerPlayer2().getPlayer().getName();
    	
    	player1Column.setText(nameotherPlayer1);
    	player2Column.setText(nameotherPlayer2);
    	
    	if(game.isStatus()) {
    		resultTable.setVisible(false);
    		roundOtherPlayer_Label.setText("Auf anderen Spieler warten");
    	} else {
    		setResults(game);
    		resultTable.setVisible(true);
    		
    		if(game.getPointPlayer1() == game.getPointPlayer2()) {
    			roundOtherPlayer_Label.setText("Gleichstand!");
    		} else if(game.getPointPlayer1() < game.getPointPlayer2()) {
    			roundOtherPlayer_Label.setText(nameotherPlayer2 + " hat gewonnen!");
    		} else {
    			roundOtherPlayer_Label.setText(nameotherPlayer1 + " hat gewonnen!");
    		}
    	}
    }
    
    private void setResults(Game game) {
    	
    	for (Round round : game.getRounds()) {
    		String roundNumber = String.valueOf(round.getNumber());
    		String player1Result = round.getAnswerPlayer1().isCorrectAnswer()? "Richtig":"Falsch";
    		String player2Result = round.getAnswerPlayer2().isCorrectAnswer()? "Richtig":"Falsch";
    		String question = round.getQuizQuestion().getQuestionWord().getWord();;
    		String ansewrPlayer1 = round.getAnswerPlayer1().getAnswer();;
    		String ansewrPlayer2 = round.getAnswerPlayer2().getAnswer();
    		    		
    		ResultTableEntry entry = new ResultTableEntry(roundNumber,question,ansewrPlayer1, player1Result,ansewrPlayer2, player2Result);
    		resultTable.getItems().add(entry);
    	}
    }
        
    private Round findActiveRound(Game game) {
    	Player player = cb_ChoosePlayer.getValue();
    	
    	if(game.getPlayer1().getId() == player.getId()) {
	    	for(Round round : game.getRounds()) {
	    		if(round.isStatus() && hasUnansweredQuestion(round,player)) {
	    			return round;	
	    		}
	    	}
    	} else {
    		boolean result = true;
    		for(Round round : game.getRounds()) {
	    		if(round.isStatus() && hasUnansweredQuestion(round,game.getPlayer1()) ) {
	    			result = false;
	    			break;
	    		}
	    	}
    		if(result) {
    			for(Round round : game.getRounds()) {
    	    		if(round.isStatus() && hasUnansweredQuestion(round,player) ) {
    	    			return round;
    	    		}
    	    	}
    		}
    	}
    	return null;
    }
    
    private boolean hasUnansweredQuestion(Round round, Player currentPlayer) {

    	return (
    			// ich bin player 1 und meine Frage wurde noch nicht beantwortet
    			(round.getAnswerPlayer1().getPlayer().getId() == currentPlayer.getId()
    			&& round.getAnswerPlayer1().getAnswer() == null)
    			
    			|| 
    			// ich bin player 2 und meine Frage wurde noch nicht beantwortet
    			(round.getAnswerPlayer2().getPlayer().getId() == currentPlayer.getId()
    	    			&& round.getAnswerPlayer2().getAnswer() == null)
    			);
    }
    
    private Answer getPlayersAnswerObject(Round round) {
    	Player currentPlayer = cb_ChoosePlayer.getValue();
    	if(round.getAnswerPlayer1().getPlayer().getId() == currentPlayer.getId()) {
    		return  round.getAnswerPlayer1();
    	} else {
    		return  round.getAnswerPlayer2();
    	}
    }
    
    
    private boolean allRoundsFinished(Game game) {
    	for(Round round : game.getRounds()) {
    		if(round.isStatus()) {
    			return false;	
    		}
    	}
    	return true;
    }
    
    private void updateGame(Game game , Round round) {
    	
    	if(roundIsNotFinished(round)) 
    		return;
    	
    	String correctAnswer = round.getQuizQuestion().getAnswerWord().getWord();	
    	String answerPl1 = round.getAnswerPlayer1().getAnswer();
    	String answerPl2 = round.getAnswerPlayer2().getAnswer();
   		int pointPl1 = answerPl1.equals(correctAnswer) ? 1:0;
   		int pointPl2 = answerPl2.equals(correctAnswer) ? 1:0;
    		
   		if(pointPl1 == pointPl2) {
   			round.getAnswerPlayer1().setResultat(ScoreResult.DRAWN);
    		round.getAnswerPlayer2().setResultat(ScoreResult.DRAWN);
    	}else if (pointPl1 > pointPl2) {
    		round.getAnswerPlayer1().setResultat(ScoreResult.WON);
    		round.getAnswerPlayer2().setResultat(ScoreResult.LOST);
    	} else {
    		round.getAnswerPlayer1().setResultat(ScoreResult.LOST);
    		round.getAnswerPlayer2().setResultat(ScoreResult.WON);
   		}   		
    	round.setStatus(false);
    		
    	game.setPointPlayer1(game.getPointPlayer1() + pointPl1);
    	game.setPointPlayer2(game.getPointPlayer2() + pointPl2);
    		
   		if(allRoundsFinished(game))
   			game.setStatus(false);
    	
    }
    
    private boolean roundIsNotFinished(Round round) {
    	return (round.getAnswerPlayer1().getAnswer() == null || round.getAnswerPlayer2().getAnswer() == null);
    }
    
  
    private void updateAnswer(Round currentRound, Button clickedButton) {
    	
    	Answer answer = getPlayersAnswerObject(currentRound);
    	String choosenAnswer = clickedButton.getText();
    	String correctAnswer = currentRound.getQuizQuestion().getAnswerWord().getWord();
    	answer.setAnswer(choosenAnswer);
    	
    	if(correctAnswer.equals(choosenAnswer)) {
    		answer.setCorrectAnswer(true);
    	}
    }
       
    private void setCellStyle() {
    	this.list_ActiveGame.setCellFactory(lv -> new ListCell<Game>() {
    	    @Override
    	    protected void updateItem(Game game, boolean empty) {
    	    	if(cb_ChoosePlayer.getValue() == null)
    	    		return;
    	    	
    	        super.updateItem(game, empty);
    	        if (empty) {
    	            setText(null);
    	            setStyle("");
    	        } else {
    	        	
    	        	if (cb_ChoosePlayer.getValue().getId() == game.getPlayer1().getId()) {
    	        		setText("Spiel- vs " + game.getPlayer2().getName()+ " " + game.getGameID());
    	        	} else {
    	        		setText("Spiel- vs " + game.getPlayer1().getName()+ " " + game.getGameID());
    	        	}
    	        	setColor(game, this);
    	        }
    	    }
    	});
    }
    
    private void setColor(Game game, ListCell<Game> listCell ) {
    	if (game.isStatus()) { 
        	if (findActiveRound(game) == null) {
        		listCell.setStyle("-fx-background-color: #fbffc7");
        	}else {
        		listCell.setStyle("-fx-background-color: #E8F6F3");
        	}
        } else {
        	listCell.setStyle("-fx-background-color: #FDEDEC");
        }
    }

    public void setSpinningUpdateButton(boolean spinning, String informationText) {
    	String spinnerImage;
    			
    	if (spinning) {
    		spinnerImage = getClass().getResource("/spinning.gif").toExternalForm();
    		updateButton.setDisable(true);
    		informationLabel.setText(informationText);
    		informationLabel.setVisible(true);
    		
    	}else {
    		spinnerImage = getClass().getResource("/update.png").toExternalForm();
    		updateButton.setDisable(false);
    		informationLabel.setVisible(false);
    	}
        
    	Image img = new Image(spinnerImage);
        ImageView view = new ImageView(img);
        view.setFitHeight(30); 
        view.setFitWidth(30); 
        updateButton.setPrefSize(30, 30);
        updateButton.setGraphic(view);
    }
         
    private void updateView() {
    	GetAllPlayerWorker worker = new GetAllPlayerWorker();
		setOnPlayerWorkerDone(worker);
		Thread workerThread = new Thread(worker);
		workerThread.start();
    }
    
    private void  setOnPlayerWorkerDone(GetAllPlayerWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        List<Player> allPlayer = worker.getPlayers();
		        
		        if(worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        } else if(allPlayer != null) {
					Player selectedPlayer = cb_ChoosePlayer.getValue();
					cb_ChoosePlayer.getItems().clear();
					cb_ChoosePlayer.getItems().addAll(allPlayer);
					if (allPlayer != null) {
						for(Player player : allPlayer) {
							if(!cb_ChoosePlayer.getItems().contains(player)) {
								cb_ChoosePlayer.getItems().add(player);
							} 
						}
					}
					
					for(int i = 0; i < cb_ChoosePlayer.getItems().size(); i++) {
						Player player = cb_ChoosePlayer.getItems().get(i);
						if(!allPlayer.contains(player)) {
							cb_ChoosePlayer.getItems().remove(i);
						}
					}
			
					if( selectedPlayer != null && dropdownPlayerContainsPlayerCheck(selectedPlayer)) {
						cb_ChoosePlayer.getSelectionModel().select(selectedPlayer);
						GetAllGameWorker worker = new GetAllGameWorker(selectedPlayer);
			    		setOnWorkerDone(worker);
			    		Thread workerThread = new Thread(worker);
			    		workerThread.start();
					} else {
						cb_ChoosePlayer.valueProperty().set(null);
						cb_ChoosePlayer.getSelectionModel().select(null);
						cb_ChoosePlayer.setValue(null);
						
					}
		        } 	
		        setSpinningUpdateButton(false,null);
		    }
		});
    }
    
    private boolean dropdownPlayerContainsPlayerCheck(Player selectedPlayer) {
    	for (Player player : cb_ChoosePlayer.getItems()) {
    		if(player.getId() == selectedPlayer.getId())
    			return true;
    	}
    	return false;
    }
    
    private void setOnWorkerDone(GetAllGameWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        List<Game> allGames = worker.getGames();
		        if(allGames != null) {
		        	updateAllGamesToView(allGames);
		        } else if (worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        }
		        gameSelected(null);
		        answerButtonsSelectable(true);
		        setSpinningUpdateButton(false,null);
		    }
		});
    }
    
    private void setOnWorkerDoneOnPlayerSelected(GetAllGameWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        List<Game> allGames = worker.getGames();
		        if(allGames != null) {
		        	updateAllGamesToView(allGames);
		        } else if (worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        }
		     	
		        list_ActiveGame.getSelectionModel().clearSelection();
		        roundOtherPlayer_Label.setText("Erstelle oder wähle ein Spiel.");
		        setSpinningUpdateButton(false,null);
		    }
		});
    }
    
    private boolean mainPlayerIsNotSelected() {
    	return cb_ChoosePlayer.getSelectionModel().isEmpty();
    }

	private Stage loadNewGameDialog(FXMLLoader loader) throws IOException {	
	    loader.setLocation(getClass().getResource("/NewGameDialog.fxml"));
	    
	    AnchorPane dialogPane = (AnchorPane) loader.load();    
	    Stage dialogStage = new Stage();
	    dialogStage.initModality(Modality.WINDOW_MODAL);
	    dialogStage.initOwner(null);
	    Scene scene = new Scene(dialogPane);
	    dialogStage.setScene(scene);
	    
		return dialogStage;
	}
    
    private void updateAllGamesToView(List<Game> allGames) {
    	int selectedGame = list_ActiveGame.getSelectionModel().getSelectedIndex();
  
    	list_ActiveGame.getItems().clear();
    	list_ActiveGame.getItems().addAll(allGames);
    	list_ActiveGame.getSelectionModel().select(selectedGame);
    	list_ActiveGame.getFocusModel().focus(selectedGame);
    }
    
	private void answerButtonsSelectable(boolean isselectabel) {
		if (isselectabel) {
			btn_Answer1.setDisable(false);
			btn_Answer2.setDisable(false);
			btn_Answer3.setDisable(false);
			btn_Answer4.setDisable(false);
		} else {
			btn_Answer1.setDisable(true);
			btn_Answer2.setDisable(true);
			btn_Answer3.setDisable(true);
			btn_Answer4.setDisable(true);
		}
	}

	public ListView<Game> getList_ActiveGame() {
		return list_ActiveGame;
	}

	public void setList_ActiveGame(ListView<Game> list_ActiveGame) {
		this.list_ActiveGame = list_ActiveGame;
	}
	

    
}

