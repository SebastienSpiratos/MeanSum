import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * The tile panel displays all the tiles (one per digit) of the game.
 *
 */
public class TilePanel extends JPanel  implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6467756282189564041L;
	/**
	 * The tile panel object holds a reference to the game model to
	 * request information to display (view) and to modify its state (controller)
	 */
	private GameModel gameModel;
	private ColorGenerator colorgen = new ColorGenerator();
	private Tile[] tiles=null;

	/**
	 * Constructor
	 * @param gameModel the model to handle
	 */
	public TilePanel(GameModel gameModel) {
		if (gameModel == null)
			throw new IllegalArgumentException("Should provide a valid instance of GameModel!");
		this.gameModel = gameModel;
		gameModel.addObserver(this);
		this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		initTiles();
	}
	
	/**
	 * Method that initializes the tiles and sets up their listeners
	 */
	private void initTiles(){
		// Basic example :
		// Remove tiles to make sure there are no left overs on init
		if(tiles != null)
			for (int i=0; i< tiles.length; ++i) {
				this.remove(tiles[i]);
			}
		// Empties the tiles list
		tiles = null;
		
		// Refills the tiles list
		tiles = new Tile[gameModel.numberOfTiles];
		for(int i=0; i< tiles.length ; ++i){
			// Creates the tile
			tiles[i] = new Tile(Character.getNumericValue(this.gameModel.numberSuite.charAt(i)), i+1);
			// Sets a layout and a border for the tile
			tiles[i].setLayout(new BorderLayout());
			tiles[i].setBorder(BorderFactory.createLineBorder(Color.black));
			// Adds JLabel to contain the number
			JLabel label = new JLabel(Integer.toString(tiles[i].getValue()));
			// Sets font for the number
			label.setFont(new Font("Bold", Font.BOLD, 85));
			// Adds a white background and sets it to opaque for the JLabel
			label.setBackground(Color.WHITE);
			label.setOpaque(true);
			// Sets a BorderLayout to the tile
			tiles[i].add(label, BorderLayout.CENTER);
			this.add(tiles[i]);
			// ********************IMPORTANT*******************************
			// ********DOES NOT UPDATE THE VIEW IN THE MOUSE LISTENER******
			// This code, part of the 'controller', must only update
			// the state of gameModel, which will notify TilePanel than
			// its state has been changed by calling its method "update". 
			// So, the view must be updated from the method "update". 
			tiles[i].addMouseListener(new MouseAdapter(){				
				/**
				 * Method that is called on mouseClick
				 */
				public void mouseClicked(MouseEvent e) {
					
					// Gets the tile that was clicked
					Tile tile = (Tile)e.getSource();
					// Makes sure this tile was not clicked before
					if(!tile.getClicked()) {
						// Updates the clicked tile counter
						++gameModel.clickedCounter;
						// Sets tile to clicked 
						tile.setClicked();
					}	
					// Remove this tile from its previous group 
					removeTileFromPreviousGroup(tile);
				
					// Add the tile to its new group
					gameModel.userGroups.add(new Group(tile));
					// Calls function to notify observers
					gameModel.sumChanged();
					// Checks if game is over
					checkIfGameOver();
									
					// If game isn't won or lost
					if(!gameModel.gameOver){
						// Sets color for the group
						Component tileColor = new JLabel();
						tileColor = tile.getComponentAt(1,1);
						tileColor.setBackground(colorgen.getCurrent());
						changeColour();
					}
				}
				// Code for group selection
				// Required variables
				private Component tileColor2 = new JLabel();
				boolean mouseEnteredNextTile = false;
				// Mouse pressed boolean
				boolean mouseDown = false;
				
				// Sets boolean to true if mouse is being pressed
				public void mousePressed(MouseEvent e) {
					mouseDown = true;
				}
				// Sets boolean to false if mouse is being pressed
				public void mouseReleased(MouseEvent e) {
					mouseDown = false;
				}
				// Selects the tile that was entered and
				// keeps it in a variable along with the JLabel
				// that contains its color
				public void mouseEntered(MouseEvent e) {
					Tile tile1 = (Tile) e.getSource();
					tileColor2 = tile1.findComponentAt(1,1);
				}
				
				// Selects the next valid tile when the mouse leaves
				// the first clicked tile if the mouse is held down
				public void mouseExited(MouseEvent e) {
					// Will be used to keep the initial tile's position
					int tilePosition;
					// Sets boolean to true since the mouse exited the first tile
					mouseEnteredNextTile = true;
					// Initialize the label that will contain the second tile's color
					Component tileColor = new JLabel();
					// Get the tile
					Tile tile1 = (Tile) e.getSource();
					// Initialize a new tile to contain the second tile
					Tile tile2 = new Tile();
					// Checks if mouse is being held down and if mouse exited the first tile 
					if(mouseDown && mouseEnteredNextTile) {
						// Gets the first tile's position
						tilePosition = tile1.getPosition();
						// Increments the first tile's position by one
						tilePosition += 1;
						// Searches the tile at that incremented position
						for (int i = 0; i < tilePosition; ++i) {
							tile2 = tiles[i];
						}
						// Notifies the game model that the sum changed
						gameModel.sumChanged();
						// Sets the first tile as clicked if it isn't
						if(!tile1.getClicked()) {
							tile1.setClicked();
							// Increment the counter
							++gameModel.clickedCounter;
						}
						// Remove this tile from its previous group 
						removeTileFromPreviousGroup(tile1);
						
						// Sets the second tile as clicked if it isn't
						if(!tile2.getClicked()) {
							tile2.setClicked();
							// Increment the counter
							++gameModel.clickedCounter;
						}
						// Remove this tile from its previous group 
						removeTileFromPreviousGroup(tile2);
						// Add the tiles to their new group
						gameModel.userGroups.add(new Group(tile1, tile2));
						// Notifies the game model that the sum changed
						gameModel.sumChanged();
						// Sets the color for both tiles
						tileColor = tile2.findComponentAt(1,1);
						tileColor2.setBackground(colorgen.getCurrent());
						tileColor.setBackground(colorgen.getCurrent());
						colorgen.next();
						// Checks if game is over
						checkIfGameOver();
					}
				}

			});
		}
	}
	
	/**
	 * Changes the active colour
	 */
	public void changeColour() {
		colorgen.next();
	}
	
	/**
	 * Removes a tile from its previous group.
	 * @param tile The tile to remove.
	 */
	public void removeTileFromPreviousGroup(Tile tile) {
		for(int i = 0; i < gameModel.userGroups.size(); ++i) {
			if(gameModel.userGroups.get(i).contains(tile)) {
				gameModel.userGroups.get(i).removeTile(tile);
			}
		}	
	}
	
	/**
	 * Method that checks if the game is over. It modifies gameModel attributes
	 * accordingly. It is called each time the sum is changed.
	 */
	public void checkIfGameOver() {
		Tile tile = new Tile();
		// Checks if the number of clicked tiles is equal to the total number of tiles
		// for victory conditions
		if(gameModel.clickedCounter == gameModel.numberOfTiles) {
			// Game is won or lost
			gameModel.gameOver = true;
			for(int i=0; i< tiles.length ; ++i){
				tile = tiles[i];
				// Gets each tile
				Component label = new JLabel();
				label = tile.getComponentAt(1,1);
				// Checks if total sum is equal to goal and sets either green or red
				// as background color
				if(gameModel.goal == gameModel.currentSum) 
					label.setBackground(Color.green);
				else 
					label.setBackground(Color.red);
			}
			// Increment level on win
			if(gameModel.goal == gameModel.currentSum) {
				if(gameModel.arcadeEnabled && gameModel.level < 20)
					++gameModel.level;
				gameModel.gameWon = true;
			}
		}
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
		if(code==GameModel.MessCode.RESET){
			// The game has been reset. Use information of the gameModel 
			// to update the TilePanel accordingly
			// Iterate through the whole tiles list
			for(int i=0; i< tiles.length ; ++i){
				// Set each tile to not clicked and set background to white
				Tile tile = tiles[i];
				tile.setNotClicked();
				Component label = new JLabel();
				label = tile.getComponentAt(1,1);
				label.setBackground(Color.white);
			}
		}
	}
}
