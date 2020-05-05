/**
 * The class implements the game name Gomoku
 * @author Hieu Nguyen
 * @version 1.0
 * @since 2019-12-07
 */ 
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.geometry.Insets;
import javafx.scene.paint.*;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.CornerRadii;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Gomoku extends Application{
  //The 2D array to represent the button of the board//
  private Button[][] board;
  //The 2D array to represent the button of the board as integers//
  private int[][] intBoard;
  //The final field to set the boolean value of turn. The turn value will determine the move of black or white//
  private final BooleanProperty turn = new SimpleBooleanProperty(true);
  //The int value of black move//
  private final int blackMove = 1;
  //The int value of white move//
  private final int whiteMove = -1;
  //The int value of the square with no move//
  private final int noMove = 0;
  //The int array represents x coordinates//
  private final int[] dirX = {-1, 1, 0, 0, -1, 1, -1, 1};
  //The int array represents y coordinates//
  private final int[] dirY = {0, 0, -1, 1, -1, -1, 1, 1};
  //The field of grid pane//
  private GridPane gridPane;
  //The field represents the pane//
  private BorderPane pane;
  //The field represents the text board//
  private ScrollPane scroll;
  //The field represent the board inside the text board//
  private VBox box;
  //The field represent the nth turn of black and white//
  private int turnNumber;
  
  /**
   * The method to set the turn number
   * @param x the number
   */
  public void setTurnNumber(int x){
    this.turnNumber = x;
  }

  /**
   * The method to create a button board and integer board with specified dimensions
   * @param row A variable of row dimension
   * @param column A variable of column dimension
   */ 
  public void createGomoku(int row, int column){
    this.board = new Button[row][column];
    this.intBoard = new int[row][column];
  }
  
  /**
   * The method to get the integer board
   * @return the 2D integer board
   */ 
  public int[][] getBoard2(){
    return this.intBoard;
  }
  
  /**
   * The method to get the button board
   * @return The button board
   */ 
  public Button[][] getBoard(){
    return this.board;
  }
  
  /**
   * The method to create the interface board for the game
   * @param newStage A stage to show the scene
   */ 
  public void start(Stage newStage){
    createGomoku(Integer.parseInt(getParameters().getRaw().get(1)), Integer.parseInt(getParameters().getRaw().get(2)));
    gridPane = new GridPane(); 
    pane = new BorderPane();
    box = new VBox();
    scroll = new ScrollPane();
    Button newGame = new Button();
    
    pane.setCenter(gridPane);
    pane.setBottom(scroll);
    pane.setTop(newGame);
    scroll.setPrefSize(200, 150);
    scroll.setContent(box);
    newGame.setText("New Game");
    //The nested loop to create and format the button on the board. Also it initializes the value in the integer board//
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[i].length; j++){
        Button square = new Button();
        Move haha = new Move();
        square.setPrefSize(30,30);
        square.setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)), new BackgroundFill(null,null,null)));
        board[i][j] = square;
        intBoard[i][j] = 0;
        gridPane.add(square, j, i);
        board[i][j].setOnAction(haha);
      }
    }
    Scene scene = new Scene(pane);
    newStage.setScene(scene);
    newStage.show();
    newGame.setOnAction( e -> 
                        {
      for(int k = 0; k < board.length; k++){
        for(int t = 0; t < board[k].length; t++){
          Button square = new Button();
          Move haha = new Move();
          square.setPrefSize(30,30);
          square.setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)), new BackgroundFill(null,null,null)));
          board[k][t] = square;
          intBoard[k][t] = 0;
          gridPane.add(square, t, k);
          board[k][t].setOnAction(haha);
        }
      }
      box = new VBox();
      pane.setCenter(gridPane);
      pane.setBottom(scroll);
      scroll.setPrefSize(200, 150);
      scroll.setContent(box);
      setTurnNumber(0);
    });
  }
  
  /**
   * The method to set the turn to be false
   */ 
  private void setTurnFalse(){ 
    turn.set(false); 
  }
  
  /**
   * The method to set turn to be true
   */ 
  private void setTurnTrue(){ 
    turn.set(true); 
  }
  
  /**
   * The method to count the number of consecutive white or black moves on the board in a specified direction
   * @param intBoard The current integer board
   * @param row      The current row position
   * @param column   The current column position
   * @param dirX     The x coordinate of the specified direction
   * @param dirY     The y coordinate of the specified direction
   * @return The number of consecutive white or black moves in that specified direction
   */ 
  public int numberInLine(int[][] intBoard, int row, int column, int dirX, int dirY){
    int count = 0;
    int x = row;
    int y = column;
    while((x >= 0 && x < intBoard.length) && (y < intBoard[0].length && y >= 0) && intBoard[row][column] != 0 && intBoard[row][column] == intBoard[x][y]){
      x += dirY;
      y += dirX;
      count += 1;
    }
    return count;
  }
  
  /**
   * The method to check from the square on the board, in a specified direction, whether the line is opened
   * @param intBoard The current integer board
   * @param row      The current row position
   * @param column   The current column position
   * @param dirX     The x coordinate of the specified direction
   * @param dirY     The y coordinate of the specified direction
   * @return If the line is not opened, return false. Otherwise, return true
   */ 
  public boolean isOpen(int[][] intBoard, int row, int column, int dirX, int dirY){
    if((row + numberInLine(intBoard, row, column, dirX, dirY) * dirY >= 0 && row + numberInLine(intBoard, row, column, dirX, dirY) * dirY < intBoard.length) && (column + numberInLine(intBoard, row, column, dirX, dirY) * dirX >= 0 && column + numberInLine(intBoard, row, column, dirX, dirY) * dirX < intBoard[0].length)){
      if(intBoard[row + numberInLine(intBoard, row, column, dirX, dirY) * dirY][column + numberInLine(intBoard, row, column, dirX, dirY) * dirX] == noMove){
        return true;
      }
      else if(intBoard[row + numberInLine(intBoard, row, column, dirX, dirY) * dirY][column + numberInLine(intBoard, row, column, dirX, dirY) * dirX] != intBoard[row][column]){
        return false;
      }
      else{
        return false;
      }
    }
    return false;
  }
  
  /**
   * The method counts the number of consecutive black or white on 4 direction, vertical, horizontal and 2 diagonals, then return the number of line that meet the number
   * of consecutive moves.
   * @param intBoard The current integer board
   * @param i     The current row position
   * @param j     The current column position
   * @param target The specified consecutive moves
   * @return The number of lines that meet the number of consecutive moves
   */ 
  public int fourDirectionCount(int[][] intBoard, int i, int j, int target){
    int count1 = 0; //The number of consecutive moves on the horizontal direction
    int count2 = 0; //The number of consecutive moves on the vertical direction
    int count3 = 0; //The number of consecutive moves on the first diagonal direction
    int count4 = 0; //The number of consecutive moves on the second diagonal direction
    int count5 = 0; //The number of direction that reach the number of consecutive moves
    int[] hihi = {-1, 1}; //The array of x or y coordinates
    int[] arr = {count1, count2, count3, count4}; //The array contains the number of consecutive moves for each direction
    //The loop to calculate the number of consecutive moves in horizontal direction
    for(int k = 0; k < hihi.length; k++){
      arr[0] += numberInLine(intBoard, i, j, 0, hihi[k]);
    }
    //The loop to calculate the number of consecutive moves in vertical direction
    for(int k = 0; k < hihi.length; k++){
      arr[1] += numberInLine(intBoard, i, j, hihi[k], 0);
    }
    //The loop to calculate the number of consecutive moves in the first diagonal direction
    for(int k = 0; k < hihi.length; k++){
      arr[2] += numberInLine(intBoard, i, j, hihi[k], hihi[k]);
    }
    //The loop to calculate the number of consecutive moves in the second diagonal direction
    for(int k = 0; k < hihi.length; k++){
      arr[3] += numberInLine(intBoard, i, j, -hihi[k], hihi[k]);
    }
    //The loop to check the number of lines that reach the number of the consecutive moves
    for(int t = 0; t < arr.length; t++){
      if(arr[t] - 1 == target){
        count5++;
      }
    }
    return count5;
  }
  
  /**
   * The method to check whether the horizontal, vertical, and 2 diagonal directions are opened in front and end direction
   * @param intBoard The current integer board
   * @param i     The current row position
   * @param j     The current column position
   * @return Return true if 4 directions are opened and false if it is not
   */ 
  public boolean fourDirectionOpen(int[][] intBoard, int i, int j){
    boolean one;
    boolean two;
    boolean three;
    boolean four;
    int[] hihi = {-1, 1};
    one = isOpen(intBoard, i, j, 0, 1) && isOpen(intBoard, i, j, 0, -1);
    two = isOpen(intBoard, i, j, 1, 0) && isOpen(intBoard, i, j, -1, 0);
    three = isOpen(intBoard, i, j, 1, 1) && isOpen(intBoard, i, j, -1, -1);
    four = isOpen(intBoard, i, j, -1, 1) && isOpen(intBoard, i, j, 1, -1);
    return one && two && three && four;
  }
  
  /** 
   * The method to check the three-three rule
   * @param intBoard The current integer board
   * @param i     The current row position
   * @param j     The current column position
   * @param target The target number of moves for three-three rule
   * @return Return true if the three-three rule is violate and false if it is not
   */ 
  public boolean threeThree(int[][] intBoard, int i, int j, int target){
    int count = 0;
    if(intBoard[i][j] != noMove){
      if(fourDirectionCount(intBoard, i, j, target) > 1 && fourDirectionOpen(intBoard, i, j) == true){
        return true;
      }
      else{
        return false;
      }
    }
    return false;
  }
  
  /** 
   * The method to check the four-four rule
   * @param intBoard The current integer board
   * @param i     The current row position
   * @param j     The current column position
   * @param target The target number of moves for four-four rule
   * @return Return true if the three-three rule is violate and false if it is not
   */ 
  public boolean fourFour(int[][] intBoard, int i, int j, int target){
    int count = 0;
    if(intBoard[i][j] != noMove){
      if(fourDirectionCount(intBoard, i, j, target) > 1){
        return true;
      }
      else{
        return false;
      }
    }
    return false;
  }
  
  /**
   * The method to check one side meets the win condition
   * @param intBoard The current integer board
   * @param i     The current row position
   * @param j     The current column position
   * @param target The target number of moves to win
   * @return Return true if the number of consecutive moves of one side reach the target number and false if it is not
   */ 
  public boolean winCondition(int[][] intBoard, int i, int j, int target){
    if(intBoard[i][j] != noMove){
      if(fourDirectionCount(intBoard, i, j, target) == 1){
        return true;
      }
    }
    return false;
  }
  
  /**
   * The nested class represents the move
   */
  public class Move implements EventHandler<ActionEvent>{
    /**
     * The method to handle the move of black and white
     * @param e The action on the board
     */ 
    public void handle(ActionEvent e){
      //The nested loop to traverse every button on the board//
      for(int i = 0; i < board.length; i++){
        for(int j = 0; j < board[i].length; j++){
          //If the button got clicked and the square is empty//
          if(e.getSource() == board[i][j] && intBoard[i][j] == noMove){
            if(getParameters().getRaw().size() == 3 || getParameters().getRaw().size() == 1){
              //If turn is true, black move//
              if(turn.get()){
                //If the four-four and three-three rule are not violated//
                if(fourFour(intBoard, i, j, Integer.parseInt(getParameters().getRaw().get(0)) - 1) == false || threeThree(intBoard, i, j, Integer.parseInt(getParameters().getRaw().get(0)) - 2) == false){
                  board[i][j].setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)),
                                                          new BackgroundFill(Color.BLACK, new CornerRadii(40), new Insets(3))));
                  intBoard[i][j] = blackMove;
                  setTurnFalse();
                }
                //If the four-four and three-three rule are violated//
                if(fourFour(intBoard, i, j, Integer.parseInt(getParameters().getRaw().get(0)) - 1) == true || threeThree(intBoard, i, j, Integer.parseInt(getParameters().getRaw().get(0)) - 2) == true){
                  board[i][j].setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)), new BackgroundFill(null,null,null)));
                  intBoard[i][j] = noMove;
                  setTurnTrue();
                }
              }
              //If turn is false, white move//
              else if(!turn.get()){
                //If the four-four and three-three rule are not violated//
                if(fourFour(intBoard, i, j, Integer.parseInt(getParameters().getRaw().get(0)) - 1) == false || threeThree(intBoard, i, j, Integer.parseInt(getParameters().getRaw().get(0)) - 2) == false){
                  board[i][j].setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)),
                                                          new BackgroundFill(Color.WHITE, new CornerRadii(40), new Insets(3))));
                  intBoard[i][j] = whiteMove;
                  setTurnTrue();
                }
                //If the four-four and three-three rule are violated//
                if(fourFour(intBoard, i, j, Integer.parseInt(getParameters().getRaw().get(0)) - 1) == true || threeThree(intBoard, i, j, Integer.parseInt(getParameters().getRaw().get(0)) - 2) == true){
                  board[i][j].setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)), new BackgroundFill(null,null,null)));
                  intBoard[i][j] = noMove;
                  setTurnFalse();
                }
              }
              //If the win condition is met//
              if(winCondition(intBoard, i, j, Integer.parseInt(getParameters().getRaw().get(0))) == true){
                for(int k = 0; k < board.length; k++){
                  for(int t = 0; t < board[0].length; t++){
                    board[k][t].setOnAction(null);
                  }
                }
              }
            }
            //If the argument array length is not three or one//
            else{
              //If turn is true, black move//
              if(turn.get()){
                //If the four-four and three-three rule are not violated//
                if(fourFour(intBoard, i, j, 4) == false || threeThree(intBoard, i, j, 3) == false){
                  board[i][j].setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)),
                                                          new BackgroundFill(Color.BLACK, new CornerRadii(40), new Insets(3))));
                  intBoard[i][j] = blackMove;
                  turnNumber += 1;
                  box.getChildren().add(new Label(turnNumber + ". " + "White move!!"));
                  setTurnFalse();
                }
                //If the four-four and three-three rule are violated//
                if(fourFour(intBoard, i, j, 4) == true || threeThree(intBoard, i, j, 3) == true){
                  board[i][j].setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)), new BackgroundFill(null,null,null)));
                  intBoard[i][j] = noMove;
                  box.getChildren().add(new Label("Can't move!!"));
                  box.getChildren().add(new Label("Black move!!"));
                  setTurnTrue();
                }
              }
              //If turn is false, white move//
              else if(!turn.get()){
                //If the four-four and three-three rule are not violated//
                if(fourFour(intBoard, i, j, 4) == false || threeThree(intBoard, i, j, 3) == false){
                  board[i][j].setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)),
                                                          new BackgroundFill(Color.WHITE, new CornerRadii(40), new Insets(3))));
                  intBoard[i][j] = whiteMove;
                  turnNumber += 1;
                  box.getChildren().add(new Label(turnNumber + "." + "Black move!!"));
                  setTurnTrue();
                }
                //If the four-four and three-three rule are violated//
                if(fourFour(intBoard, i, j, 4) == true || threeThree(intBoard, i, j, 3) == true){
                  board[i][j].setBackground(new Background(new BackgroundFill(Color.PINK, new CornerRadii(0), new Insets(1)), new BackgroundFill(null,null,null)));
                  intBoard[i][j] = noMove;
                  box.getChildren().add(new Label("Can't move!!"));
                  box.getChildren().add(new Label("White move!!"));
                  setTurnFalse();
                }
              }
              //If win condition is met//
              if(winCondition(intBoard, i, j, 5) == true){
                if(intBoard[i][j] == 1){
                  box.getChildren().add(new Label("BLACK WIN"));
                }
                else if(intBoard[i][j] == -1){
                  box.getChildren().add(new Label("WHITE WIN"));
                }
                //The loop to deactivate every button on the board//
                for(int k = 0; k < board.length; k++){
                  for(int t = 0; t < board[0].length; t++){
                    board[k][t].setOnAction(null);
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  /**
   * The main method for class Gomoku
   * @param args String arguments array
   */ 
  public static void main(String[] args){
    int win = 5; //The standard winning condition
    int row = 19; //The standard row dimension
    int column = 19; //The standard column dimension
    try{
      switch (args.length) {
        case 0:
          break;
        case 1:
          win = Integer.parseInt(args[0]);
          break;
        case 2:
          row = Integer.parseInt(args[1]);
          column = Integer.parseInt(args[2]);
          break;
        case 3:
          win = Integer.parseInt(args[0]);
          row = Integer.parseInt(args[1]);
          column = Integer.parseInt(args[2]);
          break;
      }
      Application.launch(new String[]{Integer.toString(win), Integer.toString(row), Integer.toString(column)});
    }
    catch(Exception e){
      System.out.println("Please print only integer for the dimension and winning condition");
    }
  } 
}