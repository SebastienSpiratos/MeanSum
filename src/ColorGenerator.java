import java.awt.Color;

/**
 * This class allows to iterate on a table of colors
 *
 */
public class ColorGenerator {
	/**
	 * A table of colors that can be used to draw the tiles
	 */
	private Color[] colors;
	
	// NOTE: this attribute, the method associated with it, and the code related to it
	// are for demonstration purposes only. YOU SHOULD REMOVE THESE FROM YOUR APPLICATION.
	private int currentIndex = 0;
	private Color current=null;
	
	/**
	 * Constructor
	 */
	public ColorGenerator(){
		initializeColours();
	}
	
	/**
	 * Reset the color index to 0
	 */
	public void reset(){
		currentIndex = 0;
	}
	
	private void initializeColours() {
		// Some tile colours in the '0xRRGGBB' format
		String[] tileColourCodes = new String[] {
				"0x89CF0", "0xF4C2C2", "0xFFBF00", "0xFBCEB1",
    			"0x6495ED", "0x9BDDFF", "0xFBEC5D",	"0xFF7F50",
    			"0x00FFFF", "0x98777B", "0x99BADD", "0x654321"
    			};
		
		// Allocate and fill our colour array with the colour codes
		colors = new Color[tileColourCodes.length];
		for (int i = 0; i < colors.length; ++i)
			colors[i] = Color.decode(tileColourCodes[i]);
		current = colors[0];
	}
   
	/**
	 * 
	 * @return the current color
	 */
	public Color getCurrent(){
		return current;
	}
	
	/**
	 * 
	 * @return the current color index
	 */
	public int getCurrentIndex(){
		return currentIndex;
	}
	
	/**
	 * 
	 * changes the next generated color
	 */
	public void next(){
		currentIndex++;
		if(currentIndex>=colors.length){
			current = new Color((float)Math.random(),
					(float)Math.random(),(float)Math.random());
			
		}else{
			current = colors[currentIndex];
		}		
	}
}
