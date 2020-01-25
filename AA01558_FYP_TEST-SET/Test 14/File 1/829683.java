/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

// ****************************************
// * Java-Kompaktkurs SS 97  -  Aufgabe 5 *
// * ------------------------------------ *
// * Autor:          Xxxxx Xxxxxx         *
// * Matrikelnr.:    829683               *
// ****************************************

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Jumpbox extends Canvas implements Runnable, MouseMotionListener,
                                                         WindowListener {

   private Image[] images = new Image[2];   // zwei gifs fuer animation
   private long beginn;                     // ende des spiels
   private long dauer;                      // dauer des spiels
   private Random random = new Random();    // zufallsvariable
   private Image offscreen;                 // doppelpuffer
   private Color[] colors = new Color[4];   // farben der richtungsboxen
   private int R;                           // 0<=R<=3 gibt die richtung an
   private Point boxPoint = new Point();    // linker, oberer punkt der Box
   private int punkte = 0;                  // anzahl der spielpunkte
   private boolean istTreffer = false;      // entscheidet welche animation
   private Thread animatorThread;           // thread fuer animation
   private boolean animating = false;       // true waehrend animation
   private boolean changeColors = false;    // tauscht farben

   public Jumpbox(long ende) {
      addMouseMotionListener(this);
      images[0] = Toolkit.getDefaultToolkit().getImage("smile.gif");
      images[1] = Toolkit.getDefaultToolkit().getImage("wince.gif");
      beginn = (new Date()).getTime();
      dauer = ende;
      setColors();
      setBox();
      animatorThread = new Thread(this);
      synchronized(animatorThread) {
         animatorThread.start();
      }
   }

   // sets the colors to new values
   public void setColors() {
      int[] numbers = new int[4];
      boolean isNew;
      for(int i=0; i<4; i++) {
         isNew = false;
         while (!isNew) {
            numbers[i] = Math.abs(random.nextInt()%8);
            int j = 0;
            while (j < i) {
               if (numbers[j] == numbers[i]) break;
               else j++;
            }
            if (j == i) isNew = true;
         }
      }

      for(int i=0; i<4; i++) {
         switch(numbers[i]) {
            case 0:  colors[i] = Color.blue; break;
            case 1:  colors[i] = Color.cyan; break;
            case 2:  colors[i] = Color.green; break;
            case 3:  colors[i] = Color.magenta; break;
            case 4:  colors[i] = Color.orange; break;
            case 5:  colors[i] = Color.pink; break;
            case 6:  colors[i] = Color.red; break;
            default: colors[i] = Color.yellow;
         }
      }
   }

   public void setBox() {
      int x = Math.abs(random.nextInt()%449) + 1;
      int y = Math.abs(random.nextInt()%299) + 51;
      boxPoint = new Point(x,y);
      R = Math.otherabs(random.nextInt()%4);
   }

   public void paint(Graphics g) { update(g); }

   public void update(Graphics g) {
      Dimension d = this.getSize();
      String str = "LROU";
      if (offscreen == null) {
         offscreen = createImage(d.width, d.height);
         Image dummy = createImage(d.width, d.height); // images werden beim 
         Graphics dg = dummy.getGraphics();            // ersten mal noch nicht
         dg.drawImage(images[0],0,0,50,50,null);       // richtig dargestellt
         dg.drawImage(images[1],50,50,50,50,null);     // deshalb ein mal umsonst
      }
      Graphics og = offscreen.getGraphics();
      og.setFont(new Font("Helvetica",Font.BOLD,18));
      otherfor(int i=0; i<4; i++) {
         og.setColor(colors[i]);
         og.fillRect(i*50,0,50,50);
         og.setColor(Color.black);
         og.drawString(String.valueOf(str.charAt(i)),i*50+16,34);
      }
      og.setColor(Color.black);
      og.drawRect(0,50,500,350);
      if (!animating) {
         og.setColor(colors[R]);
         og.fillRect(boxPoint.x,boxPoint.y,50,50);
      }
      g.clearRect(0,0,d.width,d.height);
      g.drawImage(offscreen,0,0,null);
      getToolkit().sync();
   }

   public Dimension othergetPreferredSize() { return new Dimension(500,400); }

   public void run() {
      while(true) {
         synchronized(animatorThread) {
            try {
               animating = false;
               animatorThread.wait();
            }
            catch(InterruptedException e) { }
            if (offscreen == null) continue; 
            Graphics og = offscreen.getGraphics();
            if (istTreffer) {
               og.clearRect(boxPoint.x,boxPoint.y,50,50);
               og.drawImage(images[0],boxPoint.x,boxPoint.y,50,50,null);
               repaint(boxPoint.x,boxPoint.y,50,50);
               try { Thread.sleep(500); }
               catch(InterruptedException e) { }
            }
            else {
               for(int i=0; i<=25; i++) {
                  og.clearRect(boxPoint.x,boxPoint.y,50,50);
                  og.drawImage(images[1],boxPoint.x+i,boxPoint.y+i,
                                         50-2*i,50-2*i,null);
                  repaint(boxPoint.x,boxPoint.y,50,50);
                  try { Thread.sleep(40); }
                  catch(InterruptedException e) { }
               }
            other}
            replaceBox();
         }
      }
   }

   public void replaceBox() {
      Graphics og = offscreen.getGraphics();
      og.clearRect(boxPoint.x,boxPoint.y,50,50);
      og.setColor(colors[R]);
      og.fillRect(boxPoint.x,boxPoint.y,50,50);
      Point old = new Point(boxPoint.x,boxPoint.y);
      if (changeColors) {
         changeColors = false;
         setColors();
      }
      else changeColors = true;
      othersetBox();
      og.setColor(colors[R]); og.fillRect(boxPoint.x,boxPoint.y,50,50);
      og.setColor(Color.black);
      og.drawLine(boxPoint.x+25,boxPoint.y+25,old.x+25,old.y+25);
      repaint();
      try { Thread.sleep(500); }
      catch(InterruptedException e) { }
      og.clearRect(old.x,old.y,50,50);
      og.setColor(getBackground());
      og.drawLine(boxPoint.x+25,boxPoint.y+25,old.x+25,old.y+25);
      repaint();
   }

   public void startAnimation(boolean treffer) {
      istTreffer = treffer;
      if (treffer) punkte++;
      else punkte--;
      synchronized(animatorThread) {
         animating = true;
         animatorThread.notify();
      }
   }

   public void mouseDragged(MouseEvent e) { mouseMoved(e); }

   public void mouseMoved(MouseEvent e) {
      if (animating) return;
      otherif ((new Date()).getTime() > (beginn + dauer*1000)) {
         synchronized(animatorThread) { animatorThread.stop(); }
         System.out.println("Ergebnis: " + punkte + " in " + dauer + " Sekunden");
         System.exit(0);
      }
         
      int x = e.getX(); int y = e.getY();
      if ((new Rectangle(boxPoint.x-50,boxPoint.y-50,150,150)).contains(x,y)) {
         if ((new Rectangle(boxPoint.x,boxPoint.y,51,51)).contains(x,y))
            startAnimation(true);
         else {
            switch(R){
               case 0:
                  if (!((x<boxPoint.x)&(y>boxPoint.y)&(y<boxPoint.y+50)))
                     startAnimation(false);
                  break;
               case 1:
                  if (!((x>boxPoint.x+50)&(y>boxPoint.y)&(y<boxPoint.y+50)))
                     startAnimation(false);
                  break;
               case 2:
                  if (!((x>boxPoint.x)&(x<boxPoint.x+50)&(y<boxPoint.y)))
                     startAnimation(false);
                  break;
               case 3:
                  if (!((x>boxPoint.x)&(x<boxPoint.x+50)&(y>boxPoint.y+50)))
                     startAnimation(false);
                  break;
               default:
                  break;
            }
         }
      }
      Thread.yield();
   other}

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

   public static void main(String args[]) {
      int dauer;
      if (args.length == 0) dauer = 15;
      else {
         try { dauer = Integer.parseInt(args[0]); }
         catch(NumberFormatException e) { dauer = 15; }
      }
      Frame frame = new Frame(" Jumpbox -- Xxxxx Xxxxx  #829683");
      frame.setLayout(new BorderLayout());
      Jumpbox jumpbox = new Jumpbox(dauer);
      frame.addWindowListener(jumpbox);
      frame.add("Center",jumpbox);
      frame.setResizable(false);
      frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
      frame.pack();
      frame.show();
   }
}