/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.awt.*;
import java.util.*;


public class Jumpbox extends Canvas implements Runnable {



  //Vorgaben

  static final int
     links = 0, rechts = 1, oben = 2, unten = 3;  
  static final String
     direction[] = { "L", "R", "O", "U" }; 
  static final int 
     StSpielZeit    =  20,
     StBoxGroesse    =  50, // Standardgroesse der Box
     StXFeldGroesse = 500,   
     StYFeldGroesse = 400;  
      
  static final Color      
     farben[] = {Color.blue, Color.cyan, Color.green, Color.magenta,
                 Color.orange, Color.pink, Color.red, Color.yellow};

  // Variablen zur Kommunikation zwischen Threads
  static final int       
     

Springen = 0, Smilen = 1, Grummeln = 2, Warten = 3;

 
  Vector aktuelleFarben;
  int   nextaction = Warten; 
  int   frameN;                
  Image screen = null;    
  Graphics gs;            
  Random zufall;          
  Image smileimage;   
  Image winceimage;     

  int xGroesse;     
  int yGroesse;     
  int xB;        
  int yB;            
  long endtime;     
  int pointsN = 0;  
  int jumpsN = 0;   
  int 

xBnew;     
  int yBnew;     
  int boxsize;   
  int okDirection;  
  int  runtime;  

 
  Jumpbox(int x, int y, int bsize, int runTime, 
             String niceImage, String badImage) {
     Toolkit tk = Toolkit.getDefaultToolkit();
     xGroesse = x;
     yGroesse = y;
     boxsize = bsize;
     runtime = runTime;
     endtime = System.currentTimeMillis() + 1000*runtime;
     smileimage = tk.getImage(niceImage);
     winceimage = tk.getImage(badImage);
     resize(xGroesse, yGroesse);
     zufall = new Random(endtime);
  }
  


  public static void main(String args[]) {
     int theRunTime;
     try { theRunTime = Integer.parseInt(args[0]); }
     catch (RuntimeException e) { theRunTime = StSpielZeit; }
     Frame 

f = new Frame("Xxxx Xxxxxxxx, 518309");
     Jumpbox j = new Jumpbox(StXFeldGroesse, StYFeldGroesse, StBoxGroesse,
                             theRunTime, "smile.gif", "wince.gif");
     f.add("Center", j);
     f.pack();
     f.show();                 // show game window
     (new Thread(j)).start();  // start animation loop thread
  }
  


  public void run() {
     int msec;
     // endless animation loop:
     Thread.

currentThread().setPriority(Thread.MIN_PRIORITY);
     for (;;) {
        if (nextaction == Warten) {        // suspend animation
           try { 
             synchronized(this) { this.wait(); }
           }
           catch (InterruptedException e) { ; }
        }
        else if (nextaction == Smilen) {     // perform smile
           if ((msec = smile(gs, frameN++)) > 0)
              sleep(msec);
           else {
              nextaction = Springen;
              frameN = 0;
              Thread.yield();
           }
        }
        else if (nextaction == Grummeln) {     // perform wince
           if ((msec = wince(gs, frameN++)) > 0)
              sleep(msec);
           else {
              nextaction = Springen;
              frameN = 0;
              Thread.yield();
           }
        }
        else if (nextaction == Springen) {      // perform jump
           if ((msec = jump(gs, frameN++)) > 0)
              sleep(msec);
           else {
              try { 
                 synchronized(this) {
                    nextaction = Warten;
                    this.wait();
                 }
              }
              catch (InterruptedException e) { ; }
           }
        }
        else {                               
           System.

exit(1);
        }
     }
  }


  
  public void paint(Graphics g) {
     update(g);
  }


  public void update(Graphics g) {
     if (screen == null) {
        // create and initialize double buffer upon first call:
        screen = createImage(xGroesse,yGroesse);  
        gs = screen.getGraphics();          
        paintInitial(gs); 
     

}
     g.drawImage(screen, 0, 0, this); 
  }



  synchronized public boolean mouseMove(Event evt, int x, int y) {
     if (System.currentTimeMillis() > endtime)
        finish();
     if (insideB(x, y) && nextaction == Warten) {
        // B reached correctly:
        pointsN++;
        nextaction = Smilen;
        frameN = 0;
        this.notify();
     }
     else if (tooClosetoB(x, y) && nextaction == Warten) {
        // B approached incorrectly:
        pointsN--;
        nextaction = Grummeln;
        frameN = 0;
        this.notify();
     }
     else {
        // B not reached at all or animation still underway:
        ;  // nothing happens
     

}
     return true;
  }



  synchronized public boolean mouseDrag(Event evt, int x, int y) {
     return mouseMove(evt, x, y);
  }



  void finish() {
     System.out.println("**** Ergebnis: " + 
                        pointsN + " Punkte in " + runtime + " Sekunden ****");
     System.exit(0); // stop the whole program
  }


  int jump(Graphics g, int frameN) {
     if (frameN == 0) {
        neueBoxPos();
        // draw line from center of current box to center of new box position:
        g.setColor(Color.black);
        g.drawLine(xB    + boxsize/2, yB    + boxsize/2,
                   xBnew + boxsize/2, yBnew + boxsize/2);
        repaint();
        return 500;  // more to come
     }
     else {
        g.setColor(getBackground());
        g.drawLine(xB    + boxsize/2, yB    + boxsize/2,
                   xBnew + boxsize/2, yBnew + boxsize/2);
        paintNoBox(g);  // remove box from old position
        paintBox(g, true);    // show box at new position
        repaint();
        return 0;  // jump animation finished
     }
  }



  int smile(Graphics g, int frameN) {
     if (frameN == 0) {
        paintNoBox(g);
        g.drawImage(smileimage, xB, yB, boxsize, boxsize, this);
        repaint(xB, yB, boxsize, boxsize);
        return 500; 
     }
     else {
        paintBox(g, false);
        repaint(xB, yB, boxsize, boxsize);
        return 0;
     }
  }


 
  int wince(Graphics g, int frameN) {
     int size;      // size of icon in this frame
     int clearsize; // size of area to clear before drawing icon
     if (frameN >= 0 && frameN < boxsize/2) {
        // next shrinking frame:
        size = boxsize - 2*frameN;
        clearsize = size == boxsize ? boxsize : size+2;
        g.setColor(getBackground());
        g.fillRect(xB, yB, clearsize, clearsize); 
        g.drawImage(winceimage, xB, yB, size, size, this);
        repaint(xB, yB, clearsize, clearsize);
        // perform shrink animation within one second (if CPU is fast enough):
        return 1000/(boxsize/2);  
     }
     else {
        paintBox(g, false);
        repaint(xB, yB, boxsize, boxsize);
        return 0;  
     }
  }


 
  void paintNewColors(Graphics g) {
     Vector remainingColors = new Vector(farben.length);
     for (int i = 0; i < farben.length; i++) {
        remainingColors.addElement(farben[i]);
     }
     // select four colors from remainingColors into aktuelleFarben:
     aktuelleFarben = new Vector(4);
     for (int i = 0; i < 4; i++) {
        int pos = rand(0, remainingColors.size()-1);
        aktuelleFarben.addElement(remainingColors.elementAt(pos));
        remainingColors.removeElementAt(pos); // so we cannot choose it again
     }
     // paint the new direction color boxes:
     

for (int i = 0; i < 4; i++) {
        g.setColor((Color)aktuelleFarben.elementAt(i));   // select next color
        g.fillRect(i*boxsize, 0, boxsize, boxsize);      // draw box
        // draw label into box (with very rough positioning):
        g.setColor(Color.black);
        g.drawString(direction[i], i*boxsize + boxsize/3, boxsize/2);
     }
  }


 
  void paintInitial(Graphics g) {
     g.setColor(Color.white);
     g.drawRect(0, boxsize, xGroesse-1, yGroesse-boxsize-1); // action area border
     neueBoxPos();    // choose new x and y
     paintBox(g, true);
  }


 
  void paintBox(Graphics g, boolean newbox) {
     if (newbox) {
        xB = xBnew;
        yB = yBnew;
        if (jumpsN++ % 3 == 0)
           paintNewColors(g);
        okDirection = rand(0, 3);
     }
     g.

setColor((Color)aktuelleFarben.elementAt(okDirection));
     g.fillRect(xB, yB, boxsize, boxsize);
  }


 
  void paintNoBox(Graphics g) {
     g.setColor(getBackground());
     g.fillRect(xB, yB, boxsize, boxsize);
  }


 
  boolean insideB(int x, int y) {
     return (x >= xB && x < xB + boxsize &&
             y >= yB && y < yB + boxsize);
  }


  boolean tooClosetoB(int x, int y) {
     int xrange = boxsize;  // how far intimate zone reaches in x direction
     int yrange = boxsize;  // how far intimate zone reaches in y direction
     boolean seemsIntimate =
        (x >= xB - xrange && x < xB+boxsize + xrange &&
         y >= yB - yrange && y < yB+boxsize + yrange);
     // (allowed part does NOT include the inside of B):
     boolean isInAllowedPart =
        (okDirection == links &&
           x >= xB - xrange    && x < xB &&
           y >= yB             && y < yB+boxsize) 
        ||
        (okDirection == rechts &&
           x >= xB + boxsize   && x < xB+boxsize + xrange &&
           y >= yB             && y < yB+boxsize) 
        ||
        (okDirection == oben &&
           x >= xB             && x < xB+boxsize &&
           y >= yB - yrange    && y < yB) 
        ||
        (okDirection == unten &&
           x >= xB             && x < xB+boxsize &&
           y >= yB+boxsize     && y < yB+boxsize + yrange);
     return (seemsIntimate && !isInAllowedPart);
  }

  int rand(int low, int high) {
    return Math.abs(zufall.nextInt()) % (high-low+1) + low;
  }

  void sleep(int msec) {
     try { Thread.sleep(msec); }
     catch (InterruptedException e) { ; }
  }
  


  void neueBoxPos() {
    xBnew = rand(1, xGroesse - boxsize - 1);
    yBnew = rand(boxsize + 1, yGroesse - boxsize - 1);
  }
}
