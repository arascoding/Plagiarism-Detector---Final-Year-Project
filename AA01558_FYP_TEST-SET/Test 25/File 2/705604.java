/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

/* Jumpbox.java  Programmieraufgabe im Rahmen des JAKK SS 97
   von Xxxxxxxx Xxxxx #0705604
   */
import java.awt.*;
import java.util.*;
import java.awt.image.*;

class Jumpbox extends Frame implements Runnable
{
  // Instanzenvariablen
  
  // Fenstergroessen
  static int 
other
or = 40;
  final static int fenster_w = 500;
  final static int fenster_h = 400;
  final static int rb_w = 50;
  final static int rb_h = 50;
  // Intimbereichgroessen
  final static int intim_w = 50;
  final static int intim_h = 50;
  // Farben
  final static Color [] farbtopf = {Color.blue,Color.cyan,Color.green,Color.magenta,Color.orange,Color.pink,Color.red,Color.yellow};

  static  Color [] rb_farben = new Color[4];
  static char [] richtung = {'L','R','O','U'};   
  // Box
  public  int box_x;
  public  int box_y;
  public  int box_w = 50;
  public  int box_h = 50;
  public  int oldbox_x;
  public  int oldbox_y;
  public  int box_pos;

  // springen
  public int spri_x;
  public int spri_y;
  public int oldspri_x;
  public int oldspri_y;

  // Animation
  public int image_x;
  public int image_y;
  public int image_w;
  public int image_h;
  public int oldimage_x;
  public int oldimage_y;
  public int oldimage_w;
  public int oldimage_h;
  
  public int delay = 0;
  public int 
other
fps = 20;
  
  public int frame = 0;
  public static String smilefiles[] ={"smile.gif","smile1.gif","smile2.gif","smile3.gif","smile4.gif","smile5.gif"};
  public static String wincefiles[] ={"wince.gif","wince1.gif","wince2.gif","wince3.gif","wince4.gif","wince5.gif"};
  public Image smileImg[];
  public Image winceImg[];
  public Image animage;

  // Spiel
  int 
other
time = 15;
  long playms = 0;
  public int punkte = 0;
  public int punkt = 0;
  public int durchgang = 0;
 

  public volatile boolean gri= false;
  public volatile boolean gru= false;
  public volatile boolean spri = false;
  public boolean neue_skala = false;
  public boolean init = true;

  // AWT
  Toolkit tk;
  
  Thread aniThread;
  Image puffer = null;
  Object syn;
  Graphics og;

  public static void main(String args[])
  {   
    new Jumpbox(args);
  }

  public Jumpbox(String args[])
  {
    super("Xxxxxxxx Xxxx #0705604");
    if (args.length==1) time = Integer.parseInt(args[0]);
    resize(fenster_w,fenster_h+or);
    syn = new Object();
    smileImg = new Image[smilefiles.length];
    winceImg = new Image[wincefiles.length];

    delay = (fps > 0) ? (1000 / fps) : 100;
    neue_Farben();
      neue_Box();
      oldbox_x=box_x;
      oldbox_y=box_y;
      oldspri_x=oldbox_x;
      oldspri_y=oldbox_y;
      tk = getToolkit();
      
      for (int i=0;i<smilefiles.length;i++) {
	smileImg[i] = tk.getImage(smilefiles[i]);
      }
      for (int i=0;i<wincefiles.length;i++) {
	winceImg[i] = tk.getImage(wincefiles[i]);
      }
     //  gri=true;
//       shoot_Thread();
//       gru=true;
//       playms=System.currentTimeMillis();
//       while(playms+1500>System.currentTimeMillis()) { };
//       shoot_Thread();
    //neue_Box();
    //neue_Farben();
     show();
    //    
  }

  public void update(Graphics g)
  {
    if (og ==null) {
      puffer = createImage(fenster_w,fenster_h);
      og = puffer.getGraphics();
    }
    paint(og);
    g.drawImage(puffer,0,0,this);
  }

  public void paint(Graphics g)
  {
    // Animationen
    if ((gri) || (gru))  {
      g.setColor(getBackground());
      g.fillRect(oldimage_x,oldimage_y,oldimage_w,oldimage_h);
      if (frame!=-1)
	g.drawImage(animage,image_x,image_y,getBackground(),this);
      //oldimage=animage;
      oldimage_x=image_x;
      oldimage_y=image_y;
     }
    else {
      if (spri) {
	g.setColor(getBackground()); 
	g.fillRect(oldspri_x,oldspri_y,box_w,box_h);
	g.drawLine(oldbox_x+25,oldbox_y+25,box_x+25,box_y+25);
	if (frame!=-1) {
	g.setColor(Color.black);
	g.drawLine(oldbox_x+25,oldbox_y+25,box_x+25,box_y+25);
	g.fillRect(spri_x+20-frame*4,spri_y+20-frame*4,frame*8,frame*8);
	}
	oldspri_x=spri_x;
	oldspri_y=spri_y;
      }
      else { // Box & Rahmen
	g.setColor(Color.white);
	g.drawLine(0,rb_h+or,fenster_w,rb_h+or);
	g.setFont(new Font("TimesRoman",Font.PLAIN,24));
	// Zeichne Rahmenskala 
	
other
for (int i=0;i<rb_farben.length;i++) {
	  g.setColor(rb_farben[i]);
	  g.fillRect(i*rb_w,or,rb_w,rb_h);
	  g.setColor(Color.black);
	  g.drawChars(richtung,i,1,i*rb_w+20,or+20);
	}
	// loesche alte Box
	g.setColor(getBackground()); 
	g.fillRect(oldbox_x,oldbox_y,box_w,box_h);
	oldbox_x = box_x;
	oldbox_y = box_y;
	// Zeichne Box
	g.setColor(rb_farben[box_pos]);
	g.fillRect(box_x,box_y,box_w,box_h);
      }
    }           
  }
  
 public boolean handleEvent(Event ev) // Events from root frame:
  {
    switch(ev.id)
      {
      case Event.WINDOW_DESTROY: {
	aniThread = null;
 	System.exit(0);
	break;
       }
      //  case Event.MOUSE_DOWN: {
//  	maus_bewegt(ev.x,ev.y);
//  	break;
//        }
      }
    return super.handleEvent(ev);
  }
  public synchronized boolean mouseDrag(Event ev,int mx,int my)
  {
    return mouseMove(ev,mx,my);
  }

  public synchronized boolean mouseMove(Event ev,int mx,int my)
  { 
    if (init) { // Hier gehts los!
      init = false;
      
      durchgang=0;
      playms=System.currentTimeMillis(); // Startzeit nehmen!
      return true;
    }
    else { 
      if (aniThread==null || !aniThread.isAlive())  {
	if (playms+time*1000<System.currentTimeMillis()) {
	  System.out.println("Ergebnis: "+punkte+" Punkte in "+time+" Sekunden! ");
	  System.exit(0);
	}
	else {
	  if (maus_in_B(mx,my)) {
	    gri=true;
	    shoot_Thread();
	  }
	  else {
	    if (maus_in_Intim(mx,my)) {
	      gru=true;
	      shoot_Thread();
	    }
	  }
	}
      }
      return true;
    }
  }

  public synchronized void shoot_Thread()
  { 
    aniThread =new Thread(this);
   
    aniThread.start();
    Thread.currentThread().yield();
    if (gri==true) punkte+=1;
    else punkte-=1;

    durchgang++;
    if (durchgang>=2) durchgang=0; 
  }

 public void run()   // Thread fuer Animation
  {
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    //System.out.println("IM THread!");
    if (gri) { // Smiley Animation
      frame =0;
           //  System.out.println("SMILEY!");
       long startTime = System.currentTimeMillis();
       while (frame<smileImg.length) {
	image_x = box_x;
	image_y = box_y;
	image_w = 50; // nur fuer loeschen
	image_h = 50; // "
	animage = smileImg[frame];
	frame++;
	repaint();
	//System.out.println("SMILEY!");
	try {
	  startTime += delay;
	  // Thread.sleep(100);
	  Thread.sleep(Math.max(0,startTime-System.currentTimeMillis())); 
	} catch (InterruptedException e) {
	  gri = false;
	  break;
	}
       }
       
    }
    else {
      if (gru) {
// 	System.out.println("GRUMMEL!");
	 frame=0;
	 long startTime = System.currentTimeMillis();
	 while (frame<winceImg.length) {
	   //  while (Thread.currentThread() == aniThread) {
	  
	   image_x = box_x;
	   image_y = box_y;
	   image_w = 50; // nur fuer loeschen
	   image_h = 50; // "
	   animage = winceImg[frame];
	   frame++;
	   repaint();
	   //System.out.println("GRUMMEL ANI!");
	   try {
	     startTime += delay;
	     //Thread.sleep(100);
	     Thread.sleep(Math.max(0,startTime-System.currentTimeMillis())); 
	   } catch (InterruptedException e) {
	     gru = false;
	     break;
	   }
	 }
      }
    }
    gri =false;
    gru =false;
    repaint();
    if (durchgang==0)  neue_Farben();
    neue_Box();

    spri=true; // jetzt springen-Animation
    long dx=(box_x-oldbox_x)/5;
    long dy=(box_y-oldbox_y)/5;
    long startTime = System.currentTimeMillis();
    frame=0;
    for (int i=0; i<=5;i++) {
      spri_x=oldbox_x+(int) dx*i;
      spri_y=oldbox_y+(int) dy*i;
      frame=i;
      startTime+=delay;
      try {
	Thread.sleep(Math.max(0,startTime-System.currentTimeMillis())); 
	//	Thread.sleep(30);
      } catch (InterruptedException e) { break; }
      repaint();
    }
    frame=-1; //loeschen
    try {
    Thread.sleep(10);
    } catch (InterruptedException e) {  }
    repaint();
    spri=false;
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {  }
    repaint();
  }
   
    
  public void  neue_Box()
  {
   Random rg = new Random(new Date().getTime());
   box_x = (int) (rg.nextFloat() *(fenster_w-box_w-2*intim_w))+intim_w;
   box_y = (int) ((rg.nextFloat() *(fenster_h-box_h-2*intim_h)))+or+intim_h;
   box_pos = (int) (rg.nextFloat() *4);
   
  }

  public  void neue_Farben()
  {
    Random rg = new Random(new Date().getTime());
    Vector ft = new Vector(farbtopf.length);
    for (int i =0;i<farbtopf.length;i++) ft.addElement(farbtopf[i]);
    for (int i= 0;i<rb_farben.length;i++) {
      int zz = ((int) ( rg.nextFloat() * (farbtopf.length-i)));
      rb_farben[i] = (Color)ft.elementAt(zz);
      ft.removeElementAt(zz);
    } 
    neue_skala = true;
  }

  public boolean maus_in_B(int mx, int my)
  {
    if ((mx>=box_x && mx<=box_x+box_w) && (my>=box_y && my<=box_y+box_h)) return true;
    else return false;
  }

  public boolean maus_in_Intim(int mx, int my)
  {
    int x1 = box_x-intim_w;
    int x4 = box_x+box_w+intim_w;
    int y1 = box_y-intim_h;
    int y4 = box_y+box_h+intim_h;
    if (((mx>=x1) && (mx<=x4))&&((my>=y1) && (my<=y4))) {
      int x3 = box_x+box_w;
      int y3 = box_y+box_h;
      
other
switch(box_pos) {
      case 0: return !((mx<=x3)&&(my>=box_y)&&(my<=y3));
      case 1: return !((mx>=box_x)&&(my>=box_y)&&(my<=y3));
      case 2: return !((mx>=box_x)&&(mx<=x3)&&(my<=y3));
      case 3: return !((mx>=box_x)&&(mx<=x3)&&(my>=box_y));
      default:return false;
      }
    }
    else return false;
  }

 
}