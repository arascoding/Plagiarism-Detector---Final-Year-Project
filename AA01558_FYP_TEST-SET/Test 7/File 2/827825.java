/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.awt.*;
import java.lang.*;
import java.util.*;

public class Jumpbox extends Canvas implements Runnable {

	// Zufallsgenerator
	Random Zufall;
	// Buchstaben, die die Orientierung einer Box angeben
	String[] Orientierung = { "L", "R", "O", "U"};
	// Farben der Richtungsboxen  L R O U
	Color[] Boxen = {Color.black,Color.black,Color.black,Color.black};
	// Typ der Richtungsbox B (Index auf Boxen L R O U)
	int 
other
B = 0;
	// Koordinaten der Maus,  von B, von neuem B
	int Bx, By, newBx, newBy;
	
	// Bilder bei Erfolg, Punktverlust, aktuell darzustellen
	Image Winner, Loser;
	// fuer DoubleBuffer
	Image offscreen;
	Graphics offscreengr;
	// dient dazu festzuhalten, wann die Richtungsboxen neu zu zeichnen sind
	int refresh = 0;
	
	// Variablen fuer Animationschleife
        Thread animatorThread;
	int 
other
imageWidth=50;


	//  Zeiten bzw. Ergebnisse
	long startTime = 0;
	static long playTime = 15;
	long Punkte = 0;
	
	// zeigt an, was gezeichnet werden soll
	int drawmode = -2;
	// speichert letzte Aktion
	final static int idle = 1;
	final static int exit = 2;
	final static int wince = 3;
	final static int smile = 4;
		


/***** Methoden zur Eventbehandlung *****/


	private boolean neutral (int x, int y) {
		// Hilfsfunktion, die abklaert ob Mauszeiger im neutralen Bereich ist,
		// wenn man von ausgeht, dass  sich die Maus schon im kritischen Bereich befindet
		boolean result = false;
		
		switch (B) {
			case 0: // L
				if ( (x <= Bx) && !( (y < By) || (y > By+50))) 
					result = true;
					break;
			case 1: // R
				if ( (x >= Bx+50) && !( (y < By) || (y > By+50))) 
					result = true;
					break;
			case 2: // O
				if ( (y <= By) && !( (x < Bx) || (x > Bx+50)) ) 
					result = true;
					break;
			case 3: // U
				if ( (y >= By+50) && !( (x < Bx) || (x > Bx+50)) ) 
					result = true;
					break;
		}
		return result;
	}
	
	synchronized private void mouseControl(int x, int y) {
		if (((System.currentTimeMillis()/1000) < ((startTime/1000) + playTime))
				 && (drawmode == idle)) {
			// pruefe auf kritischen Bereich
			if ( ((Bx-50) < x) && (x < (Bx+100)) &&
				 ( ((By-50) < y) && (51 < y)) && (y < (By+100)) ) {
					// Zielbereich (Gewonnen)
					if ( (Bx < x) && (x < (Bx+50)) &&
						 (By < y) && (y < (By+50)) ) {
						Punkte++;
						drawmode = smile;
						this.notify();
					} else {
						// intimer Bereich (Verloren)
						if (!(neutral(x,y))) {
							Punkte--;
							drawmode = wince;
							this.notify();
						}
					}
			}
			
		} else { 
			if (!((System.currentTimeMillis()/1000) < ((startTime/1000) + playTime)))
				drawmode = exit;
				this.notify();
		}
	}
	
    synchronized public boolean mouseDown(Event e, int x, int y) {
		mouseControl( x, y);
    	return true;
    }

    synchronized public boolean mouseUp(Event e, int x, int y) {
		mouseControl( x, y);
    	return true;
    }

    synchronized public boolean mouseMove(Event e, int x, int y) {
		mouseControl( x, y);
    	return true;
    }

    synchronized public boolean mouseDrag(Event e, int x, int y) {
		mouseControl( x, y);
    	return true;
    }


/***** Methoden zum zufaelligen Wechsel von Farben, etc. *****/


    public void shuffleBcoords() {
    	// erzeugt zufaellig neue Koordinaten vom aktuellen B
    	// (wird nur zur Initialisierung des Spiels benoetigt)
    	// von 1 bis 448 
    	Bx = new Float(((Zufall.nextFloat())*448)+1).intValue();
    	// von 51 bis 348 
    	By = new Float(((Zufall.nextFloat())*298)+51).intValue();
    }

    public void shuffleNewBcoords() {
    	// erzeugt zufaellig neue Koordinaten vom naechsten B
    	// von 1 bis 448 
    	newBx = new Float(((Zufall.nextFloat())*448)+1).intValue();
    	// von 51 bis 348 
    	newBy = new Float(((Zufall.nextFloat())*298)+51).intValue();
    }

    public void shuffleB() {
    	// sucht zufaellig eine bestimmte Art von Richtungsbox aus
    	B = new Float((Zufall.nextFloat())*4).intValue();
    }

    public void shuffleRichtungsboxen() {
    	// erzeugt zufaellige neue Farben fuer die Richtungsboxen
    	int[] a = {1,1,1,1};
    	// erzeuge 4 verschiede Zahlen von 1 bis 8, die jeweils Repraesentationen
    	// einer Farbe sind
    	for(int i = 0; i < 4; i++) {
    		a[i] = new Float(((Zufall.nextFloat())*8)+1).intValue();
    		int pointer = 0;
    		// Stelle sicher, dass jede Farbe nur einmal vorkommen wird
    		// (terminiert sicher)
    		while (i > pointer) {
    			if (a[i] == a[pointer]) {
    				a[i] = new Float(((Zufall.nextFloat())*8)+1).intValue();
    				pointer = 0;
    			} else {pointer++;}
    		}
    	}
    	// wandle die Zufallszahlen in Zufallsfarben um
		for (int i = 0; i < 4; i++) {
			switch (a[i]) {
				case 1 : Boxen[i] = Color.blue; break;
				case 2 : Boxen[i] = Color.cyan; break;
				case 3 : Boxen[i] = Color.green; break;
				case 4 : Boxen[i] = Color.magenta; break;
				case 5 : Boxen[i] = Color.orange; break;
				case 6 : Boxen[i] = Color.pink; break;
				case 7 : Boxen[i] = Color.red; break;
				case 8 : Boxen[i] = Color.yellow; break;
			}
		}
    };
    

/***** Methoden zum Zeichen *****/


    //Draw the current frame of animation.
	private void clearSurface(Graphics offscreengr) {
		offscreengr.clearRect(0,0,500,400);
	}

	private void drawRichtungsboxes(Graphics offscreengr) {
	// aktuelle Richtungsboxen mit Beschriftung zeichnen
		int x1 = 0;
		offscreengr.clearRect(0, 0, 200 , 50);
		
other
for (int i = 0; i < 4; i++) {
			offscreengr.setColor(Boxen[i]);
			offscreengr.fillRect(x1,0,50,50);
			offscreengr.setColor(getForeground());
			offscreengr.drawString(Orientierung[i], x1 + 25, 0 + 25);
			x1 += 50;
		}
	}

	private void drawSpielrahmen(Graphics offscreengr) {
			offscreengr.setColor(getForeground());
			offscreengr.drawRect(0,50,499,349);
	}
	
	private void drawB(Graphics offscreengr, boolean highBox) {
		if (highBox) { // Box anzeigen
			offscreengr.setColor(Boxen[B]);
			offscreengr.fillRect(Bx, By, 50, 50);
		} else { // Box loeschen
			offscreengr.setColor(getBackground());
			offscreengr.fillRect(Bx, By, 50, 50);
		}
	}
	
	private void drawDirectionLine(Graphics offscreengr, boolean highLine) {
		if (highLine) offscreengr.setColor(getForeground());
		else offscreengr.setColor(getBackground());
		offscreengr.drawLine(Bx + 25, By + 25, newBx + 25, newBy + 25);
	}
	

	private void jump() {
		// zeichne Springen- Sequenz
   		shuffleNewBcoords();
	   	drawDirectionLine(offscreengr,true);
	   	drawB(offscreengr,true);
    	repaint();
    	sleep(500);
	   	drawDirectionLine(offscreengr,false);
	   	drawB(offscreengr,false);
    	repaint();
		// pruefe, ob Richtungdboxen neue Farben bekommen sollen
    	if (refresh == 2) {
    		refresh = 0;
    		shuffleRichtungsboxen();
    		drawRichtungsboxes(offscreengr);
    		repaint(0,0,200,50);
    	} else { refresh++; }
    	Bx = newBx;
    	By = newBy;
    	
other
shuffleB();
	   	drawB(offscreengr,true);
    	repaint(Bx, By, 50, 50);
	};
	
	public void screeninit() {
		clearSurface(offscreengr);
		drawRichtungsboxes(offscreengr);
		drawSpielrahmen(offscreengr);
		drawB(offscreengr,true);
	
other
};

    public void paint(Graphics g) {
		// zeichne alles neu, z.B. wenn Fenstergroesse veraendert wurde
		update(g);
	}    

    public void update(Graphics g) {
 		if (offscreengr == null) {
         	offscreen = createImage(500,400);
            offscreengr = offscreen.getGraphics();
			screeninit();
		};
		g.drawImage(offscreen,0,0,this);
	}
 
 
/***** Methoden zum Ablaufkontrolle *****/


	Jumpbox() {
        // Initialisiere Zufallsgenerator mit Systemzeit
        Zufall = new Random();
        // Suche 4 verschieden Zufallsfarben fuer die Richtungsboxen aus
        shuffleRichtungsboxen();
        // suche zufaellig neue Farbe fuer B
        shuffleB();
        // suche zufaellig Position von erster Box
        shuffleBcoords();
        // Grundfarben setzen
        setBackground(Color.white);
        setForeground(Color.black);
		// benoetigte Bilder laden
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Winner = toolkit.getImage("smile.gif");
        Loser = toolkit.getImage("wince.gif");
        // resize
        resize(500,400);
		// StartZeit festhalten
        startTime = System.currentTimeMillis();
        drawmode = idle;
     }
     
     private 
other
void sleep(int time) {
     	try { animatorThread.sleep(time); } catch (InterruptedException iec) {}
     }

    public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		for(;;) {
    	switch (drawmode) {
    		case idle:
    			try { synchronized (this) {
    				this.wait(); }
    			} catch (InterruptedException iec) {}
    			break;
    		case smile: // Animation laufen lassen
   				offscreengr.drawImage(Winner, Bx, By, 50, 50, this);
    			repaint(Bx, By, 50, 50);
   				sleep(500);
				jump();
   				drawmode = idle;
    			break;
     		case wince: // Animation laufen lassen
				while (imageWidth >= 2) {
 				offscreengr.clearRect(Bx, By, 50, 50);
   				offscreengr.drawImage(Loser, Bx + ((50-imageWidth)/2), By + ((50-imageWidth)/2),imageWidth,imageWidth, this);
    				repaint(Bx, By, 50, 50);
    				imageWidth -=2;
    				sleep(40);

    			}
    			imageWidth = 50;
   				sleep(500);
				jump();
   				drawmode = idle;
    			break;
    		case exit: // Spiel beenden
    			System.out.println("Ergebnis: "+Punkte+" in "+playTime+" Sekunden");
    			System.out.println("(fuer Mac user wird die Anzeige 2 sec gehalten)");
    			sleep(2000);
				System.exit(0);
    	}
    	}
    }
    
    public static void main(String args[]) {
        // hole Spielzeit aus Kommandozeile
        if (args.length > 0) {
            try {
                playTime = Integer.parseInt(args[0]);
            } catch (Exception e) { playTime = 15;}
        }
		Frame 
other
tanteTrude = new Frame("Xxxx Xxxxxxx  0827825");
        Jumpbox jmp = new Jumpbox();
		tanteTrude.add("Center", jmp);
		tanteTrude.pack();
		// zeigen was Sache ist
		tanteTrude.show();
		//Animations Thread starten
		(new Thread(jmp)).start();
    }
}