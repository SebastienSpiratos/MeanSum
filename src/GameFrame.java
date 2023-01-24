import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * The game frame is the main window of the game. It instantiates the model
 * and the view-controller. The frame is filled by a single panel containing
 * all the elements, which is the {@link GameViewController} object.
 *
 */
public class GameFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2404129435273640727L;
	/**
	 * Handles both the graphical interface (tiles, labels, buttons, etc.)
	 * and the user input (mouse events, button clicks, etc.)
	 */
	private GameViewController gameViewController;
	
	
	/**
	 * The constructor instantiates the model and view-controller
	 * and builds the game window.
	 */
	public GameFrame() {		
		// Initialize the UI
		initUI();
		
		// Initialize the view and set it as the main component our window
		gameViewController = new GameViewController();
		setContentPane(gameViewController);
	}
	
	/**
	 * Initializes the main properties of the game window
	 */
	public void initUI() {
		setTitle("Mean Sum");
		setSize(800, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GameFrame game = new GameFrame();
				game.setVisible(true);
			}
		});
	}
}
