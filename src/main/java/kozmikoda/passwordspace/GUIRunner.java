package kozmikoda.passwordspace;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUIRunner extends Application{
    Stage window;
    Scene mainScene;

    FXMLLoader fxml;


    public void start(Stage s) throws Exception{

        fxml = new FXMLLoader(getClass().getResource("gui.fxml"));
        window = fxml.load();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        window.setX(screenBounds.getWidth()/3.8);
        window.setY(screenBounds.getHeight()/4.5);



        window.initStyle(StageStyle.TRANSPARENT);

        mainScene = window.getScene();
        mainScene.setFill(Color.TRANSPARENT);

        window.getIcons().add(new Image(getClass().getResourceAsStream("passwordSpaceStart.png")));

        window.setScene(mainScene);
        window.show();

    }

    public static void main() {
        launch();
    }

}
