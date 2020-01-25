/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.awt.*;
import java.util.Random;
import java.util.Vector;


public class Jumpbox extends Canvas implements Runnable {
  static final int zNormal = 0, zLachen = 1, zWeinen = 2, zSprung = 3;
  static final String rStr[] = {"L", "R", "O", "U"};
         Random    ran;
         int       Dauer = 15;
         long      ende;
         Image     im = null;
         Graphics  gs = null;
         int       Zustand  = zNormal;
         int       Punkte = 0;
         int       Anzahl = 0;
         int       Richtung;
         Vector    Farben;
         int       Bx, By, NeuX, NeuY;
  Image  iLachen;
  Image  iWeinen;

  // Konstruktor
  Jumpbox(int D) {
    Toolkit tk = Toolkit.getDefaultToolkit();
    iLachen = tk.getImage("smile.gif");
    iWeinen = tk.getImage("wince.gif");
    Dauer = D;
    resize(500, 400);
    ran = new Random(System.currentTimeMillis());
    ende = System.currentTimeMillis() + 1000 * Dauer;
    Farben = new Vector(8);
    Farben.addElement(Color.blue);
    Farben.addElement(Color.cyan);
    Farben.addElement(Color.green);
    Farben.addElement(Color.magenta);
    Farben.addElement(Color.orange);
    Farben.addElement(Color.pink);
    Farben.addElement(Color.red);
    Farben.addElement(Color.yellow);
  }

  
  // Main
  public static void main(String args[]) {
    int Dauer = 15;
    if (args.length > 0) {
      Dauer = Integer.parseInt(args[0]);
    };
    Frame f = new Frame("Xxxxxx Xxxxx   #862246");
    Jumpbox j = new Jumpbox(Dauer);
    f.add("Center", j);
    f.pack();
    f.show();
    Thread t = new Thread(j);
    t.start();
  }

  // run
  public void run() {
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    for (;;) {
      if (Zustand == zNormal) {
        try { synchronized(this) { this.wait(); } 
        } catch (InterruptedException e) { ; }
      } else if (Zustand == zSprung) {
        Sprung(gs);
        try {
          synchronized (this) {
            Zustand = zNormal;
            this.wait();
	  }
	} catch (InterruptedException e) { ; }
      } else if (Zustand == zLachen) {
        Lachen(gs);
        Zustand = zSprung;
        Thread.yield();
      } else if (Zustand == zWeinen) {
        Weinen(gs);
        Zustand = zSprung;
        Thread.yield();
      }
    }
  }

  
  // ---- Paint ----
  public void paint(Graphics g) {
    update(g);
  }

  // ---- update ----
  public void update(Graphics g) {
    if (im == null) {
      im = createImage(500, 400);
      gs = im.getGraphics();
      g.setColor(Color.white);
      g.drawRect(0, 50, 499, 349);
      // erzeuge neue Box und Farben
      neueBox();
      neueFarben();
      // zeichne neue Box
      Bx = NeuX; By = NeuY;
      Richtung = Math.abs(ran.nextInt()) % 4;
      g.setColor((Color)Farben.elementAt(Richtung));
      g.fillRect(Bx, By, 50, 50);        
      repaint();
    }
    g.drawImage(im, 0, 0, this);
  }


  // ---- mouseMove ----
  synchronized public boolean mouseMove(Event e, int x, int y) {
    if (System.currentTimeMillis() > ende) {
      ergebnis();
    }
    if (x >= Bx && x < Bx + 50 &&
        y >= By && y < By + 50 && 
        Zustand == zNormal) {
      // B wurde korrekt erreicht
      Punkte++;
      Zustand = zLachen;
      this.notify();
    } else if (falscheAnnaeherung(x, y) && Zustand == zNormal) {
      // B wurde falsch erreicht
      Punkte--;
      Zustand = zWeinen;
      this.notify();
    }
    return true;
  }

  // ---- mouseDrag ----
  synchronized public boolean mouseDrag(Event e, int x, int y) {
    return mouseMove(e, x, y);
  }

  
  // gibt das Ergebnis aus
  void ergebnis() {
    System.out.println("Ergebnis: " + Punkte + " Punkte in " + 
                       Dauer + " Sekunden");
    System.exit(0);
  }

  // ---- Sprung ----
  void Sprung(Graphics g) {
    // erzeuge neue Box
    neueBox();
    // zeichne Linie zur neuen Box
    g.setColor(Color.black);
    g.drawLine(Bx + 25, By + 25, NeuX + 25, NeuY + 25);
    repaint();
    // warte
    try { Thread.sleep(500); } catch (InterruptedException e) { ; }
    // lï¿½sche Linie und alte Box
    g.setColor(getBackground());
    g.drawLine(Bx + 25, By + 25, NeuX + 25, NeuY + 25);
    g.fillRect(Bx, By, 50, 50);
    // erzeuge neue Farben
    if (++Anzahl % 3 == 0) {
       neueFarben();
    }
    // zeichne neue Box
    Bx = NeuX; By = NeuY;
    Richtung = Math.abs(ran.nextInt()) % 4;
    g.setColor((Color)Farben.elementAt(Richtung));
    g.fillRect(Bx, By, 50, 50);        
    repaint();
  }

  // ---- Lachen ----
  void Lachen(Graphics g) {
    // lï¿½sche Box und zeichne lachendes Gesicht
    g.setColor(getBackground());
    g.fillRect(Bx, By, 50, 50);
    g.drawImage(iLachen, Bx, By, 50, 50, this);
    repaint(Bx, By, 50, 50);
    // warte    
    try { Thread.sleep(500); } catch (InterruptedException e) { ; }
    // zeichne Box
    g.setColor((Color)Farben.elementAt(Richtung));  
    g.fillRect(Bx, By, 50, 50);        
    repaint(Bx, By, 50, 50);
  }

  // ---- Weinen ----
  void Weinen(Graphics g) {
    // lï¿½sche Box und zeichne weinendes Gesicht
    g.setColor(getBackground());
    g.fillRect(Bx, By, 50, 50);
    g.drawImage(iWeinen, Bx, By, 50, 50, this);
    repaint(Bx, By, 50, 50);
    // warte    
    try { Thread.sleep(500); } catch (InterruptedException e) { ; }
    // zeichne Box
    g.setColor((Color)Farben.elementAt(Richtung));
    g.fillRect(Bx, By, 50, 50);        
    repaint(Bx, By, 50, 50);
  }

  // ---- erzeuge neue Farben ----
  void neueFarben() {
    Vector hFarben = new Vector (8);
    while (Farben.size() > 0) {
      int i = Math.abs(ran.nextInt()) % Farben.size();
      hFarben.addElement(Farben.elementAt(i));
      Farben.removeElementAt(i);
    }
    Farben = hFarben;
   
    // male Richtungsbereich
    for (int i= 0; i<4; i++) {
      gs.setColor((Color)Farben.elementAt(i));
      gs.fillRect(i*50, 0, 50, 50);
      gs.setColor(Color.black);
      gs.drawString(rStr[i], i * 50 + 10, 25);
    }
  }

  // prï¿½ft, ob die Annï¿½herung falsch war
  boolean falscheAnnaeherung(int x, int y) {
    if (x < Bx - 50 || x > Bx + 100 ||
        y < By - 50 || y > By + 100) return false;

    if (Richtung != 0) {
       if (x >= Bx -  50 && x < Bx &&
           y >= By       && y < By +  50) return true;
    }

    if (Richtung != 1) {
       if (x >= Bx +  50 && x < Bx + 100 &&
           y >= By       && y < By +  50) return true;
    }

    if (Richtung != 2) {
       if (x >= Bx       && x < Bx +  50 &&
           y >= By -  50 && y < By      ) return true;
    }

    if (Richtung != 3) {
       if (x >= Bx       && x < Bx +  50 &&
           y >= By +  50 && y < By + 100) return true;
    }

    return false;
  }

  // ---- erzeugt eine neue Box
  void neueBox() {
    NeuX = Math.abs(ran.nextInt()) % 450 + 1;
    NeuY = Math.abs(ran.nextInt()) % 299 + 51;
  }

}

