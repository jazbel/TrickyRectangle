import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The "Board" class Purpose: To make and keep track of a puzzle Board
 * @author Sakeena Panju
 * @version January 25, 2012
 */
public class Board
{
	// Variable declaration
	protected int[][] board;
	protected Piece[] pieces = new Piece[13];

	/**
	 * Creates an empty board (5 by 11), with all spots initialized to contain a
	 * zero
	 */
	public Board()
	{
		board = new int[5][11];
		for (int boardY = 0; boardY < this.board.length; boardY++)
		{
			for (int boardX = 0; boardX < this.board[0].length; boardX++)
			{
				this.board[boardY][boardX] = 0;
			}
		}

	}

	/**
	 * Adds a Piece to the Board
	 * @param piece the Piece to draw
	 * @param startX the starting x position (not in pixels)
	 * @param startY the starting y position (not in pixels)
	 * @param orientation to orientation in which to place the Piece
	 */
	public void addPiece(Piece piece, int startX, int startY, int orientation)
	{
		Point[] pieceToDraw = piece.positions[orientation];
		piece.orientation = orientation;
		board[startY][startX] = piece.type;
		for (Point piecePart : pieceToDraw)
		{
			board[startY + piecePart.y][startX + piecePart.x] = piece.type;
		}
	}

	/**
	 * Removes a Piece from the Board
	 * @param piece the type of the Piece to remove (ex. 1 is the type for an
	 *        Ipiece)
	 */
	public void removePiece(int piece)
	{
		for (int boardY = 0; boardY < this.board.length; boardY++)
		{
			for (int boardX = 0; boardX < this.board[0].length; boardX++)
			{
				if (this.board[boardY][boardX] == piece)
					this.board[boardY][boardX] = 0;
			}
		}
	}

	/**
	 * Checks if a puzzle has been solved
	 * @return true if the puzzle was solved, false otherwise
	 */
	public boolean gameOver()
	{
		for (int boardY = 0; boardY < this.board.length; boardY++)
		{
			for (int boardX = 0; boardX < this.board[0].length; boardX++)
			{
				if (this.board[boardY][boardX] == 0)
					return false;
			}
		}
		return true;
	}

	/**
	 * Removes all Pieces from a Board
	 */
	public void clear()
	{
		for (int boardY = 0; boardY < this.board.length; boardY++)
		{
			for (int boardX = 0; boardX < this.board[0].length; boardX++)
			{
				this.board[boardY][boardX] = 0;
			}
		}
	}

	/**
	 * Converts this Board into a String by displaying the Board as 
	 * 5 rows and 11 columns containing Piece types
	 * @return a String representation of this Board
	 */
	public String toString()
	{
		StringBuffer strB = new StringBuffer();
		for (int boardY = 0; boardY < this.board.length; boardY++)
		{
			for (int boardX = 0; boardX < this.board[0].length; boardX++)
			{
				strB.append(this.board[boardY][boardX] + " ");
			}
			strB.append("\n");
		}
		return strB.toString();
	}

}
