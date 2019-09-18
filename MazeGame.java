import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
/**
 * @author Eloisa Perez-Bennetts :)
 * @date April, 2017
 */
public class MazeGame {
	/* You can put variables that you need throughout the class up here.
     * You MUST INITIALISE ALL of these class variables in your initialiseGame
     * method.
     */

	static int lives, steps, gold, rows, columns, exitX, exitY, a, b;
	static char[][] board; // rows, columns 
	static File doc;
	static Scanner scan;


	public static void main (String[] args){
		if (args.length < 1){
			System.out.println("Error: Too few arguments given. Expected 1 argument, found 0.");
			System.out.println("Usage: MazeGame [<game configuration file>|DEFAULT]");
			return;
		} 
		else if (args.length > 1){
			System.out.println("Error: Too many arguments given. Expected 1 argument, found " + args.length + ".");
			System.out.println("Usage: MazeGame [<game configuration file>|DEFAULT]");
			return;
		}
		else {		
			try {
				initialiseGame(args[0]);						
			} catch(IOException e) {
				System.out.printf("Error: Could not load the game configuration from '%s'.\n", args[0]);
				return;
			}
			Scanner keyboard = new Scanner(System.in);	
			while(!isGameEnd()){
				if (!keyboard.hasNextLine()) {
					System.out.println("You did not complete the game.");
					return;
				}
				String input = keyboard.nextLine();
				try {
					performAction(input);
				} catch (IllegalArgumentException e) {
					System.out.println("Error: Could not find command '" + input + "'.");
					System.out.println("To find the list of valid commands, please type 'help'.");
				}
			}
		}
	}

	/**
     * Initialises the game from the given configuration file.
     * This includes the number of lives, the number of steps, the starting gold
     * and the board.
     *
     * If the configuration file name is "DEFAULT", load the default
     * game configuration.
     *
     * NOTE: Please also initialise all of your class variables.
     *
     * @args configFileName The name of the game configuration file to read from.
     * @throws IOException If there was an error reading in the game.
     *         For example, if the input file could not be found.
     */

	public static void initialiseGame(String configFileName) throws IOException {		
		if (configFileName.equals("DEFAULT")){

			lives = 3;
			steps = 20;
			gold = 0;
			rows = 4;
			columns = 10;

			board = new char[][]{
				{'#','@',' ','#','#',' ',' ','&','4','#'},
				{'#','#',' ',' ','#',' ','#','#',' ','#'},
				{'#','#','#',' ',' ','3','#',' ',' ',' '},
				{'#','#','#','#','#','#','#',' ',' ','#'}
			};			
		} else {			
			doc = new File (configFileName);
			scan = new Scanner(doc);

			lives = scan.nextInt();
			steps = scan.nextInt();
			gold = scan.nextInt();
			rows = scan.nextInt();
			
			int maxLength = 0;				
			for (int i = 0; i < (rows + 1); i++){
				String row = scan.nextLine();
				int length = row.length();

				if(length > maxLength){
					maxLength = length;
				}
			}
			columns = maxLength;

			board = new char[rows][columns];

			scan.close();
			scan = new Scanner(doc);
			scan.nextLine();  // Getting rid of the 1st line.
			for(int i = 0; i < rows; i++){
				String line = scan.nextLine();
				//System.out.println(line);
				for(int j = 0; j < columns; j++){
					if (j >= line.length()) {				//Why doesn't this still work??		
						board[i][j] = ' ';
					} else {
						board[i][j] = line.charAt(j);						
					}
				} 				
			} 
			
			for(int y = 0; y < rows; y++){				
				for(int x = 0; x < columns; x++){
					if(board[y][x] == '@'){
						exitX = x;
						exitY = y;
					} //if there isn't an @, will return default value (0)
				} 
			}
			
			a = getCurrentXPosition();
			b = getCurrentYPosition();
		}
	}

	/**
     * Save the current board to the given file name.
     * Note: save it in the same format as you read it in.
     * That is:
     *
     * <number of lives> <number of steps> <amount of gold> <number of rows on the board>
     * <BOARD>
     *
     * @args toFileName The name of the file to save the game configuration to.
     * @throws IOException If there was an error writing the game to the file.
     */
	public static void saveGame(String toFileName) throws IOException {

		File write = new File (toFileName);			
		PrintWriter PW = new PrintWriter(write);

		PW.println(lives + " " + steps + " " + gold + " " + rows);
		for(int i = 0; i < rows; i++){			
			for(int j = 0; j < columns; j++){
				PW.printf("%c", board[i][j]); // row, column (y, x)
			} PW.println("");
		}
		PW.close();
		System.out.printf("Successfully saved the current game configuration to '%s'.\n", toFileName);
	}

	public static int getCurrentXPosition() {
		int blah = 0;
		for(int y = 0; y < rows; y++){				
			for(int x = 0; x < columns; x++){
				if(board[y][x] == '&'){
					blah = x;					
				} //if there isn't an &, will return default value (0)
			} 
		}
		return blah;	
	}

	public static int getCurrentYPosition() {
		int blah = 0;
		for(int y = 0; y < rows; y++){				
			for(int x = 0; x < columns; x++){
				if(board[y][x] == '&'){
					blah = y;					
				} //if there isn't an &, will return default value (0)
			} 
		}
		return blah;	
	}

	public static int numberOfLives() {
		return lives;
	}

	public static int numberOfStepsRemaining() {
		return steps;
	}

	public static int amountOfGold() {
		return gold;
	}

	public static int exitXPosition() {	
		int blah = 0;
		for(int y = 0; y < rows; y++){				
			for(int x = 0; x < columns; x++){
				if(board[y][x] == '@'){
					blah = x;					
				} //if there isn't an @, will return default value (0)
			} 
		}
		return blah;	
	}
	
	public static int exitYPosition() {	
		int blah = 0;
		for(int y = 0; y < rows; y++){				
			for(int x = 0; x < columns; x++){
				if(board[y][x] == '@'){
					blah = y;					
				} //if there isn't an @, will return default value (0)
			} 
		}
		return blah;	
	}
	
	/**
     * Checks to see if the player has completed the maze.
     * The player has completed the maze if they have reached the destination.
     *
     * @return True if the player has completed the maze.
     */
	public static boolean isMazeCompleted() {
		if ((a == exitX) && (b == exitY)) {
			//System.out.println("Frick yeah finally!");
			return true;
		} else {
			return false;	
		}
	}

	/**
     * Checks to see if it is the end of the game.
     * It is the end of the game if one of the following conditions is true:
     *  - There are no remaining steps.
     *  - The player has no lives.
     *  - The player has completed the maze.
     *
     * @return True if any one of the conditions that end the game is true.
     */
	public static boolean isGameEnd() {
		if (isMazeCompleted()) {
			System.out.println("Congratulations! You completed the maze!");
			System.out.println("Your final status is:");
			System.out.println("Number of live(s): " + lives);
			System.out.println("Number of step(s) remaining: " + steps);
			System.out.println("Amount of gold: " + gold);		
			return true;		
		} else if (lives <= 0 && steps <= 0){
			System.out.println("Oh no! You have no lives and no steps left.");
			System.out.println("Better luck next time!");			
			return true;
		} else if (lives <= 0) {
			System.out.println("Oh no! You have no lives left.");
			System.out.println("Better luck next time!");
			return true;
		} else if (steps <= 0) {
			System.out.println("Oh no! You have no steps left.");
			System.out.println("Better luck next time!");
			return true;
		} else {
			return false;
		}        
	}

	/**
     * Checks if the coordinates (x, y) are valid.
     * That is, if they are on the board.
     *
     * @args x The x coordinate.
     * @args y The y coordinate.
     * @return True if the given coordinates are valid (on the board),
     *         otherwise, false (the coordinates are out or range).
     */
	public static boolean isValidCoordinates(int x, int y) {        
		if (x < columns && x >= 0 && y < rows && y >= 0){
			return true;
		} else {
			return false;
		}
	}


	/**
     * Checks if a move to the given coordinates is valid.
     * A move is invalid if:
     *  - It is move to a coordinate off the board.
     *  - There is a wall at that coordinate.
     *  - The game is ended.
     *
     * @args x The x coordinate to move to.
     * @args y The y coordinate to move to.
     * @return True if the move is valid, otherwise false.
     */
	public static boolean canMoveTo(int x, int y) {
		if (isMazeCompleted()){
			return true;
		}
		else if(isValidCoordinates(x, y) && !isGameEnd() && board[y][x] != '#'){
			return true;
		} 
		else {
			System.out.println("Invalid move. One life lost.");
			lives --;
			return false;
		}

	} 

	/**
     * Move the player to the given coordinates on the board.
     * After a successful move, it prints "Moved to (x, y)."
     * where (x, y) were the coordinates given.
     *
     * If there was gold at the position the player moved to,
     * the gold should be collected and the message "Plus n gold."
     * should also be printed, where n is the amount of gold collected.
     *
     * If it is an invalid move, a life is lost.
     * The method prints: "Invalid move. One life lost."
     *
     * @args x The x coordinate to move to.
     * @args y The y coordinate to move to.
     */
	public static void moveTo(int x, int y) {       
		steps--;
		a = x;
		b = y;
		if (canMoveTo(x, y)){
			int x1 = getCurrentXPosition();
			int y1 = getCurrentYPosition();

			System.out.printf("Moved to (%d, %d).\n", x, y);
			if ("0123456789".indexOf(board[y][x]) != -1){
				int plusGold = (int)(board[y][x] - '0'); 
				gold += plusGold;
				System.out.printf("Plus %d gold.\n", plusGold);
			}
			//if (board[y][x] != board[exitYPosition()][exitXPosition()]){
				board[y1][x1] = '.';
				board[y][x] = '&';
			//}			
		}
	}

	public static void printHelp() {
		System.out.println("Usage: You can type one of the following commands.");
		System.out.println("help         Print this help message.");
		System.out.println("board        Print the current board.");
		System.out.println("status       Print the current status.");
		System.out.println("left         Move the player 1 square to the left.");
		System.out.println("right        Move the player 1 square to the right.");
		System.out.println("up           Move the player 1 square up.");
		System.out.println("down         Move the player 1 square down.");
		System.out.println("save <file>  Save the current game configuration to the given file.");
	}

	public static void printStatus() {
		System.out.println("Number of live(s): " + lives);
		System.out.println("Number of step(s) remaining: " + steps);
		System.out.println("Amount of gold: " + gold);
	}

	public static void printBoard() {
		for(int i = 0; i < rows; i++){			
			for(int j = 0; j < columns; j++){
				System.out.printf("%c", board[i][j]); // row, column (y, x)
			} System.out.println("");
		}
	}

	/**
     * Performs the given action by calling the appropriate helper methods.
     * [For example, calling the printHelp() method if the action is "help".]
     *
     * The valid actions are "help", "board", "status", "left", "right",
     * "up", "down", and "save".
     * [Note: The actions are case insensitive.]
     * If it is not a valid action, an IllegalArgumentException should be thrown.
     *
     * @args action The action we are performing.
     * @throws IllegalArgumentException If the action given isn't one of the
     *         allowed actions.
     */
	public static void performAction(String action) throws IllegalArgumentException {

		String upperAction = action.toUpperCase();

		if (upperAction.equals("HELP")){
			printHelp();
		} else if (upperAction.equals("STATUS")){
			printStatus();
		} else if (upperAction.equals("BOARD")){
			printBoard();
		} else if (upperAction.equals("LEFT")){
			moveTo((getCurrentXPosition()-1), getCurrentYPosition());					
		} else if (upperAction.equals("RIGHT")){
			moveTo((getCurrentXPosition()+1), getCurrentYPosition());
		} else if (upperAction.equals("UP")){
			moveTo((getCurrentXPosition()), getCurrentYPosition()-1);
		} else if (upperAction.equals("DOWN")){
			moveTo((getCurrentXPosition()), getCurrentYPosition()+1);
		} else if (upperAction.split(" ")[0].equals("SAVE") && upperAction.split(" ").length == 2){ 
			try {
				saveGame(action.split(" ")[1]); 
			} catch (IOException e) {
				System.out.println("Error: Could not save the current game configuration to '" + action.split(" ")[1] + "'.");
			}
		} else {
			throw new IllegalArgumentException();
		}
	}	
/*	
	public static void testValid(int x, int y) {
		System.out.println(isValidCoordinates(x, y));
	} 
*/	/**
     * The main method of your program.
     *
     * @args args[0] The game configuration file from which to initialise the
     *       maze game. If it is DEFAULT, load the default configuration.
     */
	// public static void main(String[] args) {
	// Run your program (reading in from args etc) from here.
	//}

}
