import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * This class represents a tile that is shown in the TilePanel
 * of the game. It contains a number and a position that defines where
 * it is located in the TilePanel.
 */
public class Tile extends JPanel{
	private static final int HEIGHT = 300;
	/**
	 * Properties
	 */
	private static final long serialVersionUID = -9220911530985968430L;
	private Color mColor=Color.WHITE;
	private int value;
	private boolean isClicked;
	private int position;

	/**
	 * Default empty constructor
	 */
	public Tile() {
		this.value = 0;
		this.position = 0;
		this.isClicked = false;
	}
	
	/**
	 * Constructor with parameters
	 * @param value The number that will be contained in the tile
	 * @param position The position of the tile on the TilePanel
	 */
	public Tile(int value, int position) {
		this.value = value;
		this.position = position;
		this.isClicked = false;
	}
	
	/**
	 * Sets the position of a tile
	 * @param position The position to set to the tile
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Gets the current position of a tile
	 * @return The current position of the tile
	 */
	public int getPosition() {
		return this.position;
	}
	
	/**
	 * Sets the tile as clicked
	 */
	public void setClicked() {
		this.isClicked = true;
	}
	
	/**
	 * Sets the tile as unclicked
	 */
	public void setNotClicked() {
		this.isClicked = false;
	}
	
	/**
	 * Method that returns whether the tile is clicked or not
	 * @return A boolean that specifies if tile is clicked or not
	 */
	public boolean getClicked(){
		return this.isClicked;
	} 
	
	/**
	 * Sets the value of a tile
	 * @param value The value to add to the tile
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Gets the value of a tile
	 * @return The value of the tile
	 */
	public int getValue() {
		return this.value;
	}
	/**
	 * Paints the tile on the GUI
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(mColor);
		g.fillRect(0, 0, this.getWidth(), HEIGHT);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.getWidth(), HEIGHT);
	}
}
