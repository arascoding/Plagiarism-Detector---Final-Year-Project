/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.awt.*;
import java.util.Random;
import java.lang.Math;

public class Jumpbox extends Frame 
{
  private Color[] Colors = new Color[4]; // Farben der Richtungsboxen (0=L,1=R,2=O,3=U)
  private int boxColor;            // Schluessel fuer Farbe der Box (zw. 0 und 4)
  private boolean smile;
  private boolean init 
other
= true;  
  private int Punkte = 0;
  private int Spruenge = 0;
  Image pict1 = getToolkit().getImage("smile.gif");
  Image pict2 = getToolkit().getImage("wince.gif");
  private int Dauer;
  private long time;
  private int xOrig, yOrig;  // Ursprung des sichtbaren Bereichs im Fenster
  private Random zzg = new Random();
  private int xpos, ypos;             // Koordinaten der Box B
  private boolean newBox = false;
  private boolean newRBoxes = false;

 
  public Jumpbox(String[] args) {
    
other
super("Xxxx Xxxxxxx, 861005");
    resize(500, 400);
    if (args.length==1) Dauer = new Integer(args[0]).intValue();
    else Dauer = 15;
    time = System.currentTimeMillis();
  }

  public void setSize() {
    Insets in = insets();
    resize(500 + in.left + in.right + 1, 400 + in.top + in.bottom);
    xOrig = in.left;
    yOrig = in.top;
    setResizable(false);
  }

  public void update(Graphics g) {
    paint(g);
  }

  public void paint(Graphics g) {
    if (init) {
      init = false;
      setColors();
      drawRBoxes(g);
      xpos = Math.abs((zzg.nextInt() % 450)) + xOrig;
      ypos = Math.abs((zzg.nextInt() % 300)) + yOrig + 50;
      boxColor = Math.abs((zzg.nextInt() % 4));
      g.setColor(Colors[boxColor]);
      g.fillRect(xpos, ypos, 50, 50);
      g.setColor(Color.black);
      g.drawRect(xOrig, yOrig + 50, 500, 350);
    }
    else {
      g.setColor(Color.black);
      g.drawRect(xOrig, yOrig + 50, 500, 350);
      if (newBox) {
        if (smile) {
          try {
            g.
other
setColor(Colors[boxColor]);
            for (int i=0;i<4;i++) {
              g.drawImage(pict1, xpos, ypos, 50, 50, this);
              Thread.sleep(100);
              g.fillRect(xpos, ypos, 50, 50);
              Thread.sleep(100);
            }
            g.drawImage(pict1, xpos, ypos, 50, 50, this);
            Thread.sleep(200);
          }
          catch (InterruptedException IE) {}
        }
        else {
          try {
            g.setColor(Colors[boxColor]);
            for (int i=0;i<4;i++) {
              g.drawImage(pict2, xpos, ypos, 50, 50, this);
              Thread.sleep(100);
              g.fillRect(xpos, ypos, 50, 50);
              Thread.sleep(100);
            }
            g.drawImage(pict2, xpos, ypos, 50, 50, this);
            Thread.sleep(200);
          }
          catch (InterruptedException IE) {}
        }
        if (newRBoxes) {
          setColors();
          newRBoxes = false;
          drawRBoxes(g);
        }
        setBox(g);
        newBox = false;
      }
      drawRBoxes(g);
      drawBox(g);
    }
  
other
}

  private Color getColor(int Schluessel) {
    switch (Schluessel) {
      case 0:  return Color.blue;
      case 1:  return Color.cyan;
      case 2:  return Color.green;
      case 3:  return Color.magenta;
      case 4:  return Color.orange;
      case 5:  return Color.pink;
      case 6:  return Color.red;
      default: return Color.yellow;
    }
  }

  public void drawRBoxes(Graphics g) {
    g.
other
setColor(Colors[0]);
    g.fillRect(xOrig, yOrig, 50, 50);
    g.setColor(Colors[1]);
    g.fillRect(xOrig + 50, yOrig, 50, 50);
    g.setColor(Colors[2]);
    g.fillRect(xOrig + 100, yOrig, 50, 50);
    g.setColor(Colors[3]);
    g.fillRect(xOrig + 150, yOrig, 50, 50);
    g.setColor(Color.black);
    g.drawString("L", xOrig + 20, yOrig + 30);
    g.drawString("R", xOrig + 70, yOrig + 30);
    g.drawString("O", xOrig + 120, yOrig + 30);
    g.drawString("U", xOrig + 170, yOrig + 30);
  }

//Zeichnet neue Box B und fuer 500 msec eine Verbindungslinie
  public void setBox(Graphics g) {
    int 
other
newxpos = Math.abs((zzg.nextInt() % 450)) + xOrig;
    int newypos = Math.abs((zzg.nextInt() % 300)) + yOrig + 50;
    int newboxColor = Math.
other
abs((zzg.nextInt() % 4));
    g.setColor(Colors[newboxColor]);
    g.fillRect(newxpos, newypos, 50, 50);
    g.setColor(Color.black);
    g.drawLine(xpos + 25, ypos + 25, newxpos + 25, newypos + 25);
    try { Thread.sleep(500); } catch (InterruptedException e) {}
    g.setColor(getBackground());
    g.drawLine(xpos + 25, ypos + 25, newxpos + 25, newypos + 25);
    g.clearRect(xpos, ypos, 50, 50);
    xpos = newxpos;
    ypos = newypos;
    boxColor = newboxColor;
  }

  public void drawBox(Graphics g) {
    g.setColor(Colors[boxColor]);
    g.fillRect(xpos, ypos, 50, 50);
  }

//Legt Farben der Richtungsboxen fest
  public void setColors() {
    int usedColors = 0;
    int newColor = 0;
    for (int z = 0; z < 4; z++) {
      int zz = (zzg.nextInt() % 8);
      if (zz < 0) zz = -zz;
      newColor = (int) Math.pow(2.0, zz);
      while ((usedColors & newColor) != 0) {
        zz = (zzg.nextInt() % 8);
        if (zz < 0) zz = -zz;
        newColor = (int) Math.pow(2.0, zz);
      }
      usedColors = usedColors | newColor;
      Colors[z] = getColor(zz);
    }
  }

//Ueberprueft, ob sich der Mauszeiger (x,y) im Intimbereich der Box B befindet
  public boolean inIntim(int x, int y) {
    if (y < (yOrig + 50)) return false;
    
other
switch (boxColor) {
      case 0: return (!((x < xpos) && (!((y < ypos) || (y > ypos +50)))));
      case 1: return (!((x > xpos + 50) && (!((y < ypos) || (y > ypos +50)))));
      case 2: return (!((y < ypos) && (!((x < xpos) || (x > xpos +50)))));
      case 3: return (!((y > ypos + 50) && (!((x < xpos) || (x > xpos +50)))));
    }
    return true;
  }

  public boolean handleEvent(Event e) {
    if (e.id == Event.WINDOW_DESTROY) {
      this.hide();
      this.dispose();
      System.exit(0);
    }
    return super.handleEvent(e);
  }

  public boolean mouseMove(Event e, int x, int y) {
    checkTime();
    if ((x >= xpos) && (x <= xpos + 50) && (y >= ypos) && (y <= ypos + 50)) {
      newBox = true;
      smile = true;
      Punkte++;
      Spruenge++;
      if ((Spruenge % 3) == 0) newRBoxes = true;
      repaint();
      return true;
    }
    else if ((x >= xpos - 50) && (x <= xpos + 100) && (y >= ypos - 50) && (y <= ypos + 100)) {
      if (inIntim(x, y)) {
        newBox = true;
        smile = false;
        Punkte--;
        Spruenge++;
        if ((Spruenge % 3) == 0) newRBoxes = true;
        repaint();
        return true;
      }
    }
    return super.mouseMove(e, x, y);
  }

  public boolean mouseDrag(Event e, int x, int y) {
    return this.mouseMove(e, x, y);
  }

  private void checkTime() {
    if (((System.currentTimeMillis() - time) / 1000) > Dauer) {
      this.hide();
      this.dispose();
      System.out.println("Ergebnis: " + Punkte + " Punkte in " + Dauer + " Sekunden");
      System.exit(0);
    }
  }
 
  public static void main(String[] args) {
    Jumpbox test = new Jumpbox(args);
    test.show();
    test.setSize();
  }
}