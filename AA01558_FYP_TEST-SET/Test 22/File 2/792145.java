/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

/*
 * Jumpbox
 *
 * 02-MAY-97, Xxxxxx Xxxxxx #0792145
 *
 *
 */
 
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Random;
 
public class Jumpbox extends Canvas 
					 implements Runnable, MouseMotionListener {
					 
	final private static int LINKS  = 0;
	final private static int RECHTS = 1;
	final private static int 
other
OBEN   = 2;
	final private static int UNTEN  = 3;
	
	final private static int SMILE  = 0;
	final private static int WINCE  = 1;
	
	final private static int UNIT   = 50;

	private long startTime;
	private int duration;			// Duration of the game
	
	private int jumps;
	private int credits;
	private int whatNext;
	
	private int direction;
	private Rectangle oldBox;
	private Rectangle currentBox;
	private Rectangle hotSpot;
	private Rectangle entrance;

	private Random random;

	private Image offImage;			// Objects for the offscreen buffer
	private Graphics offGraphics;
	private Dimension offDimension;
	
	private Image smileImage;
	private Image winceImage;
	
	private Color LROUColors[];
	
	private Thread loop;


	/** 
	 * Create a new Jumpbox game.
	 * @param duration the duration of the game.
	 */
	public Jumpbox(int duration) {
		
		this.duration = duration;
		jumps = 0;
		credits = 0;
		
		// Setup the game area
		setSize(10*UNIT, 8*UNIT);
		addMouseMotionListener(this);
		
		// Initialize a new Random object
		startTime = System.currentTimeMillis();
		random = new Random(startTime);

		// Initialize Boxes.
		makeLROUColors();
		currentBox = new Rectangle (0, 0, 0, 0);
		hotSpot = new Rectangle (0, 0, 0, 0);
		makeJumpBox();
		
		// Load images
		Toolkit tk 
other
= getToolkit();
		smileImage = tk.getImage("smile.gif");
		winceImage = tk.getImage("wince.gif");

		// Start the main loop
		loop = new Thread(this);
		loop.start();		
	}
	
	/**
	 * Makes a new Jumpbox.
	 */
	synchronized private void makeJumpBox() {
		
		// Save the old position
		oldBox = currentBox;
		
		// Randomize a new position
		Dimension dim = getSize();
		int rangeX = dim.width - 3*UNIT;
		int rangeY = dim.height - 4*UNIT;
		int newX = 0; 
		int newY = 0;
		// The new position is valid only, if it is not within 
		// the old hot spot.
		do {
			newX = Math.abs(random.nextInt()) % rangeX + UNIT;
			newY = Math.abs(random.nextInt()) % rangeY + 2*UNIT;
			currentBox = new Rectangle(newX, newY, UNIT, UNIT);
		} while (currentBox.intersects(hotSpot));
		
		// Set up hot spot and entrance...
		hotSpot = new Rectangle(newX-UNIT, newY-UNIT, 3*UNIT, 3*UNIT);
		direction = Math.abs(random.nextInt()) % 4;
		switch(direction) {
			case LINKS: 
				entrance = new Rectangle(newX-UNIT, newY, UNIT, UNIT);
				break;
			case RECHTS:
				entrance = new Rectangle(newX+UNIT, newY, UNIT, UNIT);
				break;
			case OBEN:
				entrance = new Rectangle(newX, newY-UNIT, UNIT, UNIT);
				break;
			case UNTEN:
				entrance = new Rectangle(newX, newY+UNIT, UNIT, UNIT);
				break;
		}
		
		// System.err.println("Box at (" + newX + ", " + newY 
		//			+ "), orientation: " + direction);
	}
	
	/**
	 * Makes new colors for the LROU Boxes.
	 */
	synchronized private void makeLROUColors() {
		// Set up a vector with available colors
		Vector colors 
other
= new Vector();
		colors.addElement(Color.blue);
		colors.addElement(Color.cyan);
		colors.addElement(Color.green);
		colors.addElement(Color.magenta);
		colors.addElement(Color.orange);
		colors.addElement(Color.pink);
		colors.addElement(Color.red);
		colors.addElement(Color.yellow);
		
		// Select four distinct colors
		LROUColors = new Color[4];
		for (int i = 0; i < 4; i++) {
			int colorNo = Math.abs(random.nextInt()) % colors.size();
			LROUColors[i] = (Color) colors.elementAt(colorNo);
			colors.removeElementAt(colorNo);
		}
	}
	
    /**
	 * How long have we been playing?
	 */
	private long secondsPlayed() {
		return ((System.currentTimeMillis()-startTime) / 1000);
	}
	
	/**
	 * Animators loop.
	 */
	public void run() {
		while (true) {
			synchronized (this) {
				try {
					this.wait();
				}
				catch (InterruptedException e) {}
			}
			switch(whatNext) {
				case SMILE:
					// System.err.println("Smile!");
					explodeImage(smileImage, 
						currentBox.x, currentBox.y, 50, 1000);
					credits++;
					break;
				case WINCE:
					// System.err.println("Wince!");			
					implodeImage(winceImage, 
						currentBox.x, currentBox.y, 50, 1000);
					credits--;
					break;
			}
			jumps++;
			if ((jumps % 2) == 0) {
				makeLROUColors();
				drawLROUBoxes();
			}
			makeJumpBox();
			showJump(500);		
			clearJumpBox();
			drawJumpBox();
			repaint();
			Thread.yield();
		}
	
other
}
	
	
	/**
	 * Draws Jumpbox.
	 */
	synchronized private void drawJumpBox() {
		offGraphics.setColor(LROUColors[direction]);
		offGraphics.fillRect(currentBox.x, currentBox.y, 
					currentBox.width, currentBox.height);
	}
	
	
	/**
	 * Clears old Jumpbox.
	 */
	synchronized private void clearJumpBox() {
		offGraphics.setColor(Color.white);
		offGraphics.fillRect(oldBox.x, oldBox.y, 
					oldBox.width, oldBox.height);
	}
	
	
	/**
	 * Shows next jump.
	 */
	private void showJump(int duration) {
	
		int fromX = oldBox.x + UNIT/2;
		int fromY = oldBox.y + UNIT/2;
		int toX = currentBox.x + UNIT/2;
		int toY = currentBox.y + UNIT/2;
		synchronized(this) {
			offGraphics.setColor(Color.black);
			offGraphics.drawLine(fromX, fromY, toX, toY);
		}
		repaint();
		try {
			Thread.sleep(duration);
		}
		catch (InterruptedException e) {}
		synchronized(this) {
			offGraphics.setColor(Color.white);
			offGraphics.drawLine(fromX, fromY, toX, toY);
		}
	}
	
	
	/**
	 * Draws the LROU color boxes.
	 */
	synchronized private void drawLROUBoxes() {
		String letters = "LROU";
		Dimension dim = getSize();
		offGraphics.setColor(Color.lightGray);
		offGraphics.fillRect(0, 0, dim.width, UNIT);
		
other
for (int i = 0; i < 4; i++) {
			offGraphics.setColor(LROUColors[i]);
			offGraphics.fillRect(0 + UNIT*i, 0, UNIT, UNIT);
			offGraphics.setColor(Color.black);
			offGraphics.drawString(letters.substring(i, i+1), 
				UNIT/2 + UNIT*i, UNIT/2);
		}
	}
	
	/**
	 * Shows an animation of an imploding image.
	 */
	private void implodeImage(Image image, int x, int y, 
					int size, int duration) {
		int inc   = 2;				
		int steps = size / (2*inc);
		int pause = duration / steps;
		for (int i = steps; i > 0; i--) {
			synchronized(this) {
				offGraphics.setColor(Color.white);
				offGraphics.fillRect(x, y, size, size);
				offGraphics.drawImage(image, x+(steps-i)*inc, y+(steps-i)*inc, 
					inc*i*2, inc*i*2, this);
			}
			repaint(x, y, size, size);
			try {
				Thread.sleep(pause);
			}
			catch (InterruptedException e) {}
		}
	}
	
	
	/**
	 * Shows an animation of an exploding image.
	 */
	private void explodeImage(Image image, int x, int y, int size, int duration) {
		int inc   = 2;				
		int steps = size / (2*inc);
		int pause = duration / steps;
		for (int i = 1; i <= steps; i++) {
			synchronized (this) {
				offGraphics.setColor(Color.white);
				offGraphics.fillRect(x, y, size, size);
				offGraphics.drawImage(image, x+(steps-i)*inc, y+(steps-i)*inc, 
					inc*i*2, inc*i*2, this);
			}
			repaint(x, y, size, size);
			try {
				Thread.sleep(pause);
			}
			catch (InterruptedException e) {}
		}
	}
	
	
	/**
	 * Repaints this canvas.
	 */
	public void paint (Graphics g) {
		
		// System.err.println("paint()");
		
		// Use update() to diplay the offscreen buffer.
		update(g);
	}
	
	
	/**
	 * Updates this canvas.
	 */
	synchronized public void update (Graphics g) {
	
		// System.err.println("update()");
	
		Dimension dim = getSize();
	
		// Is the offscreen buffer still valid?
        if ( (offGraphics == null)
          || (dim.width != offDimension.width)
          || (dim.height != offDimension.height) ) {
            offDimension = dim;
            offImage = createImage(dim.width, dim.height);
            offGraphics = offImage.getGraphics();
			// System.err.println("New off screen buffer. "
			//        + "Size = (" + dim.width + ", " + dim.height + ")");
			
			// The following drawing operations are only needed once
			// (after creating the off screen buffer...)
			offGraphics.setColor(Color.white);
			offGraphics.
other
fillRect(0, 0, dim.width, dim.height);
			offGraphics.setColor(Color.black);
			offGraphics.drawRect(0, 0, dim.width, UNIT);
			offGraphics.drawRect(0, UNIT, dim.width, dim.height-UNIT);
			drawLROUBoxes();
		    // clearJumpBox();
			drawJumpBox();
        }
		
		// Copy the offsreen buffer into the game area
		g.drawImage(offImage, 0, 0, this);
	}
	
	
	/**
	 * Handle mouse drags.
	 */
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	
	/**
	 * Handle mouse movements.
	 * This is important for the game.
	 */
	public void mouseMoved(MouseEvent e) {
		if (secondsPlayed() > duration) {
			System.out.println("Das Spiel ist zu Ende.");
			System.out.println("Es wurden " + credits + " Punkte in " 
									+ duration + " Sekunden erzielt.");
			System.exit(0);
		}
		else {
			int x = e.getX();
			int y = e.getY();
			if (currentBox.contains(x, y)) 
				whatNext = SMILE;
			else if (hotSpot.contains(x, y) && !entrance.contains(x, y))
				whatNext = WINCE;
			else
				return;
			
			// This will display an animation and our box will jump on...
			synchronized(this) {
				this.notifyAll();
			}
		}
	}
	
	
	/**
	 * Start up the application.
	 * @param args command line argument, which specifies the duration
	 * 			for the Jumpbox game. If none given, the default 
	 *			duration is 15 seconds.
	 */
	static public void main(String args[]) {
	
		// Check for command line arguments
		int duration;
		if (args.length == 1) {
			try {
				duration = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e) {
				duration = 15;
			}
		}
		else
			duration = 15;
			
		// Open a frame containing the Jumpbox game
		Frame frame = new Frame("Jumpbox - Xxxxxx Xxxxx #0792145");
		frame.add(new Jumpbox(duration));
		frame.pack();
		// frame.setResizable(false);
		frame.show();
	}

}