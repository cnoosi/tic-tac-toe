package Networking;

import BoardUI.BoardUIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class UIProcess
{
    FXMLLoader fxmlLoader = new FXMLLoader();
    Parent root;
    BoardUIController controller;

    public UIProcess()
    {
        try
        {
            root = FXMLLoader.load(getClass().getResource("/BoardUI/BoardUI.fxml"));
            controller = (BoardUIController)fxmlLoader.getController();
        }
        catch (Exception E) {}
    }

    public void updateBoardUI(int currentToken, int row, int col, int nextToken)
    {

        controller.setImage(currentToken, row, col);
    }

}
