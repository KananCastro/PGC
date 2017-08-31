package application;
	
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


public class Main extends Application {
    
    
	
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		
		try {
			
			Scene scene = new Scene(root,900,480);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
                        primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		root.setCenter(new RootLayout());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}