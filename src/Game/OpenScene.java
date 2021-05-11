package Game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OpenScene {

    public void start(Stage window, String fxmlFile, String headerName) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        window.setTitle(headerName);
        window.setResizable(false);
        window.setScene(new Scene(root));
        window.show();
    }

    public void start(Stage window, Parent root, String headerName) throws Exception {
        window.setTitle(headerName);
        window.setResizable(false);
        window.setScene(new Scene(root));
        window.show();
    }
}
