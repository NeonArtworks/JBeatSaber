package at.neonartworks.jbeatsaber.editor;

import java.io.IOException;

import at.neonartworks.jbeatsaber.editor.config.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JBeatSaberEditor extends Application
{

	private double width = Config.WIDTH;
	private double height = Config.HEIGHT;

	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setTitle(Config.TITLE);
		Parent root = null;
		// stage.getIcons().add(new
		// Image(JBeatSaberEditor.class.getResourceAsStream("/icon.png")));

		FXMLLoader loader = new FXMLLoader(JBeatSaberEditor.class.getResource("/window.fxml"));
		try
		{
			root = loader.load();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
		
		Controller cntrl = (Controller) loader.getController();
		cntrl.initController(stage);

	}

	public static void main(String[] args)
	{
		launch(args);
	}
}