/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

// Jumpbox-Spiel, written by Xxxxx Xxxx, #0861732
// 29.5.97                         JDK 1.1.1, Linux

other
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class Jumpbox extends Frame implements Runnable, MouseMotionListener {
  private static Jumpbox Box;
  private static Thread thr;     // (Haupt-)Programm - Thread
  private static Thread thrAnim; // Animations - Thread
  private static long lStartTime;// Startzeit des Programms in Sekunden 
  private static int iZeit;      // Spielzeit
  private static int iScore;     // Spielstand
  private static Random ran;     // Zufallszahl, initialisiert mit Systemzeit
  private static int iXPos;      // X-Position der Gamebox
  private static int iYPos;      // Y-Position der Gamebox
  private static int iOldXPos;   // X-Position der vorherigen Gamebox
  private static int iOldYPos;   // Y-Position der vorherigen Gamebox
  private static Polygon pIntim; // Intimbereich
  private static Polygon pZiel;  // ist die Box in der Mitte von pIntim
  private static int iNewSkala;  // vor jedem 3. Sprung Skala wechseln
  private static Color[] cSkala; // Enthaelt die Farben der Richtungsskala, und ...
  private static int[] iSkala;   // ... enthaelt entsprechende integer Werte.
  private static int iRandBoxPos;// (0..39) fuer absolute Pos. der Gamebox
  private static int iGameboxStatus; // gibt Index auf Farbe der Gamebox
  private static Color cOldColor;// ist die Farbe der vorherigen Gamebox
  private static Image imagSmilie;
  private static Image imagWhinie;
  private static boolean bSmilie;// true: Smilie Animation soll gezeigt werden 
  private static boolean bWhinie;// true: Zeige Whinie Animation

  // Konstruktor
  public Jumpbox() {
    super("Jumpbox  -  Xxxxxxx Xxxx, #0861732");
    bSmilie = bWhinie = false;
    iScore = iNewSkala = 0;
    
    imagSmilie = Toolkit.getDefaultToolkit().getImage("smile.gif");
    imagWhinie = Toolkit.getDefaultToolkit().getImage("wince.gif");

    setSize(510, 430);		       // y-Richt.: +20 wg Titelleiste, +5 Fensterrahmen
    addMouseMotionListener(this);
    show();
  }
  
  // Ja, wo laufen sie denn ?
  public void run() {
    while (Thread.currentThread() == thrAnim) {
      if (bWhinie || bSmilie) {
        try {
          repaint();
          thrAnim.sleep(40);
        } 
other
catch (InterruptedException ex) {
          ;
        }
      }
      synchronized (thr) {
        thr.notify();
      }
      synchronized (thrAnim) {
        try {
          repaint();
          thrAnim.wait();
        } catch (InterruptedException ex) {
          ;
        }
      }
    }
  }
  
  // zeichnet das Window 
  public void paint(Graphics g) {
    drawRichtungsSkala(g);
    g.drawRect(5, 75, getSize().width - 11, getSize().height - 81);
    if (bSmilie)
      SmilieAnimation(g);
    if (bWhinie)
      WhinieAnimation(g);    
    // Verbindungslinie fuer 1/2 Sekunde zeigen
    if ((bSmilie) || (bWhinie)) {
      g.setColor(Color.black);
      g.drawLine(iOldXPos + 25, iOldYPos + 25, iXPos + 25, iYPos + 25);
      synchronized (thr) {
        try {
          thr.wait(500);
        } catch (java.lang.InterruptedException ex) {
          ;
        }
      }
      g.setColor(Color.white);
      g.drawLine(iOldXPos + 25, iOldYPos + 25, iXPos + 25, iYPos + 25);
    }
    bSmilie = bWhinie = false;
    draw50x50Box(g, iXPos, iYPos, cSkala[iGameboxStatus]);
    //g.drawPolygon(pIntim);                       // Zeichnet den Intimbereich
  }
    
  // Eventhandler
  public void mouseDragged (MouseEvent mev) {
    mouseMoved(mev);
  }

  public void mouseMoved (MouseEvent mev) {
    // Spielzeit abgelaufen ?
    if ((lStartTime + (iZeit * 1000)) < (System.currentTimeMillis())) {
      System.out.println("Ergebnis: " + iScore + " Punkte in " + iZeit 
                         + " Sekunden.");
      System.exit(0);   
    }
    // Maus im Intimbereich ?
    if (pIntim.contains(mev.getX(), mev.getY())) {
      // Minuspunkt registrieren
      iScore--;
      // Whinie-Animation abspielen
      bWhinie = true;
      // springen
      iOldXPos = iXPos;
      iOldYPos = iYPos;
      cOldColor = cSkala[iGameboxStatus];
      calcGamebox();
      iNewSkala++;
      if (iNewSkala == 3) {
        iNewSkala = 0;
        AssignColors();
      }
      repaint();
    }
    // Maus im Zielbereich ?
    if (pZiel.contains(mev.getX(), mev.getY())) {
      // Pluspunkt registrieren
      iScore++;
      // Smilie-Animation abspielen
      bSmilie = true;
      // springen
      iOldXPos = iXPos;
      iOldYPos = iYPos;
      cOldColor = cSkala[iGameboxStatus];
      calcGamebox();
      iNewSkala++;
      if (iNewSkala == 3) {
        iNewSkala = 0;
        AssignColors();
      }
      repaint();
    }
  }
  
  // Initialisiert das Spielfenster
  private void initialisize(Container ct) {
    ct.setBackground(Color.white);
    ct.setLayout(new FlowLayout(FlowLayout.CENTER));
    // Die Richtungsskala oben zeichnen
    AssignColors();    
    calcGamebox();
  }

  // Berechnet neue GameBox und deren Intimbereich
  private void calcGamebox() {
    iRandBoxPos = (int)(java.lang.Math.round(ran.nextFloat() * 39));
    iGameboxStatus = (int)(java.lang.Math.round(ran.nextFloat() * 3));

    // Spielfeld ist in 40 Blocke unterteilt (0..39), von links nach rechts,
    // oben nach unten. Absolute Pos in iRandBoxPos. Box noch zeichnen.
    // ( Ein Rand um das Spielfeld aus (50x50) Bloecken wird freigelassen fuer
    //   den Intimbereich der Randbloecke ! )
    if ((iRandBoxPos >= 0) && (iRandBoxPos <= 7)) {
      iXPos = (55 + iRandBoxPos * 50);
      iYPos = 125;
    }
    if ((iRandBoxPos >= 8) && (iRandBoxPos <= 15)) {
      iXPos = (55 + (iRandBoxPos - 8) * 50);
      iYPos = 175;
    }
    if ((iRandBoxPos >= 16) && (iRandBoxPos <= 23)) {
      iXPos = (55 + (iRandBoxPos  - 16) * 50);
      iYPos = 225;
    }
    if ((iRandBoxPos >= 24) && (iRandBoxPos <= 31)) {
      iXPos = (55 + (iRandBoxPos - 24) * 50);
      iYPos = 275;
    }
    if ((iRandBoxPos >= 32) && (iRandBoxPos <= 39)) {
      iXPos = (55 + (iRandBoxPos  - 32) * 50);
      iYPos = 325;
    }
    int[] xPointsBox = {iXPos, iXPos + 50, iXPos + 50, iXPos};
    int[] yPointsBox = {iYPos, iYPos, iYPos + 50, iYPos + 50};
    pZiel = new Polygon(xPointsBox, yPointsBox, 4);

    //Intimbereich berechnen
    if ( iGameboxStatus == 0) {
      int[] xPoints = {iXPos + 49, iXPos - 49, iXPos -49, iXPos + 98,
                       iXPos + 98, iXPos - 49, iXPos - 49, iXPos + 49};
      int[] yPoints = {iYPos, iYPos, iYPos - 49, iYPos - 49, 
                       iYPos + 98, iYPos + 98, iYPos + 49, iYPos + 49};
      pIntim = new Polygon(xPoints, yPoints, 8); 
     } else if ( iGameboxStatus == 1) {
      int[] xPoints = {iXPos, iXPos + 98, iXPos + 98, iXPos - 49,
                       iXPos - 49, iXPos + 98, iXPos + 98, iXPos};
      int[] yPoints = {iYPos, iYPos, iYPos - 49, iYPos - 49,
                       iYPos + 98, iYPos + 98, iYPos + 49, iYPos + 49};
      pIntim = new Polygon(xPoints, yPoints, 8); 
     } else if ( iGameboxStatus == 2 ) {
      int[] xPoints = {iXPos, iXPos, iXPos - 49, iXPos - 49,
                       iXPos + 98, iXPos + 98, iXPos + 49, iXPos + 49};
      int[] yPoints = {iYPos + 49, iYPos - 49, iYPos - 49, iYPos + 98,
                       iYPos + 98, iYPos - 49, iYPos - 49, iYPos + 49};
      pIntim = new Polygon(xPoints, yPoints, 8); 
     } else {
      int[] xPoints = {iXPos, iXPos, iXPos - 49, iXPos - 49,
                       iXPos + 98, iXPos + 98, iXPos + 49, iXPos + 49};
      int[] yPoints = {iYPos, iYPos + 98, iYPos + 98, iYPos - 49,
                       iYPos - 49, iYPos + 98, iYPos + 98, iYPos};
      pIntim = new Polygon(xPoints, yPoints, 8); 
     }
  }
    
  // ordnet O, U, R, L zufaellige, paarweise disjunkte Farben zu
  private void AssignColors() {
    Color colDummy = Color.white;
    int iRandomNumber = 0;    
    boolean boolFlag;
    cSkala = new Color[4];
    iSkala = new int[4];
    
    for (int i = 0; i < 4; i++) {
      boolFlag = true;
      while (boolFlag) {
        boolFlag = false;
        iRandomNumber = (int)(java.lang.Math.round(ran.nextFloat() * 7));
        for (int j = 0; j < i; j++) {
          if (iSkala[j] == iRandomNumber)
            boolFlag = true;
        }
      }
      switch(iRandomNumber) { 
        
other
case 0: colDummy = Color.blue; break;
        case 1: colDummy = Color.cyan; break;
        case 2: colDummy = Color.green; break;
        case 3: colDummy = Color.magenta; break;
        case 4: colDummy = Color.orange; break;
        case 5: colDummy = Color.pink; break;
        case 6: colDummy = Color.red; break;
        case 7: colDummy = Color.yellow; break;
      }
      cSkala[i] = colDummy;
      iSkala[i] = iRandomNumber;
    }
  }

  // zeichnet die erste 'Zeile' des Fensters (Richtungsskala)
  private void drawRichtungsSkala(Graphics g) {
    draw50x50Box(g, 0, 25, cSkala[0]);
    draw50x50Box(g, 50, 25, cSkala[1]);
    draw50x50Box(g, 100, 25, cSkala[2]);
    draw50x50Box(g, 150, 25, cSkala[3]);
    g.setColor(Color.black);
    g.drawString("L", 20, 50);
    g.drawString("R", 70, 50);
    g.drawString("O", 120, 50);
    g.drawString("U", 170, 50);
  }
  
  // Zeichnet eine farbige 50x50 Punkte Box an die Koordinaten (iLeft, iTop)
  private void draw50x50Box(Graphics g, int iLeft, int iTop, Color col) {
    g.setColor(col);
    g.fillRect(iLeft, iTop, 50, 50);
  
other
}

  // 'Gut gemacht' Animation
  private void SmilieAnimation(Graphics g) {
    for (int i = 25; i > 0; i--) {
      g.setColor(Color.black);
      g.drawImage(imagSmilie, iOldXPos, iOldYPos, 50 - 2 * i, 50 - 2 * i, this);
      synchronized (thrAnim) {
        thrAnim.notify();
      }
      synchronized (thr) {
        try {
          thr.wait();
        } catch (InterruptedException ex) {
          ;
        }
        g.setColor(cOldColor);
        g.drawRect(iOldXPos - 1, iOldYPos - 1, 52, 52);
      }
    }
  }
  
  // 'Du Loser' Animation
  private void WhinieAnimation(Graphics g) {
    for (int i = 0; i < 25; i++) {
      g.
other
setColor(Color.black);
      g.drawImage(imagWhinie, iOldXPos, iOldYPos, 50 - 2 * i, 50 - 2 * i, this);
      synchronized (thrAnim) {
        thrAnim.notify();
      }
      synchronized (thr) {
        try {
          thr.wait();
        } catch (InterruptedException ex) {
          ;
        }
        g.setColor(cOldColor);
        g.drawRect(iOldXPos - 1, iOldYPos - 1, 52, 52);
        g.setColor(Color.white);
        g.fillRect(iOldXPos, iOldYPos, 50, 50);
      }
    }
  }
  
  // Das Hauptprogramm
  public static void main(String[] argv) {
    if (argv.length > 0) {
      try {
        iZeit = (int)(new Integer(0).parseInt(argv[0]));
      } catch (NumberFormatException nfe) {
        iZeit = 15;
        System.out.println(nfe);
      }
    } else {
      iZeit = 15;
    }
    lStartTime = System.currentTimeMillis();
    ran = new Random(java.lang.System.currentTimeMillis()); 
    Box = new Jumpbox();
    thr = new Thread(Box);
    thr.start();
    thrAnim = new Thread(Box);
    thrAnim.start();
    Box.initialisize(Box);
  }
}

//  __  ___                _____            __   __       __
// |  |/  /.-----.----.---(_   _)--.-----. |  |_|  |-----|  |--.----. 
// |     < |  -  |   _|__ --| | -__|     | |   _   |  -  |     |     |
// |__|\__\|__|__|__| |_____|_|____|__|__| |__| |__|__|__|__|__|__|__|
//              eMail: karsten.hahn@stud.kit.edu