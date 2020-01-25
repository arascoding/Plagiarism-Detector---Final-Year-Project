/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.io.*;
import java.util.*;
import java.awt.*;

public class Jumpbox extends Canvas implements Runnable {

  static Frame Jumpframe;
  static Jumpbox jumpbox;
  static int dauer;
  Color c[];
  Random zZahlen;
  int bxalt, byalt, bx, by;
  int direction;
  int punkte;
  static long Starttime;
  Image Smiley, Wince;
  int SprungNr;
  boolean showSmiley, showWince, showLine;
  int delay=40;
  Thread animatorThread;
 
  public Jumpbox() {
    zZahlen = new Random(System.currentTimeMillis());
    c = new Color[4];
    bx = 0; by= 0;
    setRandomColors();
    setRandomPos();
    setRandomDir();
    punkte = 0;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Smiley = toolkit.getImage("smile.gif");
    Wince = toolkit.getImage("wince.gif");
    SprungNr = 0;
    showSmiley = showWince = showLine = false;
   
  }
  
  private void inIntim() {
    punkte--;
    grummel();
    sprung();
  }

  public boolean mouseMove(Event e, int x, int y) {
    if (System.currentTimeMillis() >= Starttime+dauer*1000) {
      System.out.println("Ergebnis: "+ punkte + " Punkte in " + dauer + " Sekunden");
      System.exit(0);
      return true;
    }
    if ((x>=bx) && (x<bx+50) && (y>=by) && (y<by+50)) {
      punkte++;
      grins();
      sprung();
      return true;
    }
    if ((x>=bx-50) && (x<bx+100) && (y>=by-50) && (y<by+100)) {
      switch(direction) {
      case 0:
	if (!((x<bx) && !(y<by ||y>by+50))) { 
	  inIntim();
	}
	return true;
      case 1:
	if (!((x>bx+49) && !(y<by ||y>by+50))) { 
	  inIntim();
	}
	return true;
      case 2:
	if (!((y<by) && !(x<bx ||x>bx+50))) { 
	  inIntim();
	}
	return true;
      case 3:
	if (!((y>by+49) && !(x<bx ||x>bx+50))) { 
	  inIntim();
	}
	return true;	
      }
    }
    return false;
  }
    

  private void grummel() {
    if (animatorThread == null) {
      animatorThread = new Thread(this);
    }
    showWince = true;
    animatorThread.start();
    try {
       
      Thread.sleep(1000);
      animatorThread = null;
      showWince = false;
    }
    catch (InterruptedException e) {
      
    }
    System.out.println("Grummel");
  }

  private void grins() {
    if (animatorThread == null) {
       animatorThread = new Thread(this);
     }
    showSmiley = true;
     animatorThread.start();
     try {
       
       Thread.sleep(500);
       animatorThread = null;
       showSmiley = false;
     }
     catch (InterruptedException e) {
       
     }

    System.out.println("Grins");
  }

  private void sprung() {
    showLine = true;
    repaint();
    try {
      Thread.sleep(500);
    }
    catch (InterruptedException e) {}
    showLine = false;

    SprungNr++;
    if (SprungNr == 3) {
      SprungNr = 0;
      setRandomColors();
    }    
    setRandomPos();
    setRandomDir();
    repaint();
    
  }

  private void setRandomDir() {
    direction = Math.abs(zZahlen.nextInt()%4);
  }
  
  private void setRandomPos() {
    bxalt = bx;
    byalt = by;
    bx = 50 + Math.abs(zZahlen.nextInt()%350);
    by = 100 + Math.abs(zZahlen.nextInt()%200); 
  }
  
  private void setRandomColors() {
    int z[] = new int[4];
    boolean doppelt;
    for (int i = 0; i<4;i++) {
      do {
	doppelt 
other
= false;
	z[i] =  Math.abs(zZahlen.nextInt()%8);
	for (int j = 0 ;j <i; j++) {
	  if (z[i] == z[j]) doppelt = true;
	}
      }
      while(doppelt);
    }
    for (int i = 0; i < 4; i++) {
      
other
switch (z[i]) {
      case 0:
	c[i] = Color.blue;
	break;
      case 1:
	c[i] = Color.cyan;
	break;
      case 2:
	c[i] = Color.green;
	break;
      case 3:
	c[i] = Color.magenta;
	break;
      case 4:
	c[i] = Color.orange;
	break;
      case 5:
	c[i] = Color.pink;
	break;
      case 6:
	c[i] = Color.red;
	break;
      case 7:
	c[i] = Color.yellow;
	break;
      }
    }
  }

  public void run() {

    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

    long astartTime = System.currentTimeMillis();

    while (Thread.currentThread() == animatorThread) {
      
      repaint();
      try {
	astartTime += delay;
	Thread.sleep(Math.max(0, 
			      astartTime-System.currentTimeMillis()));
      } catch (InterruptedException e) {
	break;
      }
    }
  }

  public void paint(Graphics g) {
    update(g);
  }

  public Insets insets() {
    return new Insets(1,1,1,1);
  }
  
  public void update(Graphics g) {
    Dimension d = size();
    g.setColor(getBackground());
    g.fillRect(0, 50, d.width-1, d.height-1);
    g.setColor(Color.black);
    g.drawRect(0, 50, d.width-1, d.height-1);
    for (int i=0; i<4;i++) {
      g.setColor(c[i]);
      g.fillRect(50*i, 0, 50, 50);
    }
    g.setColor(Color.black);
    g.drawString("L", 25, 25);
    g.drawString("R", 75, 25);
    g.
other
drawString("O", 125, 25);
    g.drawString("U", 175, 25);

    g.setColor(c[direction]);
    g.fillRect(bx, by, 50, 50);

    if (showSmiley)
      g.drawImage(Smiley, bxalt, byalt, this);
    if (showWince)
      g.drawImage(Wince, bxalt, byalt, this);
    if(showLine)
      g.drawLine(bxalt, byalt, bx, by);

  }
 
  public static void main(String args[]) {
    dauer = 15;
    if (args.length > 0) 
      dauer = Integer.parseInt(args[0]); 
    jumpbox = new Jumpbox();
    Jumpframe = new Frame();
    Jumpframe.setLayout(new BorderLayout());
    Jumpframe.setTitle("Jumpbox ("+dauer+" seconds) by Xxxxxxx Xxxxxx #745586");
    jumpbox.resize(500, 400);
    Jumpframe.add("Center", jumpbox);
    Jumpframe.pack(); 
    Starttime = System.currentTimeMillis(); 
    Jumpframe.show();
  }
}