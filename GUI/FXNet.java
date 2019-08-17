package egs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import egs.Game.GameCommands;

public class FXNet extends Application {

	private static boolean isServer;

	private NetworkConnection conn;
	private TextArea messages = new TextArea();
	private int port = 0;
	private int id;
	private String ip = "127.0.0.1"; /* Default IP */
	
	private final String NEWLINE = "\n";
	private final String DBLNEWLINE = "\n\n";

	/* Server GUI */
	
	/* Main Menu */
	private Parent initServerMenuUI(Stage primaryStage) {
		primaryStage.setTitle(isServer ? "Server Menu" : "Client Menu");
		
		TextField textPort = new TextField("Enter Port ####");
		Button btnStart = new Button("Start Server");
		Button btnExit = new Button("Exit Game");

		HBox root = new HBox(20, textPort, btnStart, btnExit);
		root.setPrefSize(600, 600);

		btnStart.setOnAction(event -> {
			if (!Game.isInteger(textPort.getText()))
				textPort.setText("Integers Only eg: 5555");
			else
				port = Integer.parseInt(textPort.getText());

			conn = createServer();
			try {
				conn.startConn();

				primaryStage.setScene(new Scene(initServerGameUI(primaryStage)));
			} catch (Exception e) {
				System.out.println("Error starting connection.");
			}

		});

		btnExit.setOnAction(event -> {
			try {
				conn.closeConn();
			} catch (Exception e) {}

			System.exit(0);

		});

		return root;
	}
	
	/* Run Games */
	private Parent initServerGameUI(Stage primaryStage) {
		messages.setPrefHeight(550);

		primaryStage.setTitle(ip + " " + (isServer ? "Game Server " : "Client Player") + port);

		TextField textPortNum = new TextField();
		textPortNum.setText("5555");
		Button btnAnnounce = new Button("Announce Winner");
		Button btnExit = new Button("Exit Game");

		btnAnnounce.setOnAction(event -> {
			try {
				conn.send("");
			} catch (Exception e) {
				System.out.println("Error sending command data.");
			}
		});

		btnExit.setOnAction(event -> {
			try {
				conn.closeConn();
			} catch (Exception e) {
			}

			System.exit(0);

		});

		VBox root = new VBox(20, messages, btnAnnounce, btnExit);
		root.setPrefSize(600, 600);

		return root;

	}

	/* Client GUI */
	
	/* Main Menu */
	private Parent initClientMenuUI(Stage primaryStage) {
		TextField textIP = new TextField("127.0.0.1");
		TextField textPort = new TextField("Enter Port ####");
		Button btnStart = new Button("Connect to Server");
		Button btnExit = new Button("Exit Game");

		HBox root = new HBox(20, textIP, textPort, btnStart, btnExit);
		root.setPrefSize(650, 100);

		btnStart.setOnAction(event -> {
			if (!Game.isInteger(textPort.getText()))
				textPort.setText("Integers Only eg: 5555");
			else if (textIP.getText().equals(""))
				textIP.setText("127.0.0.1");
			else {
				ip = textIP.getText();
				port = Integer.parseInt(textPort.getText());

				conn = createClient();
				try {
					conn.startConn();
					primaryStage.setScene(new Scene(initClientGameUI(primaryStage)));
				} catch (Exception e) {
					System.out.println("Error sending command data.");
				}
			}
		});

		return root;
	}

	/* Run Game */
	private Parent initClientGameUI(Stage primaryStage) {
		//messages.setPrefHeight(550);

		primaryStage.setTitle(ip + " " + (isServer ? "Server GUI " : "Client GUI ") + port);

		Button btnRock = new Button("Rock");
		Button btnPaper = new Button("Paper");
		Button btnScissors = new Button("Scissors");
		Button btnLizard = new Button("Lizard");
		Button btnSpock = new Button("Spock");

		btnRock.setOnAction(event -> {
			messages.appendText(sendCommand(GameCommands.PLAY_ROCK) + NEWLINE);
		});

		btnPaper.setOnAction(event -> {
			messages.appendText(sendCommand(GameCommands.PLAY_PAPER) + NEWLINE);
		});

		btnScissors.setOnAction(event -> {
			messages.appendText(sendCommand(GameCommands.PLAY_SCISSORS) + NEWLINE);
		});

		btnLizard.setOnAction(event -> {
			messages.appendText(sendCommand(GameCommands.PLAY_LIZARD) + NEWLINE);
		});

		btnSpock.setOnAction(event -> {
			messages.appendText(sendCommand(GameCommands.PLAY_SPOCK) + NEWLINE);
		});
		

		HBox hbox = new HBox(20, btnRock, btnPaper, btnScissors, btnLizard, btnSpock);
		//hbox.setPrefSize(600, 600);
		

		VBox vbox = new VBox(20, messages);

		TextField textPlayerSelect = new TextField();
		Button btnChallenge = new Button("Challenge Player");
		Button btnWho = new Button("Who Am I?");
		Button btnLob = new Button("Who can I Play?");
		Button btnExit = new Button("Exit Game");
		
		btnChallenge.setOnAction(event -> {
			if(Game.isInteger(textPlayerSelect.getText()))
			{
				messages.appendText(sendCommand(GameCommands.CLIENT_CHALLENGE, textPlayerSelect.getText()) + NEWLINE);
			}
			else
				messages.appendText("Enter Player ID # and press Challenge" + NEWLINE);
		});
		
		btnWho.setOnAction(event -> {
			messages.appendText(sendCommand(GameCommands.CLIENT_WHOAMI) + NEWLINE);
		});
		
		btnLob.setOnAction(event -> {
			messages.appendText(sendCommand(GameCommands.CLIENT_LOBBY) + NEWLINE);
		});


		btnExit.setOnAction(event -> {
			try {
				conn.closeConn();
			} catch (Exception e) {
			}

			System.exit(0);

		});
		
		HBox hboxTwo = new HBox(20, textPlayerSelect, btnChallenge, btnLob, btnWho, btnExit);
		
		BorderPane border = new BorderPane();
		border.setTop(hboxTwo);
		border.setCenter(vbox);
		border.setBottom(hbox);

		return border;
	}

	/* Send Commands between Server/Client */
	String sendCommand(GameCommands command) {
		return sendCommand(command, "");
	}
	
	String sendCommand(GameCommands command, String param) {
		try {
			conn.send(command.toString() + param);
		} catch (Exception e) {
			System.out.println("Error sending command data.");
		}
		return command.toString() + param;
	}

	/* Must be called to define whether instance will be Server */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			if (args[0].equals("-s"))
				isServer = true;
			else if (args[0].equals("-c"))
				isServer = false;
			else {
				System.out.println("Usage: -s for Server, -c for Client");
				System.exit(-1);
			}
		} catch (Exception e) {
			System.out.println("Fatal Error... are you trying to launch without arguments?");
			System.exit(-1);
		}

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		primaryStage.setScene(
				isServer ? new Scene(initServerMenuUI(primaryStage)) : new Scene(initClientMenuUI(primaryStage)));
		primaryStage.show();

	}

	@Override
	public void init() throws Exception {

	}

	@Override
	public void stop() throws Exception {
		try {
			conn.closeConn();
		} catch (Exception e) {
			
		}
	}

	private Server createServer() {
		return new Server(port, data -> {
			Platform.runLater(() -> {
				messages.appendText(data.toString() + DBLNEWLINE);
			});
		});
	}

	private Client createClient() {
		return new Client(ip, port, data -> {
			Platform.runLater(() -> {
				messages.appendText(data.toString() + DBLNEWLINE);
			});
		});
	}

}
