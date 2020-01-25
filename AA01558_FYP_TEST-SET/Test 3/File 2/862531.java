/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.io.*;
import java.awt.*;
import java.util.*;

public class Jumpbox extends Frame implements Runnable {

	Thread		thr;					// Thread, der die Zeit misst

	Scala		rScala;					// Richtungsskala
	Bereich		aktBereich;				// Aktionsbereich
	boolean		stopFlag = false;		// Spielende erreicht?

	long		t1;
	long		t2;

	Color []	farben = new Color[8];	// Farben
	Color []	aktFarben;				// Farben der Richtungsscala

	// Zufalszahlengenerator
	Random		rand = new Random((new Date()).getTime());


	// Konstruktor
	public Jumpbox(String s) {
		super(s);
	}


	public boolean mustStop() {
		return(stopFlag);
	}


	// Zeitmessung
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


	// Initialisierung
	public void init(long dauer) {
		t1 = dauer * 1000;

		// Farben initialisieren

		farben[0] = Color.blue;		farben[1] = Color.cyan;
		farben[2] = Color.green;	farben[3] = Color.magenta;
		farben[4] = Color.orange;	farben[5] = Color.pink;
		farben[6] = Color.red;		farben[7] = Color.yellow;

		aktFarben = waehleFarben(farben, 4);

		// Layout definieren

		setLayout(new BorderLayout());

		rScala = new Scala(aktFarben);
		rScala.setBackground(Color.white);
		rScala.resize(500,50);
		add("North", rScala);

		aktBereich = new Bereich(this, aktFarben, dauer);
		aktBereich.setBackground(Color.white);
		aktBereich.resize(500,350);
		add("Center", aktBereich);

		aktBereich.zeichneBox(false);

		// Thread activieren

		thr = new Thread(this);
		thr.start();

	}

	// Evet-Behandlung
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


	// Farben nichtdererministisch waehlen
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


	public static void main(String args[]) {
		Jumpbox		f = new Jumpbox("Jumpbox von Xxxxxxx Xxxxx #862531");
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
	}
}


class Bereich extends Canvas {

	int []		xy = new int[2];
	Color		c;
	Jumpbox		jb;
	int			punkte = 0;
	int			richtung;
	long		dauer;
	Random		rand = new Random((new Date()).getTime());
	Color []	farben;
	boolean		imgFlag = false;
	Object		sem;

	Image 		smile;
	Image 		wince;


	public Bereich(Jumpbox jb, Color [] col, long dauer) {
		super();
		this.jb = jb;
		this.dauer = dauer;
		farben = col;
		sem = this;

		// Bilder initialisieren

		wince = Toolkit.getDefaultToolkit().getImage("wince.gif");
		smile = Toolkit.getDefaultToolkit().getImage("smile.gif");
	}


	void zeichneBox(boolean flag)
	{
		Graphics g = getGraphics();
		int []	pos = new int[2];

		pos[0] = Math.abs(rand.nextInt()) % (450);
		pos[1] = Math.abs(rand.nextInt()) % (300);

		c = jb.waehleFarben(farben, 1)[0];
		for(richtung = 0; richtung < farben.length; richtung++) {
			if(farben[richtung] == c)
				break;
		}

		if(flag) {
			g.setColor(Color.black);
			g.drawLine(xy[0] + 25, xy[1] + 25, pos[0] + 25, pos[1] + 25);
			repaint(xy[0] + 25, xy[1] + 25, pos[0] + 25, pos[1] + 25);

			try {
				Thread.sleep(500);
			} catch(Exception e) {};

			g.setColor(Color.white);
			g.drawLine(xy[0] + 25, xy[1] + 25, pos[0] + 25, pos[1] + 25);
			repaint(xy[0] + 25, xy[1] + 25, pos[0] + 25, pos[1] + 25);

			g.fillRect(xy[0], xy[1], 50, 50);

			repaint(xy[0], xy[1], 50, 50);
		}
		xy = pos;
		

repaint();
	}


	public boolean mouseDrag(Event evt, int x, int y) {
		return(mouseMove(evt, x, y));
	}


	// Maus-Bewegungen behandeln
	public boolean mouseMove(Event evt, int x, int y) {
		if(jb.mustStop()) {
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

		if((x >= xy[0] && x <= xy[0] + 50) &&
		   (y >= xy[1] && y <= xy[1] + 50)) {
			punkte++;
			smileAnim();
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
			winceAnim();
			zeichneBox(true);
			return(true);
		}

		return(false);
	}


	void smileAnim() {
		Graphics	g = getGraphics();

		g.

setColor(Color.white);
		g.fillRect(xy[0], xy[1], 50, 50);
		g.drawImage(smile, xy[0], xy[1], 50, 50, this);
		repaint(xy[0], xy[1], 50, 50);

		try {
			Thread.sleep(500);
		} catch(Exception e) {};

		g.fillRect(xy[0], xy[1], 50, 50);

		g.setColor(c);
		g.fillRect(xy[0], xy[1], 50, 50);
		repaint(xy[0], xy[1], 50, 50);
	}


	void winceAnim() {
		Graphics	g = getGraphics();

		try {
			g.setColor(Color.white);
			for(int i = 0; i < 25; i++) {
				g.drawImage(wince, xy[0]+i, xy[1]+i, 50-i*2, 50-i*2, this);
				repaint(xy[0], xy[1], 50, 50);

				Thread.sleep(40);

				g.fillRect(xy[0], xy[1], 50, 50);
			}
			g.setColor(c);
			g.fillRect(xy[0], xy[1], 50, 50);
			repaint(xy[0], xy[1], 50, 50);
		} catch(Exception e) {};
	}


	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(0, 0, size().width - 1, size().height - 1);

		// Bilder initialisieren

		if(!imgFlag) {
			g.clipRect(0, 0, 0, 0);
			g.drawImage(smile, xy[0], xy[1], 50, 50, this);
			for(int i = 0; i < 25; i++)
				g.drawImage(wince, xy[0]+i, xy[1]+i, 50-i*2, 50-i*2, this);
			g.clipRect(0, 0, size().width, size().height);
			imgFlag = true;
		}

		g.setColor(c);
		g.fillRect(xy[0], xy[1], 50, 50);
	}


	public void update(Graphics g) {
		paint(g);
	}
}


class Scala extends Canvas {

	Color []	col;

	public Scala(Color [] col) {
		super();

		this.col = col;
	}

	public 

void paint(Graphics g) {
		g.setColor(col[0]);
		g.fillRect(0, 0, 50, 50);
		g.setColor(Color.black);
		g.drawString("L", 20, 30);

		g.setColor(col[1]);
		g.fillRect(50, 0, 50, 50);
		g.setColor(Color.black);
		g.drawString("R", 70, 30);

		g.setColor(col[2]);
		g.fillRect(100, 0, 50, 50);
		g.setColor(Color.black);
		g.drawString("O", 120, 30);

		g.setColor(col[3]);
		g.fillRect(150, 0, 50, 50);
		g.setColor(Color.black);
		g.drawString("U", 170, 30);
	}

	public void update(Graphics g) {
		paint(g);
	}
}