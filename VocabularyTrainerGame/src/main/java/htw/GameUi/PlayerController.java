package htw.GameUi;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import htw.ClientAdapter.PlayerClientAdapter;
import htw.PlayerManagementInter.DuplicatePlayerException;
import htw.PlayerManagementInter.Player;
import htw.PlayerManagementInter.PlayerNotFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.CreatePlayerWorker;
import model.DeletePlayerGamesWorker;
import model.DeletePlayerWorker;
import model.GetAllPlayerWorker;


public class PlayerController {
	
	private PlayerClientAdapter pca;
	
    @FXML
    private NumberAxis bar_yAxis;

    @FXML
    private Pane playerPane;

    @FXML
    private ListView<Player> listView_AllPlayer;

    @FXML
    private BarChart<String, Number> bar_PlayerStat;

    @FXML
    private Label label_PlayerName;

    @FXML
    private Button btn_DeletePlayer;

    @FXML
    private Button btn_AddPlayer;

    @FXML
    private Button btn_EditPlayer;

    @FXML
    private CategoryAxis bar_xAxis;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Label informationLabel;
    
    public void initialize() {
    	
	    pca = new PlayerClientAdapter();
	    setSpinningUpdateButton(true, "Die Spieler werden geladen ... ");
	  
	    GetAllPlayerWorker worker = new GetAllPlayerWorker();
	    setOnGetAllPlayerWorkerDone(worker);
    	Thread workerThread = new Thread(worker);
    	workerThread.start(); 
    }
    
    @FXML
    void updatePlayerView(ActionEvent event) {
    	setSpinningUpdateButton(true, "Die Spieler werden geladen ... ");    	
		GetAllPlayerWorker worker = new GetAllPlayerWorker();
		setOnGetAllPlayerUpdateWorkerDone(worker);
		Thread workerThread = new Thread(worker);
		workerThread.start(); 
    }
    
    @FXML
    void onClick_DeletePlayer(ActionEvent event) {
    	
	    Player player = this.listView_AllPlayer.getSelectionModel().getSelectedItem();
	    if(player != null ) {
	    		
	    	if( !deleteGameAndPlayerDialog())
	    		return;
	    	setSpinningUpdateButton(true, "Der Spieler wird gelöscht ...");	
	    	deleteAllGamesOfPlayer(player);
	
	    } else {
	    	new Alert(AlertType.INFORMATION, "Wählen Sie einen Spieler aus!" ,ButtonType.OK).show();
	    }
    	
    }
    
    @FXML
    void onClick_AddPlayer(ActionEvent event) {
    	TextInputDialog dialog = new TextInputDialog("Max Mustermann");
    	dialog.setTitle("Neuen Spieler anlegen.");
    	dialog.setHeaderText("Geben Sie den Namen des Spielers ein.");
    	dialog.setContentText("Name:");

  
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){
    		setSpinningUpdateButton(true, "Der Spieler wird erstellt ... ");
    		CreatePlayerWorker worker = new CreatePlayerWorker(result.get());
    		setOnPlayerCreatedWorkerDone(worker);
    		Thread workerThread = new Thread(worker);
    		workerThread.start(); 
    	} 
    }
    
    @FXML
    void onClick_EditPlayer(ActionEvent event) {
    	Player player = this.listView_AllPlayer.getSelectionModel().getSelectedItem();    	
    	int index = this.listView_AllPlayer.getSelectionModel().getSelectedIndex();
    	
    	if(player == null) {
    		new Alert(AlertType.INFORMATION, "Wählen Sie einen Spieler aus!" ,ButtonType.OK).show();
    		return;
    	}
    		
    	TextInputDialog dialog = new TextInputDialog(player.getName());
    	dialog.setTitle("Spieler bearbeiten");
    	dialog.setContentText("Ändern Sie den Namen des Spielers:");
    	
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent() && player.getName() != result.get()){
    	    Player playerNew;
			try {
				playerNew = pca.editPlayerName(player, result.get());
				listView_AllPlayer.getItems().remove(player);
		    	listView_AllPlayer.getItems().add(index,playerNew);
		    	listView_AllPlayer.getSelectionModel().select(playerNew);
		    	playerSelected(null);
			} catch (RuntimeException | DuplicatePlayerException | PlayerNotFoundException e) {
				new Alert(AlertType.ERROR, e.getMessage() ,ButtonType.OK).show();
			} 
    	} 
    }
    
    @FXML
    void playerSelected(MouseEvent arg0) {
    	Player player = this.listView_AllPlayer.getSelectionModel().getSelectedItem();
    	if(player == null)
    		return;
    	
    	label_PlayerName.setText(player.getName());
    	bar_PlayerStat.getData().clear();
    	XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
    	
    	XYChart.Data<String, Number> won = new XYChart.Data<String, Number>("Gewonnen", player.getScore().getWon());
    	won.nodeProperty().addListener(new ChangeListener<Node>() {
            @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
              if (node != null) {
                node.setStyle("-fx-bar-fill: blue");
              } 
            }
          });
    	
    	XYChart.Data<String, Number> drawn = new XYChart.Data<String, Number>("Unentschieden", player.getScore().getDrawn());
    	drawn.nodeProperty().addListener(new ChangeListener<Node>() {
            @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
              if (node != null) {
                node.setStyle("-fx-bar-fill: green");
              } 
            }
          });
    	
    	series.getData().add(won);
    	series.getData().add(drawn);
    	series.getData().add(new XYChart.Data<String, Number>("Verloren", player.getScore().getLost()));
    	
    	int max = Math.max(player.getScore().getWon(),player.getScore().getDrawn());
    	max = Math.max(max,player.getScore().getLost());

    	
    	bar_PlayerStat.getData().add(series);
    	((ValueAxis<Number>) bar_PlayerStat.getYAxis()).setAutoRanging(false);
    	((ValueAxis<Number>) bar_PlayerStat.getYAxis()).setLowerBound(0);
    	((ValueAxis<Number>) bar_PlayerStat.getYAxis()).setUpperBound(max);
    	((NumberAxis) bar_PlayerStat.getYAxis()).setTickUnit(1);
    	((NumberAxis) bar_PlayerStat.getYAxis()).setMinorTickVisible(false);
   
    }
        
    private void setOnPlayerCreatedWorkerDone(CreatePlayerWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        Player player = worker.getPlayer();
		        if(player != null) {
		        	listView_AllPlayer.getItems().add(player);
		        	listView_AllPlayer.getSelectionModel().select(player);
		        	playerSelected(null);
		        }  else if (worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        	
		        }  
		        setSpinningUpdateButton(false, null);
		    
		    }
		});
    }
    
    private void setOnGetAllPlayerUpdateWorkerDone(GetAllPlayerWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        List<Player> allPlayer = worker.getPlayers();
		        if (allPlayer != null) {
					for(Player player : allPlayer) {
						if(!listView_AllPlayer.getItems().contains(player)) {
							listView_AllPlayer.getItems().add(player);
						} 
					}
					Iterator<Player> playersI = listView_AllPlayer.getItems().iterator();
					while (playersI.hasNext()) {
						Player player = playersI.next();
						if(!allPlayer.contains(player)) {
							playersI.remove();
						}
					}
				} else if (worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        	
		        }  
		        setSpinningUpdateButton(false, null);
		    
		    }
		});
    }
    
    private void setOnGetAllPlayerWorkerDone(GetAllPlayerWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        List<Player> allPlayer = worker.getPlayers();
		        if(allPlayer != null) {
		    		listView_AllPlayer.getItems().addAll(allPlayer);
		        }  else if (worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        	
		        }  
		        setSpinningUpdateButton(false, null);
		    
		    }
		});
    }
    
    private boolean deleteGameAndPlayerDialog() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Spieler löschen");
    	alert.setHeaderText("Wenn Sie den Spieler löschen werden auch alle mit Ihm verbundenen Spiele gelöscht.");
    	alert.setContentText("Möchten Sie den Spieler löschen?");

    	Optional<ButtonType> result = alert.showAndWait();
    	return result.get() == ButtonType.OK? true:false;
   
    }
    
    private void deleteAllGamesOfPlayer(Player player)  {
    	DeletePlayerGamesWorker worker = new DeletePlayerGamesWorker(player);
		setOnGameDeleteWorkerDone(worker);
		Thread workerThread = new Thread(worker);
		workerThread.start(); 
    }
    
    private void setOnGameDeleteWorkerDone(DeletePlayerGamesWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        boolean result = worker.getResult();
		        if(result) {
		        	deletePlayer(worker.getPlayer());
		        }  else if (worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        	setSpinningUpdateButton(false, null);
		        }  else if (!result ) {
		        	new Alert(AlertType.ERROR, "Die Spiele konnten nicht gelöscht werden. Aktualisieren Sie die Oberfläche und versuchen Sie es erneut." ,ButtonType.OK).show();
		        	setSpinningUpdateButton(false, null);
		        }
		    
		    }
		});
    }
    
    private void deletePlayer(Player player)  {
    	DeletePlayerWorker worker = new DeletePlayerWorker(player);
    	setOnPlayerDeleteWorkerDone(worker);
		Thread workerThread = new Thread(worker);
		workerThread.start(); 
    }
    
    private void setOnPlayerDeleteWorkerDone(DeletePlayerWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        boolean result = worker.getResult();
		        
		        if(result) {
					listView_AllPlayer.getItems().remove(worker.getPlayer());
					if(!listView_AllPlayer.getItems().isEmpty()) {
						playerSelected(null);
					} else {
						bar_PlayerStat.getData().clear();
						label_PlayerName.setText("Erstellen Sie einen neuen Spieler!");
					}
				} else if (worker.getErrorMessage() != null) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        } else if(!result ) {
					new Alert(AlertType.WARNING, "Die Spiele, aber der Spieler konnten nicht gelöscht werden. Aktualisieren Sie die Oberfläche und versuchen Sie es erneut." ,ButtonType.OK).show();
				}
		        
		        setSpinningUpdateButton(false, null);
		    }
		});
    }
    
    private void setSpinningUpdateButton(boolean spinning, String informationText) {
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

}


