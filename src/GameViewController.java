import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The view-controller class handles the display (tiles, buttons, etc.)
 * and the user input (actions from selections, clicks, etc.).
 *
 */
public class GameViewController extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6373313186026779282L;

	/**
	 * Instance of the game (logic, state, etc.)
	 */
	private GameModel gameModel;

	/**
	 * A single tile panel displays all the tiles of the game
	 */
	private TilePanel tilePanel;
	private JPanel bottomPanel;
	private JPanel modePanel;
	private JLabel currentSumLabel;
	// Initialize timer attributes
	private Timer timer;
	private int counter;
	private JLabel timerLabel;
	private double minutes;
	private double seconds;
	private String formattedMinutes;
	private String formattedSeconds;
	private String formattedTime;
	// Initialize reset counter attributes
	private JLabel resetCounterNumber;
	// Arcade Labels
	private JLabel currentMode;
	private JLabel level;
	
	/**
	 * Initializes game model with starting values for attributes
	 */
	private void initGameModel() {
		// Set static counters to 0
		currentMode = new JLabel((gameModel.arcadeEnabled) ? "Mode: Arcade" : "Mode: Training");
		gameModel.level = gameModel.level;
		level = new JLabel("Level: "+Integer.toString(gameModel.level));
		gameModel.clickedCounter = 0;
		gameModel.currentSum = 0;
		this.counter = 0;
		// Create and add panels
		tilePanel = new TilePanel(gameModel);
		this.add(tilePanel);		
		bottomPanel = new JPanel();
		this.add(bottomPanel);
		modePanel = new JPanel();
		this.add(modePanel);
		JPanel buttonPanel = new JPanel();
		JPanel infoPanel  = new JPanel();
		bottomPanel.add(buttonPanel);
		bottomPanel.add(infoPanel);
		
		
		// Adding all the other required UI components (labels, buttons, etc.)
		int currentGoal = this.gameModel.goal;
		JLabel goalLabel = new JLabel("Goal: " + currentGoal);
		currentSumLabel = new JLabel("Current sum: " + gameModel.currentSum);
		JButton nextButton = new JButton("NEXT");
		JButton resetButton = new JButton("RESET");
		JButton arcadeButton = new JButton((gameModel.arcadeEnabled) ? "TRAINING" : "ARCADE");
		JButton restartButton = new JButton("RESTART");
		// Timer component
		timerLabel = new JLabel("Time elapsed: 00:00");
		// Reset counter
		resetCounterNumber = new JLabel("Resets: "+ gameModel.resetCounter);
		// Instantiate timer and assign ActionListener to it
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Stores minutes and seconds in a variable
				minutes = Math.floor(counter / 60F);
				seconds = Math.floor(counter - minutes * 60);
				// Formats minutes and seconds properly for each possible case
				formattedMinutes = ((int)minutes >= 10) ? String.valueOf((int)minutes) : "0"+String.valueOf((int)minutes);
				formattedSeconds = ((int)seconds >= 10) ? String.valueOf((int)seconds) : "0"+String.valueOf((int)seconds);
				// Formats timer numbers
				formattedTime = formattedMinutes+":"+formattedSeconds;
				// Sets label text to timer
				timerLabel.setText("Time elapsed: "+formattedTime);
				// Only increment timer if game is not over
				if (!gameModel.gameOver)
					counter++;
			}
			
		});
		
		// Set up the listeners for the buttons
		setupListeners(nextButton, resetButton, arcadeButton, restartButton, gameModel);
		
		// Add components to the main panels
		// Goal labels
		infoPanel.add(goalLabel);
		infoPanel.add(currentSumLabel);
		// Current mode
		modePanel.add(currentMode);
		// Buttons
		buttonPanel.add(resetButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(arcadeButton);
		// Add restart button only if in arcade mode
		if(gameModel.arcadeEnabled)
			buttonPanel.add(restartButton);
		// Timer label
		bottomPanel.add(timerLabel);
		// Reset counter
		bottomPanel.add(resetCounterNumber);
		// Add level label only if in arcade mode
		if(gameModel.arcadeEnabled)
			bottomPanel.add(level);
		// Start the timer
		timer.start();
	}
	
	/**
	 * Constructor for the GameViewController
	 */
	public GameViewController() {
		gameModel = new GameModel();
		gameModel.addObserver(this);
		// The layout defines how components are displayed
		// (here, stacked along the Y axis)
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Initialize the game model
		initGameModel();
		
	}
	
	/**
	 * Method that sets up listeners for the buttons
	 * @param nextButton The next game button
	 * @param resetButton The reset game button
	 * @param gameModel
	 */
	private void setupListeners(JButton nextButton, JButton resetButton, JButton arcadeButton, JButton restartButton, 
			GameModel gameModel) {		
		// TODO Set up the required listeners on the UI components (button clicks, etc.)
		
		// Add action listener to the next button
		nextButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				gameModel.nextGame();
			}
		});
		
		// Add action listener to the reset button
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				gameModel.resetGame();
				resetCounterNumber.setText("Resets: "+Integer.toString(gameModel.resetCounter));
			}
		});
		
		// Add action listener to the arcade button
		arcadeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				gameModel.arcadeGame();
			}
		});
		
		// Add action listener to the restart button
		restartButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				gameModel.restartGame();
			}
		});
	}
	

	/** 
	 * This method is called when the observer (GameModel)
	 * calls setChanged() and notifyObservers().
	 * USE THIS METHOD TO UPDATE THE VIEW IN ACCORDANCE TO THE MODEL STATE. 
	 * */
	@Override
	public void update(Observable o, Object arg) {
		if(!(arg instanceof GameModel.MessCode))
			return;
		GameModel.MessCode code = (GameModel.MessCode) arg;
		if(code==GameModel.MessCode.NEW || code==GameModel.MessCode.ARCADE || 
				code ==GameModel.MessCode.RESTART) {
			if(code==GameModel.MessCode.ARCADE)
				// Sets or unsets arcade mode
				gameModel.arcadeEnabled = !gameModel.arcadeEnabled;
			if(code==GameModel.MessCode.RESTART)
				gameModel.level = 1;
			// Reset all the elements of the game
			this.remove(tilePanel);
			this.remove(bottomPanel);
			this.remove(modePanel);
			this.timer.stop();
			this.initGameModel();
			// Update the view
			this.repaint();
			this.revalidate();
		}
		if(code==GameModel.MessCode.RESET){
			// The game has been reset. Update information of the game view
			// in accordance to the gameModel
			gameModel.userGroups.clear();
			gameModel.clickedCounter = 0;
			gameModel.sumChanged();
			this.currentSumLabel.setText("Current sum: "+Integer.toString(gameModel.currentSum));

		}
		else if(code==GameModel.MessCode.SUMCHANGED){
			// The current sum has changed, Update the view accordingly.
			this.currentSumLabel.setText("Current sum: "+Integer.toString(gameModel.currentSum));
		}
	}
	
}
