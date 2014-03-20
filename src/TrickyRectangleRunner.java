import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.event.*;

/**
 * The "TrickyRectangleRunner" class
 * Purpose: To create the JFrame to run the game
 * @author Jazbel Wang, Sakeena Panju
 * @version January 25, 2012
 */

public class TrickyRectangleRunner extends JFrame implements ActionListener
{
	// Program constants (declared at the top, these can be used by any method)
	final int NO_OF_PIECES = 12;
	final int XLENGTH = 11;
	final int YLENGTH = 5;
	final int EMPTY = 0; // represents an empty space on the board
	final int SQUARE_SIZE = 52; // The length (in pixels) of the side of each
	// square on the board
	// Array of images for each piece on the board. Each piece has a unique
	// number (1, 2, 3...) which is also the index to this array of images.
	private Image[] images = new Image[NO_OF_PIECES + 1];
	// Array of piece object for each piece on the board.
	private Piece pieces[] = new Piece[NO_OF_PIECES + 1];
	// The board class where board squares in the area of play are
	// EMPTY or have a positive int value representing a player piece. This int
	// value is also an index to the pieces[] array of images.
	private Board wArea = new Board();
	private int selectedPiece; // Keeps track of the currently selected piece
	// (each piece has a unique number)
	private Point lastPoint;
	private JRadioButtonMenuItem easyOption, mediumOption, hardOption;
	private JMenuItem newOption, exitOption, instructionOption, aboutOption,
			solveOption; // for menu and menu events
	private DrawingPanel drawingArea;
	private int difficulty = 0;
	// Time variables: font, etc
	final Font TIME_FONT = new Font("Arial", Font.PLAIN, 100);
	private boolean timerOn;
	private Timer timer;
	private int time;
	// Screen variable to keep track of the screen
	private int screen = 1; // 1 - for menu, 2 - instruction, 3 - game
	// Background image was made 500x300 pixels (to fit 10 columns by 6 rows,
	// with each square being 50 pixels wide & high)
	Image imageBackground = new ImageIcon("GameLayout_play.jpg").getImage();
	Image menu = new ImageIcon("Menu_small.jpg").getImage();
	Image instruction = new ImageIcon("Instructions.jpg").getImage();
	Image pause = new ImageIcon("pause.jpg").getImage();
	// Queue to keep track of the last order the movable pieces were selected
	private LinkedList<Piece> moveablePieces = new LinkedList<Piece>();

	/**
	 * Constructs a new frame and sets up the game.
	 * @throws FileNotFoundException
	 */
	public TrickyRectangleRunner() throws FileNotFoundException
	{
		// Load all the images into the Image array
		loadResources();

		// Sets up the screen.
		setTitle("Tricky Rectangle");
		setLocation(10, 5);
		setResizable(false);

		// Set size of the drawing panel to equal the size of the background image
		int imageHeight = imageBackground.getHeight(TrickyRectangleRunner.this);
		int imageWidth = imageBackground.getWidth(TrickyRectangleRunner.this);
		Dimension imageSize = new Dimension(imageWidth, imageHeight);

		// Create and add the drawing panel.
		drawingArea = new DrawingPanel(imageSize);
		Container contentPane = getContentPane();
		contentPane.add(drawingArea, BorderLayout.CENTER);

		// Add all the menu and menu buttons to the JFrame
		addMenus();
		
		//Start a new game
		newGame();
	}

	/**
	 * Load image and audio files here These are stored as global variables at
	 * top of program, to be accessible to all methods. The image and audio
	 * files (gif, png, wav, etc) must be in the same folder as this program, or
	 * in a subfolder
	 */
	private void loadResources()
	{
		// Store the image for each piece in an array.
		images[1] = new ImageIcon("03_Ipiece.png").getImage();
		images[2] = new ImageIcon("02_Opiece.png").getImage();
		images[3] = new ImageIcon("01_Xpiece.png").getImage();
		images[4] = new ImageIcon("04_Cpiece.png").getImage();
		images[5] = new ImageIcon("05_Wpiece.png").getImage();
		images[6] = new ImageIcon("06_bigVpiece.png").getImage();
		images[7] = new ImageIcon("07_Zpiece.png").getImage();
		images[8] = new ImageIcon("08_smallVpiece.png").getImage();
		images[9] = new ImageIcon("09_Rpiece.png").getImage();
		images[10] = new ImageIcon("10_bigLpiece.png").getImage();
		images[11] = new ImageIcon("11_smallLpiece.png").getImage();
		images[12] = new ImageIcon("12_Ppiece.png").getImage();
	}

	/**
	 * Starts or restarts a game
	 * @throws FileNotFoundException
	 */
	private void newGame() throws FileNotFoundException
	{
		// Set the timer
		timer.stop();
		timerOn = false;
		time = 0;
		
		// Set the pieces on to the board
		initializeBoard("workingSolutions.txt");

		// Sets the currently selected piece to empty so a new game does not
		// start with a selected piece from last game
		selectedPiece = EMPTY;
		
		// paint the drawing panel when restarting a new game
		repaint(); 
	}

	/**
	 * Set all the pieces (solution) on the board using the text file, which is in the format of x y orientation
	 * @param fileName the name of the file that has all the information about the pieces in order to make a complete board
	 * @throws FileNotFoundException
	 */
	private void initializeBoard(String fileName) throws FileNotFoundException
	{
		// Get input from the file
		Scanner inFile = new Scanner(new File(fileName));
		
		//Choose a puzzle from the file randomly
		int puzzleNum = (int) (Math.random() * 100);
		for (int puzzle = 0; puzzle < puzzleNum; puzzle++)
		{
			inFile.nextLine();
			for (int piece = 1; piece <= 12; piece++)
			{
				inFile.nextLine();
			}
		}

		// Create all pieces with their locations and orientations set according to file information
		pieces[1] = new Ipiece(images[1],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[2] = new Opiece(images[2],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[3] = new Xpiece(images[3],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[4] = new Cpiece(images[4],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[5] = new Wpiece(images[5],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[6] = new BigVpiece(images[6],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[7] = new Zpiece(images[7],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[8] = new SmallVpiece(images[8],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[9] = new Rpiece(images[9],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[10] = new BigLpiece(images[10],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[11] = new SmallLpiece(images[11],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());
		pieces[12] = new Ppiece(images[12],
				(inFile.nextInt() * SQUARE_SIZE) + 32,
				(inFile.nextInt() * SQUARE_SIZE) + 397, inFile.nextInt());

		// Add the Pieces to a virtual Board object, which will represent
		// the Pieces that the player moves on the screen
		for (int index = 1; index <= 12; index++)
		{
			wArea.addPiece(pieces[index], (pieces[index].startPos.x - 32)
					/ SQUARE_SIZE, (pieces[index].startPos.y - 397)
					/ SQUARE_SIZE, pieces[index].orientation);
		}
		//Set the problem
		setProblem();
		// Set the linkedList of moveable pieces
		moveablePieces.clear();
		for (int i = 1; i <= 12; i++)
		{
			if (pieces[i].movable)
			{
				moveablePieces.add(pieces[i]);
			}
		}
		// Set the pieces to immoveable at the start of the game
		for (Piece nextPiece : moveablePieces)
		{
			nextPiece.setMoveable(timerOn);
		}
	}

	/**
	 * Remove a certain number of pieces, depending on the difficulty from a completed board to set the puzzle
	 */
	private void setProblem()
	{

		// Remove a couple of pieces from the virtual Board (wArea) - this will generate a problem for the player
		// Each piece that is removed must then be put on the screen so that the player can drag those pieces over to their correct location on the Board
		// The original positions is also stored to be able to solve the puzzle in the future
		for (int boardY = wArea.board.length - 1; boardY >= wArea.board.length
				- (2 + difficulty); boardY--)
		{
			for (int boardX = wArea.board[0].length - 1; boardX >= wArea.board[0].length
					- (2 + difficulty); boardX--)
			{
				int x = (int) (Math.random() * 416 + 32);
				int y = (int) (Math.random() * 104 + 130);
				if (wArea.board[boardY][boardX] == 1)
				{
					pieces[1].origStartPos = pieces[1].startPos;
					pieces[1].origOrientation = pieces[1].orientation;
					pieces[1].startPos = new Point(x, y);
					pieces[1].setMoveable(true);
					pieces[1].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 2)
				{
					pieces[2].origStartPos = pieces[2].startPos;
					pieces[2].origOrientation = pieces[2].orientation;
					pieces[2].startPos = new Point(x, y);
					pieces[2].setMoveable(true);
					pieces[2].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 3)
				{
					pieces[3].origStartPos = pieces[3].startPos;
					pieces[3].origOrientation = pieces[3].orientation;
					pieces[3].startPos = new Point(x, y);
					pieces[3].setMoveable(true);
					pieces[3].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 4)
				{
					pieces[4].origStartPos = pieces[4].startPos;
					pieces[4].origOrientation = pieces[4].orientation;
					pieces[4].startPos = new Point(x, y);
					pieces[4].setMoveable(true);
					pieces[4].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 5)
				{
					pieces[5].origStartPos = pieces[5].startPos;
					pieces[5].origOrientation = pieces[5].orientation;
					pieces[5].startPos = new Point(x, y);
					pieces[5].setMoveable(true);
					pieces[5].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 6)
				{
					pieces[6].origStartPos = pieces[6].startPos;
					pieces[6].origOrientation = pieces[6].orientation;
					pieces[6].startPos = new Point(x, y);
					pieces[6].setMoveable(true);
					pieces[6].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 7)
				{
					pieces[7].origStartPos = pieces[7].startPos;
					pieces[7].origOrientation = pieces[7].orientation;
					pieces[7].startPos = new Point(x, y);
					pieces[7].setMoveable(true);
					pieces[7].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 8)
				{
					pieces[8].origStartPos = pieces[8].startPos;
					pieces[8].origOrientation = pieces[8].orientation;
					pieces[8].startPos = new Point(x, y);
					pieces[8].setMoveable(true);
					pieces[8].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 9)
				{
					pieces[9].origStartPos = pieces[9].startPos;
					pieces[9].origOrientation = pieces[9].orientation;
					pieces[9].startPos = new Point(x, y);
					pieces[9].setMoveable(true);
					pieces[9].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 10)
				{
					pieces[10].origStartPos = pieces[10].startPos;
					pieces[10].origOrientation = pieces[10].orientation;
					pieces[10].startPos = new Point(x, y);
					pieces[10].setMoveable(true);
					pieces[10].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 11)
				{
					pieces[11].origStartPos = pieces[11].startPos;
					pieces[11].origOrientation = pieces[11].orientation;
					pieces[11].startPos = new Point(x, y);
					pieces[11].setMoveable(true);
					pieces[11].orientation = 0;
				} else if (wArea.board[boardY][boardX] == 12)
				{
					pieces[12].origStartPos = pieces[12].startPos;
					pieces[12].origOrientation = pieces[12].orientation;
					pieces[12].startPos = new Point(x, y);
					pieces[12].setMoveable(true);
					pieces[12].orientation = 0;
				}
				wArea.removePiece(wArea.board[boardY][boardX]);
			}
		}

	}
	
	

	/**
	 * For the menu and menu items
	 */
	private void addMenus()
	{
		// Sets up the Game MenuItems.
		newOption = new JMenuItem("New");
		newOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		newOption.addActionListener(this);

		exitOption = new JMenuItem("Exit");
		exitOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.CTRL_MASK));
		exitOption.addActionListener(this);

		// Sets up the Help MenuItems.
		instructionOption = new JMenuItem("Instructions");
		instructionOption.setMnemonic('I');
		instructionOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				InputEvent.CTRL_MASK));
		instructionOption.addActionListener(this);

		aboutOption = new JMenuItem("About");
		aboutOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				InputEvent.CTRL_MASK));
		aboutOption.setMnemonic('I');
		aboutOption.addActionListener(this);

		solveOption = new JMenuItem("Solve");
		solveOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		solveOption.setMnemonic('S');
		solveOption.addActionListener(this);

		// Sets up the Game and Help Menus.
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');

		// Add each MenuItem to the Game Menu (with a separator).
		gameMenu.add(newOption);
		gameMenu.addSeparator();
		gameMenu.add(exitOption);
		gameMenu.addSeparator();
		gameMenu.add(solveOption);

		// Create a submenu for the difficulty level
		gameMenu.addSeparator();
		JMenu submenu = new JMenu("Difficulty Level");
		submenu.setMnemonic(KeyEvent.VK_S);

		ButtonGroup group = new ButtonGroup();

		easyOption = new JRadioButtonMenuItem("Easy");
		easyOption.setSelected(true);
		easyOption.setMnemonic(KeyEvent.VK_R);
		group.add(easyOption);
		easyOption.addActionListener(this);
		submenu.add(easyOption);

		mediumOption = new JRadioButtonMenuItem("Medium");
		mediumOption.setMnemonic(KeyEvent.VK_O);
		group.add(mediumOption);
		mediumOption.addActionListener(this);
		submenu.add(mediumOption);

		hardOption = new JRadioButtonMenuItem("Hard");
		hardOption.setMnemonic(KeyEvent.VK_H);
		group.add(hardOption);
		hardOption.addActionListener(this);
		submenu.add(hardOption);

		gameMenu.add(submenu);

		// Add each MenuItem to the Help Menu (with a separator).
		helpMenu.add(instructionOption);
		helpMenu.addSeparator();
		helpMenu.add(aboutOption);

		// Adds the GameMenu and HelpMenu to the JMenuBar.
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(gameMenu);
		mainMenu.add(helpMenu);

		// Displays the menus.
		setJMenuBar(mainMenu);
	}

	/**
	 * This method is called by Java when a menu option is chosen
	 * @param event The event that triggered this method.
	 */
	public void actionPerformed(ActionEvent event)
	{
		// If the new option is selected, the board is reset and a new game
		// begins.
		if (event.getSource() == newOption)
			try
			{
				newGame();
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//Close the JFrame, if exit option is selected
		else if (event.getSource() == exitOption)
		{
			dispose();
			System.exit(0);
		}
		// Displays the instructions if the instruction option is selected.
		else if (event.getSource() == instructionOption)
		{
			JOptionPane
					.showMessageDialog(
							this,
							"Press the mouse key on the piece you wish to move."
									+ "\nThen drag and drop the piece to an empty square."
									+ "\n Use the arrow keys to flip or rotate the piece."
									+ "\n Try to finish the puzzle as fast as possible!",
							"Instructions ", JOptionPane.INFORMATION_MESSAGE);
		}
		// Displays copyright information if the about option is selected.
		else if (event.getSource() == aboutOption)
		{
			JOptionPane.showMessageDialog(this,
					"\u00a9 By Jazbel Wang, Sakeena Panju, and Joanna Wang ",
					"About Tricky Rectangle", JOptionPane.INFORMATION_MESSAGE);
		} 
		// Set the difficulty depending on the radio button that is selected
		else if (event.getSource() == easyOption)
		{
			difficulty = 0;
			try
			{
				newGame();
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if (event.getSource() == mediumOption)
		{
			difficulty = 2;
			try
			{
				newGame();
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if (event.getSource() == hardOption)
		{
			difficulty = 3;
			try
			{
				newGame();
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		// Solve the solution by placing all moveable pieces in their original spot
		else if (event.getSource() == solveOption)
		{
			for (Piece nextPiece : moveablePieces)
			{
				nextPiece.startPos = nextPiece.origStartPos;
				nextPiece.orientation = nextPiece.origOrientation;
			}
			// Stop the timer and determine whether to start a new game or exit to main screen
			timer.stop();
			repaint();
			if (JOptionPane.showConfirmDialog(drawingArea,
					"Do you want to Play Again?", "Game Over",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				try
				{
					newGame();
				} catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
			{
				// Redirect to menu screen
				screen = 1;
				repaint();
			}
		}
	}
 
	/**
	 * Creates and draws the main program panel that the user sees
	 * @author Jazbel Wang
	 *
	 */
	private class DrawingPanel extends JPanel
	{

		/**
		 * Constructs a new DrawingPanel object of the specified dimension/size
		 * @param size the size of the panel
		 */
		public DrawingPanel(Dimension size)
		{
			// Create a timer object. This object generates an event every 1 second (1000 milliseconds)
			// The TimerEventHandler object that will handle this timer
			// event is defined below as a inner class
			timer = new Timer(1000, new TimerEventHandler());
			
			// Set size of this panel
			setPreferredSize(size); 
			
			// Add mouse listeners to the drawing panel
			this.addMouseListener(new MouseHandler());
			this.addMouseMotionListener(new MouseMotionHandler());
			
			// Add key listeners and setting focus
			setFocusable(true);
			addKeyListener(new KeyHandler());
			requestFocusInWindow();
		}

		/**
		 * Repaint the drawing panel.
		 * @param g The Graphics context.
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			// Display different screens, such as main menu, instructions or game
			if (screen == 1)
			{
				// draw menu background first
				g.drawImage(menu, 5, 5, this); 
			} else if (screen == 2)
			{
				// draw instruction background first
				g.drawImage(instruction, 5, 5, this); 
			} else if (screen == 3)
			{
				// draw background
				g.drawImage(imageBackground, 5, 5, this); 

				if (timerOn)
				{ 
					// Draw pause button if the timer is on (game in play)
					g.drawImage(pause, 723, 475, this);
				}
				// Draw immovable first
				for (int i = 1; i <= 12; i++)
				{
					if (!pieces[i].movable)
					{
						pieces[i].draw(g);
					}
				}
				// Draw all the moveable pieces in order of recently selected
				for (Piece nextPiece : moveablePieces)
				{
					nextPiece.draw(g);
				}
				// Draw the selected piece being DRAGGED. Draw this last so
				// it appears over top everything.
				if (selectedPiece != EMPTY)
				{
					pieces[selectedPiece].draw(g);
				}
				// Draw the time
				g.setFont(TIME_FONT);
				g.setColor(Color.BLACK);
				String timeString = String.format("%02d:%02d",
						(int) (time / 60), time % 60);
				g.drawString(timeString, 650, 300);
			}

		} // paintComponent Method
		
		/**
		 * An inner class to deal with the timer events
		 * @author Jazbel
		 *
		 */
		private class TimerEventHandler implements ActionListener
		{
			// The following method is called each time a timer event is
			// generated (every 1000 milliseconds (1 second) in this example)
			public void actionPerformed(ActionEvent event)
			{
				// Stop timer when the game is over
				if (timerOn && wArea.gameOver())
				{
					timerOn = false;
					timer.stop();
				} else
				{
					// Increment the time (you could also count down)
					time++;

					// Repaint the screen
					repaint();
				}
			}
		}

	} // DrawingPanel

	// 
	/**
	 * Inner class to handle mouse-pressed and mouse-released events.
	 * @author Jazbel Wang 
	 *
	 */
	private class MouseHandler extends MouseAdapter
	{

		/**
		 * Finds out which piece was selected
		 * @param event in formation about the mouse pressed event
		 */
		public void mousePressed(MouseEvent event)
		{
			// Convert mouse-pressed location to board row and column
			Point pressedPoint = event.getPoint();
			// Main Menu
			if (screen == 1)
			{
				// If instruction button pressed, go to instruction menu
				if (pressedPoint.x < 652 && pressedPoint.x > 344
						&& pressedPoint.y < 270 && pressedPoint.y > 217)
				{
					screen = 2;
					repaint();
				}
				// If play game button is pressed, start up a new game
				if (pressedPoint.x < 618 && pressedPoint.x > 377
						&& pressedPoint.y < 371 && pressedPoint.y > 317)
				{
					screen = 3;
					repaint();
					try
					{
						newGame();
					} catch (FileNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// If high score button pressed, show high score dialog
				if (pressedPoint.x < 635 && pressedPoint.x > 360
						&& pressedPoint.y < 470 && pressedPoint.y > 415)
				{
					// Get all high score info
					Scanner inFile;
					try
					{
						inFile = new Scanner(new File("topScores.txt"));
						ArrayList<Score> highScoresList = new ArrayList<Score>();
						for (int nextScore = 0; nextScore < 5; nextScore++)
						{
							highScoresList.add(new Score(inFile.nextLine(),
									inFile.nextInt()));
							inFile.nextLine();
						}
						inFile.close();

						// Print the top scores chart
						// Add all the scores to a string
						StringBuffer scoresListStr = new StringBuffer();
						for (Score nextScore : highScoresList)
						{
							scoresListStr.append(nextScore.toString() + "\n");
						}
						JOptionPane.showMessageDialog(drawingArea,
								scoresListStr.toString(), "High Scores: \n",
								JOptionPane.INFORMATION_MESSAGE);
					} catch (FileNotFoundException e)
					{
						e.printStackTrace();
					} catch (IOException e)
					{
						e.printStackTrace();
					}

				}
			}
			// Instruction
			if (screen == 2)
			{
				// If back button pressed, go to menu
				if (pressedPoint.x < 564 && pressedPoint.x > 434
						&& pressedPoint.y < 599 && pressedPoint.y > 524)
				{
					screen = 1;
					repaint();
				}
			}
			// Game
			if (screen == 3)
			{
				// If mouse pressed the play and pause button
				if (pressedPoint.x < 875 && pressedPoint.x > 720
						&& pressedPoint.y < 559 && pressedPoint.y > 474)
				{
					// Alternate the timer on and off depending on whether play or pause is pressed
					timerOn = !timerOn;
					if (timerOn)
					{
						timer.start();
						repaint();
					} else
					{
						timer.stop();
					}
					// Set pieces to immoveable depending on if the timer is on
					for (Piece nextPiece : moveablePieces)
					{
						nextPiece.setMoveable(timerOn);
					}
				}
	
				// Check if moveable piece is selected
				for (int i = 1; i <= 12; i++)
				{
					if (pieces[i].contains(pressedPoint) && pieces[i].movable)
					{
						selectedPiece = i;
						// If selected, reorder the moveable pieces
						moveablePieces.remove(pieces[i]);
						moveablePieces.add(pieces[i]);
						lastPoint = pressedPoint;
						wArea.removePiece(pieces[i].type);
					}
				}
//				for (Piece nextPiece: moveablePieces)
//				{
//					if (nextPiece.contains(pressedPoint)&&nextPiece.movable)
//					{
//						selectedPiece = nextPiece.type;
//						// If selected, reorder the moveable pieces
//						moveablePieces.remove(nextPiece);
//						moveablePieces.add(nextPiece);
//						lastPoint = pressedPoint;
//						wArea.removePiece(selectedPiece);
//					}
//				}
			}
		}

		/**
		 * Finds where the mouse was released and moves the piece, if allowed
		 * @param event information about the mouse released event
		 */
		public void mouseReleased(MouseEvent event)
		{
			// Convert mouse-released location to board row and column
			Point releasedPoint = event.getPoint();
			if (screen == 3)
			{
				// Move piece, if a piece is selected
				if (selectedPiece != EMPTY)
				{
					int newX = Math
							.round((float) (pieces[selectedPiece].startPos.x - 32)
									/ (float) SQUARE_SIZE);
					int newY = Math
							.round((float) (pieces[selectedPiece].startPos.y - 397)
									/ (float) SQUARE_SIZE);
					// Snap on to grid if fits on grid
					if (pieces[selectedPiece].canFit(wArea.board, newX, newY))
					{
						pieces[selectedPiece].startPos = new Point(
								(newX) * 52 + 32, (newY) * 52 + 397);
						wArea.addPiece(pieces[selectedPiece], newX, newY,
								pieces[selectedPiece].orientation);
					}

				}
				selectedPiece = EMPTY;
				setCursor(Cursor.getDefaultCursor());
				repaint();
				// Check if the puzzle has been solved
				if (wArea.gameOver())
				{
					// Stop time and show JOptionPane
					timer.stop();
					JOptionPane
							.showMessageDialog(drawingArea,
									"You solved the puzzle!!\n You must be freakishly smart");
					// Update top scores
					// Get all high score info
					Scanner inFile;
					try
					{
						inFile = new Scanner(new File("topScores.txt"));
						ArrayList<Score> highScoresList = new ArrayList<Score>();
						for (int nextScore = 0; nextScore < 5; nextScore++)
						{
							highScoresList.add(new Score(inFile.nextLine(),
									inFile.nextInt()));
							inFile.nextLine();
						}
						inFile.close();

						// Get the person's name
						// Enter a name dialog box
						String name = JOptionPane
								.showInputDialog("Please enter your name");
						highScoresList.add(new Score(name, time));

						// Sort the high scores by fastest time and eliminate one to have top five scores
						Collections.sort(highScoresList,
								Collections.reverseOrder());
						highScoresList.remove(highScoresList.size() - 1);
						
						// Update the high score text file
						PrintWriter outFile = new PrintWriter(new FileWriter(
								"topScores.txt"));
						for (int eachScore = 0; eachScore < highScoresList
								.size(); eachScore++)
						{
							outFile.println(highScoresList.get(eachScore)
									.getName());
							outFile.println(highScoresList.get(eachScore)
									.getScore());
						}
						outFile.close();

						// Print the top scores chart
						// Add all the scores to a string
						StringBuffer scoresListStr = new StringBuffer();
						for (Score nextScore : highScoresList)
						{
							scoresListStr.append(nextScore.toString() + "\n");
						}
						JOptionPane.showMessageDialog(drawingArea,
								scoresListStr.toString(), "High Scores: \n",
								JOptionPane.INFORMATION_MESSAGE);
					} catch (FileNotFoundException e)
					{
						e.printStackTrace();
					} catch (IOException e)
					{
						e.printStackTrace();
					}

					if (JOptionPane.showConfirmDialog(drawingArea,
							"Do you want to Play Again?", "Game Over",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						try
						{
							newGame();
						} catch (FileNotFoundException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else
					{
						// Redirect to menu screen
						screen = 1;
						repaint();
					}
				}
			}

		}
	}

	// Inner Class to handle mouse movements over the DrawingPanel
	private class MouseMotionHandler extends MouseMotionAdapter
	{
//		/**
//		 * Changes the mouse curser to a hand if it is over top of a piece
//		 * @param event information about the mouse released event
//		 */
//		public void mouseMoved(MouseEvent event)
//		{
//			// Determine if mouse is on a moveable piece
//			if (screen == 3)
//			{
//				for (Piece nextPiece : moveablePieces)
//				{
//					if (nextPiece.contains(event.getPoint())
//							&& nextPiece.movable)
//					{
//						setCursor(Cursor
//								.getPredefinedCursor(Cursor.HAND_CURSOR));
//					}
//					else
//					{
//						setCursor(Cursor.getDefaultCursor());
//					}
//					
//				}
//				
//			}
//		}

		/**
		 * Moves the selected piece when the mouse is dragged
		 * @param event information about the mouse released event
		 */
		public void mouseDragged(MouseEvent event)
		{
			// If a moveable piece is selected, update its new start position
			if (selectedPiece != EMPTY)
			{
				pieces[selectedPiece].translate(lastPoint, event.getPoint());
				lastPoint = event.getPoint();
			}
			repaint();
		}
	}
	
	/**
	 * Inner class to handle key events, such as flipping and rotating
	 * @author Jazbel Wang
	 *
	 */
	private class KeyHandler extends KeyAdapter
	{
		
		/**
		 * Change the orientation of the piece based on the arraw key pressed
		 * @param event information about the key pressed event
		 */
		public void keyPressed(KeyEvent event)
		{
			int keyCode = event.getKeyCode();
			// During the game
			if (screen == 3)
			{
				// Rotate piece with right and left arrow key (clockwise and counterclockwise respectively)
				// Only applies to pieces that can rotate( 2 orientations)
				if (keyCode == KeyEvent.VK_RIGHT
						&& pieces[selectedPiece].positions.length > 1)

				{
					pieces[selectedPiece].rotateRight();
				} else if (keyCode == KeyEvent.VK_LEFT
						&& pieces[selectedPiece].positions.length > 1)
				{
					pieces[selectedPiece].rotateLeft();
				}
				// Flip piece with up and down arrow key
				// Only applies to pieces that can flip(8 orientations)
				else if (keyCode == KeyEvent.VK_UP
						&& pieces[selectedPiece].positions.length == 8)
				{
					pieces[selectedPiece].flip();
				} else if (keyCode == KeyEvent.VK_DOWN
						&& pieces[selectedPiece].positions.length == 8)
				{
					pieces[selectedPiece].flip();
				}
				repaint();
			}
		}
	}

	// The main is the starting point of the program and constructs the game.
	public static void main(String[] args) throws FileNotFoundException
	{
		// Create the TrickyRectangleRunner JFrame and set visible
		TrickyRectangleRunner demo = new TrickyRectangleRunner();
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.pack();
		demo.setVisible(true);
	} // Main method
} // class