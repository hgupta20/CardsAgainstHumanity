package sample;
        import java.io.BufferedReader;
        import java.io.FileReader;
        import java.io.IOException;
        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.HashMap;
        import javafx.application.Application;
        import javafx.application.Platform;
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.geometry.Insets;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextArea;
        import javafx.scene.layout.*;
        import javafx.stage.Stage;

       // = createClient("127.0.0.1",5555);


    //************************************************************************************
//creates object card
class Card {
    private String sentence;  //variables stored into each card

    Card(String s){  //constructor
        sentence = s;
    }

    //getters and setters
    String getSentence() {
        return sentence;
    }

}
//************************************************************************************
//object of deck
class Deck {

    //data members of Deck
    private int currentCard = 0; //keeps track of what card to deal for answers
    private int totalNumCards = 0;  //keeps track of total number of cards for answers
    private int totalNumQuestions = 0; //total number of questions
    private int currentQuestion = 0;  //keeps track of what question card is being used
    private ArrayList<Card> Deck = new ArrayList<>(); //contains all the cards for deck
    private ArrayList<Card> Scenarios = new ArrayList<>(); //contains scenarios for cards against humanity


    //what the constructor calls to create the Deck
    private void createDeck() {

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/answers.txt")); //reads answer cards from file
            String line = reader.readLine();
            while (line != null) {
                totalNumCards++;
                this.Deck.add(new Card(line));
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/questions.txt")); //reads question cards from file
            String line = reader.readLine();
            while (line != null) {
                totalNumQuestions++;
                this.Scenarios.add(new Card(line));
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Shuffle(Deck); //shuffles deck
        Shuffle(Scenarios); //shuffles Scenarios

    }

    //construcor that creates the Deck
    Deck(){
        createDeck();
    }

    //shufffles the deck of cards to make sure cards dealt are random
    void Shuffle( ArrayList<Card> Deck) {
        Collections.shuffle(Deck);
        Collections.shuffle(Deck);

    }
    //deals a single card and when you reach the end of deck it shuffles and starts at 0 index
    Card dealCard(){
        if(currentCard < totalNumCards){
            return Deck.get(currentCard++);
        } else {
            Shuffle(Deck);
            currentCard = 0;
            return Deck.get(currentCard);
        }
    }

    Card dealQuestion(){
        if(currentQuestion < totalNumQuestions){
            return Scenarios.get(currentQuestion++);
        } else {
            Shuffle(Scenarios);
            currentQuestion = 0;
            return Deck.get(currentQuestion);
        }
    }
    //deals an entire hand using the dealCard function
    ArrayList<Card> dealHand(){
        ArrayList<Card> temp = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            temp.add(dealCard()) ;
        }
        return temp;
    }

}

//************************************************************************************
//CAH implimentation of the game
class CAH  {
    //data members of CAH
    private Deck card = new Deck(); //creates and constructs the deck for the game
    private ArrayList<Player> Players  = new ArrayList<>();  //list of players
    ArrayList<Card> CenterCards = new ArrayList<>(); //list of cards in the center
    private int numplayers = 4; //keeps track of num players
    private int p1 = 0,p2 = 0,p3 = 0,p4 = 0;
    private int totalvotes = 0;


    //adds cards to center that each person plays
    void  addToCenter(Card temp){
        CenterCards.add(temp);
    }
    //plays the game if a player has trump card and the rest do not they get a point
    void play() {
    }

    public void addVote(int num){
        switch (num) {
            case 1:
                p1++;
                totalvotes++;
                System.out.println("vote 1");
                break;
            case 2:
                p2++;
                totalvotes++;
                System.out.println("vote 2");
                break;
            case 3:
                p3++;
                totalvotes++;
                System.out.println("vote 3");
                break;
            case 4:
                p4++;
                totalvotes++;
                System.out.println("vote 4");
                break;
            default:break;
        }
        System.out.println("Total votes " + totalvotes);
        if(totalvotes >= 4){
            System.out.println("take score");
            updateScores();
            p1 = p2 = p3 = p4 = totalvotes = 0;
            printScores();
        }
    }

    private void updateScores(){
        if((p1 > p2) && (p1 > p3) && (p1 > p4)){
            getPlayers().get(0).updateScore();
            System.out.println("Player 1 Scored");

        }
        if((p2 > p1) && (p2 > p3) && (p2 > p4)){
            getPlayers().get(1).updateScore();
            System.out.println("Player 2 Scored");

        }
        if((p3 > p2) && (p3 > p1) && (p3 > p4)){
            getPlayers().get(2).updateScore();
            System.out.println("Player 3 Scored");

        }
        if((p4 > p2) && (p4 > p3) && (p4 > p1)){
            getPlayers().get(3).updateScore();
            System.out.println("Player 4 Scored");
        }
    }

    //prints the cards in center//used for testing
    void printScores(){
        for(int i =0; i < 4;i++){
            System.out.println( "Player " + (i+1) + getPlayers().get(i).getScore());
        }
    }
    //default constructor for pitch
    CAH(){

    }

    //gets the deck of cards
    Deck getCard() {
        return card;
    }
    //gets list of Players
    ArrayList<Player> getPlayers() {
        return Players;
    }
    //gets the number of players
    int getNumplayers() {
        return numplayers;
    }
    //sets the number of players from gui
    void setNumplayers(int numberPlayers) {
        this.numplayers = numberPlayers;
        //creates each player and assigns them a hand of cards
        for (int i = 0; i < numplayers; i++) {
            Player temp = new Player();
            Players.add(temp);
            Players.get(i).setHand(card.dealHand());
        }
    }

    ArrayList<Card> getCenter(){
        return CenterCards;
    }

}

//************************************************************************************
//player object
class Player {

    //data members of the player object
    private ArrayList<Card> hand = new ArrayList<>();
    private int score = 0;

    //gets the hand from the player
    ArrayList<Card> getHand() {
        return hand;
    }
    //get the bet the player makes

    void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void updateScore() {
        score++;
    }

    public int getScore() {
        return score;
    }
}


//************************************************************************************
//GUI CLASS
public class Gui extends Application {
    private Client createClient(String ip, int port) {
        return new Client("127.0.0.1", 5555, data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }






    private TextArea messages = new TextArea();
    Client conn1;
    //basic data fields and types
    private Button card1, card6, card5, card4, card3, card2;
    private HashMap<String, Scene> sceneMap;
    private Stage myStage;

    //main that runs game
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        //gui data members
        sceneMap = new HashMap<>();
        myStage = primaryStage;
        CAH game = new CAH(); //creates the game
        game.setNumplayers(4); //assigns players to one so that gui will run
        game.addToCenter(game.getCard().dealCard());
        game.addToCenter(game.getCard().dealCard());
        game.addToCenter(game.getCard().dealCard());
        game.addToCenter(game.getCard().dealCard());


//*****************************************************************************
        //first scene choosing player numbers and labels

        primaryStage.setTitle("Cards Against Humanity");
        Label players = new Label("Connect to server");
        Label QuestionCard = new Label(game.getCard().dealQuestion().getSentence());

       // Button player2 = new Button("2 Players");
       // Button player3 = new Button("3 Players");
       Button Conenct = new Button("Connect to Server");
        //sets all num boxes to invisible
        QuestionCard.setVisible(false);


        //if two players then creates 2 players and makes textbox 1 and 2 visbible
   /*     player2.setOnAction(event -> {
            myStage.setScene(sceneMap.get("gamePlay"));
            QuestionCard.setVisible(true);
        });
        //if two players then creates 3 players and makes textbox 1,2,3 visbible
        player3.setOnAction(event -> {
            myStage.setScene(sceneMap.get("gamePlay"));
            QuestionCard.setVisible(true);
        });
        //if two players then creates 4 players and makes textbox 1,2,3,4 visbible
*/
        Conenct.setOnAction(event -> {
            conn1=createClient("127.0.0.1",5555);
            try{
                conn1.startConn();

            }
            catch(Exception e1)
            {}
            myStage.setScene(sceneMap.get("gamePlay"));
            QuestionCard.setVisible(true);

        });

//***************************************************************************
        //voting buttons
        Button voteP1 = new Button("");
        Button voteP2 = new Button("");
        Button voteP3 = new Button("");
        Button voteP4 = new Button("");
        voteP4.setVisible(false);
        voteP3.setVisible(false);
        voteP2.setVisible(false);
        voteP1.setVisible(false);
        voteP4.setMaxWidth(Double.MAX_VALUE);
        voteP3.setMaxWidth(Double.MAX_VALUE);
        voteP2.setMaxWidth(Double.MAX_VALUE);
        voteP1.setMaxWidth(Double.MAX_VALUE);


        //Sets the cards to a different size
        //creates the card buttons and sets all player card vals inside the buttons
        card1 = new Button(game.getPlayers().get(0).getHand().get(0).getSentence());
        card1.setMaxWidth(Double.MAX_VALUE);
        card2 = new Button(game.getPlayers().get(0).getHand().get(1).getSentence());
        card2.setMaxWidth(Double.MAX_VALUE);
        card3 = new Button(game.getPlayers().get(0).getHand().get(2).getSentence());
        card3.setMaxWidth(Double.MAX_VALUE);
        card4 = new Button(game.getPlayers().get(0).getHand().get(3).getSentence());
        card4.setMaxWidth(Double.MAX_VALUE);
        card5 = new Button(game.getPlayers().get(0).getHand().get(4).getSentence());
        card5.setMaxWidth(Double.MAX_VALUE);
        card6 = new Button(game.getPlayers().get(0).getHand().get(5).getSentence());
        //enables the cards

        //action event that handles what occurs when a card is picked
        EventHandler<ActionEvent> returnButton = event -> {
            Button b = (Button) event.getSource();
            //when card is clicked it adds player and ai player card to middle of deck
            game.CenterCards.clear();

            if (b == card1) {
                for (int i = 0; i < game.getNumplayers(); i++) {
                    game.addToCenter(game.getPlayers().get(i).getHand().get(0));
                    //System.out.println(game.getPlayers().get(i).getHand().get(0).getSentence());
                    game.getPlayers().get(i).getHand().set(0, game.getCard().dealCard());
                    card1.setText(game.getPlayers().get(i).getHand().get(0).getSentence());
                    String temp = game.getPlayers().get(i).getHand().get(0).getSentence();
                    try {
                        conn1.send((Serializable) (temp));
                    }
                    catch(Exception e1){}

                  //  conn1.send( card1.setText(game.getPlayers().get(i).getHand().get(0).getSentence()));
                    //ser
                }
            }
            if (b == card2) {
                for (int i = 0; i < game.getNumplayers(); i++) {
                    game.addToCenter(game.getPlayers().get(i).getHand().get(1));
                    game.getPlayers().get(i).getHand().set(1, game.getCard().dealCard());
                    card2.setText(game.getPlayers().get(i).getHand().get(1).getSentence());
                    String temp = game.getPlayers().get(i).getHand().get(1).getSentence();
                    try {
                        conn1.send((Serializable) (temp));
                    }
                    catch(Exception e1){}

                }
            }
            if (b == card3) {
                for (int i = 0; i < game.getNumplayers(); i++) {
                    game.addToCenter(game.getPlayers().get(i).getHand().get(2));
                    game.getPlayers().get(i).getHand().set(2, game.getCard().dealCard());
                    card3.setText(game.getPlayers().get(i).getHand().get(2).getSentence());
                    String temp = game.getPlayers().get(i).getHand().get(1).getSentence();
                    try {
                        conn1.send((Serializable) (temp));
                    }
                    catch(Exception e1){}

                }
            }
            if (b == card4) {
                for (int i = 0; i < game.getNumplayers(); i++) {
                    game.addToCenter(game.getPlayers().get(i).getHand().get(3));
                    game.getPlayers().get(i).getHand().set(3, game.getCard().dealCard());
                    card4.setText(game.getPlayers().get(i).getHand().get(3).getSentence());
                    String temp = game.getPlayers().get(i).getHand().get(1).getSentence();
                    try {
                        conn1.send((Serializable) (temp));
                    }
                    catch(Exception e1){}
                }
            }
            if (b == card5) {
                for (int i = 0; i < game.getNumplayers(); i++) {
                    game.addToCenter(game.getPlayers().get(i).getHand().get(4));
                    game.getPlayers().get(i).getHand().set(4, game.getCard().dealCard());
                    card5.setText(game.getPlayers().get(i).getHand().get(4).getSentence());
                    String temp = game.getPlayers().get(i).getHand().get(1).getSentence();
                    try {
                        conn1.send((Serializable) (temp));
                    }
                    catch(Exception e1){}

                }
            }
            if (b == card6) {
                for (int i = 0; i < game.getNumplayers(); i++) {
                    game.addToCenter(game.getPlayers().get(i).getHand().get(5));
                    game.getPlayers().get(i).getHand().set(5, game.getCard().dealCard());
                    card6.setText(game.getPlayers().get(i).getHand().get(5).getSentence());
                    String temp = game.getPlayers().get(i).getHand().get(1).getSentence();
                    try {
                        conn1.send((Serializable) (temp));
                    }
                    catch(Exception e1){}
                }
            }


            voteP1.setVisible(true);
            voteP2.setVisible(true);
            voteP3.setVisible(true);
            voteP4.setVisible(true);

            voteP1.setMaxWidth(Double.MAX_VALUE);
            voteP2.setMaxWidth(Double.MAX_VALUE);
            voteP3.setMaxWidth(Double.MAX_VALUE);
            voteP4.setMaxWidth(Double.MAX_VALUE);

            voteP1.setText(game.getCenter().get(0).getSentence());
            voteP2.setText(game.getCenter().get(1).getSentence());
            voteP3.setText(game.getCenter().get(2).getSentence());
            voteP4.setText(game.getCenter().get(3).getSentence());
            game.getCenter().clear();


            card1.setVisible(false);
            card2.setVisible(false);
            card3.setVisible(false);
            card4.setVisible(false);
            card5.setVisible(false);
            card6.setVisible(false);


            //b.setDisable(true); //disables buttons
            //b.setVisible(false); //makes button invisible

        };


        //chooses clubs as trump cards
        voteP1.setOnAction(event -> {
            game.addVote(1);
            setCardsVisible(game, QuestionCard, voteP1, voteP2, voteP3, voteP4);

        });
        //chooses spades as trump card
        voteP2.setOnAction(event -> {
            game.addVote(2);
            setCardsVisible(game, QuestionCard, voteP1, voteP2, voteP3, voteP4);

        });
        //sets hearts as trump card
        voteP3.setOnAction(event -> {
            game.addVote(3);
            setCardsVisible(game, QuestionCard, voteP1, voteP2, voteP3, voteP4);

        });
        //sets diamonds as trump card
        voteP4.setOnAction(event -> {
            game.addVote(4);
            setCardsVisible(game, QuestionCard, voteP1, voteP2, voteP3, voteP4);

        });
        //sets cards as return buttons
        card1.setOnAction(returnButton);
        card2.setOnAction(returnButton);
        card3.setOnAction(returnButton);
        card4.setOnAction(returnButton);
        card5.setOnAction(returnButton);
        card6.setOnAction(returnButton);

        //*********************************************************************************
        //building gui
        BorderPane bottom = new BorderPane();
        HBox Cards = new HBox(10, card1, card2, card3, card4, card5, card6);
        HBox votes = new HBox(10, voteP1, voteP2, voteP3, voteP4);
        HBox scores = new HBox(5, QuestionCard);
        scores.setPadding(new Insets(30));
        bottom.setTop(scores);
        bottom.setCenter(votes);
        bottom.setBottom(Cards);
        Scene Gameplay = new Scene(bottom);
        sceneMap.put("gamePlay", Gameplay);

        BorderPane menu = new BorderPane();
        HBox PaneCenter = new HBox(10, players,Conenct);


        menu.setCenter(PaneCenter);
        Scene MainMenu = new Scene(menu);
        sceneMap.put("How Many Players", MainMenu);
        primaryStage.setScene(sceneMap.get("How Many Players"));
        primaryStage.show();
    }

    private void setCardsVisible(CAH game, Label questionCard, Button voteP1, Button voteP2, Button voteP3, Button voteP4) {
        setVoteInvis(game, questionCard, voteP1, voteP2, voteP3, voteP4);
        card1.setVisible(true);
        card2.setVisible(true);
        card3.setVisible(true);
        card4.setVisible(true);
        card5.setVisible(true);
        card6.setVisible(true);
    }

    private void setVoteInvis(CAH game, Label questionCard, Button voteP1, Button voteP2, Button voteP3, Button voteP4) {
        questionCard.setText(game.getCard().dealQuestion().getSentence());
        voteP1.setVisible(false);
        voteP2.setVisible(false);
        voteP3.setVisible(false);
        voteP4.setVisible(false);
    }


}