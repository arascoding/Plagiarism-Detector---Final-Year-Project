/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

/*--------------------------------------------------------------

	File: 				Jumpbox.java
	
	Description:	contains the main application
	
................................................................

	JAKK Aufgabe 5
	
	Xxxxxx Xxxxxx 0729057

--------------------------------------------------------------*/

other
import java.awt.*;
import java.util.Random;

//**************************************************************
//	J u m p b o x
//**************************************************************

public class Jumpbox extends Canvas implements Runnable {
//--------------------------------------------------------------
/*	private fields 																						*/
//--------------------------------------------------------------

	static final String			goodImgNm	= "smile.gif";
	static final String			badImgNm	= "wince.gif";
	static final int				stIdle = 0, stGoodAnim = 1, stBadAnim = 2, stFinishAnim = 3;
	
	static final Color			stdCol[] = {	Color.blue, 	 Color.cyan, 	 Color.green, 
																				Color.magenta, Color.orange, Color.pink,
																				Color.red, 		 Color.yellow };
	static final String			names[]		= { "L","R","O","U" };
  static final int				sizeH			= 500;
  static final int				sizeV			= 400;
  static final int				stdTime		=  15;
  static final int				sizeBox		=  50;

	Random					rand 			= new Random();
	int							colors[] 	= new int[4];
  Image						
other
offscreen	= null;
  boolean					gameOver	= false;
	int							state			= stIdle;		//	the state of the animation thread
	long						timelimit;
	int							timeSecs;
	int							bx, by;								//	box coordinates (top/left)
	int							dir;

	int							points;
	int							move;
  Image						imgGood;
  Image 					imgBad;
  Image 					screen;
  Graphics				gs;
  

//--------------------------------------------------------------
/**	creator				 																						*/
//--------------------------------------------------------------
	Jumpbox(int playtime)
	{
		timeSecs	= playtime;
		timelimit = System.currentTimeMillis() + playtime * 1000;
		move			= 0;
		points		= 0;
		rand.setSeed(System.currentTimeMillis());
		
		SetupWindow();
		NextMove();
		
		(new Thread(this)).start();		//	start animation
	}

//--------------------------------------------------------------
/*	SetupWindow		 																						*/
//--------------------------------------------------------------
	void SetupWindow()
	{
    Frame 		f		= new Frame("Xxxxxx Xxxxxxx, 729057");
    Toolkit 	tk	= Toolkit.getDefaultToolkit();
    
    this.reshape(0, 0, sizeH, sizeV);
    
    f.add("Center", this);
    f.pack();
    f.show();
    
    imgGood	= tk.getImage(goodImgNm);
    imgBad	= tk.getImage(badImgNm);
    screen	= createImage(sizeH, sizeV);
    gs			= screen.getGraphics();
    gs.setColor(Color.white);
    gs.fillRect(0, 0, sizeH, sizeV);
    gs.setColor(Color.black);
    gs.drawRect(0, sizeBox, sizeH-1, sizeV - sizeBox - 1);
    
	}
	
//--------------------------------------------------------------
/*	ChooseColors	 																						*/
//--------------------------------------------------------------
	void ChooseColors()
	{
		int 			i, k;
		boolean		ok;
		
		for (i=0; i<4; i++)
		{
			ok = false;
			while (!ok)
			{
				ok = true;
				colors[i] = Math.abs(rand.nextInt()) % 8;

				for (k=0; k<i; k++)
					if (colors[k] == colors[i])
					{
						ok = false;
						break;
					}
			}
		}

		for (i=0; i<4; i++)
		{
			gs.setColor(stdCol[colors[i]]);
			gs.fillRect(i * 50, 0, 50, 50);
			
			gs.setColor(stdCol[colors[(i + 1) % 4]]);
			gs.drawString(names[i], i*50 + 20, 30);
		}
	}

//--------------------------------------------------------------
/** rnd																												

		returns a random number between <lo> (including) and <hi>
		(excluding)																								*/
//--------------------------------------------------------------
	public int rnd(int lo, int hi)
	{
		return (Math.abs(rand.nextInt()) % (hi - lo)) + lo;
	}

//--------------------------------------------------------------
/** StartMove																									*/
//--------------------------------------------------------------
	public void StartMove(boolean showLine)
	{
		int		oldx	= bx;
		int		oldy	= by;
		
		bx	 	 = rnd(			 1, sizeH - sizeBox);
		by		 = rnd(sizeBox, sizeV - sizeBox);
		dir		 = rnd(0, 4);

		if (showLine)
		{
			int					bsize2 = sizeBox / 2;
			Rectangle		r;
			
			// set up enclosing rectangle
			r	
other
= new Rectangle(oldx + bsize2 - 1, oldy + bsize2 - 1, 2, 2);
			r.add(bx + bsize2 - 1, by + bsize2 - 1);
			r.add(bx + bsize2 + 1, by + bsize2 + 1);
			
			// draw line
			gs.setColor(Color.black);
			gs.drawLine(oldx + bsize2, oldy + bsize2, bx + bsize2, by + bsize2);
			sleepNoX(500);
			this.repaint(r.x, r.y, r.width, r.height);
			
			// erase line
			gs.setColor(Color.white);
			gs.drawLine(oldx + bsize2, oldy + bsize2, bx + bsize2, by + bsize2);
		}
		
		gs.setColor(stdCol[colors[dir]]);
		gs.fillRect(bx, by, sizeBox, sizeBox);
		
		this.repaint();
	}
	
//--------------------------------------------------------------
/** StopGame																									*/
//--------------------------------------------------------------
	synchronized public void StopGame()
	{
		state = stFinishAnim;
		this.notify();
	}

//--------------------------------------------------------------
/** StopMove																									*/
//--------------------------------------------------------------
	synchronized public void StopMove(boolean good)
	{
		if (good)
		{
			points++;
			state = stGoodAnim;
			this.notify();
		}
		else
		{
			points--;
			state = stBadAnim;
			this.notify();
		}
	}

//--------------------------------------------------------------
/** NextMove																									*/
//--------------------------------------------------------------
	public void NextMove()
	{
		if ((move++ % 3) == 0)
			ChooseColors();

		StartMove(move > 1);
	}		

//--------------------------------------------------------------
/** mouseDrag																									*/
//--------------------------------------------------------------
	public boolean mouseDrag(Event evt, int x, int y)
	{
		return mouseMove(evt, x, y);
	}

//--------------------------------------------------------------
/** mouseMove																									*/
//--------------------------------------------------------------
	public boolean mouseMove(Event evt, int x, int y)
	{
		if (state == stIdle)
		{
			if ((bx <= x) && (x <= bx + 50) && (by <= y) && (y <= by + 50))
				StopMove(true);
			else if (timelimit < System.currentTimeMillis())
				StopGame();
			else if ((bx - 50 <= x) && (x <= bx + 100) && (by - 50 <= y) && (y <= by + 100))
			{
				boolean		iL =  x			 < bx;
				boolean		iR = bx + 50 <  x;
				boolean		iT =  y 		 < by;
				boolean		iB = by + 50 <  y;
				
				if (	((dir != 0) && iL)
					 ||	((dir != 1) && iR)
					 || ((dir != 2) && iT)
					 || ((dir != 3) && iB))
					StopMove(false);
			}
		}
		
		return true;
	}

//--------------------------------------------------------------
/** paint																											*/
//--------------------------------------------------------------
  public void paint(Graphics g)
  {
    update(g);
  }

//--------------------------------------------------------------
/** update																										*/
//--------------------------------------------------------------
	public void update(Graphics g)
	{
		g.drawImage(screen, 0, 0, this);
	}

//--------------------------------------------------------------
/** run																												*/
//--------------------------------------------------------------
	public void run()
	{
		while (!gameOver)
		{
			if (state == stIdle)
				animationStop();
			
other
else if (state == stGoodAnim)
			{
				animationGood();

				state = stIdle;
				NextMove();
			}
			else if (state == stBadAnim)
			{
				animationBad();

				state = stIdle;
				NextMove();
			}
			else if (state == stFinishAnim)
			{
				animationBad();

				gameOver = true;
			}
			
			Thread.yield();
		}

		
other
Terminate();
	}

//--------------------------------------------------------------
/**	Terminate		 																							*/
//--------------------------------------------------------------
	public void Terminate()
	{
     System.out.println("Ergebnis: " + points + " Punkte in " + timeSecs + " Sekunden\n");
     System.exit(0);
	}

//--------------------------------------------------------------
/** waitNoX																										*/
//--------------------------------------------------------------
	synchronized public void waitNoX()
	{
		try { this.wait(); }
		catch	(InterruptedException e) { }
	}

//--------------------------------------------------------------
/** sleepNoX																									*/
//--------------------------------------------------------------
	synchronized public void sleepNoX(int ticks)
	{
		try { Thread.sleep(ticks); }
		catch	(InterruptedException e) { }
	}

//--------------------------------------------------------------
/** animationStop																							*/
//--------------------------------------------------------------
	synchronized public void animationStop()
	{
		waitNoX();
	}

//--------------------------------------------------------------
/** animationGood																							*/
//--------------------------------------------------------------
	synchronized public void animationGood()
	{
		// erase box
		gs.setColor(Color.white);
		gs.fillRect(bx, by, sizeBox, sizeBox);
		
		// draw image
    gs.
other
drawImage(imgGood, bx, by, sizeBox, sizeBox, this);
    repaint(bx, by, sizeBox, sizeBox);
   	sleepNoX(500);

		// erase box
		gs.setColor(Color.white);
		gs.fillRect(bx, by, sizeBox, sizeBox);
    repaint(bx, by, sizeBox, sizeBox);
	}

//--------------------------------------------------------------
/** animationBad																							*/
//--------------------------------------------------------------
	synchronized public void animationBad()
	{
		int		i, x, y, size;
		
		// erase box
		gs.setColor(Color.white);
		gs.fillRect(bx, by, sizeBox, sizeBox);

		x		 = bx;
		y		 = by;
		size = sizeBox;

		for (i=0; i<sizeBox/2; i++, x++, y
other
++, size -= 2)
		{
			// draw image
  	  gs.drawImage(imgBad, x, y, size, size, this);
    	repaint(x-1, y-1, size+2, size+2);
	    sleepNoX(1000/(sizeBox/2));

			// erase box
			gs.setColor(Color.white);
			gs.fillRect(x, y, size, size);
  	}
	}

//--------------------------------------------------------------
/**	main				 																							*/
//--------------------------------------------------------------
	public static void main(String args[])
	{
		int		runtime = stdTime;

		if (args.length > 0)
		{
			runtime = Integer.parseInt(args[0]);
			if (runtime == 0)
				runtime = stdTime;
		}

		new Jumpbox(runtime);
	}

};