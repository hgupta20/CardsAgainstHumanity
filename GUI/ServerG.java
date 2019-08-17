package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ServerG extends Application{

    private boolean isServer = true;
    int score = 0;
    Button connect = new Button("Connect");
    Button port = new Button("Port");
    Button exit = new Button("Exit");
    private NetworkConnection  conn;
    private TextArea messages = new TextArea();
    TextField getPort;
    Scene firstScene;
    List<String> hand = new ArrayList<>();
    String result;

    private Parent createContent(Stage primaryStage) {
        messages.setPrefHeight(550);
        TextField input = new TextField();
        exit.setOnAction(event -> {
            try {
                primaryStage.close();
            }
            catch (Exception invalidHand){

            }
        });

        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");
            try {
                conn.send(message);
            }
            catch(Exception e) {

            }

        });

        VBox root = new VBox(20, messages, input,exit);
        root.setPrefSize(600, 600);

        return root;



    }

    private Scene createServerGUI() {
        /*messages.setPrefHeight(550);
        TextField input = new TextField();

        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");
            try {
                conn.send(message);
            }
            catch(Exception e) {

            }

        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);

        return root;*/
        BorderPane borderPane = new BorderPane();
        HBox layout = new HBox(100);
        getPort = new TextField();
        getPort.setPrefWidth(60);
        layout.getChildren().add(port);
        layout.getChildren().add(getPort);
        layout.getChildren().add(connect);
        TextArea message = new TextArea();
        message.setPrefHeight(550);
        borderPane.setCenter(layout);
        Scene firstScene = new Scene(borderPane);

        return firstScene;






    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene(createServerGUI());
        connect.setOnAction(event -> {

            int convertPort = parseInt(getPort.getText());
            conn = createServer(convertPort);

            // try and catch exception
            try {
                conn.startConn();
            }
            catch (Exception connection){
                connection.printStackTrace();
            }

            // set the scene
            primaryStage.setScene(new Scene(createContent(primaryStage)));

        });
        primaryStage.show();

    }
    //@Override
    /*public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene(new Scene(createClientGUI()));
        primaryStage.show();

    }*/


    /*@Override
    public void init() throws Exception{
        //conn.startConn();
    }*/

    @Override
    public void stop() throws Exception{
        conn.closeConn();
    }

    private Server createServer(int port) {
        return new Server(port, data-> {
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
                /*if(!hand.isEmpty()){

                }*/

                /*hand.add(data.toString());
                for (int i =0; i < hand.size(); i++){
                    messages.appendText("Player" + i+1 + ": " + hand.get(i) + " Length: " + hand.size() + "\n");

                }
                if (hand.size() < 2){
                    messages.appendText("Waiting for other Client\n");
                }
                else if (hand.size() == 2){
                    // play the game
                    messages.appendText("Playing the Game\n");
                    result = RPSLS(hand.get(0), hand.get(1));
                    messages.appendText("Result " + result + "\n");

                }
                else{
                    messages.appendText("Internal Error, Work on it\n");
                }*/


            });
        });
    }

    private Client createClient() {
        return new Client("127.0.0.1", 5555, data -> {
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
            });
        });
    }


}
