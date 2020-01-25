/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

// ----------------------------------------------------------------------
// -----     JumpBox von Xxxx Xxxxxxxx    -------------------------------
// -----     Matr.Nr. 826833              -------------------------------
// ----------------------------------------------------------------------

import java.util.*;
import java.awt.*;
import java.awt.event.*;

class Jumpbox extends Canvas implements MouseMotionListener, WindowListener,
                                        Runnable {
   private int spielDauer;
   private Date date;
   private long spielBeginn;
   private int punkte=0;
   private int richtung;
   private int[] posBox = new int[2];
   private int zaehler=0;
   private Color[] richtungsFarben = new Color[4];
   private Random random = new Random((new Date()).getTime());
   // alles fuer die Animation
   private Image smile;
   private Image wince;
   private Image aktuellesBild;
   private Thread animatorThread;
   private int delay = 40;
   private boolean frozen=true;                // false waehrend der Animation
   // Double Buffering
   private Image offScreen;

   // Konstruktor
   public Jumpbox(int spielDauer) {
      spielBeginn = (new Date()).getTime();
      this.spielDauer=spielDauer;
      Font font = new Font(" ", Font.BOLD, 20);
      setFont(font);
      smile = Toolkit.getDefaultToolkit().getImage("smile.gif");
      wince = Toolkit.getDefaultToolkit().getImage("wince.gif");
      addMouseMotionListener(this);
      wuerfeleFarben();
      posBox[0] = (int) (random.nextFloat()*450);
      posBox[1] = 50 + (int) (random.nextFloat()*300);
      richtung = (int) (random.nextFloat()*4);
      animatorThread = new Thread(this);
      synchronized(animatorThread) {
         animatorThread.start();
      }
   }

   public Dimension 
other
getPreferredSize() { return new Dimension(501,401); }

   // Thread-Methode fuer Animation
   public void run() {
      while(true) {
         synchronized(animatorThread) {
            try {
               frozen=true;
               animatorThread.wait();
            }
            catch(InterruptedException e) {}
            if (offScreen == null) continue;  // falls Mouse bei Start auf Box
            //Remember the starting time
            long startTime = System.currentTimeMillis();

            Graphics og = offScreen.getGraphics();
            if (aktuellesBild == wince) {
               for (int i=0; i<=25; i++) {
                  og.clearRect(posBox[0], posBox[1], 50, 50);
                  og.drawImage(wince, posBox[0]+i, posBox[1]+i,
                                      50-i*2, 50-i*2, null);
                  //Display it.
                  repaint(posBox[0], posBox[1], 50, 50);
                  //Delay depending on how far we are behind.
                  try {
                     startTime += delay;
                     Thread.sleep(Math.max(0, startTime-System.currentTimeMillis()));
                  } catch (InterruptedException e) {}
               }
            } else {
               og.drawImage(smile, posBox[0], posBox[1], 50, 50, null);
               repaint(posBox[0], posBox[1], 50, 50);
               try {
               Thread.sleep(500);
               } catch(InterruptedException e) {}
            
other
}
            neueBox(og);
         } 
      }
   }

   // waehlt zufaellig 4 Farben aus 8 aus
   private void wuerfeleFarben() {
      boolean[] feld = new boolean[8];
      int index;
      final Color[] farbTabelle = { Color.blue, Color.cyan,
                                    Color.green, Color.magenta,
                                    Color.orange, Color.pink,
                                    Color.red, Color.yellow };

      for (int i=0; i<8; i++) feld[i] = false;
      for (int i=0; i<4; i++) {
         do {
           index = (int) (random.nextFloat()*8);
         } while (feld[index] == true);
         richtungsFarben[i] = farbTabelle[index];
         feld[index] = true;
      }
   }
   
   // zeichnet die 4 verschiedenfarbigen Richtungsboxen
   private void zeichneNeueRichtungsBoxen(Graphics g) {
      String str = "LROU";
      
other
for (int i=0; i<4; i++) {
         g.setColor(richtungsFarben[i]);
         g.fillRect(i*50, 0, 50, 50);
         g.setColor(Color.black);
         g.drawString(String.valueOf(str.charAt(i)),i*50+16,34);
      }
   }

   // Methode berechnet und zeichnet neue Box
   // zwischen alter und neuer Box wird eine Verbindungslinie gezogen
   private void neueBox(Graphics g) {
      g.setColor(richtungsFarben[richtung]);
      g.fillRect(posBox[0], posBox[1], 50, 50);
      if (++zaehler == 2) {
            wuerfeleFarben();
            repaint(0, 0, 200, 50);             // nur die neuen Richtunsboxen malen
            zaehler=0;
       }
      richtung = (int) (random.nextFloat()*4);
      int[] neuPosBox = new int[2];

      neuPosBox[0] = (int) (random.nextFloat()*450);
      neuPosBox[1] = 50 + (int) (random.
other
nextFloat()*300);

      g.setColor(richtungsFarben[richtung]);
      g.fillRect(neuPosBox[0], neuPosBox[1], 50, 50);
      g.setColor(Color.darkGray);
      g.drawLine(posBox[0]+25, posBox[1]+25, neuPosBox[0]+25, neuPosBox[1]+25);
      repaint();
      try {
         Thread.sleep(500);
      } catch(InterruptedException ie) {}
      // wieder alles loeschen
      g.setColor(Color.white);
      g.fillRect(posBox[0], posBox[1], 50, 50);
      g.drawLine(posBox[0]+25, posBox[1]+25, neuPosBox[0]+25, neuPosBox[1]+25);
      g.setColor(richtungsFarben[richtung]);
      g.fillRect(neuPosBox[0], neuPosBox[1], 50, 50);
      repaint();
      posBox = neuPosBox;
   }

   public void update(Graphics g) {
      Dimension dim = getSize();
      if (offScreen == null) 
         offScreen = createImage(dim.width, dim.height);
      Graphics og = offScreen.getGraphics();
      zeichneNeueRichtungsBoxen(og);                  // zeiche Richtungsboxen
      og.setColor(Color.red);                         // zeichen Rahmen
      og.drawRect(0, 50, 500, 350);
      if (frozen) {                                   // zeichne Box
         og.setColor(richtungsFarben[richtung]);
         og.fillRect(posBox[0], posBox[1], 50, 50);
      }
      g.
other
clearRect(0, 0, dim.width, dim.height);
      g.drawImage(offScreen, 0, 0, null);
   }

   public void paint(Graphics g) {
      update(g);
   }

   // Mouse-Methoden
   public void mouseDragged(MouseEvent me) { mouseMoved(me); }
   public void mouseMoved(MouseEvent me) {
      
other
if ((new Date()).getTime() > spielBeginn+spielDauer*1000) {
         synchronized(animatorThread) { animatorThread.stop(); }
         System.out.println("Punktezahl "+punkte+ " in "+spielDauer+" Sekunden");
         System.exit(0);
      }
      if (!frozen) return;                  // nur wenn keine Animation laeuft
      boolean treffer = false;

      // ist Mousezeiger in "50 Pixel"-Umgebung
      int x=posBox[0]-50;
      int y=posBox[1]-50;
      int merkePunkte = punkte;
      if (x < 0) x = 0;
      if (y < 0) x = 0;
      x=me.getX()-x;
      y=me.getY()-y;
      if ((x < 150) & (y < 150) & (x > 0) & (y > 0)) {
         x=me.getX();
         y=me.getY();
         // Mouse-Zeiger in Box b
         if ((x >= posBox[0])    & (y >= posBox[1]) &
             (x <= posBox[0]+50) & (y <= posBox[1]+50)) treffer = true;
         else {
            switch(richtung) {
            case 0  : if ((x < posBox[0])    & (y > posBox[1]) & (y < posBox[1]+50)) return;
                      break;  
            case 1  : if ((x > posBox[0]+50) & (y > posBox[1]) & (y < posBox[1]+50)) return;
                      break;
            case 2  : if ((y < posBox[1])    & (x > posBox[0]) & (x < posBox[0]+50)) return;
                      break;
            case 3  : if ((y > posBox[1]+50) & (x > posBox[0]) & (x < posBox[0]+50)) return;
            }
         }
         
         if (treffer) {
            punkte++;
            aktuellesBild = smile;
         } else {
            punkte--;
            aktuellesBild = wince;
         }
         synchronized(animatorThread) {
            frozen=false;
            animatorThread.notify();
         }
      }
   
other
}

   // Window-Methoden
   public void windowActivated(WindowEvent event) {}
   public void windowClosed(WindowEvent event) {}
   public void windowDeactivated(WindowEvent event) {}
   public void windowDeiconified(WindowEvent event) {}
   public void windowIconified(WindowEvent event) {}
   public void windowOpened(WindowEvent event) {}
   public void windowClosing(WindowEvent event) {
      System.exit(0);
   }

   public static void main(String[] args) {
      int spielDauer;
      if (args.length == 0) spielDauer = 15;
      else {
         try { spielDauer = Integer.parseInt(args[0]); }
         catch(NumberFormatException e) { spielDauer = 15; }
      }
      Jumpbox jumpbox = new Jumpbox(spielDauer);
      Frame frame = new Frame("Jumpbox von Xxxx Xxxxxxxx #826833");
      BorderLayout bl = new BorderLayout();
      frame.setLayout(bl);
      frame.add("Center", jumpbox);
      frame.pack();
      frame.addWindowListener(jumpbox);
      frame.setVisible(true);
   }
}