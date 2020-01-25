/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.awt.*;
otherimport java.util.*;
import java.applet.*;
import java.net.*;

public class Jumpbox extends Canvas implements Runnable
{
  static  String Copyr = "JUMPBOX  von Xxxxxx Xxxxx, 0861061  (";
  public  static int seconds;	// die Spielzeit
  static  Jumpbox Leinwand; 	// Die Leinwand
  Frame   Fenster;
  // Konstanten
  final String Richtungszeichen[]={"L","R","O","U"};
  final int otherFROM_LEFT   = 0;
  final int FROM_RIGHT  = 1;
  final int FROM_TOP    = 2;
  final int FROM_BOTTOM = 3;
  // Variablen;
  int punkte = 0;
  int runde = 0;
  int Richtungsboxen[]={0,0,0,0};
  int otherspielfarbe = 0;
  int KlotzX =-1000;
  int KlotzY =-1000;
  int von_wo = FROM_LEFT;
  long starttime;
  Image offscreenImage;
  Graphics offscreenGraphics;
  Image Smily   = getToolkit().getImage("smile.gif");
  Image Wince = getToolkit().getImage("wince.gif");
  boolean started = false;
  boolean update = true;
  
  // Fuer die Threads
  Thread Animator; 	// Der Wince-Animator
  Object O; 		// Zur Synchronisation
  
  public static void main(String argv[])
  {
      // System.out.println("HINWEIS: Die Namen der GIF-Dateien muessen komplett kleingeschrieben sein.");
      // System.out.println("         andernfalls laufen die Animationen wohl nicht.");
      Leinwand = new Jumpbox(argv);
  }

  public Jumpbox(String argv[])
  {
      Fenster = new Frame();
      setBackground(Color.lightGray);
      setForeground(Color.lightGray);
      if (argv.length==1) seconds = new Integer(argv[0]).intValue();
      else seconds = 15;
      Fenster.setTitle(Copyr + seconds + " seconds)");
      Fenster.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
      Fenster.othershow();
      NewRichtungsboxen();
      NewBox();
      Fenster.add(this);
      Fenster.pack();
      getGraphics().drawImage(Wince,300,0,this); 	// vorab anzeigen,
      getGraphics().drawImage(Smily, 300,0,this); 	// damit sie beim 1. Hit/Miss
      repaint();                                   			// auch WIRKLICH angezeigt werden
      starttime = System.currentTimeMillis();
      O = new Object();
      Animator = new Thread(this);
      Animator.start();
      started = true;
  }

  public Dimension minimumSize()
  { return new Dimension(500,400);
  }

  public Dimension preferredSize()
  {  return new Dimension(500,400);
  }

  public void MakeBuffer()
  {
      offscreenImage = createImage(this.size().width,this.size().height);
      offscreenGraphics = offscreenImage.getGraphics();
  }

  public void update(Graphics g)
  {
      if (offscreenImage==null) MakeBuffer();
      if (update)
      {
          offscreenGraphics.setColor(getBackground()); offscreenGraphics.fillRect(0,50,size().width,size().height-50); offscreenGraphics.setColor(getForeground());
      }
      otherpaint(g);
  }

  public void MaleKlotz(Graphics g, int X, int Y, Color farbe)
  {
      g.setColor(farbe);
      g.fillRect(X,Y+50,50,50);
  }

  public void paint(Graphics g)
  {
      if (offscreenImage==null) MakeBuffer();
       /* offscreenGraphics.setColor(Color.yellow);
      offscreenGraphics.drawRect(0,0,this.size().width-1,50-1); */
      if (update)
      {
         offscreenGraphics.setColor(Color.white);
         offscreenGraphics.drawRect(0,50,this.size().width-1,this.size().height-50-1);
    
         for (int i=0; i<4; i++)
         {
             offscreenGraphics.setColor(Farbe(Richtungsboxen[i]));
             offscreenGraphics.fillRect(i*50,0,50,50);
             offscreenGraphics.setColor(Color.black);

             FontMetrics FM = getFontMetrics(offscreenGraphics.getFont());
             offscreenGraphics.drawString(Richtungszeichen[i],(50-FM.stringWidth(Richtungszeichen[i]))/2+i*50,
                                                       (50-FM.getHeight())/2+FM.getHeight());
         }
         MaleKlotz(offscreenGraphics,KlotzX,KlotzY,Farbe(spielfarbe));
      }
      g.drawImage(offscreenImage,0,0,this);
  }

  private void NewRichtungsboxen()
  {
     Random zufall = new Random();
     int col;
     boolean b;
    
     for (int i=0;i<4;i++)
     {
        Richtungsboxen[i]=0;
        do
        {
           b=true;
           col=Math.abs(zufall.nextInt() % 8);
           otherfor (int k=0;k<i;k++)
           {  if (col==Richtungsboxen[k]) { b=false; break; } 
           }
        }
        while (b==false);
        Richtungsboxen[i]=col;
     }
  }

  private void NewBox()
  {
      Random zufall = new Random();
      von_wo=Math.abs(zufall.nextInt() % 4);

      spielfarbe=Richtungsboxen[von_wo];
    
      int x=KlotzX; int y=KlotzY; long i;
      KlotzX=Math.abs(zufall.nextInt() % 450);
      KlotzY=Math.abs(zufall.nextInt() % 300);
  }

  protected Color Farbe(int i) 		// Zuordnung Int --> Color.XXXX
  {
     otherswitch (i)
     {
        case 0  : return Color.blue; 
        case 1  : return Color.cyan; 
        case 2  : return Color.green;
        case 3  : return Color.magenta;
        case 4  : return Color.orange;
        case 5  : return Color.pink;
        case 6  : return Color.red;
        case 7  : return Color.yellow;
        default : return Color.darkGray;
     }
  }

  public boolean mouseMove(Event E,int x,int y)
  {
      check(x,y);
      return true;
  }

  public boolean mouseDrag(Event E,int x,int y)
  {
     check(x,y);
     return true;
  }

  private void neue_Runde(boolean B)
  {
      int oldX=KlotzX; int oldY=KlotzY; Color oldColor=Farbe(spielfarbe);
      runde++;
      if (B) punkte++; 
      else punkte--;
      if ((runde%3)==0) NewRichtungsboxen();
      NewBox();

      repaint();
      if (oldX>=0)
      {
         if (offscreenImage==null) MakeBuffer();
         offscreenGraphics.setColor(getBackground()); offscreenGraphics.fillRect(0,50,size().width,size().height-50); offscreenGraphics.setColor(getForeground());
         Graphics g =getGraphics();
         g.drawImage(offscreenImage,0,0,this);
         MaleKlotz(g,oldX,oldY,oldColor);
         MaleKlotz(g,KlotzX,KlotzY,Farbe(spielfarbe));
         g.setColor(Color.black);
         g.drawLine(oldX+25,oldY+50+25,KlotzX+25,KlotzY+50+25);
         try { Thread.sleep(500); 
               } catch(InterruptedException E) {}
         offscreenGraphics.setColor(getBackground()); offscreenGraphics.fillRect(0,50,size().width,size().height-50); offscreenGraphics.setColor(getForeground());
         repaint();
      }
  }

  private boolean inIntimbereich(int x, int y) // (x,y) ist lokal auf Aktionsflaeche!
   { // geht davon aus, das InBox(x,y) false ist.
    otherswitch (von_wo)
     { 
/*      case FROM_LEFT   : return ((y>=KlotzY-50)&&(y<KlotzY)&&(x>=KlotzX-50)&&(x<=KlotzX+100))||
                                ((y>KlotzY+50)&&(y<=KlotzY+100)&&(x>=KlotzX-50)&&(x<=KlotzX+100))||
                                ((y>=KlotzY)&&(y<=KlotzY+50)&&(x>KlotzX+50)&&(x<=KlotzX+100));
      case FROM_RIGHT  : return ((y>=KlotzY-50)&&(y<KlotzY)&&(x>=KlotzX-50)&&(x<=KlotzX+100))||
                                ((y>KlotzY+50)&&(y<=KlotzY+100)&&(x>=KlotzX-50)&&(x<=KlotzX+100))||
                                ((y>=KlotzY)&&(y<=KlotzY+50)&&(x>=KlotzX-50)&&(x<KlotzX));
      case FROM_TOP    : return ((x>=KlotzX-50)&&(x<KlotzX)&&(y>=KlotzY-50)&&(y<=KlotzY+100))||
                                ((x>KlotzX+50)&&(x<=KlotzX+100)&&(y>=KlotzY-50)&&(y<=KlotzY+100))||
                                ((x>=KlotzX)&&(x<=KlotzX+50)&&(y>KlotzY+50)&&(y<=KlotzY+100));
      case FROM_BOTTOM : return ((x>=KlotzX-50)&&(x<KlotzX)&&(y>=KlotzY-50)&&(y<=KlotzY+100))||
                                ((x>KlotzX+50)&&(x<=KlotzX+100)&&(y>=KlotzY-50)&&(y<=KlotzY+100))||
                                ((x>=KlotzX)&&(x<=KlotzX+50)&&(y>=KlotzY-50)&&(y<KlotzY));
*/
      case FROM_LEFT   : return ((x>=KlotzX-50)&&(x<=KlotzX+100)&&(y>=KlotzY-50)&&(y<=KlotzY+100))&&
                                (!((x<KlotzX   )&& (!((y<KlotzY)||(y>KlotzY+50)))));
      case FROM_RIGHT  : return ((x>=KlotzX-50)&&(x<=KlotzX+100)&&(y>=KlotzY-50)&&(y<=KlotzY+100))&&
                                (!((x>KlotzX+50)&& (!((y<KlotzY)||(y>KlotzY+50)))));
      case FROM_TOP    : return ((x>=KlotzX-50)&&(x<=KlotzX+100)&&(y>=KlotzY-50)&&(y<=KlotzY+100))&&
                                (!((y<KlotzY   )&& (!((x<KlotzX)||(x>KlotzX+50)))));
      case FROM_BOTTOM : return ((x>=KlotzX-50)&&(x<=KlotzX+100)&&(y>=KlotzY-50)&&(y<=KlotzY+100))&&
                                (!((y>KlotzY+50)&& (!((x<KlotzX)||(x>KlotzX+50)))));

      default : return false;
     }
   }

  private boolean inBox(int x,int y) // (x,y) ist lokal auf Aktionsflaeche!
  {
      return ((x>=KlotzX)&&(x<=KlotzX+50)&&(y>=KlotzY)&&(y<=KlotzY+50));
  }

  public void check(int x,int y)
  {
      if (!started) return;
      if (((System.currentTimeMillis()-starttime)/1000)>seconds)
      {
         hide(); 
         System.out.println("Ergebnis: "+punkte+" Punkte in "+seconds+" Sekunden");
         System.exit(0); 
      }
      try 
      {
         if (y<50) y=-999; // Richtungsboxen zaehlen nicht zum Aktionsbereich
         if (inBox(x,y-50)) // inBox rechnet aktionsflaechen-lokale (x,y)
         { // Animation geht auch lokal ...
            Graphics otherg = getGraphics(); g.setColor(Farbe(spielfarbe));
        /*    for (int i=0;i<4;i++)
            {
               g.drawImage(Smily,KlotzX,KlotzY+50,50,50,this);
               Thread.sleep(100);
               g.fillRect(KlotzX,KlotzY+50,50,50);
               Thread.sleep(100);
            }
        */
            g.drawImage(Smily,KlotzX,KlotzY+50,50,50,this);
            //Thread.sleep(100);
            Thread.sleep(500);
            repaint();
            neue_Runde(true); 
            repaint();
         }
         else 
            if (inIntimbereich(x,y-50)) // inIntimbereich rechnet aktionsflaechen-lokale (x,y)
            { // ... oder Animation mit einem Thread :)
                synchronized(O) { O.notify(); }
                Thread.sleep(100); // Der Animation Zeit zum Synchronized(O) geben
                synchronized(O) { repaint(); } // wenn die wait() macht, gehts hier weiter
                neue_Runde(false); 
                repaint();
            }
      } catch (InterruptedException IE) {};
  }

  public void run()
  {
     synchronized (O)
     {
        for (;;)
        { 
           try { O.wait();} catch (InterruptedException E) {}
           Graphics g = getGraphics();
           Image im = Wince;
           if (offscreenGraphics == null) MakeBuffer();
           for (int i=0; i<25; i++)
           {
              /* g.setColor(Farbe(spielfarbe));
              g.fillRect(KlotzX,KlotzY+50,50,i);
              g.fillRect(KlotzX,KlotzY+100-i,50,i);
              g.fillRect(KlotzX,KlotzY+50,i,50);
              g.fillRect(KlotzX+50-i,KlotzY+50,i,50);
              g.drawImage(im,KlotzX+i,KlotzY+50+i,50-2*i,50-2*i,this);
	*/
              offscreenGraphics.setColor(Farbe(spielfarbe));
              offscreenGraphics.fillRect(KlotzX, KlotzY+50,50,i);
              offscreenGraphics.fillRect(KlotzX,KlotzY+100-i,50,i);
              offscreenGraphics.fillRect(KlotzX,KlotzY+50,i,50);
              offscreenGraphics.fillRect(KlotzX+50-i,KlotzY+50,i,50);
              offscreenGraphics.drawImage(im,KlotzX+i,KlotzY+50+i,50-2*i,50-2*i,this);
              g.drawImage(offscreenImage,0,0,this);
              try { Thread.sleep(40); } catch (InterruptedException E) {}
           }
           g.fillRect(KlotzX,KlotzY+50,50,50);
           //getGraphics().drawImage(offscreenImage,0,0,this);
           //update=false; update(g); update=true;
       }
     }
   }
 }