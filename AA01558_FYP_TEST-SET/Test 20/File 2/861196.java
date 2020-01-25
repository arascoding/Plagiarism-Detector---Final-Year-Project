/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

/** Aufgabe 5
    Jumpbox.java
    Xxxxx Xxxxxxx, MatNr.: 861196
    JDK 1.0.2 for Linux
    */

import java.awt.*;
import java.io.*;
other
import java.util.Vector;
import java.util.Random;
import java.lang.Thread;

public class Jumpbox extends Canvas implements Runnable {

  /* ----------------------------------------------------------------------

     Konstanten und globale, wichtige Variablen

     ---------------------------------------------------------------------- 
     */

  // Konstanten
  static final int maxX = 500;
  static final int maxY = 400;
  static final int boxXY = 50;
  static final int scaleY = 50;

  // Solange dauert ein Spielchen (normalerweise 15sek) ...
  static int time = 15;
  // ... und um diese (System-)Zeit ist es zu Ende.
  long endOfGame = 0;
  // Hier steht drin, was als nï¿½chstes getan werden muï¿½
  static int toDo = 0;
  static final int 
other
doNothing = 0;
  static final int doSmile = 1;
  static final int doWince = 2;
  static final int doJump = 3;

  // Buffer und dazugeh. Graphics
  Image buffer = null;
  Graphics graphics;

  // Farben, die die Box annehmen kann
  static final Color aviableColors[] = {Color.blue, Color.cyan, Color.green,
					Color.magenta, Color.orange,
					Color.pink, Color.red, Color.yellow};
  // Hier werden die vier Farben gespeichert, die momentan die Richtungen
  // angeben.
  Vector validColors;
  // Richtungsname der Farben, bzw. Richtungsnamen allgemein
  static final String possibleDirections[] = { "Links", "Rechts",
					       "Oben" , "Unten" };
  // Gibt an, von welcher Richtung man sich nï¿½hern darf
  static int rightDirection = 0;
  // Gibt an, alle wieviel Sprï¿½nge die Farben gewechselt werden sollen
  int changeColors = 3;

  // Bilderchen
  Image smileGif;
  String smileName = "NuggetBag.gif";
  Image winceGif;
  String winceName = "CrashCartoon.gif";

  // Zï¿½hlt die Anzahl der Sprï¿½nge
  int jumpCounter = 0;

  // Koordinaten der Box
  int x;
  int y;
  // Neue, zukï¿½nftige Koordinaten der Box
  int xNew;
  int yNew;

  // Punktestand
  static int points = 0;
  // 
  static int frameCounter = 0;

  //
  Random 
other
random;

  /* ----------------------------------------------------------------------

     Klassenkonstruktor, nimmt Initialisierungen vor

     ---------------------------------------------------------------------- 
     */

  // Konstruktor
  Jumpbox() {
    Toolkit tk = Toolkit.getDefaultToolkit();
    // Bilder laden
    smileGif = tk.getImage(smileName);
    winceGif = tk.getImage(winceName);
    // Auf meine Grï¿½e einstellen
    resize(maxX, maxY);
    // Um diese (System-)Zeit ist das Spiel zu Ende
    endOfGame = System.currentTimeMillis() + (1000 * time);
    // Initialisierung des Zufallsgenerators - geht mit der Systemzeit
    // am einfachsten da indeterministisch.
    random = new Random(System.currentTimeMillis());
  };

  /* ----------------------------------------------------------------------

     Hauptroutine des Threads - Endlosschleife, die bei Bedarf reagiert

     ---------------------------------------------------------------------- 
     */

  // run
  public void run() {
    // Zeit zum Verschlafen
    int rest = 0;
    while (true) {
      switch (toDo) {
      case doSmile:
	if ((rest = showSmile(graphics, frameCounter++)) > 0) {
	  try {
	    Thread.sleep(rest);
	  } catch (InterruptedException ie) {
	  }
	} else {
	  frameCounter = 0;
	  toDo = doJump;
	  Thread.yield();
	}
	break;
      case doWince:
	if ((rest = showWince(graphics, frameCounter++)) > 0) {
	  try {
	    Thread.sleep(rest);
	  } catch (InterruptedException ie) {
	  }
	} else {
	  frameCounter = 0;
	  toDo = doJump;
	  Thread.yield();
	}
	break;
      case doJump:
	if ((rest = showJump(graphics, frameCounter++)) > 0) {
	  try {
	    Thread.sleep(rest);
	  } catch (InterruptedException ie) {
	  }
	} else {
	  try {
	    synchronized(this) {
	      toDo = doNothing;
	      this.wait();
	    }
	  } catch (InterruptedException ie) {
	  }
	}
	break;
      case doNothing:
      default:
	try {
	  synchronized(this) {
	    this.wait();
	  }
	} catch (InterruptedException ie) {
	}
      }
    }
  
other
}

  /* ----------------------------------------------------------------------

     Initialisierung des Spielfeldes

     ---------------------------------------------------------------------- 
     */

  // paint - Hier wird das Spielfeld erzeugt bzw. im Buffer gemalt
  public void paint(Graphics g) {
    update(g);
  }

  public void update(Graphics g) {
    // Am Anfang muï¿½ der Buffer initialisiert werden
    if (buffer == null) {
      buffer = createImage(maxX, maxY);
      graphics = buffer.getGraphics();
      graphics.setColor(Color.white);
      graphics.drawRect(0, scaleY, maxX-1, maxY-scaleY-1);
      // Problem: Lï¿½cheln und Grummeln wird beim ersten Mal nicht angezeigt,
      // deshalb zeiche ich beide Images hier am Anfang in den leeren Raum.
      // Grummeln muï¿½ sogar 'animiert' werden, sonst erscheint es nicht als
      // Animation, sondern nur als Einzelbild. Leider ist mir keine bessere
      // Lï¿½sung eingefallen.
      // Scheint ein Problem vom Linux-JDK-1.0.2 zu sein
      showSmile(graphics, 0);
      int dummy = 0;
      int dummyXY;
      while (dummy < (boxXY/2)) {
	dummyXY = boxXY - (2 * dummy);
	undoBox(g);
	g.drawImage(winceGif, dummy, dummy, dummyXY, dummyXY, this);
	repaint(0, 0, boxXY, boxXY);
	dummy++;
      }
      // Neue Koordinaten 'berechnen'.
      xNew = getRandomInt(1, maxX - boxXY - 1);
      yNew = getRandomInt(scaleY + 1, maxY - boxXY - 1);
      x = xNew;
      y = yNew;
      if ((jumpCounter++ % changeColors) == 0) {
	selectNewColors(graphics);
      }
      rightDirection = getRandomInt(0, 3);
      doBox(graphics);
    }
    g.drawImage(buffer, 0, 0, this);
  }

  /* ----------------------------------------------------------------------

     Auswahl der gerade 'richtungsweisenden' Farben aus den mï¿½glichen

     ---------------------------------------------------------------------- 
     */

  // selectNewColors - Hier werden die 4 neuen Richtungsfarben gewï¿½hlt
  // und dargestellt.
  void selectNewColors(Graphics g) {
    // leeren des alten Vectors
    validColors = new Vector(4);
    // Kopie der mï¿½glichen Farben anfertigen, aus der immer die schon
    // gewï¿½hlte entfernt wird, um keine Doppelten zu bekommen.
    Vector copyOfAviableColors = new Vector(aviableColors.length);
    for (int i = 0; i < aviableColors.length; i++) {
      copyOfAviableColors.addElement(aviableColors[i]);
    }
    for (int i = 0; i < 4; i++) {
      int r = getRandomInt(0, copyOfAviableColors.size() - 1);
      validColors.addElement(copyOfAviableColors.elementAt(r));
      // Und gleich Zeichnen
      g.setColor((Color)copyOfAviableColors.elementAt(r));
      g.fillRect(i * boxXY, 0, boxXY, boxXY);
      g.setColor(Color.black);
      g.drawString(possibleDirections[i], (i * boxXY), boxXY/2);
      // 'Verbrauchtes' Element aus Kopie entfernen
      copyOfAviableColors.removeElementAt(r);
    }
  };

  /* ----------------------------------------------------------------------

     Methoden zum Zeichnen und Lï¿½schen der Box

     ---------------------------------------------------------------------- 
     */

  // doBox - malt die neue Box an den neuen Koordianten
  void doBox(Graphics g) {
    g.
other
setColor((Color) validColors.elementAt(rightDirection));
    g.fillRect(x, y, boxXY, boxXY);
  }

  // undoBox - lï¿½scht die alte Box durch ï¿½bermalen mit Hintergrundfarbe
  void undoBox(Graphics g) {
    g.setColor(getBackground());
    g.fillRect(x, y, boxXY, boxXY);
  }
    
  /* ----------------------------------------------------------------------

     Methoden fï¿½r die Mouseevents

     ---------------------------------------------------------------------- 
     */

  // mouseMove
  synchronized public boolean mouseMove(Event event, int mouseX, int mouseY) {
    // Wenn die Zeit um ist -> raus!
    if (System.currentTimeMillis() > endOfGame) {
      gameOver();
    }
    if (toDo == doNothing) {
      // Habe ich mit der Mouse die Box getroffen?
      if (boxHit(mouseX, mouseY)) {
	points++;
	frameCounter = 0;
	toDo = doSmile;
	this.notify();
      } else if (isInIntimateRegion(mouseX, mouseY)) {
	points--;
	frameCounter = 0;
	toDo = doWince;
	this.notify();
      } else {
      }
    }
    return true;
  }

  // mouseDrag -- Es soll das gleiche geschehen wie bei mouseMove
  synchronized public boolean mouseDrag(Event event, int mouseX, int mouseY) {
    return mouseMove(event, mouseX, mouseY);
  }

  /* ----------------------------------------------------------------------

     Methoden zum Prï¿½fen, wo die Mouse ist

     ---------------------------------------------------------------------- 
     */

  // boxHit - wurde die Box von der Mouse 'getroffen'?
  boolean boxHit(int mouseX, int mouseY) {
    if (mouseX >= x && mouseX < (x + boxXY) &&
	mouseY >= y && mouseY < (y + boxXY)) {
      return true;
    }
    return false;
  }

  // isInIntimateRegion
  boolean isInIntimateRegion(int mouseX, int mouseY) {
    // Zuerst wird geprï¿½ft, ob die Koordinaten in einer rechteckigen Umgebung
    // mit +/-boxXY um die Box liegen -> Verdacht auf Intimbereich
    int minSuspiciousX = x - boxXY;
    int maxSuspiciousX = x + (2 * boxXY);
    int minSuspiciousY = y - boxXY;
    int maxSuspiciousY = y + (2 * boxXY);
    if (mouseX >= minSuspiciousX && mouseX < maxSuspiciousX &&
	mouseY >= minSuspiciousY && mouseY < maxSuspiciousY) {
      // Jetzt wir geguckt, ob sie die Mouse in der Box befindet, oder auf dem
      // Gebiet der richtigen Richtung -> ist beides erlaubt
      if (boxHit(mouseX, mouseY)) {
	return false;
      }
      int checkX = x;
      int checkY = y;
      switch (rightDirection) {
      case 0:
	checkX = x - boxXY;
	break;
      case 1:
	checkX = x + boxXY;
	break;
      case 2:
	checkY = y - boxXY;
	break;
      case 3:
	checkY = y + boxXY;
	break;
      }
      if (mouseX >= checkX && mouseX < (checkX + boxXY) &&
	  mouseY >= checkY && mouseY < (checkY + boxXY)) {
	return false;
      }
      return true;
    }
    return false;
  };

  /* ----------------------------------------------------------------------

     Methoden zum Zeichen der Smileies bzw. der Linie

     ----------------------------------------------------------------------
     */

  // showSmile - Zeigt das Lï¿½cheln
  int showSmile(Graphics g, int frame) {
    if (frame == 0) {
      undoBox(g);
      g.drawImage(smileGif, x, y, boxXY, boxXY, this);
      repaint(x, y, boxXY, boxXY);
      return 500;
    } else {
      doBox(g);
      repaint(x, y, boxXY, boxXY);
    }
    return 0;
  }      

  // showWince - Zeigt das Grummeln
  int showWince(Graphics g, int frame) {
    int newXY;
    if (frame >= 0 && frame < (boxXY/2)) {
      newXY = boxXY - (2 * frame);
      undoBox(g);
      g.drawImage(winceGif, x + frame, y + frame, newXY, newXY, this);
      repaint(x, y, boxXY, boxXY);
      return 500/newXY;
    } else {
      doBox(g);
      repaint(x, y, boxXY, boxXY);
      return 0;
    }
  }

  // showJump - Zeichnet die Linie zum nï¿½chsten Platz der Box
  int showJump(Graphics g, int frame) {
    if (frame == 0) {
      // Line zeichnen
      xNew = getRandomInt(1, maxX - boxXY - 1);
      yNew = getRandomInt(boxXY + 1, maxY - boxXY - 1);
      g.setColor(Color.black);
      g.drawLine(x + (boxXY/2), y + (boxXY/2),
		 xNew + (boxXY/2), yNew + (boxXY/2));
      repaint();
      return 500;
    } else {
      // Linie & alte Box lï¿½schen, neue zeichenen
      g.setColor(getBackground());
      g.drawLine(x + (boxXY/2), y + (boxXY/2),
		 xNew + (boxXY/2), yNew + (boxXY/2));
      undoBox(g);
      x = xNew;
      y = yNew;
      if ((jumpCounter++ % changeColors) == 0) {
	selectNewColors(g);
      }
      rightDirection = getRandomInt(0, 3);
      doBox(g);
      repaint();
      return 0;
    }      
  }

  /* ----------------------------------------------------------------------

     Hilfsmethode fï¿½r die Zufallszahlen

     ---------------------------------------------------------------------- 
     */

  // getRandomInt - liefert Zufallszahl zwischen min und max zurï¿½ck
  int getRandomInt(int min, int max) {
    return (Math.abs(random.
other
nextInt()) % (max - min + 1)) + min;
  };

  // gameOver -- Ende der Vorstellung
  void gameOver() {
    System.out.println("GAME OVER!\nErgebnis: " + points + " Punkte in " + 
		       time + " Sekunden.");
    System.exit(0);
  }

  /* ----------------------------------------------------------------------

     Hauptprogramm

     ---------------------------------------------------------------------- 
     */

  // Main
  public static void main(String args[]) {
    // Wurde eine  angegeben?
    if (args.length != 0) {
      // Wandle erstes Argument in Integer
      try {
	time = Integer.parseInt(args[0]);
      } catch ( RuntimeException rte) {
      }
    
other
}
    // Frame aufbauen
    Frame mainFrame = new Frame("JumpBox - Xxxxx Xxxxxxxx, #861196");
    Jumpbox jb = new Jumpbox();
    mainFrame.add("Center", jb);
    mainFrame.pack();
    mainFrame.show();
    // Thread starten
    (new Thread(jb)).start();
  }

}