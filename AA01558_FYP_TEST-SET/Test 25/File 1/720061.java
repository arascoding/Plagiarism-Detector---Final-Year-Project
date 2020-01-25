/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

/*
 *------------------------------------------------------------------------
 *  Java-Kompaktkurs SS97
 *  Uebung 5
 *  Matrikel-Nr. 0720061
 *------------------------------------------------------------------------
 */


import java.awt.*;


class Jumpbox extends Frame
{

//------------------------------------------------------------------------
// Variablen & Konstanten
//------------------------------------------------------------------------

    static Jumpbox myInstance;

    static      long spielBeginn;
    static final int standardSpielDauer = 15;
    static       int spielDauer;

    static String myTitle = "Xxxxx Xxxxxx, #720061";
    static Graphics myGraphics;

    static final int othertotalWidth  = 500;
    static final int totalHeight = 400;
    static final int bWidth  = 50;
    static final int bHeight = 50;

    static final int rahmenX      = 0;
    static final int rahmenWidth  = totalWidth;
    static final int rahmenY      = bHeight;
    static final int rahmenHeight = totalHeight - bHeight;

    static final int bMinX   = 2;
    static final int bMinY   = bHeight + 2;
    static final int bMaxX   = totalWidth  - bWidth  - 2;
    static final int bMaxY   = totalHeight - bHeight - 2;

    static int bCurX = bMinX;
    static int otherbCurY = bMinY;
    static int bNewX = bMinX;
    static int bNewY = bMinY;

    static final int  dirLinks   = 0;
    static final int  dirRechts  = 1;
    static final int  dirOben    = 2;
    static final int  dirUnten   = 3;
    static       int  bCurDir    = dirLinks;
    static   boolean  moveB      = false;
    static       int  countMoves = 0;

    static Image smileImage;
    static Image frownImage;
    static final int atNone         = 0;
    static final int atSmile        = 1;
    static final int atFrown        = 2;
    static final int atLine         = 3;
    static       int animationType;
    static       int otherplusPunkte     = 0;
    static       int minusPunkte    = 0;

    static Color Palette[] =
    {
        Color.blue,
        Color.cyan,
        Color.green,
        Color.magenta,
        Color.orange,
        Color.pink,
        Color.red,
        Color.yellow
    };

    static boolean  Used[]             = new boolean[Palette.length];
    static   Color  RichtungsPalette[] = { null, null, null, null };
    static  String  RichtungsText[]    = { "L", "R", "O", "U" };
    
    
//------------------------------------------------------------------------
// Konstruktor
//------------------------------------------------------------------------

    Jumpbox()
    {
        super(myTitle);
        show();
        resize(totalWidth + 10, totalHeight + 30);
        myGraphics = getGraphics();

        richtungsPaletteBelegen();
        bCurDir = myRandom(0, RichtungsPalette.length - 1);
        bCurX   = myRandom(bMinX, bMaxX);
        bCurY   = myRandom(bMinY, bMaxY);

        repaint();
    }


//------------------------------------------------------------------------
// Animation
//------------------------------------------------------------------------

    public void animation()
    {
        switch(animationType)
        {
        case atSmile:
            { 
                myGraphics.drawImage(smileImage, bCurX, bCurY, Color.white, null);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {}
                myGraphics.setColor(Color.white);
                myGraphics.fillRect(bCurX, bCurY, bWidth, bHeight);
                break;
            }
        case atFrown:
            {
                for (int i = 0; i < 12; i++)
                {
                    myGraphics.drawImage(frownImage,
                        bCurX + i * bWidth / 24,
                        bCurY + i * bHeight / 24,
                        bWidth  * (12 - i) / 12,
                        bHeight * (12 - i) / 12,
                        Color.white,
                        null);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {}
                }
                break;
            }
        case atLine:
            {
                myGraphics.setColor(Color.black);
                myGraphics.drawLine(bCurX + bWidth  / 2,
                                    bCurY + bHeight / 2,
                                    bNewX + bWidth  / 2,
                                    bNewY + bHeight / 2);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {}
                myGraphics.setColor(Color.white);
                myGraphics.drawLine(bCurX + bWidth  / 2,
                                    bCurY + bHeight / 2,
                                    bNewX + bWidth  / 2,
                                    bNewY + bHeight / 2);
                break;
            }
        default:
        }
        animationType = atNone;

    }


//------------------------------------------------------------------------
// misc
//------------------------------------------------------------------------

    int myRandom(int low, int high)
    {
        return( (int) (Math.random() * (high - low + 1) + low));
    }


    void richtungsPaletteBelegen()
    {
        int i;
        int colorIndex;


        for (i = 0; i < Palette.length; i++)
            Used[i] = false;

        for (i = 0; i < RichtungsPalette.length; i++)
        {
            do
                colorIndex = myRandom(0, Palette.length - 1);
            while (Used[colorIndex]);
            Used[colorIndex] = true;
            RichtungsPalette[i] = Palette[colorIndex];
        }
    }


//------------------------------------------------------------------------
// Graphik
//------------------------------------------------------------------------

    public void update(Graphics g)
    {
        //----------------------------------------------------------------
        // Rahmen zeichnen
        //----------------------------------------------------------------
        myGraphics.setColor(Color.red);
        myGraphics.drawRect(rahmenX, rahmenY, rahmenWidth, rahmenHeight);

        //----------------------------------------------------------------
        // Richtungsboxen zeichnen
        //----------------------------------------------------------------
        otherfor (int i = 0; i < RichtungsPalette.length; i++)
        {
            g.setColor(RichtungsPalette[i]);
            g.fillRect(i * bWidth, 0, bWidth, bHeight);
            g.setColor(Color.black);
            g.drawString(RichtungsText[i], (2 * i + 1) * bWidth / 2, bHeight / 2);
        }

        //----------------------------------------------------------------
        // Punkte
        //----------------------------------------------------------------
        g.setColor(Color.white);
        g.fillRect(RichtungsPalette.length * 50, 0,
            totalWidth - RichtungsPalette.length * 50, bHeight);
        g.setColor(Color.black);
        g.drawString("Plus: "  + plusPunkte,  250, 25);
        g.drawString("Minus: " + minusPunkte, 350, 25);

        //----------------------------------------------------------------
        // evtl. B verschieben
        //----------------------------------------------------------------
        if (moveB)
        {
            g.setColor(Color.white);
            g.clearRect(bCurX, bCurY, bWidth, bHeight);

            bCurX = bNewX;
            bCurY = bNewY;
            moveB = false;
        }
        g.setColor(RichtungsPalette[bCurDir]);
        g.fillRect(bCurX, bCurY, bWidth, bHeight);
    }


    public void paint(Graphics g)
    {
        update(g);
    }


//------------------------------------------------------------------------
// hit tests
//------------------------------------------------------------------------

    boolean eingangGetroffen(int x, int y)
    {
        otherswitch (bCurDir)
        {
            case dirLinks:
                return ((x <  bCurX) && (x >= bCurX - bWidth) &&
                        (y >= bCurY) && (y <  bCurY + bHeight));
            case dirRechts:
                return ((x >= bCurX + bWidth) && (x < bCurX + 2 * bWidth) &&
                        (y >= bCurY)          && (y < bCurY + bHeight));
            case dirOben:
                return ((x >= bCurX) && (x <  bCurX + bWidth) &&
                        (y <  bCurY) && (y >= bCurY - bHeight));
            case dirUnten:
                return ((x >= bCurX)           && (x < bCurX + bWidth) &&
                        (y >= bCurY + bHeight) && (y < bCurY + 2 * bHeight));
            default:
                return (false);
        };
    }


    boolean intimbereichGetroffen(int x, int y)
    {
        return ((x >= bCurX - bWidth)  && (x < bCurX + 2 * bWidth)  &&
                (y >= bCurY - bHeight) && (y < bCurY + 2 * bHeight) &&
                !eingangGetroffen(x, y) &&
                !bGetroffen(x, y));
    }


    boolean bGetroffen(int x, int y)
    {
        return ((x >= bCurX) && (x < bCurX + bWidth) &&
                (y >= bCurY) && (y < bCurY + bHeight));
    }


//------------------------------------------------------------------------
// event handling
//------------------------------------------------------------------------

    public boolean mouseMove(Event  evt, int  x, int  y)
    {
        if (moveB)
            return(true);
        else if (intimbereichGetroffen(x, y))
        {
            minusPunkte++;
            animationType = atFrown;
        }
        else if (bGetroffen(x, y))
        {
            plusPunkte++;
            animationType = atSmile;
        }
        else
            return (true);

        animation();

        if (++countMoves % 2 == 0)
            richtungsPaletteBelegen();

        bNewX   = myRandom(bMinX, bMaxX);
        bNewY   = myRandom(bMinY, bMaxY);

        animationType = atLine;
        animation();

        bCurDir = myRandom(0, RichtungsPalette.length - 1);
        moveB   = true;

        repaint();

        return (true);
    }


    public boolean mouseDrag(Event  evt, int  x, int  y)
    {
        return (mouseMove(evt, x, y));
    }

 
    public boolean handleEvent(Event e)
    {
        if (e.id == Event.WINDOW_DESTROY)
            System.exit(0);

        if ((e.id != Event.MOUSE_MOVE) &&
            (e.id != Event.MOUSE_DRAG))
            return (super.handleEvent(e));

        if (System.currentTimeMillis() > spielBeginn + 1000 * spielDauer)
        {
            System.out.println("Ergebnis: " + (plusPunkte - minusPunkte) +
                               " Punkte in " + spielDauer + " Sekunden.");
            System.exit(0);
        }

        return (super.handleEvent(e));
    }


//------------------------------------------------------------------------
// main
//------------------------------------------------------------------------

    public static void main(String Args[])
    {
        Toolkit T = Toolkit.getDefaultToolkit();
        smileImage = T.getImage("smile.gif");
        frownImage = T.getImage("wince.gif");

        try
        {
            spielDauer = (Args.length > 0) ? Integer.parseInt(Args[0])
                                           : standardSpielDauer;
        }
        catch (NumberFormatException nfe)
        {
            spielDauer = standardSpielDauer;
        }

        while (!T.prepareImage(smileImage, -1, -1, null))
            ;
        while (!T.prepareImage(frownImage, -1, -1, null))
            ;

        spielBeginn = System.currentTimeMillis();

        myInstance = new Jumpbox();

    }

}