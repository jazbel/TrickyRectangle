import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * The "SmallLpiece" class
 * Purpose: To make and keep track of a SmallLpiece
 * @author Jazbel Wang, Sakeena Panju, Joanna Wang
 * @version January 25, 2012
 */
public class SmallLpiece extends Piece
{
	
	/**
	 * Construct a Piece that will be ready for drawing on the Board
	 * @param image the image which will be used to represent this Piece
	 * @param x the x position of the startPos
	 * @param y the y position of the startPos
	 * @param orientation in which to draw this Piece
	 */
	public SmallLpiece(Image image, int x, int y, int orientation) {
		super(image, x, y, orientation); 
		type = SMALLL_PIECE; 
		positions = new Point[][]{{new Point (0,1), new Point (0,2), new Point (1,2)}, 
				{new Point (1,0), new Point (2,0), new Point (2,-1)},
				{new Point (0,-1), new Point (0,-2), new Point (-1,-2)},
				{new Point (-1,0), new Point (-2,0), new Point (-2,1)},
				{new Point (0,-1), new Point (0,-2), new Point (1,-2)},
				{new Point (1,0), new Point (2,0), new Point (2,1)},
				{new Point (0,1), new Point (0,2), new Point (-1,2)}, 
				{new Point (-1,0), new Point (-2,0), new Point (-2,-1)}};
	}
	
	/**
	 * Construct a basic Piece
	 */
	public SmallLpiece()
	{
		super();
		type = SMALLL_PIECE; 
		positions = new Point[][]{{new Point (0,1), new Point (0,2), new Point (1,2)}, 
				{new Point (1,0), new Point (2,0), new Point (2,-1)},
				{new Point (0,-1), new Point (0,-2), new Point (-1,-2)},
				{new Point (-1,0), new Point (-2,0), new Point (-2,1)},
				{new Point (0,-1), new Point (0,-2), new Point (1,-2)},
				{new Point (1,0), new Point (2,0), new Point (2,1)},
				{new Point (0,1), new Point (0,2), new Point (-1,2)}, 
				{new Point (-1,0), new Point (-2,0), new Point (-2,-1)}};
	}
	
}
