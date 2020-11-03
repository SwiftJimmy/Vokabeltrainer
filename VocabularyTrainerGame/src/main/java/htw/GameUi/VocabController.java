package htw.GameUi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import htw.VocabularyManagmentInter.VocabManagementServiceException;
import htw.VocabularyManagmentInter.Vocabulary;
import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.Word;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import model.CreateVocabularyListWorker;
import model.GetAllVocabularyListWorker;

public class VocabController {

    @FXML
    private TreeView<String> vocabTree;
	
    @FXML
    private Pane vocabPane;

    @FXML
    private Button btn_SelectFile;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Label informationLabel;
    
    private TreeItem<String> root;
    
    public void initialize() throws IOException {
    	root = new TreeItem<String> ("Vokabel Baum");
    	try {
	    	vocabTree.setRoot(root);
	    	vocabTree.setShowRoot(false);
	    	setSpinningUpdateButton(true, "Die Vokabellisten werden geladen ...");
	    	updateView();
	    	
		} catch (VocabManagementServiceException e) {
			new Alert(AlertType.ERROR, e.getMessage() ,ButtonType.OK).show();
		}
    }

    @FXML
    void openDirectory(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
    	fileChooser.setSelectedExtensionFilter(extFilter);
    	
        File selectedDirectory = fileChooser.showOpenDialog(null);
        if (selectedDirectory != null && selectedDirectory.isFile()) {
        	VocabularyList vocablist = readFile(selectedDirectory.getAbsolutePath());
        	
        	if(vocablist == null) {
        		new Alert(AlertType.ERROR, "Die Datei konnte nicht eingelesen werden. Überprüfen Sie das Format." ,ButtonType.OK).show();
        		return;
        	}

        	setSpinningUpdateButton(true, "Die Vokabelliste wird eingelesen ... ");
        	CreateVocabularyListWorker worker = new CreateVocabularyListWorker(vocablist.getBook(), vocablist.getChapter(),vocablist.getVocabularies(),vocablist.getLanguage1(), vocablist.getLanguage2());
        	setOnWorkerDone(worker);
        	Thread workerThread = new Thread(worker);
    		workerThread.start();
    		
        }
        	
    }
    
    @FXML
    void updateVocabView(ActionEvent event) {
    	setSpinningUpdateButton(true, "Die Vokabellisten werden aktualisiert ... ");
    	updateView();
    }
    
    private void updateView() {
    	GetAllVocabularyListWorker worker = new GetAllVocabularyListWorker();
		setOnWorkerDone(worker);
		Thread workerThread = new Thread(worker);
		workerThread.start();
    }
    
    private void setOnWorkerDone(GetAllVocabularyListWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		    	
		        List<VocabularyList> allvocabLists = worker.getVocabularyLists();
		        
		        if (worker.getErrorMessage() != null) {
		        	setSpinningUpdateButton(false, null);
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        } else {
		        	replaceView(allvocabLists);
		        }
		    }
		});
    }
    
    private void replaceView(List<VocabularyList> list) {
    	root.getChildren().clear();
        for(VocabularyList vocabList : list) {
        	addVocabularyListToTree(vocabList );
        }
        setSpinningUpdateButton(false, null);

    }
    
    
    
    private void setSpinningUpdateButton(boolean spinning, String informationtext) {
    	String spinnerImage;
    	if (spinning) {
    		spinnerImage = getClass().getResource("/spinning.gif").toExternalForm();
    		updateButton.setDisable(true);
    		informationLabel.setText(informationtext);
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
    
    
    private void addVocabularyListToTree(VocabularyList vocabList ) {
    	TreeItem<String> book = new TreeItem<String> (vocabList.getBook());
		
		TreeItem<String> chapter = new TreeItem<String> (vocabList.getChapter());
		
		
		for(Vocabulary vocab :vocabList.getVocabularies()) {
			TreeItem<String> vocabulary = new TreeItem<String> ("Vokabel " + vocab.getVocabularyId());
			TreeItem<String> language1 = new TreeItem<String> (vocabList.getLanguage1());
			TreeItem<String> language2 = new TreeItem<String> (vocabList.getLanguage2());
			
			
			for (Word word: vocab.getWord_language_one()) {
				language1.getChildren().add(new TreeItem<String>(word.getWord()));
			}
			
			for (Word word: vocab.getWord_language_two()) {
				language2.getChildren().add(new TreeItem<String>(word.getWord()));
			}
			vocabulary.getChildren().add(language1);
			vocabulary.getChildren().add(language2);
			chapter.getChildren().add(vocabulary);
		}
		
		
		int indexalreadyExist =  findIndexOfDouplicateBooks(root,book);
		if(indexalreadyExist>=0) {
			root.getChildren().get(indexalreadyExist).getChildren().add(chapter);
		} else {
			book.getChildren().add(chapter);
			root.getChildren().add(book);
		}
    }
    
    
    private void setOnWorkerDone(CreateVocabularyListWorker worker) {
    	worker.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
		        new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		        VocabularyList vocabList = worker.getVocabularyList();
		        
		        
		        
		        if (worker.getErrorMessage() != null || vocabList == null ) {
		        	new Alert(AlertType.ERROR, worker.getErrorMessage() ,ButtonType.OK).show();
		        } else {
		        	addVocabularyListToTree(vocabList);
		        	new Alert(AlertType.INFORMATION, "Die Vokabeln wurde erstellt" ,ButtonType.OK).show();
		        }
		        setSpinningUpdateButton(false, null);
		    }
		});
    }
    
    private int findIndexOfDouplicateBooks(TreeItem<String>  root, TreeItem<String>  book) {
    	for(int i = 0; i <root.getChildren().size(); i++) {
    		if(root.getChildren().get(i).getValue().equals(book.getValue())){
    			return i;
    		}
    	}
    	return -1;
    	
    }
    
    
    private VocabularyList readFile(String filePath){
		
	    String line;
 	    String chapter = null;
	    String langFrom = null;
	    String langTo = null;
	    String book = null;
	    VocabularyList vocabList = null;	    
	    List<Vocabulary> vocabularys = new ArrayList<Vocabulary>();
	    try {
	    	BufferedReader br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				if (isHeaderLine(line)) { 
					String[] header = getHeaderInformation(line);	
					chapter = header[0] == ""? null:header[0];
			    	langFrom = header[1] == ""? null:header[1];
			    	langTo = header[2] == ""? null:header[2];
			    	book = header[3] == ""? null:header[3];
			    	
				} else if (isVocabularyLine(line)){
					// split languages
					String[] languageWordLists = line.split(" : "); 
					
					// remove curly brackets
					languageWordLists = cleanWords(languageWordLists); 
					
					// split into single words
					String[] wordsFromLanguage = languageWordLists[0].split(", "); 
					String[] wordsTotLanguage = languageWordLists[1].split(", ");
					
					// generate word objekts
					List<Word> fromWordlist = generateWordObjectList(wordsFromLanguage, langFrom ); 
					List<Word> toWordlist = generateWordObjectList(wordsTotLanguage, langTo );
					
					if(fromWordlist.isEmpty() || toWordlist.isEmpty() )
						return null;
					
					// generate vocabulary
					Vocabulary vocabulary = new Vocabulary(fromWordlist,toWordlist);
					
					// add to vocabularys list
					vocabularys.add(vocabulary);
			    }		    		    	
			}
			br.close();
			
			if (vocabularys.isEmpty() || chapter == null || book == null || langFrom == null || langTo == null)
				return null;
			
			vocabList =  new VocabularyList(chapter,book,  vocabularys,langFrom,langTo);
		} catch (IOException e) {
			new Alert(AlertType.ERROR, "Die Datei kann nicht eingelesen werden." ,ButtonType.OK).show();
		} 
		return vocabList;
	} 
	
	private boolean isHeaderLine(String line) {
		return line.startsWith("{{{") && line.endsWith("}}}");
	}
	
	private boolean isVocabularyLine(String line) {
		return line.startsWith("{") && line.endsWith("}") && (line.substring(2) != "{");
	}
	
	private String[] cleanWords(String[] languageWordLists) {
		for(int i = 0; i < languageWordLists.length; i++) {
			languageWordLists[i] = languageWordLists[i].replaceAll("\\{\\{\\{(.*?)\\}\\}\\}", ""); // remove example sentences
			languageWordLists[i] = languageWordLists[i].replaceAll("\\}", ""); // remove all curly bracktes
			languageWordLists[i] = languageWordLists[i].replaceAll("\\{", ""); // remove all curly bracktes	    		
		} 
		return languageWordLists;
	}
	
	private String[] getHeaderInformation(String line) {
		String[] header = new String[4];
		Pattern p = Pattern.compile("\\{\\{\\{(.*?)\\}\\}\\}");
		Matcher m = p.matcher(line);
		int index = 0;
		while(m.find()) {
			header[index] = m.group(1);
		    index++;			    
		}
		return header;
	}
	
	private List<Word> generateWordObjectList(String[] words, String language ) {
		 List<Word> wordList = new ArrayList<Word>();
		for(String wordString : words) {
			Word word = new Word(wordString, language);
			wordList.add(word);
		}
		return wordList;
	}

	public Button getUpdateButton() {
		return updateButton;
	}

	public void setUpdateButton(Button updateButton) {
		this.updateButton = updateButton;
	}
}