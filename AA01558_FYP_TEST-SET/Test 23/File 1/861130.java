/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.applet.*;
import java.awt.*;
import java.util.*;

public class Jumpbox extends Applet implements Runnable {

    static Applet applet;     // applet to be created in main()
    static boolean firstUpdateDone = false;

    /*
     Main Application
     everything starts here
     */

    public static void main(String args[]) {
        if (args.length==1) {
            try {
                StopTime = (Long.valueOf(args[0])).longValue();
                StopTime *= 1000;
            } catch (NumberFormatException nfe) {
                System.out.println("Illegal parameter");
                System.out.println("Usage: Jumpbox [secs]");
                System.out.println("using standard: 15 secs");
                StopTime = 15000;
            }
        } else StopTime = 15000;
        Frame frame=new Frame("Xxxxx Xxxxxxx #861130");
        applet=new Jumpbox();
        applet.init();
        frame.add("Center",applet);
        frame.resize(applet.size());
        // frame.setResizable(false);
        frame.show();
        applet.start();
    }

    /*
     function of Applet
     */

    static Image vI=null;
    static Graphics vG;

    static Thread animThread;
    static Image grinsAnim[] = new Image[1];
    static Image grummAnim[] = new Image[25];

    Point B_Box = new Point(0,0);       // Box B            +[ 50, 50]
    Point R_Box = new Point(0,0);       // Richtungs Box R  +[ 50, 50]
    Point I_Box = new Point(0,0);       // Intim Box I      +[150,150]

    static long StrtTime;
    static long CurrTime;
    static long StopTime;

    static boolean is_mouse_event;
    static Point M_Point = new Point(0,0);

    static Color otherColorList[] = { Color.blue,Color.cyan,Color.green,Color.magenta,Color.orange,Color.pink,Color.red,Color.yellow };
    static Color RBoxColors[] = new Color[4];
    static boolean isColorUsed[] = new boolean[8];

    static Random RND=new Random(System.currentTimeMillis());

    static int Points=0;

    static int R_Direction;

    static boolean anim_ended = false;
    static Point anim_pos = new Point(0,0);
    static Image anim_images[] = new Image[25];    // all should fit
    static int   anim_frames = 0;
    static long  anim_time;

    public othervoid update(Graphics g) {
        if (!firstUpdateDone) {
            vI=createImage(500,400);
            vG=vI.getGraphics();
            vG.setColor(Color.gray);
            vG.fillRect(0,0,500-1,400-1);
            vG.setColor(Color.white);
            vG.drawRect(0,50,500-1,400-50-1);
            firstUpdateDone=true;
        }
        g.drawImage(vI,0,0,this);
    }

    public void paint(Graphics g) {
        update(g);
    }


    public void init() {
        MediaTracker othertracker=new MediaTracker(this);
        grinsAnim[0] = Toolkit.getDefaultToolkit().getImage("smile.gif");
        tracker.addImage(grinsAnim[0],0);
        for(int i=0; i<25; i++) {
            grummAnim[i]=Toolkit.getDefaultToolkit().getImage("grumm"+i+".gif");
            tracker.addImage(grummAnim[i],0);
        }
        try {
            tracker.waitForID(0);       // wait till all files are loaded
        } catch (InterruptedException e) { }
        animThread = new Thread(this);
        resize(550,450);
    }

    public void start() {
        boolean perform_sprung = false;
        int loop_no;

        // wait till we are ready to go
        while (!firstUpdateDone) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) { }
        }
        // start Animation Thread, which will wait for notify
        animThread.start();

        StrtTime = System.currentTimeMillis();
        StopTime += StrtTime;

        loop_no =0;
        New_Box_B();

        do {
            if (loop_no%3 == 0)
                New_R_Colors();

            vG.setColor(Color.gray);
            vG.fillRect(0,50,500-1,400-1);
            vG.setColor(Color.white);
            vG.drawRect(0,50,500-1,400-50-1);

            vG.setColor(RBoxColors[R_Direction]);
            vG.fillRect(B_Box.x,B_Box.y,50-1,50-1);
            
            repaint();

            while (true) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException ie) { }
                }
                if (is_mouse_event) {
                    if        ( isInBox(M_Point, B_Box, 50) ) {
                        startAnim( B_Box.x, B_Box.y, grinsAnim, 1, 500);
                        synchronized (this) {
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException ie) { }
                        }
                        perform_sprung = true;
                        Points++;
                        loop_no++;
                    } else if ( isInBox(M_Point, R_Box, 50) ) {
                    } else if ( isInBox(M_Point, I_Box, 150) ) {
                        startAnim( B_Box.x, B_Box.y, grummAnim, 25, 1000);
                        synchronized (this) {
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException ie) { }
                        }
                        perform_sprung = true;
                        Points--;
                    }
                    is_mouse_event = false;
                }
                if (perform_sprung) {
                    while (!anim_ended) {
                        synchronized (this) {
                            try {
                                Thread.sleep( 10 );
                            } catch (InterruptedException ie) { }
                        }
                    }
                    int oldx = B_Box.x;
                    int oldy = B_Box.y;
                    New_Box_B();
                    vG.setColor( Color.black );
                    vG.drawLine( oldx, oldy, B_Box.x, B_Box.y );
                    repaint();
                    synchronized (this) {
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException ie) { }
                    }
                    perform_sprung = false;
                    break;
                }
            }
            loop_no++;
        } while (System.currentTimeMillis() < StopTime);
        CurrTime = System.currentTimeMillis();
        CurrTime = ( CurrTime - StrtTime ) / 1000;
        System.out.println("Ergebnis: "+Points+" in "+CurrTime+" Sekunden");
        System.exit(0);
    }

    boolean isInBox( Point M, Point B, int len ) {
        if ( (M.x>=B.x) && (M.x<B.x+len) && (M.y>=B.y) && (M.y<B.y+len) )
            return true;
        else
            return false;
    }

    public void New_Box_B() {
        // I supposed, that new IntimBox should fit also in (0,0)-(500,400)

        I_Box.move( Math.abs(RND.nextInt())%350, 50+Math.abs(RND.nextInt())%200 );
        B_Box.move( 50+I_Box.x, 50+I_Box.y );

        R_Direction=Math.abs(RND.nextInt()) % 4;
        switch (R_Direction) {
            case 0:  R_Box.move( B_Box.x-50, B_Box.y );  break;  // "L"
            case 1:  R_Box.move( B_Box.x+50, B_Box.y );  break;  // "R"
            case 2:  R_Box.move( B_Box.x, B_Box.y-50 );  break;  // "O"
            case 3:  R_Box.move( B_Box.x, B_Box.y+50 );  break;  // "U"
            default:
        }
    other}

    public void New_R_Colors() {
        int i,idx;
        for(i=0; i<ColorList.length; i++)
            isColorUsed[i]=false;
        for(i=0; i<4; i++) {
            do {
              idx = Math.abs(RND.nextInt()) % ColorList.length;
            } while (isColorUsed[idx]);
            isColorUsed[idx]other=true;
            RBoxColors[i]=ColorList[idx];
            vG.setColor(ColorList[idx]);
            vG.fillRect(i*50,0,50-1,50-1);
        }
        vG.setColor(Color.black);
        vG.drawString("L",25+  0,25);
        vG.drawString("R",25+ 50,25);
        vG.drawString("O",25+100,25);
        vG.drawString("U",25+150,25);
    }

    public boolean mouseDrag(Event evt,int x,int y) {
        synchronized(applet) {
             if (!is_mouse_event) {
               M_Point.move(x,y);
               is_mouse_event = true;
             }
            applet.notify();
        }
        return true;
    }

    public boolean mouseMove(Event evt,int x,int y) {
        // two time the same code, but we want to be fast here
        synchronized(applet) {
             if (!is_mouse_event) {
               M_Point.move(x,y);
               is_mouse_event = true;
             }
            applet.notify();
        }
        return true;
    }


    protected void startAnim(int XPos,int YPos,Image Animation[], int aframes, long time) {
        anim_pos.move( XPos, YPos );
        anim_frames = Animation.length;
        anim_time   = time;
        anim_frames = aframes;
        anim_ended = false;
        for (int i=0; i<anim_frames; i++)
            anim_images[i] = Animation[i];
        synchronized (animThread) {
            animThread.notify();
        }
    }

    /*
     Animation Thread
    */

    public void run() {
        long AnimStartTime;
        long AnimFrameTime;
        long AnimDelayTime;

        while (true) {
            try {
                synchronized(this) {
                    wait();
                }
            } catch (InterruptedException ie) { }
            if (anim_frames != 0) {
                // start() seems to automatically call notify()
                // so we check anim_frames != 0
                AnimFrameTime = anim_time / anim_frames;
                for (int i=0; i<anim_frames; i++) {
                    AnimStartTime = System.currentTimeMillis();
                    vG.setColor(Color.gray);
                    vG.fillRect(anim_pos.x,anim_pos.y,50,50);
                    vG.drawImage(anim_images[i],anim_pos.x+i,anim_pos.y+i,this);
                    repaint();
                    AnimDelayTime = System.currentTimeMillis() - AnimStartTime;
                    try {
                        Thread.sleep( Math.max(0, AnimFrameTime-AnimDelayTime));
                    } catch (InterruptedException ie) { }
                }
                anim_frames = 0;
                anim_ended = true;  // we want the applet to that we've finished
            }
        }
    }
