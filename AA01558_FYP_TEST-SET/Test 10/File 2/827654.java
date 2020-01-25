/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

/* Programmname   : Jumpbox.java
   Autor          : Xxxxx Xxxxxx
   erstellt am    : 14.1.1997
   letzt ï¿½nderung : 28.5.1997
   Info           : JAKK Aufgabe Nr.5
   ï¿½bersicht      :                                                         */


import java.awt.*;
import java.util.Random;
import java.util.Date;

public class Jumpbox extends Frame implements Runnable
{
  static Jumpbox mainWindow = null;

  Thread  thread;

  private Color   links,
                  rechts,
                  oben,
                  unten,
                  richtung;

  private final Color hintergrund = Color.white;

  private static long 
other
startzeit;
  private static int  dauer   = 20;
  private static int  x       = 0,
                      y       = 0,
                      breite  = 0,
                      hoehe   = 0,
                      treffer = 0,
                      fehler  = 0,
                      sprung  = 0;
           
  private boolean animation  = false;   
  private boolean pluspunkt  = false;   // Animation fï¿½r Pluspunkt o. fï¿½r Minuspunkt
  private boolean bildZeigen = false; 

  private static Random koordinate;

  private static Image    smile;
  private static Image    
other
wince;
  private static Image    bild;
  Image    offscreenImg = null;
  Graphics offscreenG   = null;


  public Jumpbox (String titel)
  {
     super (titel);
     resize (500, 400);
     setBackground (hintergrund);

     startzeit  = (new Date ()).getTime ();
     koordinate = new Random ();
     smile 
other
= Toolkit.getDefaultToolkit ().getImage ("SMILE.GIF");
     wince = Toolkit.getDefaultToolkit ().getImage ("WINCE.GIF");     
     
     farbenFestlegen ();
                                      
     show ();
     b_setzen ();
 
     thread = new Thread (this);
     thread.start ();

  } // end Konstruktor


  public static void main (String args[])
  {
     try
     {
        if (args.length > 0)
          dauer = Integer.parseInt (args[0]);
        
     }
     catch (NumberFormatException nfe)
     {
        System.out.println ("Usage: Jumpbox [<time in seconds>]");
        System.exit (0);
     }

     mainWindow = new Jumpbox ("Xxxxx Xxxxxxxx  827654");
  }


  public void run ()
  {          
    for (;;)
    {    
      warten ();
      
      if (animation)
        if (pluspunkt)
        {
          b_grinst (); 
          b_springt ();
        }
        else
        {
          b_grummelt ();
          b_springt ();
        }        
     }
  }


  public synchronized void warten ()
  {
     animation = false;

     try {wait (); } catch (InterruptedException ie) {}
  }


  public synchronized void animation ()
  {
     animation = true;

     notifyAll ();
  }


  public void update (Graphics g)
  {
     if (offscreenG == null)
     {
        System.out.println ("Startup!");
        offscreenImg  = createImage (500, 400);
        offscreenG    = offscreenImg.getGraphics ();
        offscreenG.setColor (hintergrund);
        offscreenG.fillRect (0, 0, 500, 400);

        richtungsSkala ();
        // Keine Ahnung, aber wenn das nicht dabei, dann wird
        // das erste mal bei einem Fehler keine Animation gezeigt!
        offscreenG.drawImage (wince, x, y, 50, 50, this);

        b_zeichnen (x, y, richtung);
     }
 
     if (bildZeigen)
        offscreenG.drawImage (bild, x, y, breite, hoehe, this);
    
     g.drawImage (offscreenImg, 0, 0, this);
  }


  public void show ()
  {
     super.show();
     
     invalidate();
     validate();
  }

  public void paint (Graphics g)
  {
     update (g);
     System.out.println ("paint");                         
  
other
}


  private void richtungsSkala ()
  {
     offscreenG.setColor (links);
     offscreenG.fillRect (0, 0, 50, 50);
     offscreenG.setColor (rechts);
     offscreenG.fillRect (50, 0, 50, 50);
     offscreenG.setColor (oben);
     offscreenG.fillRect (100, 0, 50, 50);
     offscreenG.setColor (unten);
     offscreenG.fillRect (150, 0, 50, 50);
     offscreenG.setColor (Color.black);
     offscreenG.drawString ("L", 20, 30);
     offscreenG.drawString ("R", 70, 30);
     offscreenG.drawString ("O", 120, 30);
     offscreenG.drawString ("U", 170, 30);   
  }
  
  public boolean handleEvent (Event event)
  {
     if (((event.id == Event.MOUSE_MOVE) |
          (event.id == Event.MOUSE_DRAG)) &
          (!animation))
     {
        // Spieldauer ï¿½berschrittten?
        long zeit = (new Date ()).getTime ();
        if (zeit > (startzeit + (dauer * 1000)))
        {
          System.out.println ("Ergebnis: " +
                              Integer.toString (treffer - fehler) +
                              " Punkte in " +
                              Integer.toString (dauer) +
                              " Sekunden.");
          System.exit (0);
        }

        if (istTreffer (event.x, event.y))
        {
           treffer++;
           sprung++;

           if (sprung == 3)
           {
              farbenFestlegen ();
              richtungsSkala ();
              repaint (0, 0, 500, 52);
           }

           pluspunkt = true;

           animation ();
        }
        else if (intimbereich (event.x, event.y))
        {
           fehler++;
           sprung++;

           if (sprung == 3)
           {
              farbenFestlegen ();
              richtungsSkala ();
              repaint (0, 0, 500, 52);
           }

           pluspunkt = false;

           animation ();
        }
           
        return true;
     }

     return false;
  }


  private void b_springt ()
  {
     int x_alt = x;
     int y_alt = y;

     b_zeichnen (x, y, richtung);

     b_setzen ();

     offscreenG.setColor (Color.red);
     offscreenG.drawLine ((x_alt + 25), (y_alt + 25), (x + 25), (y + 25));
         
     repaint (0, 51, 500, 400);
     
     pause (500);

     b_zeichnen (x_alt, y_alt, hintergrund);
     offscreenG.setColor (hintergrund);
     offscreenG.drawLine ((x_alt + 25), (y_alt + 25), (x + 25), (y + 25));
     b_zeichnen (x, y, richtung);

     repaint (0, 51, 500, 400);                                      
  }


  private void b_grinst ()
  {
     bild       = smile;
     bildZeigen = true;
     breite     = 50;
     hoehe      = 50;

     repaint (x, y, 50, 50);

     pause (500);

     bildZeigen = false;  
  }


  private void b_grummelt ()
  {
     bild       = wince;
     bildZeigen = true;
     breite     = 50;
     hoehe      = 50;

     int x_alt = x;
     int y_alt = y;
          
     for (int n = 1; n <= 25; n++)
     {
        b_zeichnen (x_alt, y_alt, hintergrund);

        offscreenG.setColor (Color.red);

        repaint (x_alt, y_alt, 50, 50);

        pause   (40);
         
        breite = breite - 2;
        hoehe  = hoehe - 2;
        x      = x + 1;
        y      = y + 1;
     }                     

     bildZeigen = false;
     x          = x_alt;
     y          = y_alt;

     b_zeichnen (x, y, richtung);

     repaint ();
  }


  private void b_setzen ()
  {
     // int x, y;

     do
     {
        x = (int) (koordinate.nextFloat () * 1000);
     }
     //while ((x < 0) | (x > 450));
     while ((x < 20) | (x > 430));

     do
     {
        y = (int) (koordinate.nextFloat () * 1000);
     }
     //while ((y < 50) | (y > 350));
     while ((y < 70) | (y > 330));

     richtung = getFarbe ();
  }


  private void b_zeichnen (int x, int y, Color farbe)
  {
     offscreenG.setColor (farbe);
     offscreenG.fillRect (x, y, 50, 50);
  }


  private boolean istTreffer (int x, int y)
  {
     if ((x >= this.x) & (x <= (this.x + 50)) &
         (y >= this.y) & (y <= (this.y + 50)))
     {
        return true;
     }
     return false;
  }

         
  private boolean intimbereich (int x, int y)
  {
     if ((x >= (this.x - 50)) & (x <= (this.x + 100)) &
         (y >= (this.y - 50)) & (y <= (this.y + 100)))
     {
        if ((richtung.equals (links)) &
            ((x < this.x) & !((y < this.y) | (y > (this.y + 50)))))
        {
           return false;
        }
        else if ((richtung.equals (rechts)) &
                 ((x > (this.x + 50)) & !((y < this.y) | (y > (this.y + 50)))))
        {
           return false;
        }
        else if ((richtung.equals (oben)) &
                 ((y < this.y) & !((x < this.x) | (x > (this.x + 50)))))
        {
           return false;
        }
        else if ((richtung.equals (unten)) &
                 ((y > (this.y + 50)) & !((x < this.x) | (x > (this.x + 50)))))
        {
           return false;
        }
        else
        {
           return true;
        } 
     }
     return false;
  }


  private void farbenFestlegen ()
  {
     boolean vorhanden  = false;
     Color   richtung[] = new Color [4];

     sprung = 0;  
                                       
     for (int i = 0; i < 4; i++)
     {     
        do
        {  
           Color   farbe     = getFarbe (true);
           vorhanden = false;

           for (int j = 0; j < i; j++)
           {                                   
              if (farbe == richtung [j])
              {
                 vorhanden = true;
                 break;
              }
           }   

           richtung [i] = farbe;
        }
        while (vorhanden);
     }

     links  = richtung [0];
     rechts = richtung [1];
     oben   = richtung [2];
     unten  = richtung [3];
  }


  private Color getFarbe ()
  {
     return getFarbe (false);
  }


  private Color getFarbe (boolean ausAllen)
  {                                       
     Random  random  = new Random ();
     int     farbe   = 0;
     int     grenze  = 8;

     if (!ausAllen) { grenze = 4; }

     do
     {
        farbe = (int) (random.nextFloat () * 10);
     }
     while ((farbe < 0) | (farbe >= grenze));

     if (ausAllen)
     {
        
other
switch (farbe)
        {
           case 0 : return Color.blue;
           case 1 : return Color.cyan;
           case 2 : return Color.green;
           case 3 : return Color.magenta;
           case 4 : return Color.orange;
           case 5 : return Color.pink;
           case 6 : return Color.red;
           case 7 : return Color.yellow;
        }
     }
     else
     {
        
other
switch (farbe)
        {
           case 0 : return links;
           case 1 : return rechts;
           case 2 : return oben;
           case 3 : return unten;
        }
     }

     return Color.black; // Compiler motzt!
  }


  private void pause (int time)
  {
     try { Thread.sleep (time); }
     catch (InterruptedException e) {}
  }

} // end class Jumpbox