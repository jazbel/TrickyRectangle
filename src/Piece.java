import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The "BigLpiece" class
 * Purpose: To make and keep track of an Lpiece
 * @author Jazbel Wang, Sakeena Panju, Joanna Wang
 * @version January 25, 2012
 */
public class Piece
{
	//C
	final int I_PIECE = 1;
	final int O_PIECE = 2;
	final int X_PIECE = 3;
	final int C_PIECE = 4;
	final int W_PIECE = 5;
	final int BIGV_PIECE = 6;
	final int Z_PIECE = 7;
	final int SMALLV_PIECE = 8;
	final int R_PIECE = 9;
	final int BIGL_PIECE = 10;
	final int SMALLL_PIECE = 11;
	final int P_PIECE = 12;
	
	
	//Set the location of the bottom left point of the piece
	protected Point centre;
	protected int type;
	protected int orientation;
	protected int origOrientation;
	protected Point startPos;
	protected Point origStartPos;
	protected Point[][] positions;
	private Image image;
	protected boolean movable;

	/**
	 * Construct a basic Piece
	 */
	public Piece()
    {
            startPos= new Point (0,0);
    }
	
	/**
	 * Construct a Piece that will be ready for drawing on the Board
	 * @param image the image which will be used to represent this Piece
	 * @param x the x position of the startPos (in pixels)
	 * @param y the y position of the startPos (in pixels)
	 * @param orientation in which to draw this Piece
	 */
	public Piece(Image image, int x, int y, int orientation)
    {
            this.image = image;
            startPos = new Point(x,y);
            this.orientation = orientation;
            this.movable = false;
            
    }

	public String toString()
	{
		return (type + " " + startPos.x + " " + startPos.y + " " + orientation);
	}
	public void setMoveable(boolean isMoveable)
	{
		this.movable = isMoveable;
	}

	// Set the position of a Piece from a point
	public void setPosition(Point newPosition)
	{
		centre = newPosition;
	}

	// Set the position of a Piece from x and y
	public void setPosition(int x, int y)
	{
		centre = new Point(x, y);
	}

	// Move from one point to another
	public void move(Point from, Point to)
	{
		centre = new Point(centre.x - from.x + to.x, centre.y - from.y + to.y);
	}

	// Get a list of possible orientations at a specific location
	public ArrayList<Integer> findAllAvailable(int[][] board, int startX, int startY)
	{
		ArrayList<Integer> possibilities = new ArrayList<Integer>();
		//Iterate through all the orientations
		for (int position = 0 ; position < positions.length; position++)
		{
			boolean canFit = true;
			for (int point = 0 ; point < positions[position].length && canFit; point++ )
			{
				int x = startX + positions[position][point].x;
				int y = startY + positions[position][point].y;
				if (!(x >= 0 && x < board[0].length && y >= 0 && y < board.length && board[y][x] == 0))
				{
					canFit = false;
				}
			}
			if (canFit)
			{
				possibilities.add(position);
			}
		}
		return possibilities;
	}
	
	public boolean canFit(int[][] board, int startX, int startY)
	{
		boolean canFit = true;
		for (int point = -1 ; point < positions[orientation].length && canFit; point++ )
		{
			int x, y;
			if (point == -1)
			{
				x = startX;
				y = startY;
			}
			else
				{
				x = startX + positions[orientation][point].x;
				y = startY + positions[orientation][point].y;
				}
			if (!(x >= 0 && x < board[0].length && y >= 0 && y < board.length && board[y][x] == 0))
			{
				canFit = false;
			}
		}
		return canFit;
	}

	// Flip the piece
	 
		public void flip()
	 
		{
	 
			if (orientation < 4)
	 
			{
	 
				orientation += 4;
	 
			}
	 
			else if (orientation >= 4)
	 
			{
	 
				orientation -= 4;
	 
			}
	 
		}
	 



		// Rotate the piece
	 
		public void rotateRight()
	 
		{
	 
			if (positions.length == 4)
	 
			{
	 
				if (orientation > 0)
	 
				{
	 
					orientation--;
	 
				}
	 
				else
	 
				{
	 
					orientation = positions.length - 1;
	 
				}
	 
			}
	 
			else
	 
			{
	 
				if (orientation < 4)
	 
				{
	 
					if (orientation > 0)
	 
					{
	 
						orientation--;
	 
					}
	 
					else
	 
					{
	 
						orientation = 3;
	 
					}
	 
				}
	 
				else if (orientation >= 4)
	 
				{
	 
					if (orientation > 4)
	 
					{
	 
						orientation--;
	 
					}
	 
					else
	 
					{
	 
						orientation = 7;
	 
					}
	 
				}
	 
			}
	 
			

		}
	 



		// Rotate the piece
	 
		public void rotateLeft()
	 
		{
	 
			if (positions.length == 4)
	 
			{
	 
				if (orientation < 3)
	 
				{
	 
					orientation++;
	 
				}
	 
				else
	 
				{
	 
					orientation = 0;
	 
				}
	 
			}
	 
			else
	 
			{
	 
				if (orientation < 4)
	 
				{
	 
					if (orientation < 3)
	 
					{
	 
						orientation++;
	 
					}
	 
					else
	 
					{
	 
						orientation = 0;
	 
					}
	 
				}
	 
				else if (orientation >= 4)
	 
				{
	 
					if (orientation < 7)
	 
					{
	 
						orientation++;
	 
					}
	 
					else
	 
					{
	 
						orientation = 4;
	 
					}
	 
				}
	 
			}
	 
		}
		
	
	// Draw the piece
	public void draw(Graphics g)
	{
		g.drawImage(image, startPos.x, startPos.y, null);
		for (int dot = 0; dot < positions[0].length; dot++)
		{
			g.drawImage(image, startPos.x + 52*positions[orientation][dot].x, startPos.y + 52*positions[orientation][dot].y, null);
		}
	}
	
	public boolean contains(Point point)
	{
		if (Point.distance(startPos.x + 26, startPos.y + 26, point.x, point.y) <= 26)
		{
			return true;
		}
		for (int dot = 0; dot < positions[0].length; dot++)
		{
			if (Point.distance(startPos.x + 52*positions[orientation][dot].x + 26, startPos.y + 52*positions[orientation][dot].y + 26, point.x, point.y) <= 26)
			{
				return true;
			}
		}
		return false;
	}
	
	public void translate(Point from, Point to)
	{
		startPos.x += to.x - from.x;
		startPos.y += to.y - from.y;
	}
}
