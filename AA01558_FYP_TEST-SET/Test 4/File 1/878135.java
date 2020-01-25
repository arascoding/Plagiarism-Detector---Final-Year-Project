/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.io.*;
import java.awt.*;
import java.util.*;
import java.applet.Applet;

public class Jumpbox extends Frame implements Runnable {

	Thread		thr;					

	static Scala		rScala;				
	static Bereich		aktBereich;		
	boolean		stopFlag = false;

	long		dauer;			

	long		t1;
	long		t2;

	int			punkte = 0;	

	int			richtung;

	Color []	farben = new Color[8];
	Color []	aktFarben;			

	Random		rand;			

	static Image		smile = Toolkit.getDefaultToolkit().getImage("smile.gif");
	static Image		wince = Toolkit.getDefaultToolkit().getImage("wince.gif");


	public Jumpbox(String s) {
		super(s);
	}


	public void run() {
		long	t0 = (new Date()).getTime();
		try {
			while(t0 + t1 > (new Date()).getTime()) {
				Thread.sleep(1000);
				Thread.yield();
			}
		} catch(InterruptedException e) {}

		stopFlag = true;
	}


	public void init(long dauer) {
		this.dauer = dauer;
		t1 = dauer * 1000;


		rand = new Random((new Date()).getTime());


		farben[0] = Color.blue;		farben[1] = Color.cyan;
		farben[2] = Color.green;	farben[3] = Color.magenta;
		farben[4] = Color.orange;	farben[5] = Color.pink;
		farben[6] = Color.red;		farben[7] = Color.yellow;

		aktFarben = waehleFarben(farben, 4);


		setLayout(new BorderLayout());

		rScala = new Scala();
		rScala.setBackground(Color.white);
		rScala.resize(500,50);
		add("North", rScala);

		aktBereich = new Bereich();
		aktBereich.setBackground(Color.white);
		aktBereich.resize(500,350);
		add("Center", aktBereich);

		zeichneBox(false);

		rScala.setBoxColor(aktFarben);


		thr = new Thread(this);
		thr.start();

	}

	void zeichneBox(boolean flag)
	{
		int [] pos = nextPos(500,350);
		Color c = waehleFarben(aktFarben, 1)[0];
		richtung = getRichtung(aktFarben, c);

		if(flag) {
			aktBereich.drawLine(pos, true);
			try {
				Thread.sleep(500);
			} catch(Exception e) {};
			aktBereich.drawLine(pos, false);
			aktBereich.eraseBox();
		}
		aktBereich.setBoxPos(pos, c);
		aktBereich.setBoxVisible(true);
		rScala.setBoxColor(aktFarben);
		rScala.repaint();
		aktBereich.repaint();
	}


	public boolean handleEvent(Event evt) {
		switch (evt.id) {
		case Event.WINDOW_DESTROY:
			System.exit(0);
			return(true);
		case Event.WINDOW_ICONIFY:
			thr.suspend();
			t2 = (new Date()).getTime();
			return(true);
		case Event.WINDOW_DEICONIFY:
			t1 += (new Date()).getTime() - t2;
			thr.resume();
			return(true);
		default:
			return super.handleEvent(evt);
		}
	}


	public boolean mouseDrag(Event evt, int x, int y) {
		return(mouseMove(evt, x, y));
	}

	public boolean mouseMove(Event evt, int x, int y) {
		if(stopFlag) {
			System.out.print("Ergebnis: ");
			System.out.print(punkte);
			if(punkte == 1)
				System.out.print(" Punkt in ");
			else
				System.out.print(" Punkte in ");
			System.out.print(dauer);
			System.out.println(" Sekunden");
			System.exit(0);
		}

		int [] xy = aktBereich.getBoxPos();

		// HACK!!!
		y -= 80;

		if((x >= xy[0] && x <= xy[0] + 50) &&
		   (y >= xy[1] && y <= xy[1] + 50)) {
			punkte++;
			animSmile(smile, 500);
			zeichneBox(true);
			return(true);
		}

		int x1 = xy[0] - 50;	x1 = x1 < 0 ? 0 : x1;
		int y1 = xy[1] - 50;	y1 = y1 < 0 ? 0 : y1;

		if((x >= x1 && x <= x1 + 150) &&
		   (y >= y1 && y <= y1 + 150)) {
			switch(richtung) {
			case 0:			// links
				if(x < xy[0] && !(y < xy[1] || y > xy[1] + 50))
					return(true);
				break;
			case 1: 		// rechts
				if(x > xy[0] + 50 && !(y < xy[1] || y > xy[1] + 50))
					return(true);
				break;
			case 2:			// oben
				if(y < xy[1] && !(x < xy[0] || x > xy[0] + 50))
					return(true);
				break;
			case 3:			// unten
				if(y > xy[1] + 50 && !(x < xy[0] || x > xy[0] + 50))
					return(true);
				break;
			}
			punkte--;
			animSmile(wince, 1000);
			zeichneBox(true);
			return(true);
		}

		return(false);
	}


	void animSmile(Image img, int time) {
		aktBereich.drawImage(img);
		try {
			Thread.sleep(time);
		} catch(Exception e) {};
		aktBereich.drawBox();
	}


	Color [] waehleFarben(Color [] farben, int num) {
		int			len = farben.length;
		Color [] 	f = new Color[num];
		boolean []	u = new boolean[len];
		int			z = 0;
		int			r;

		for(int i = 0; i < len; i++)
			u[i] = false;

		while(z < num) {
			r = Math.abs(rand.nextInt()) % len;
			if( r < 0)
				r = r + len;
			if(!u[r]) {
				u[r] = true;	// farbe benutzen
				f[z] = farben[r];
				z++;
			}
		}

		return(f);
	}


	int getRichtung(Color [] farben, Color c) {
		for(int i = 0; i < farben.length; i++) {
			if(farben[i] == c)
				return(i);
		}
		return(-1);
	}


	int [] nextPos(int w, int h) {
		int []	res = new int[2];

		res[0] = Math.abs(rand.nextInt()) % (w - 50);
		res[1] = Math.abs(rand.nextInt()) % (h - 50);

		return(res);
	}


	public static void main(String args[]) {
		Jumpbox		f = new Jumpbox("Java Jumpbox von Xxxxx Xxxxxxxx #878135");
		long		dauer;

		switch(args.length) {
		case 0:
			dauer = 15;
			break;
		case 1:
			try {
				Long d = new Long(args[0]);
				dauer = d.longValue();
				break;
			} catch (NumberFormatException e) { }
		default:
			System.err.println("Usage: Jumpbox [sec]");
			return;
		}

		f.init(dauer);
		f.resize(510,430);
		f.show();
		rScala.drawImage(smile, 200,0);
		rScala.drawImage(wince,250,0);
	}
}


class Bereich extends Canvas {

	int			x;
	int			y;
	Color		c;
	boolean		visible = false;

	public void setBoxVisible(boolean v) {
		visible = v;
	}

	public void setBoxPos(int [] xy, Color c) {
		x = xy[0];
		y = xy[1];
		this.c = c;
	}

	public int [] getBoxPos() {
		int [] xy = new int[2];
		xy[0] = x;
		xy[1] = y;
		return(xy);
	}

	public void clipRect(int x, int y, int w, int h) {
		Graphics g = getGraphics();
		g.clipRect(x, y, w, h);
	}

	public void drawImage(Image img) {
		Graphics g = getGraphics();
		g.drawImage(img, x, y, 50, 50, this);
		repaint(x, y, 50, 50);
	}

	public void drawBox() {
		if(visible) {
			Graphics g = getGraphics();
			g.setColor(c);
			g.fillRect(x, y, 50, 50);
			repaint(x, y, 50, 50);
		}
	}

	public void drawLine(int pos[], boolean flag) {
		Graphics g = getGraphics();
		if(flag)
			g.setColor(Color.black);
		else
			g.setColor(Color.white);
		g.drawLine(x + 25, y + 25, pos[0] + 25, pos[1] + 25);
		repaint(x + 25, y + 25, pos[0] + 25, pos[1] + 25);
	}

	public void eraseBox() {
		if(visible) {
			Graphics g = getGraphics();
			g.setColor(Color.white);
			g.fillRect(x, y, 50, 50);
		}
		repaint(x, y, 50, 50);
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(0, 0, size().width - 1, size().height - 1);

		if(visible) {
			g.setColor(c);
			g.fillRect(x, y, 50, 50);
		}
	}
}


class Scala extends Canvas {

	Color	c1, c2, c3, c4;

	public void setBoxColor(Color [] col) {
		c1 = col[0];
		c2 = col[1];
		c3 = col[2];
		c4 = col[3];
	}

 
    public void drawImage(Image img,int x,int y) {
      Graphics g = getGraphics();
        g.drawImage(img, x, y, 50, 50, this);
        repaint(x, y, 50, 50);
    }
 
	public void paint(Graphics g) {
		g.setColor(c1);
		g.fillRect(0, 0, 50, 50);
		g.setColor(Color.black);
		g.drawString("L", 20, 30);

		g.setColor(c2);
		g.fillRect(50, 0, 50, 50);
		g.setColor(Color.black);
		g.drawString("R", 70, 30);

		g.setColor(c3);
		g.fillRect(100, 0, 50, 50);
		g.setColor(Color.black);
		g.drawString("O", 120, 30);

		g.setColor(c4);
		g.fillRect(150, 0, 50, 50);
		g.setColor(Color.black);
		g.drawString("U", 170, 30);
	}
}