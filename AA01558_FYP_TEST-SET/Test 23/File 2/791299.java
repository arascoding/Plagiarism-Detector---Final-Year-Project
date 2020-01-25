/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

/* Synchonisationen sind in diesem Applet nicht notwendig, da durch das
 gegenseitige Warteverhalten sichergestellt ist, dass immer nur ein Thread
 zeichnen kann.
 */

import java.awt.*;
import java.applet.*;
import java.util.*;

public class Jumpbox extends Applet implements Runnable{
  static boolean AppletCanStart=false;
  static Image vImg=null;
  static Graphics vG;
  static Image GrinsAnim[]=new Image[1];
  static Image GrummelAnim[]=new Image[25];
  static Thread animThread;
  static Color 
other
ColorList[]={Color.blue,Color.cyan,Color.green,Color.magenta,Color.orange,Color.pink,Color.red,Color.yellow};
  static boolean boolColorListFlags[]=new boolean[ColorList.length];
  static Color RBoxFarben[]=new Color[4];
  static Random RND=new Random(System.currentTimeMillis());
  static long StartTime;
  static long EndTime=15000;
  static String Zeit="15";
  static int Points=0;
  static int DstXBox,DstYBox,DstRichtung;
  static Applet applet;
  static int AnimXPos,AnimYPos;
  static Image Animation[];
  static long AnimMSPF;
  static boolean NoMouse=true;
  static int LastXMouse=0,LastYMouse=0;
  
  public void init(){ // Init. fuer Hauptprogramm und animThread
    animThread=new Thread(this);
    MediaTracker 
other
tracker=new MediaTracker(this);
    GrinsAnim[0]=Toolkit.getDefaultToolkit().getImage("smile.gif");
    tracker.addImage(GrinsAnim[0],0);
    for(int i=0;i<25;i++){
      GrummelAnim[i]=Toolkit.getDefaultToolkit().getImage("grumel"+i+".gif");
      tracker.addImage(GrummelAnim[i],0);
    }
    try{
      tracker.waitForID(0);
    } catch (InterruptedException e){
    }
  }

  public Dimension preferredSize(){
    return new Dimension(500,400);
  }

  protected void mouseAction(int x,int y){
    LastXMouse=x;
    LastYMouse=y;
    if (System.currentTimeMillis()>=EndTime)
      synchronized(applet){
        applet.notify();
      }
    if (NoMouse)
      return;
    Rectangle BBox=new Rectangle(DstXBox-50,DstYBox-50,150,150);
    if (!BBox.inside(x,y))
      return;
    BBox.reshape(DstXBox,DstYBox,50,50);
    if (BBox.inside(x,y)){
      NoMouse=true;
      Points++;
      setAnimParams(DstXBox,DstYBox,GrinsAnim,500);
      return;
    }
    BBox.move((DstRichtung<2)?DstXBox+DstRichtung*100-50:DstXBox,
              (DstRichtung>1)?DstYBox+(DstRichtung-2)*100-50:DstYBox);
    if (BBox.inside(x,y))
      return;
    NoMouse=true;
    Points--;
    setAnimParams(DstXBox,DstYBox,GrummelAnim,40);
  }
  
  public boolean mouseDrag(Event evt,int x,int y){ // Maus mit Taste
    mouseAction(x,y);
    return true;
  }
  
  public boolean mouseMove(Event evt,int x,int y){ // Maus ohne Taste
    mouseAction(x,y);
    return true;
  
other
}

  public void MischeRichtungen(){
    int i,idx;
    for(i=0;i<ColorList.length;i++)
      boolColorListFlags[i]=false;
    for(i=0;i<4;i++){
      while(boolColorListFlags[idx=Math.abs(RND.nextInt())%ColorList.length]);
      boolColorListFlags[idx]
other
=true;
      RBoxFarben[i]=ColorList[idx];
      vG.setColor(ColorList[idx]);
      vG.fillRect(i*50,0,50,50);
    }
    vG.setColor(Color.black);
    vG.drawString("L",17,28);
    vG.drawString("R",67,28);
    vG.drawString("O",117,28);
    vG.drawString("U",167,28);
  }

  public void GenNewBox(){
    DstRichtung=Math.abs(RND.nextInt())%4;
    int MinX=50,MinY=100,MaxX=400,MaxY=300;
    switch(DstRichtung){
      case 0:
        MinX+=50;
        break;
      case 1:
        MaxX-=50;
        break;
      case 2:
        MinY+=50;
        break;
      case 3:
        MaxY-=50;
        break;
    }
    Rectangle BBox=new Rectangle();
    do{
      DstXBox=MinX+Math.abs(RND.nextInt())%(MaxX-MinX);
      DstYBox=MinY+Math.abs(RND.nextInt())%(MaxY-MinY);
      BBox.reshape(DstXBox-50,DstYBox-50,150,150);
    } while (BBox.inside(LastXMouse,LastYMouse));
  }

  protected void Springe(){ // Springen der Box
    int oldDstXBox=DstXBox,oldDstYBox=DstYBox;
    GenNewBox();
    vG.setColor(Color.white);
    vG.drawLine(oldDstXBox+25,oldDstYBox+25,DstXBox+25,DstYBox+25);
    repaint();
    try{
      Thread.sleep(500);
    } catch (InterruptedException ie){
    }
    vG.setColor(Color.gray);
    vG.fillRect(1,51,498,348);
  }
  
  public void start(){ // Start von Hauptprogramm
    StartTime=System.currentTimeMillis();
    EndTime+=StartTime;
    int Durchlauf=0;
    do{
      if (Durchlauf%3==0)
        MischeRichtungen();
      if (Durchlauf>0){
        Springe();
      } else
        GenNewBox();
      vG.setColor(RBoxFarben[DstRichtung]);
      vG.fillRect(DstXBox,DstYBox,50,50);
      repaint();
      NoMouse=false;
      try{
        synchronized(applet){
          applet.wait();
        }
      } catch (InterruptedException ie){
      }
      Durchlauf++;
    } while(System.currentTimeMillis()<EndTime);
    System.out.println("Ergebnis: "+Points+" in "+Zeit+" Sekunden");
    System.exit(0);
  }

  protected void setAnimParams(int XPos,int YPos,Image Anim[],long MSPF){
    // Setzt die Startwerte fuer die naechste Animation
    AnimXPos=XPos;
    AnimYPos=YPos;
    Animation=Anim;
    AnimMSPF=MSPF;
    synchronized(animThread){
      animThread.notify();
    }
  }
  
  public void run(){ // Start von animThread
    for(;;){
      try{
        synchronized(animThread){
          animThread.wait();
        }
      } catch (InterruptedException ie){
      }
      int AnimPos=0;
      long EndPeriod=System.currentTimeMillis(),DiffPeriod;
      while (AnimPos<Animation.length){
        EndPeriod+=AnimMSPF;
        vG.drawImage(Animation[AnimPos++],AnimXPos,AnimYPos,this);
        repaint(AnimXPos,AnimYPos,50,50);
        DiffPeriod=EndPeriod-System.currentTimeMillis();
        if (DiffPeriod>0)
          try{
            Thread.sleep(DiffPeriod);
          } catch (InterruptedException ie){
          }
      }
      synchronized(applet){
        applet.notify();
      }
    }
  }

  public void paint(Graphics g){ // Paint von Applet
    update(g);
  }

  public 
other
void update(Graphics g){ // Update von Applet
    if (vImg==null){
      vImg=createImage(500,400);
      vG=vImg.getGraphics();
      vG.setColor(Color.gray);
      vG.fillRect(0,0,500,400);
      vG.setColor(Color.white);
      vG.drawRect(0,50,500-1,350-1);
      AppletCanStart=true;
    }
    g.drawImage(vImg,0,0,this);
  }
  
  public static void main(String args[]){ // Start von Jumpbox
    // Kommandozeilen-Argumente auswerten
    if (args.length>1){
      System.out.println("Usage: java Jumpbox <Sec>");
      System.exit(0);
    } else if (args.length==1){
      Zeit=args[0];
      EndTime=(new Long(Zeit)).longValue()*1000;
    }
    // Applet ausfuehren
    applet=new Jumpbox();
    applet.init();
    Frame frame=new Frame("Xxxxxxx Xxxx, #791299");
    frame.setResizable(false);
    frame.add("Center",applet);
    frame.pack();
    frame.show();
    while (!AppletCanStart)
      try{
        Thread.sleep(100);
      } catch (InterruptedException ie){
      }
    animThread.start();
    applet.start();
  }
  
}