
/** 
 * @Description Class extending JLabel and implementation the mouse listeners 
 * 
 * @author Anita George & Tanveen Kaur
 */
package Project;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import javax.swing.JOptionPane;

public class BoxWord extends JLabel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private FontMetrics fontmetrics;
	private Font font;
	private int x,y;
	private int clickedX, clickedY;
	private int deltaX, deltaY;
	private int dragX, dragY;
	private int newX, newY;

	
	public BoxWord() 
	{
		/**
		 *  Constructor setting and getting the Font of the words to get height 
	     *   and the width 
	     */
		font = new Font("Times New Roman", Font.BOLD, 14);
		fontmetrics = getFontMetrics(font);
		setBackground(Color.lightGray);
		setOpaque(true);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		/**
		 *  Method to delete the word if the mouse is clicked twice
		 */
		int answer;
		if(e.getClickCount()==2) {
			answer = JOptionPane.showConfirmDialog(null, "Do you want to delete the word: " + getText(), "Delete the word", JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION) {
				setVisible(false);
				WordDrag.removePosition(getText());
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		/** 
		 * Listener to change the color of the word when mouse pressed onto it
		 * and gets the clicked x and y co-ordinates for the drag functionality
		 */
		setBackground(Color.GREEN);		
		x = getBounds().x;
		y = getBounds().y;
		clickedX = e.getXOnScreen();
		clickedY = e.getYOnScreen();

	}

	

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		/**
		 * Set the new location of the word and place it there when Mouse released
		 * and prevents the overlapping of the words
		 */
		setBackground(Color.lightGray);
		
		if((dragX != 0)&&(dragY != 0)) {
			deltaX = dragX - clickedX;
			deltaY = dragY - clickedY;
		}
		newX = x + deltaX;
		newY = y + deltaY;
		Rectangle r = new Rectangle(newX, newY, getWidth(), getHeight());
		
		while(!WordDrag.addPosition(r, getText())) {
			setLocation(x, y);
			JOptionPane.showMessageDialog(null, "The words cannot overlap each other. Try Again!", "Overlapping words", JOptionPane.ERROR_MESSAGE);
			return;
		}
		setLocation(newX, newY);
	}

	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
	/** 
	 * Method to show the word dragging along with the mouse
	 * 	
	 */
		dragX = e.getXOnScreen();
		dragY = e.getYOnScreen();
		if((dragX != 0)&&(dragY != 0)) {
			deltaX = dragX - clickedX;
			deltaY = dragY - clickedY;
		}
		newX = x + deltaX;
		newY = y + deltaY;
		setLocation(newX, newY);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
	
	public FontMetrics getFontmetrics() {
		// TODO Auto-generated method stub
		return fontmetrics;
	}
}

