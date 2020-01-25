/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.awt.*;
import java.util.*;

public class Jumpbox extends Frame
{
  public final static int Links  = 0;
  public final static int otherRechts = 1;
  public final static int Oben   = 2;
  public final static int Unten  = 3;
 
  public static Random mZufall;
  public static int mDauer; // in Sekunden

  public static int mXPos = 200; // Koordinaten des Rechtecks
  public static int mYPos = 200; 
  public static int mMouseX;
  public static int mMouseY;
  public static int mRichtung = Links; // ï¿½ffnung L R O U
  public static int mPunkte = 0;
  public static int mSpruenge = 0;

  public static Color mColors[] = { Color.blue, Color.cyan, Color.green, Color.magenta, Color.orange, Color.pink, Color.red, Color.yellow }; // Farben im allgemeinen

  public static int    mRichtungsFarbe[]; // Farbnummer im Array
  public static String mRichtungsName[] = { "L", "R", "O", "U" };
  public static Insets mInsets;
  public static Thread Animation;
  public static Image     OffImage;
  public static Graphics  OffGraphics;
  public static Dimension OffDimension;
  public static Image     Smiley;
  public static Image     Grummel;

	public static void main( String[] argumente)
	{
		if ( argumente.length > 0)
			mDauer = Integer.parseInt( argumente[0] );
		else
			mDauer = 15;

		mZufall = new Random();
		mRichtungsFarbe = new int[4];
		
		// Load Animatin Picts from Disc
		Toolkit toolkit = Toolkit.getDefaultToolkit();
                Smiley  = toolkit.getImage("smile.gif");
		Grummel = toolkit.getImage("wince.gif");

		newColors();
		newJumper();

		Jumpbox  myFenster = new Jumpbox();

		// try to force image loading NOW... did't work really
		for ( int theSize = 50; theSize > 0; theSize -= 2)
		  toolkit.prepareImage( Smiley, theSize, theSize, myFenster);
		toolkit.otherprepareImage( Grummel, 50, 50, myFenster);

		// myFenster.repaint();
		myFenster.resize( 500, 400 ); // ca. groesse
		myFenster.show();
		mInsets = myFenster.insets();
		myFenster.resize( 500 + mInsets.left +mInsets.right, 400+mInsets.top+mInsets.bottom);
		// myFenster.show();
		myFenster.repaint();

		// Animations Thread starten
		// Runnable AnimRun = new Jumpbox()
		// Thread t = new Thread( AnimRun )
		// t.start

		// warte
		try
		{
		  Thread.sleep( mDauer * 1000 );
		}
		catch( InterruptedException ie)
		{
		}

		// Endergebnis
		System.out.println( "Ergebnis: " + mPunkte +" Punkte in " + mDauer + " Sekunden.");
		System.exit(0);
      	}

	public Image Buffer = null;

	public Jumpbox()
	{
	        setTitle( "Xxxxxxx Xxxxxxx (862564's) Jumpbox ( "+Jumpbox.mDauer+" Sekunden )");
	     	// pack();
      	}
	
	public void update(Graphics g)
	{
		super.update( g );
		// Buffer allokieren
		// if Buffer = null Buffer = createImage( size )
	other}

  public void paint( Graphics g)
  {
    super.paint( g );

    // zeichne den weissen Rahmen
    g.setColor( Color.white);
    g.drawRect( mInsets.left, mInsets.top + 50, 500, 450);

    // Zeichne den Jumper
    g.setColor( mColors[mRichtungsFarbe[ mRichtung ]]);
    g.fillRect( mXPos + mInsets.left, mYPos + mInsets.top + 50, 50, 50);

    // Zeichne die 4 Quadrate
    for( int i = 0; i < 4 ; i++)
    {
      g.setColor( mColors[mRichtungsFarbe[i]]);
      g.fillRect( i * 50 + mInsets.left, mInsets.top, 50, 50);
      g.setColor( Color.black);
      g.drawString( mRichtungsName[i], i * 50 + 20 + mInsets.left, 30 + mInsets.top); 
    }
  }
 
  public boolean handleEvent( Event inEvt )
  {
    boolean result = false;
    switch ( inEvt.id )
    {
        case Event.MOUSE_MOVE:
	case Event.MOUSE_DRAG:
	case Event.MOUSE_DOWN:
        case Event.MOUSE_UP:
        case Event.MOUSE_ENTER:
        case Event.MOUSE_EXIT:
	  mMouseX = inEvt.x - mInsets.left;
	  mMouseY = inEvt.y - mInsets.top - 50;
 	  handleMouse( mMouseX, mMouseY );
	  result = true;
	  break;
      default:
	result = super.handleEvent( inEvt);
    }
    return result;
  } 

  public void handleMouse( int x, int y )
  {
    if (isBereich( x, y))
       grinsen();
    if (isIntim(x,y))
       grummeln();
  }
  


  public void grinsen()
  {
    // System.out.println( "- Im Bereich - ");
    mPunkte++;

    // Animiere
    Graphics otherg = getGraphics();
    
    // resize schleife
    for ( int theSize = 50; theSize > 0; theSize -= 2)
    {
      // g.setColor( getBackground() ); // ging ohne Double Buffer nicht :)
      // g.fillRect( mXPos + mInsets.left, mXPos + mInsets.top + 50, 50, 50);     
      g.drawImage( Smiley, mXPos+mInsets.left+(50-theSize)/2, mYPos+mInsets.top+50+(50-theSize)/2, theSize, theSize,this);
      repaint();

      try
      {
	  Thread.sleep( 40 );
      }
      catch ( InterruptedException ie )
      {
      }
    }
    springen();
  }

  public void grummeln()
  {
    // System.out.println( "- Intim Bereich -");
    mPunkte--;

    // Animiere
    Graphics otherg = getGraphics();
    g.drawImage( Grummel, mXPos+mInsets.left, mYPos+mInsets.top +50,this);
    repaint();

    try
    {
      Thread.sleep( 500 );
    }
    catch ( InterruptedException ie )
    {
    }

    springen();
  }

  public void springen()
  {
    Graphics g = getGraphics();

    g.setColor( mColors[mRichtungsFarbe[ mRichtung ]]);
    g.fillRect( mXPos + mInsets.left, mYPos + mInsets.top + 50, 50, 50);

    mSpruenge++;

    // Spruenge zaehlen und evtl. farben wechseln
    if ( mSpruenge == 3 )
    {
      newColors();
      mSpruenge = 0;
    }

    int xOld = mXPos;
    int yOld = mYPos;

    boolean theOK = false;
    do
    {
      newJumper();
      theOK = ( isIntim( mMouseX, mMouseY) == false ) && ( isBereich( mMouseX, mMouseY) == false );  
    } while ( theOK == false );

    g.othersetColor( Color.black );
    g.drawLine( xOld+mInsets.left+25, yOld+mInsets.top+75,mXPos + mInsets.left + 25, mYPos + mInsets.top + 75 );

    repaint();

    try
    {
      Thread.sleep( 500 );
    }
    catch ( InterruptedException ie )
    {
    }

    g.setColor( getBackground() );
    g.drawLine( xOld+mInsets.left+25, yOld+mInsets.top+75,mXPos + mInsets.left + 25, mYPos + mInsets.top + 75 );
    g.fillRect( xOld + mInsets.left, yOld + mInsets.top + 50, 50, 50);

    repaint();
  other}


  public boolean isBereich( int x, int y)
  {
    if ( x <  mXPos ) return false;
    if ( x >= mXPos + 50 ) return false;
    if ( y <  mYPos) return false;
    if ( y >= mYPos + 50 ) return false;
    return true;
  }


  public boolean isIntim( int x, int y)
  {
    if ( x <  mXPos - 50 ) return false;
    if ( x >= mXPos + 100 ) return false;
    if ( y <  mYPos - 50 ) return false;
    if ( y >= mYPos + 100 ) return false;

    switch ( mRichtung )
    {
      case Links:
	if ( x < mXPos + 50 && y >= mYPos && y < mYPos + 50) return false;
        break;
      case Rechts:
	if ( x >= mXPos && y >= mYPos && y < mYPos + 50) return false;
        break;  
      case Oben:
	if ( y < mYPos + 50 && x >= mXPos && x < mXPos + 50) return false;
        break;
      case Unten:
	if ( y >= mYPos && x >= mXPos && x < mXPos + 50) return false;
        break;
    }
    return true;
 }


  public static void newColors()
  {
    for (int i = 0; i < 4 ; i++)
    {
      mRichtungsFarbe[i] = -1;
    }

    for ( int i = 0; i < 4 ; i++)
    {
      // System.out.println("newColors Nr. " + i);
      boolean theOK;
      do
      {
	mRichtungsFarbe[i] = (int) Math.round ( Math.floor ( mZufall.nextDouble() * 8)); // wï¿½rfel farbe
	// System.out.println( mRichtungsFarbe[i]);
	theOK = true;
        for ( int j = 0; j < i && theOK ; j++)
	{
	  theOK = mRichtungsFarbe[j] != mRichtungsFarbe[i];
	}
      } while ( theOK == false);
    }
  }

  public static void newJumper()
  {
    // Mindestabstand von der Wand sind 50 Pixel. weil.

    // Ferner muï¿½ noch geschaut werden, wo Mouse Zuletzt war
    mXPos = (int) Math.round( mZufall.nextDouble() * 300 + 50);
    mYPos = (int) Math.round( mZufall.nextDouble() * 200 + 50);
    mRichtung = (int) Math.round ( Math.floor( mZufall.nextDouble() * 4 ));
    // System.out.println( "newJumper " + mXPos + ", "+mYPos);

    // if isBereich
    // if isIntim
    // theOK = false;
  }
}