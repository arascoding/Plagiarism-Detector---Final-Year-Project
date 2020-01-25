/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

/** JAVA-Kompakt-Kurs SoSe 1997
 *  Jumpbox von Xxxxxx Xxxxxxxx
 *  Matrikel-Nr.: 862326
 *  
 *  letzte ï¿½nderung: 03.06.1997, 21:48 Uhr
 */


import java.awt.*;
import java.io.File;
import java.lang.*;


public class Jumpbox extends Frame
{
  // "Konstanten":
  // vorgegebene Farben:
  public final static Color farben[] =
  { Color.blue, Color.cyan, Color.green, Color.magenta,
    Color.orange, Color.pink, Color.red, Color.yellow };
  //Abkuerzungen fuer die Richtungen
  public final static String riName[] = { "L", "R", "O", "U" };
  // die folgende Werte sind wichtig, damit die "ï¿½ffnung" des
  //   Intimbereichs zur Box ï¿½berhaupt erreicht werden kann:
  // (es bleiben mind. 80 Pixel bis zum Rand)
  public final static int xMin[] = { 80,  0,  0,  0};
  public final static int yMin[] = { 50, 50,130, 50};
  public final static int xFak[] = {370,370,450,450};
  public final static int yFak[] = {300,300,220,220};

  // sonstige Variablen:
  public static int 

status=0; // 0=Start, 1=running, 2=Zeit abgelaufen
  public static int bisZumFarbwechsel=2;
  public static int punktzahl=0; 

  public static int dauer;
  public static int richtung;
  public static Insets jbInsets;
  public static int fIndex[];
  public static int mausX=-50, mausY=-50; // s. u.: berechnePosition
  public static int boxX, boxY;
  public static Image smile;
  public static Image wince;
  
  public final static Color hintergrundFarbe = Color.white;
 
  //////////////////////////////////////////////////////////////////

  public static void main(String arguments[])
  {
    // zuerst die Bilder laden (funktioniert sowieso nicht immer rechtzeitig):
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    smile = toolkit.getImage("smile.gif");
    wince = toolkit.getImage("wince.gif");

    status = 1;
    fIndex = new int[4];
     
    if (arguments.length == 0) dauer = 15;
        else dauer = (new Integer(arguments[0])).intValue();
    vorbereitung(); 

    Jumpbox jb = new Jumpbox();
    jb.

setTitle("Jumpbox (" + dauer + " Sekunden) von Xxxxxx Xxxxxxxx, #862326");
           
    // Die Groesse eines Frames wird "aussen" gemessen:
    // Daher nicht 500 x 400, sondern mit Kantenbreite
    // (Konstanten auslesen!) dazuaddiert!
    // Leider funktioniert das erst, nachdem das Fenster geï¿½ffnet ist, also:
    jb.resize(600,500);
    jb.show();
    // jetzt Insets besorgen, "ausmessen" und anpassen
    jbInsets = jb.insets();
    jb.resize(500 + jbInsets.left + jbInsets.right,
	      400 + jbInsets.top  + jbInsets.bottom);
    jb.repaint();

    // soweit der Vorbereitungsteil
    // alles uebrige ueber Event-Manager 
    try
    {
      Thread.sleep(dauer*1000);  // (dauer) Sekunden warten
    }
    catch(InterruptedException ie)
    {}

    // Beendigung vorbereiten:
    status = 2;
    jb.setTitle("Jumpbox (beendet)");
    // das Programm endet bei der nï¿½chsten Mausbewegung
  }


  // Behandlung von Mausbewegungen:
  public boolean handleEvent(Event ereignis)
  {
    boolean erfolg = false;
    switch (ereignis.id)
    {
      // wenn IRGENDEIN Maus-Event aufgetreten ist:
      case Event.MOUSE_MOVE: 
	if (status == 2) 
	{
	  System.out.println("Ergebnis: " + punktzahl + " Punkte in " +
			dauer + " Sekunden");
	  System.exit(0);
	}
      case Event.MOUSE_DRAG:
      case Event.MOUSE_DOWN:
      case Event.MOUSE_UP:
      case Event.MOUSE_ENTER:
      case Event.MOUSE_EXIT:
	mausX = ereignis.x - jbInsets.left;
	mausY = ereignis.y - jbInsets.top;
	return mausBehandlung(mausX, mausY);
      default:
	return false;   // also Behandlung durch hoehere Ebene veranlassen
    }
  }


  public boolean mausBehandlung(int mx, int my)
  {
    if (istErfolg(mx,my)) 
    {
      punktzahl++;
      grinsen();
      return true;
    }
    else
    if (istIntim (mx,my))
    {
      punktzahl--;
      grummeln();
      return true;
    }
    // sonst: nichts tun
    return true;
  

}

  public boolean istErfolg(int x, int y)
  {
    if ( x <  boxX ) return false;
    if ( x >= boxX + 50 ) return false;
    if ( y <  boxY) return false;
    if ( y >= boxY + 50 ) return false;
    return true;
  }

  public boolean istIntim( int x, int y)
  {
    if ( x <  boxX - 50 ) return false;
    if ( x >= boxX + 100 ) return false;
    if ( y <  boxY - 50 ) return false;
    if ( y >= boxY + 100 ) return false;

    // unter der Annahme, das bereits feststeht, dass
    // es sich NICHT um einen "Erfolg" handelt!!
 
    switch ( richtung )
    {
      case 0:  // LINKS
        x += 50; break;
      case 1:  // RECHTS
        x -= 50; break;
      case 2:  // OBEN
        y += 50;  break;
      case 3:  // UNTEN
        y -= 50; break;
    }
    return (!istErfolg(x,y));
  }
 

  public void grinsen()
  {
    // Vorbereitung:
    Graphics g = getGraphics();
   
    // Schleife:
    int 

breite = 50;
    g.setColor(hintergrundFarbe);
    for (int offset=0; offset<25; offset++)
    {    
      g.drawImage(smile, boxX + jbInsets.left + offset,
		         boxY + jbInsets.top  + offset,
		         breite, breite, this);
      repaint();
      try
      {
	Thread.sleep(40);  // AENDERN !!!!
      }
      catch(InterruptedException ie)
      {}
      g.drawRect(boxX + jbInsets.left + offset,
		 boxY + jbInsets.top  + offset,
	         breite, breite);
      breite -=2;
      repaint();
    }
    springen();
  }

  public void grummeln()
  {
    // Vorbereitung:
    Graphics 

g = getGraphics();

    g.drawImage(wince, boxX + jbInsets.left, boxY + jbInsets.top, this);
    repaint();

    try
    {
      Thread.sleep(500);
    }
    catch(InterruptedException ie)
    {}
    springen();
  }

  public void springen()
  {
    // Vorbereitung:
    Graphics g = getGraphics();

    // alle 3 Spruenge Farbe wechseln:
    if (bisZumFarbwechsel == 0)
    {
      berechneFarben();
      bisZumFarbwechsel=3;
    }
    bisZumFarbwechsel--;
    
    int altX = boxX, altY = boxY;
    
    berechnePosition();

    g.

setColor(Color.black);
    g.drawLine(altX + jbInsets.left + 25, altY + jbInsets.top + 25,
	       boxX + jbInsets.left + 25, boxY + jbInsets.top + 25);
    repaint();

    try
    {
      Thread.sleep(500);
    }
    catch(InterruptedException ie)
    {}    

    g.setColor(hintergrundFarbe);
    g.drawLine(altX + jbInsets.left + 25, altY + jbInsets.top + 25,
	       boxX + jbInsets.left + 25, boxY + jbInsets.top + 25);
    g.fillRect(altX + jbInsets.left, altY + jbInsets.top, 50, 50);
    g.setColor(farben[fIndex[richtung]]);
    g.fillRect(boxX + jbInsets.left, boxY + jbInsets.top, 50, 50);
    repaint();
  }

  private static void vorbereitung()
  {
    berechneFarben();
    berechnePosition();
  }

  private static void berechneFarben()
  {
     // die vier Farben bestimmen und in fIndex eintragen:
     for (int i=0; i<4; i++)
     {
       fIndex[i]=-1;
       while (fIndex[i]<0)
       {
	 fIndex[i]=(int) Math.round(7.98 * Math.random() - 0.49);
	 // ich gebe zu, dass dies keine Gleichverteilung produziert;
	 // aber die mir vorliegende Dokumentation ist leider etwas ungenau:
	 // Sind 0.0 und/oder 1.0 bei random() ein- oder ausgeschlossen?
	 // Wohin wird -0.5 gerundet?
         for (int j=0; j<i; j++)
	   if (fIndex[i] == fIndex[j]) fIndex[i]=-1;
       }
     }
  }


  //public void update(Graphics g)
  //{
  //  super.update(g);
  //}


  private static void berechnePosition()
  {
    // zuerst Richtung bestimmen:
    richtung = (int) Math.round ( Math.random() * 3.98 - 0.49);
   
    // ausreichend Abstand zum Rand, damit ein korrektes Erreichen auf jeden
    //    Fall mï¿½glich ist:
    boolean erlaubt = false;
    while (erlaubt == false)
    {
      boxX = (int) Math.round ( Math.random() * xFak[richtung]) + xMin[richtung];
      boxY = (int) Math.round ( Math.random() * yFak[richtung]) + yMin[richtung];
      if (!( (mausX-boxX>-60) && (mausX-boxX < 110) &&
	    (mausY-boxY>-60) && (mausY-boxX < 110) ))
	erlaubt=true;
    }
  

}


  public void paint(Graphics g)
  {
    super.paint(g);
    // Hintergrund zeichnen:
    // Grundfarbe
    g.setColor(hintergrundFarbe);
    g.fillRect(jbInsets.left ,jbInsets.top,
	       500 + jbInsets.left ,400 + jbInsets.top);
    // Rahmen
    g.setColor(Color.darkGray);
    g.drawRect(jbInsets.left, 50 + jbInsets.top,
	       499 + jbInsets.left ,349 + jbInsets.top);
     
    // die vier Quadrate zeichnen:
    for (int i=0; i<4; i++)
    {
      // die Farbquadrate
      g.setColor(farben[fIndex[i]]);
      g.fillRect(jbInsets.left + i*50, jbInsets.top, 50, 50);
      // ... und die Beschriftung
      g.setColor(Color.black); // Schwarz fuer guten Kontrast
      g.drawString(riName[i], jbInsets.left + i*50 + 22, jbInsets.top + 32);
    }

    // DIE Box zeichnen
    g.setColor(farben[fIndex[richtung]]);
    g.fillRect(boxX + jbInsets.left, boxY + jbInsets.top, 50, 50); 
  }
}
