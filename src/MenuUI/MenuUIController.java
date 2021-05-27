package MenuUI;

import AdminDashUI.AdminDashUIController;
import BoardUI.*;
import Game.*;
import Observers.Observer;
import Observers.ObserverMessage;
import Observers.Subject;
import GameHistoryUI.GameHistoryUIController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuUIController implements Initializable, Observer, Subject
{
    private       OpenScene openScene = new OpenScene();
    @FXML private Button    singlePlayerBtn;
    @FXML private Button    multiPlayerBtn;
    @FXML private Button    userMenuBtn;
    @FXML private Button    gameHistoryBtn;
    @FXML private Button    adminDashBtn;
    @FXML private MediaView mv;
    @FXML private ImageView sngl;
    @FXML private ImageView mlti;
    @FXML private Label     usernameLabel;

    private MediaPlayer mp;
    private Media me;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Observer UIProcess;


    @FXML
    public void handleSinglePlayerMode(ActionEvent event) throws Exception
    {
        notifyObservers(new ObserverMessage("from Menu", new ArrayList<>()));
//        Stage stage = (Stage) singlePlayerBtn.getScene().getWindow();
//        FXMLLoader root = new FXMLLoader();
//        root.setLocation(getClass().getResource("/BoardUI/BoardUI.fxml"));
//        Parent frame = root.load();
//        BoardUIController controller = (BoardUIController) root.getController();
//        controller.setLocalPlayerCount(1);
//        controller.resetGame();
//        openScene.start(stage, frame, "Tic-Tac-Toe - Single Player Game");
//        mp.stop();
    }

    @FXML
    public void handleTwoPlayerMode(ActionEvent event) throws Exception
    {
        notifyObservers(new ObserverMessage("from Menu"));
//        Stage stage = (Stage) multiPlayerBtn.getScene().getWindow();
//        FXMLLoader root = new FXMLLoader();
//        root.setLocation(getClass().getResource("/BoardUI/BoardUI.fxml"));
//        Parent frame = root.load();
//        BoardUIController controller = (BoardUIController) root.getController();
//        controller.setLocalPlayerCount(2);
//        controller.resetGame();
//        openScene.start(stage, frame, "Tic-Tac-Toe - Two Player Game");
//        mp.stop();
    }

    public void handleUserMenuButton(ActionEvent event) throws Exception{
        Stage stage = (Stage) userMenuBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("UserMenuUI.fxml"));
        Parent frame = root.load();
        UserMenuUIController controller = (UserMenuUIController) root.getController();
        openScene.start(stage, frame, "Tic-Tac-Toe - User Menu");
        mp.stop();
    }

    public void handleGameHistoryButton(ActionEvent event) throws Exception{
        Stage stage = (Stage) gameHistoryBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("/GameHistoryUI/GameHistoryUI.fxml"));
        Parent frame = root.load();
        GameHistoryUIController controller = (GameHistoryUIController) root.getController();
        openScene.start(stage, frame, "Tic-Tac-Toe - Game History");
        mp.stop();
    }

    public void handleAdminDashButton(ActionEvent event) throws Exception{
        Stage stage = (Stage) adminDashBtn.getScene().getWindow();
        FXMLLoader root = new FXMLLoader();
        root.setLocation(getClass().getResource("/AdminDashUI/AdminDashUI.fxml"));
        Parent frame = root.load();
        AdminDashUIController controller = (AdminDashUIController) root.getController();
        openScene.start(stage, frame, "Tic-Tac-Toe - Admin Dash");
        mp.stop();
    }

    public void stop(){
        mp.stop();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String path = new File("src/resources/images/comp 1.mp4").getAbsolutePath();

        //********************************************
        //TESTING DATABASE
        //********************************************
        Scanner scanner = new Scanner(System.in);
////        Database db = new Database();
//        System.out.println("Database Control Panel\n----------------\n" +
//                            "1) Add new user\n" +
//                            "2) Remove existing user\n" +
//                            "3) Show existing users\n" +
//                            "0) Continue to game\n");
//        int userInput = scanner.nextInt();
//        while (userInput != 0){
//            switch (userInput){
//                case 1:
////                    System.out.println(db.connect());
////                    System.out.println("Enter UserName FirstName LastName Password");
////                    scanner.nextLine();
////                    db.insert(scanner.nextLine(),scanner.nextLine(),scanner.nextLine(),scanner.nextLine());
////                    break;
//                case 2:
////                    System.out.println(("Enter the UserName you wish to delete: "));
////                    scanner.nextLine();
////                    db.deleteRow(scanner.nextLine());
////                    break;
//                case 3:
//                    DbManager.getInstance().printAllUsers();
//                    break;
//                case 0:
//                    break;
//            }
//            System.out.println("Database Control Panel\n----------------\n" +
//                    "1) Add new user\n" +
//                    "2) Remove existing user\n" +
//                    "3) Show existing users\n" +
//                    "0) Continue to game\n");
//            userInput = scanner.nextInt();
//        }



        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        mp.setCycleCount(MediaPlayer.INDEFINITE);

        //********************************************
        //ADDING GRAPHICS TO THE SINGLE PLAYER BUTTON
        //********************************************
        //User current = new User();
        //current = UserDbManager.getInstance().getCurrentUser();
        //usernameLabel.setText(current.getUserName());


        //********************************************
        //ADDING GRAPHICS TO THE SINGLE PLAYER BUTTON
        //********************************************
        String path1 = new File("src/resources/images/singlePlayerButton.gif").getAbsolutePath();
        Image img1 = new Image(new File(path1).toURI().toString());
        ImageView view1 = new ImageView(img1);
        view1.setFitHeight(180);
        view1.setPreserveRatio(true);
        //Setting a graphic to the button
        singlePlayerBtn.setGraphic(view1);
        singlePlayerBtn.setStyle("-fx-background-color: transparent;");

        //Possibly add a mouse hover function to change the image state
        singlePlayerBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });

        //********************************************
        //ADDING GRAPHICS TO THE  MULTIPLAYER BUTTON
        //********************************************
        String path2 = new File("src/resources/images/multiPlayerButton.gif").getAbsolutePath();
        Image img2 = new Image(new File(path2).toURI().toString());
        ImageView view2 = new ImageView(img2);
        view2.setFitHeight(180);
        view2.setPreserveRatio(true);
        //Setting a graphic to the button
        multiPlayerBtn.setGraphic(view2);
        multiPlayerBtn.setStyle("-fx-background-color: transparent;");

        //Possibly add a mouse hover function to change the image state
        multiPlayerBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });

    }


    // Observer & Subject Handling
    @Override
    public void update(ObserverMessage message)
    {

    }

    @Override
    public void addObserver(Object o)
    {
        UIProcess = (Observer) o;
    }

    @Override
    public void removeObserver(Object o)
    {
        UIProcess = null;
    }

    @Override
    public void notifyObservers(ObserverMessage message)
    {
        if(UIProcess != null)
            UIProcess.update(message);
    }
}
