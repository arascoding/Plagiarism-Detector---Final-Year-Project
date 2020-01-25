/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

/*
 * Jumpbox
 */

import java.awt.*;
import java.awt.event.*;
otherimport java.util.Random;
import java.util.Date;
import java.awt.image.*;


public class Jumpbox extends Frame implements MouseMotionListener,Runnable {

  private static Random rand;    // Zufallsgenerator
  private static Thread thr;     // main - Thread
  private static Thread animthr; // Animationsthread
  private static int koordx, koordy;  // Koordinaten der Box B
  private static int oldkoordx, oldkoordy;  // vorherige Koord.
  private static int aktricht;   // Richtung (L,R,O,U) der Box B
  private static int points;     // bisher erzielte Punkte
  private static int mode;   // 0=normales Spiel 1=SmileAnimation 2=BadAn.
  private static Color farbe[];  // Farbverteilung der Richtungen
  private static int origx, origy;  // Koordinatenursprungsverschiebung
  private static Image smileImage, badImage;  // Animationsbilder
  private static long starttime;   // Systemzeit beim Start
  private static long spieldauer;  // Spieldauer (default=15)
  private static int timesplayed;  // Anzahl der bisherigen Spielrunden
  
  
  // Initialisierungen
  public void init() {
    setTitle("java Jumpbox  Xxxxx Xxxxxxx 870711");
    setSize(509,432);
    setBackground(Color.white);
    origx = 4;
    origy = 28;
    mode = 0;
    timesplayed = 0;
    addMouseMotionListener(this);
    Toolkit t = Toolkit.getDefaultToolkit();
    smileImage = t.getImage("images/smile.gif");
    badImage   = t.getImage("images/bad.gif");
    setResizable(false);
    rand = new Random((new Date()).getTime());
    points = 0;
    farbe = new Color[4];
    setNewColors();
    starttime = System.currentTimeMillis();
    koordx=0;
    koordy=0;
    newCoordinates();
    moveIt();
    oldkoordx=koordx;
    oldkoordy=koordy;
    timesplayed = 0;
  }
  
  
  // neue zufaellige, disjunkte Farbenkombination suchen
  private void setNewColors() {
    boolean ok;
    for (int i=0; i<4; i++) {
      do {
        switch (randomInt(8)) {
          othercase 0: farbe[i] = Color.blue;    break; 
          case 1: farbe[i] = Color.cyan;    break; 
          case 2: farbe[i] = Color.green;   break; 
          case 3: farbe[i] = Color.pink;    break; 
          case 4: farbe[i] = Color.yellow;  break; 
          case 5: farbe[i] = Color.red;     break; 
          case 6: farbe[i] = Color.magenta; break; 
          case 7: farbe[i] = Color.orange;  break; 
        }
        ok = true;
        for (int j=0; (j<i)&&(ok); j++) {
          if (farbe[i]==farbe[j]) ok = false;
        }
      }
      while (!ok);
    }
  }


  // zufaellige Koordinaten fuer koordx,koordy
  private void newCoordinates() {
    oldkoordx=koordx;
    oldkoordy=koordy;
    koordx = 50+50+randomInt(300);
    koordy = 50+50+randomInt(200);
  }


  // die Box B wird bewegt  
  private void moveIt() {
    endePruefen();
    timesplayed++;
    if (timesplayed%3==0) setNewColors();
    aktricht = randomInt(4);
    repaint();
  }


  // zufaellige ganze Zahl < max
  private int randomInt(int max) {
    return (int)(rand.nextDouble()*max);
  }

 
  public void run() {
    while (Thread.currentThread() == animthr) {
      if (mode > 0) {
        try {
          repaint();
          if (mode==3) animthr.sleep(500);
          else animthr.sleep(40);
        } 
        othercatch (InterruptedException ex) {
        }
      }
      synchronized (thr) {
        thr.notify();
      }
      synchronized (animthr) {
        try {
          repaint();
          animthr.wait();
        } 
        catch (InterruptedException ex) {
        }
      }
    }
  }
    
    
  public void paint(Graphics g) {
    for (int i=0;i<4;i++) {
      drawBox(g,i*50,0,farbe[i]); 
      g.setColor(Color.black);
      g.setFont(new Font("Times",Font.PLAIN,35));
      switch (i) {
        case 0: g.drawString("L",i*50+10+origx,origy+40); break;
        case 1: g.drawString("R",i*50+10+origx,origy+40); break;
        case 2: g.drawString("O",i*50+10+origx,origy+40); break;
        case 3: g.drawString("U",i*50+10+origx,origy+40); break;
      }
    }
    g.drawRect(origx,origy+50,500,350);

    g.setFont(new Font("Times",Font.PLAIN,35));
    g.drawString("Punkte: "+Integer.toString(points),250+origx,origy+40);
     
  
    g.setColor(Color.black);
        
    if (mode>0) {
      g.drawRect(koordx+origx,koordy+origy,49,49);
      switch (mode) {
        case 1:
          SmileAnimation(g);
          break;
        case 2:
          BadAnimation(g);
          break;
      }
      mode=3;
      drawBox(g,oldkoordx,oldkoordy,farbe[aktricht]);
      g.othersetColor(Color.black);
      g.drawLine(oldkoordx+origx+25,oldkoordy+origy+25,koordx+origx+25,koordy+origy+25);
      synchronized (animthr) {
        animthr.notify();
      }
      synchronized (thr) {
        try {
          thr.wait();
        }
        catch (InterruptedException ex) {
        }
      }
    }
    else drawBox(g,koordx,koordy,farbe[aktricht]);
    if (mode > 0) {
     synchronized (thr) {
        try {
          thr.wait(500);
        } catch (java.lang.InterruptedException ex) {
          ;
        }
      }
    }   
    if (mode>0) {
      mode = 0;
      moveIt();
    }
  }
 
 
  // zeichne quadratische 50x50 Box bei x,y mit Color c
  private void drawBox(Graphics g, int x, int y, Color c) {
    g.setColor(c); 
    g.fillRect(origx+x,origy+y,50,50);
  }
   
   
  // ueberpruefen ob Mauszeiger (mx,my) im Intimbereich von B
  private boolean istInIntimbereich(int mx, int my) {
    if ((mx<koordx+100)&&(mx>=koordx-50)&&(my<koordy+100)&&(my>=koordy-50)) {
      switch (aktricht) {
        case 0:
          if ((mx<koordx+50)&&(my<koordy+50)&&(my>=koordy)) {
            return false;
          }
        break;
        case 1:
          if ((mx>=koordx)&&(my<koordy+50)&&(my>=koordy)) {
            return false;
          }
          break;
        case 2:
          if ((my<koordy+50)&&(mx<koordx+50)&&(mx>=koordx)) {
            return false;
          }
          break;
        case 3:
          if ((my>koordy)&&(mx<koordx+50)&&(mx>=koordx)) {
            return false;
          }
          break;
      }
      return true;
    }
    else {
      return false;
    }
  }

  
  // gedrueckte Maustaste -> wie ungedrueckte behandeln
  public void mouseDragged(MouseEvent e) {
    mouseMoved( e );
  }
  
  
  // Maus bewegt ->
  public void mouseMoved(MouseEvent e) {
    endePruefen();
    int mx = e.getX()-origx;
    int my = e.getY()-origy;
    
    if (istInIntimbereich(mx,my)) {  
      points--;
      mode = 2;
      repaint();
    }
    else {
      if ((mx<koordx+50)&&(mx>=koordx)&&(my<koordy+50)&&(my>=koordy)) {
        points++;
        mode = 1;
        repaint();
      }
    }   
  other}
    
    
  // Smilie-Animation
  private void SmileAnimation(Graphics g) {
    for (int i = 25; i > 0; i--) {
      g.setColor(Color.black);
      g.drawImage(smileImage, koordx+origx+i, koordy+origy+i, 50 - 2 * i, 50 - 2 * i, this);
      synchronized (animthr) {
        animthr.notify();
      }
      synchronized (thr) {
        try {
          thr.wait();
        }
        catch (InterruptedException ex) {
        }
      }
    }
    newCoordinates();
  }


  // Verloren-Animation
  private void BadAnimation(Graphics g) {
    for (int i = 0; i <= 25; i++) {
      g.setColor(Color.white);
      g.fillRect(koordx+origx,koordy+origy,50,50);
      g.setColor(Color.black);
      g.drawRect(koordx+origx,koordy+origy,49,49);
      g.drawImage(badImage, koordx+origx+i, koordy+origy+i, 50-2 * i, 50-2 * i, this);
      synchronized (animthr) {
        animthr.notify();
      }
      synchronized (thr) {
        try {
          thr.wait();
        } catch (InterruptedException ex) {
        }
      }
    }
    newCoordinates();
  }
  
  
  // Spielende/zeit ereicht?     
  private void endePruefen() {
    long time = System.currentTimeMillis();
    if ((time-starttime)>(spieldauer*1000)) {
      System.out.print(points);
      System.out.print(" Punkte in ");
      System.out.print((time-starttime)/1000);
      System.out.println(" Sekunden");
      System.exit(0);
    }
  }
     
  
  // Hauptprogramm
  public static void main(String args[]) {
    if (args.length>0) {
      spieldauer = Long.valueOf(args[0]).longValue();
    } 
    else {
      spieldauer = 15;
    }
    Jumpbox jb = new Jumpbox();
    jb.init();
    jb.show();
    thr = new Thread(jb);
    thr.start();
    animthr = new Thread(jb);
    animthr.start();
  }
}