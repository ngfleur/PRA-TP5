package FleurNguessan.morpion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {

		try {
			Pane root = (Pane) FXMLLoader.load(getClass().getResource("Morpion.fxml"));
			Scene scene = new Scene(root);

			primaryStage.setTitle("Morpion");
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
