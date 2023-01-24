import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ThreadLocalRandom;
 
/**
 * The game model handles the logic of the game (generating the numbers, etc.).
 * The instance of the model is used by the view-controller module
 * to trigger actions (for example, generate a new game) and retrieve information
 * about the current status of the game (the digits, the goal, etc.).
 *
 */
public class GameModel extends Observable {
	
	// Type of modification of the GameModel
	public static enum MessCode{RESET,USERGROUP, NEW, SUMCHANGED, ARCADE, RESTART}; 
	public int numberCount;
	public int goal;
	public ArrayList<Integer> numbers = new ArrayList<Integer>();
	public ArrayList<Group> userGroups = new ArrayList<Group>();
	public String numberSuite;
	public int numberOfTiles;
	public int clickedCounter;
	public int currentSum;
	// Set arcade to disabled by default
	public boolean arcadeEnabled = false;
	// Starts at level 1 by default
	public int level = 1;
	public boolean gameOver;
	public boolean gameWon;
	public int resetCounter;
	private double doubleDigitProba;
		
	/**
	 * Constructor for the GameModel
	 */
	public GameModel() {
		// Calls the method that initializes all the components of the game model
		initComponents();
	}
	/**
	 * Create a new game
	 */
	public void nextGame(){
		if(!this.arcadeEnabled) {
			initComponents();
			// The code below alerts observers than the model has been modified
			this.setChanged();
			this.notifyObservers(MessCode.NEW);
		}
		else if(this.arcadeEnabled && this.gameWon) {
			initComponents();
			// The code below alerts observers than the model has been modified
			this.setChanged();
			this.notifyObservers(MessCode.NEW);
		}
		
	}
	
	/**
	 * Reset the groups of user
	 */
	public void resetGame(){
		if(!this.gameOver) {
			// The code below alerts observers than the model has been modified
			++this.resetCounter;
			this.setChanged();
			this.notifyObservers(MessCode.RESET);
		}
	}
	
	/**
	 * Enables or disables arcade mode
	 */
	public void arcadeGame() {
		initComponents();
		
		// The code below alerts observers than the model has been modified
		this.setChanged();
		this.notifyObservers(MessCode.ARCADE);
		
	}
	
	/**
	 * Restarts the game in arcade mode
	 */
	public void restartGame() {
		initComponents();
		
		// The code below alerts observers than the model has been modified
		this.setChanged();
		this.notifyObservers(MessCode.RESTART);
		
	}
	
	/**
	 * Removes empty groups from the userGroups list.
	 */
	public void removeEmptyGroups() {
		for(int i = 0; i < userGroups.size(); ++i) {
			if(userGroups.get(i).isEmpty()) {
				userGroups.remove(i);
			}
		}
	}
	
	/**
	 * Notifies current sum has changed
	 */
	public void sumChanged(){
		// Remove empty groups from the user groups list
		removeEmptyGroups();
		
		// Initialize sum variable at 0
		int userGroupsSum = 0;
		
		// Add the sum for each group to total sum
		for(int i = 0; i < userGroups.size(); ++i) {
			userGroupsSum += userGroups.get(i).getSum();
		}
		// Set total sum to GameModel variable
		this.currentSum = userGroupsSum;
		
		// Check if the game is over
		if(this.clickedCounter == this.numberOfTiles)
			this.gameOver = true;
		// Check if the game is won
		if(this.currentSum == this.goal)
			this.gameWon = true;
		// The code below alerts observers than the model has been modified
		this.setChanged();
		this.notifyObservers(MessCode.SUMCHANGED);
	}
	
	/**
	 * Method that initializes all the components of the game model
	 */
	public void initComponents() {
		this.doubleDigitProba = 0.3 + (0.3 * level / 20);
		this.userGroups.clear();
		this.gameOver = false;
		this.gameWon = false;
		this.clickedCounter = 0;
		this.currentSum = 0;
		this.resetCounter = 0;
		int total = 0;
		this.numbers.clear();
		this.numberSuite = "";
		// Generate a random number between 3 and 6
		this.numberCount = ((!this.arcadeEnabled) ? ThreadLocalRandom.current().nextInt(3, 6 + 1) : 
			(int) ( 3 + Math.round(3.0 * level / 20)));
		// For each created group, either a number between 1 and 9 or 10 and 99
		for(int i=0; i< numberCount; ++i) {
			if(!this.arcadeEnabled) {
				// Training mode number generation
				if(ThreadLocalRandom.current().nextInt(1, 10 + 1) <= 7) {
					numbers.add(ThreadLocalRandom.current().nextInt(1, 9 + 1));
				}
				else {
					numbers.add(ThreadLocalRandom.current().nextInt(10, 99 + 1));
				}
			}
			else {
				// Arcade mode number generation
				if(ThreadLocalRandom.current().nextDouble() > this.doubleDigitProba)
					numbers.add(ThreadLocalRandom.current().nextInt(1, 9 + 1));
				else
					numbers.add(ThreadLocalRandom.current().nextInt(10, 99 + 1));
			}
			// Adds the numbers to the total to set the goal
			total += numbers.get(i);
		}
	

		this.goal = total;
		// String builder to create a number suite to insert into the tiles
		StringBuilder sb = new StringBuilder(numbers.size());
		for (int i : numbers) {
		  if(i != 0)
			  sb.append(i);
		}
		this.numberSuite = sb.toString(); 
		
		// Console logging
		this.numberOfTiles = numberSuite.length(); 
		System.out.println("Goal: " + goal);
		System.out.println("Number of groups: " + numberCount);
		System.out.println("Double digit probability: "+ this.doubleDigitProba);
		System.out.println("Numbers: " + numbers.toString());
		System.out.println("Number suite: " + numberSuite);
		System.out.println("Number of tiles: "+ numberOfTiles);
	}	
}
