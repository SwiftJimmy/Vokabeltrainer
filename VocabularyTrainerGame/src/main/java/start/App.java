package start;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	public void start(Stage primaryStage) throws IOException {
		primaryStage.show(); 
		Parent mainPane = FXMLLoader.load(App.class.getResource("/Main.fxml"));
			
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setResizable(false);
			
		primaryStage.setTitle("Vocabulary Trainer");	
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
