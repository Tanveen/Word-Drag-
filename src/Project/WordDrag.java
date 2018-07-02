/**
 * 
 */
/** @Description Main Class 
 * @author Anita George & Tanveen Kaur
 *
 */
package Project;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class WordDrag extends JFrame
{
	private static final long serialVersionUID = 1L; // eliminating warning for the IDE to configure both the classes at the same workspace
	private BufferedReader br;
	private FileReader fr;
	private List<String> words;
	private Random random;
	private JMenuBar menuBar;
	private JMenu fileM;
	private JMenuItem addWordMI;
	private int randomx, randomy, width, height;
	private BoxWord boxWord;
	private final int X_MAX = 600;
	private final int Y_MAX = 400;
	private final int X_MIN = 20;
	private final int Y_MIN = 20;
	private Rectangle rectangle;
	private static HashMap<String, Rectangle> positionList;
	
	/**
	 * Constructor to initialize the frame
	 */
	public WordDrag() 
	{
		positionList = new HashMap<>(); // HashMap for storing position of the words on the frame
		random = new Random(System.currentTimeMillis()); // to fetch random data in milliseconds to place words on different places
		createMenu();
		loadWords();
		for(String word:words) // iteration
			placeWords(word,false);
		
		setSize(X_MAX, Y_MAX);
		setLayout(null);
		setTitle("Word Drag");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * method to create menu "FILE" to add a new word to the frame
	 */
	private void createMenu() 
	{
		menuBar = new JMenuBar();
		fileM = new JMenu("File");
		addWordMI = new JMenuItem("Add a word");
		addWordMI.addActionListener(new ActionListener()  // Event for the FILE menu item
		{
			
			@Override
			public void actionPerformed(ActionEvent e) // Adding new word
			{
				String word = JOptionPane.showInputDialog(null, "Enter new word","Add new Word", JOptionPane.PLAIN_MESSAGE);
				words.add(word);
				placeWords(word, true);
				repaint();
			}
		});
		fileM.add(addWordMI); // adding Menu Item to FILE Menu
		menuBar.add(fileM); // Adding FILE Menu to MenuBar
		setJMenuBar(menuBar); //Adding the MenuBar to the frame
	}
	
	private void placeWords(String word, boolean status) 
	{
		/**
		 * This method will place words read from the file to the frame after finding 
		 * the position randomly. Words will be placed on the Label and Label is placed 
		 * on the frame. It also checks if the overlapping of the words is happening.
		 * If yes then, it doesn't allow you to place the word onto the another and selects
		 * another position to place the label
		 */
		boxWord = new BoxWord();	
		rectangle = findPosition(word);	
		while(checkoverlap(rectangle)) 
		{
			rectangle = findPosition(word);
		}
		
		positionList.put(word, rectangle);
	    boxWord.setText(word);
	    boxWord.setBounds(rectangle);
		
		if(status)
			boxWord.setBackground(Color.red);
		
		add(boxWord);
	}

	private Rectangle findPosition(String word) 
	{
		/**
		 * This method will find the position of the word to be placed.
		 * Additionally, it ensures that the positions are not too close to the
		 * edge of the frame. 
		 */	
		Rectangle rect = new Rectangle();
		randomx = random.nextInt(X_MAX-80);
		randomy = random.nextInt(Y_MAX-80);
		width = boxWord.getFontmetrics().stringWidth(word);
		height = boxWord.getFontmetrics().getHeight();
		if((randomx+width) >= X_MAX)
			randomx -= (randomx+width) - X_MAX - 10;
		else if(randomx <= X_MIN)
			randomx += X_MIN;
		
		if((randomy+height) >= Y_MAX)
			randomy -= (randomy+height) - Y_MAX - 10;
		else if(randomy <= Y_MIN)
			randomy += Y_MIN;
		
		rect = new Rectangle(randomx, randomy, width+10, height);
		
		return rect;
	}

	public static boolean checkoverlap(Rectangle rect) 
	{
		/**
		 * This function is used to check if the word is overlapping another word
		 * or not. If yes, the word on the rectangle is moved back to its original 
		 * position. If not, then its placed on its new position.
		 * 
		 * HashMap entrySet() pulls out all the positions of words from the HashMap and 
		 * condition checks if the position is not null and rectangles 
		 * on which the words are placed are not intersecting each other
		 * to ensure non overlapping
		 * 
		 * The null check was introduced to allow self-overlapping; Considering
		 * the word comparison only with other labels and not itself
		 * This is a scenario in the drag functionality. 
		 */
		boolean result = false;
		
		for(Entry<String, Rectangle> r: positionList.entrySet()){
			if((r.getValue()!=null)&&(rect.intersects(r.getValue()))) {
				result = true;
				break;
			}
		}
		return result;
	}

	private void loadWords() 
	{  
		/**
		 * This function load each and every word on to the ArrayList 
		 * after reading it from the file. 
		 */
		words = new ArrayList<>();
		String word = "";
		try {
			fr = new FileReader("/Users/TanveenK/eclipse-workspace/GroupProjectAssignment/src/Project/word.txt");
			br = new BufferedReader(fr);
			while((word = br.readLine()) != null) {
				words.add(word);
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	public static void main(String[] args) {
		/**
		 * starting the frame
		 */
		new WordDrag();
	}
	
	public static boolean addPosition(Rectangle rect, String word) 
	{
		/**
		 * This function adds the new position of the label into the HashMap
		 * after drag the word
		 * 
		 * It also replaces the new position with the old position
		 * on the frame when the word is placed on top of another word
		 */
		boolean result = false;
		
		Rectangle old = positionList.get(word);
		positionList.replace(word, null);
		
		if(!checkoverlap(rect)) {
			positionList.replace(word, rect);
			result = true;
		}
		if(!result)
			positionList.replace(word, old);
		
		return result;
	}
	public static void removePosition(String word) 
	{
		/**
		 * This method removes the position from the frame when the word is 
		 * deleted.
		 */
		positionList.remove(word);
	}

}
