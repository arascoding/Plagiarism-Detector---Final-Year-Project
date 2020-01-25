/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.awt.*;
import java.util.*;
import java.lang.Math;

/*
 *
 * Jumpbox
 *
 */

	public boolean mouseMove(Event evt, int x, int y)
	{
		return mouseAction(evt, x, y);
	}
	
	public boolean mouseDrag(Event evt, int x, int y)
	{
		return mouseAction(evt, x, y);
	}



class Jumpbox extends Frame
{
	Color[] colors = {Color.blue, Color.cyan, Color.green, Color.magenta, Color.orange, 
		Color.pink, Color.red, Color.yellow };
	boolean[] activeColors = new boolean[8];
	Random rand = new Random(new Date().getTime());
	static Font font = new Font("TimesRoman", Font.BOLD, 18);
	Point JBoxPos = new Point(randAbsMod(60, 390), randAbsMod(110, 240));
	int JBoxDir;
	static int points = 0;
	boolean jump = false;
	boolean hit = false;
	int hitcount = 0;
	static long time;
	static long starttime;
	public Jumpbox(String title)
	{
		super(title);
	}

	public static void main(String args[])
	{
		if (args.length == 0)
			time = 15;
		else if (args.length == 1)
			time = Integer.parseInt(args[0]);
		else
		{
			System.err.println("Nur 1 Argument!");
			System.exit(1);
		}

		starttime =  System.currentTimeMillis();
		Jumpbox	mw = new Jumpbox("Xxxx Xxxxxx #826764 " + time + " : seconds");
		mw.resize(500,400);
		mw.setResizable(false);
		mw.setBackground(Color.gray);
		mw.show();
		while (System.currentTimeMillis() < starttime + (time * 1000))
		{
			try
			{
				Thread.sleep(200);
			}
			catch(InterruptedException ie){};

		}

		System.out.println("Ergebnis: " + points + " Punkte in " + time + " Sekunden");
		System.exit(0);
	}

	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		if (hitcount % 3 == 0)
			drawDirBox(g);

		if (hitcount == 0)
		{
			JBoxDir = selectDir();
			g.setColor(dirToColor(JBoxDir));
			g.fillRect(JBoxPos.x, JBoxPos.y, 50, 50);
		}

		g.setColor(Color.white);
		g.drawRect(0, 50, 500, 350);

		if (jump)
		{
			if (hit)
			{
				Image img = Toolkit.getDefaultToolkit().getImage("image/Smile.gif");
				g.drawImage(img, JBoxPos.x, JBoxPos.y, this);
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException ie){};
			}
			else
			{
				Image img = Toolkit.getDefaultToolkit().getImage("image/Wince.gif");
				
				for (int i=0; i < 25; i++)
				{
					g.clearRect(JBoxPos.x, JBoxPos.y, 51, 51);
					g.drawImage(img, JBoxPos.x + i, JBoxPos.y + i, 50 - ((50*i)/25), 50 - ((50*i)/25), this);
					try
					{
						Thread.sleep(40);
					}
					catch (InterruptedException ie){};
				}
			}

			drawJBox(g);
			hit = false;
			jump = false;
		}
	}

	
	void drawDirBox(Graphics g)
	{
		String[] dir = { "L", "R", "O", "U" };

		
		for (int i=0; i < 8; activeColors[i++] = false);
		
		int r;
		for (int i=0; i < 4;)
		{
			r = randAbsMod(0,8);
			if (!activeColors[r])
			{
				activeColors[r] = true;
				i++;
			}
		}
		
		int colindex = 0;
		for (int boxnum = 0; boxnum < 4; boxnum++)
		{
			while (!activeColors[colindex])
				colindex++;
			
			g.setColor(colors[colindex++]);
			g.fillRect(boxnum*50, 0, 50, 50);
			g.setFont(font);
			g.setColor(Color.black);
			g.drawString(dir[boxnum], boxnum*50 + 18, 30);
		}
	}

	void drawJBox(Graphics g)
	{
		Point newPos = new Point(randAbsMod(60, 390), randAbsMod(110, 240));
		g.setColor(Color.black);
		g.drawLine(JBoxPos.x + 25, JBoxPos.y + 25, newPos.x + 25, newPos.y + 25);
		
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException ie){};

		g.clearRect(JBoxPos.x, JBoxPos.y, 51, 51);
		g.setColor(getBackground());
		g.drawLine(JBoxPos.x + 25, JBoxPos.y + 25, newPos.x + 25, newPos.y + 25);

		JBoxDir = selectDir();
		g.setColor(dirToColor(JBoxDir));
		g.fillRect(newPos.x, newPos.y, 50, 50);

		JBoxPos = newPos;
	}

	int selectDir()
	{
		// liefert Zahl zw. 0 und 3 und steht fï¿½r die 4 Richtungsb. L,R,O,U
		int pos;
		while (true)
		{
			pos = randAbsMod(0,8);
			if (activeColors[pos])
				break;
		}
		int j=0;
		for (int i=0; i < pos; i++)
		{
			if (activeColors[i])
				j++;
		}
		return j;
	}

	Color dirToColor(int dir)
	{
		// liefert eine Farbe aus der Richtungs-Codierung von 0 bis 3
		int i=0;
		for (int j=0; i < 8 && j <= dir; i++)
		{
			if (activeColors[i])
				j++;
		}
		return colors[--i];
	}

	int randAbsMod(int lower, int upper)
	{
		int x = rand.nextInt();
		x = Math.abs(x);
		x = (x % (upper - lower)) + lower;

		return x;
	}



	public boolean mouseAction(Event evt, int x, int y)
	{
		if (isInSquare(x, y, JBoxPos.x - 50, JBoxPos.y - 50, 150))
		{
			if (isInSquare(x, y, JBoxPos.x, JBoxPos.y, 50))
			{
				points++;
				hit = true;
				hitcount++;
				jump = true;
				repaint();
			}
			else
			{
				int xoff = 0, yoff = 0;

				switch (JBoxDir)
				{
				case 0:
					xoff = -50;
					yoff = 0;
					break;
				case 1:
					xoff = 50;
					yoff = 0;
					break;
				case 2:
					xoff = 0;
					yoff = -50;
					break;
				case 3:
					xoff = 0;
					yoff = 50;
					break;
				}

				if (!isInSquare(x, y, JBoxPos.x + xoff, JBoxPos.y + yoff, 50))
				{
					points--;
					hitcount++;
					jump = true;
					repaint();
				}
			}
		}
		return true;
	}

	public boolean isInSquare(int mx, int my, int x, int y, int size)
	{
		if (mx >= x && my >= y && mx <= x+size && my <= y+size)
			return true;
		else
			return false;
	}

}