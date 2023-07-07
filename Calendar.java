import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Calendar extends Application{ 

	public void start(Stage stage) throws Exception{ 
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("Calendar.fxml")); 
		Scene scene = new Scene(root); 
		stage.setTitle("Calendar"); 
		stage.setScene(scene); 
		stage.show(); 
	} 

	public static void main(String[] args){ 
		launch(args); 
		System.out.println();
	} 
}