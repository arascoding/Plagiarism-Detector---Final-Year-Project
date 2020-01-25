/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.util.*;
import java.awt.*;
import java.awt.image.*;

public class Jumpbox extends Frame implements Runnable {
	
	Image smile,wince,offImage;
	int count = 0,size,xO,yO,xB,yB,currentBox,result=0,colors[]=new int[4];
	Long tempTime;
	long gameTime;
	Random random = new Random();
	Thread animationThread;
	Graphics offGraphics=null;
	boolean happyFace,threadOn=false;

	public Jumpbox(String title, String[] args) {
		super(title);

		if (args.length == 0){
			tempTime = new Long("15");
			gameTime = 15;
		}
		else {
			try {
				tempTime = new Long(args[0]);
				gameTime = tempTime.longValue();
			} catch (NumberFormatException ioe) {
				System.out.println("ERROR: usage: Jumpbox <integer>");
				System.exit(0);
			}
		}
		
		gameTime = System.currentTimeMillis() + gameTime*1000;
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
      	smile = toolkit.getImage("smile.gif");
      	wince = toolkit.getImage("wince.gif");

		setColors();
		randomBox();
		randomCoord();
	}


	public void run(){
		threadOn=true;
		count++;
		if (happyFace){
			repaint();
			try{
				animationThread.sleep(500);
			} catch (InterruptedException e) {}
			result++;
		}
		else{
			for(size =25;size>0;size--){ 
				repaint();
				try {
					animationThread.sleep(40);
				} catch (InterruptedException e) {
					continue;
				}
			}
			result--;
		}
		randomCoord();
		randomBox();
		drawLine(this.getGraphics(), xO, yO, xB, yB);
		try {
			animationThread.sleep(500);
		} catch (InterruptedException e) {}		
		deleteBox(this.getGraphics(), xO, yO);
		deleteLine(this.getGraphics(),xO,yO,xB,yB);
		if (count >= 3) {
			count = 0;
			setColors();
			repaint(0,0,200,50);
		}
		drawBox(this.getGraphics(), xB, yB, colors[currentBox]);
		threadOn=false;
		repaint();
		animationThread.yield();
	}

	public void update(Graphics g) {
			if (offGraphics == null) {
					offImage = createImage(50,50);
					offGraphics = offImage.getGraphics();
				}	
		if (threadOn) {
			if (happyFace) {
					g.drawImage(smile,xB,yB,this);
			} else {
				offGraphics.setColor(Color.gray);
				offGraphics.fillRect(0,0,50,50);
				offGraphics.drawImage(wince,25-size,25-size,size*2,size*2, this);
				g.drawImage(offImage,xB,yB,this);
			}
		} else {
			paint(g);
		}
	}

	public void paint(Graphics g) {
		g.setColor(returnColor(colors[0]));
		g.fillRect(0,0,50,50);
		g.setColor(returnColor(colors[1]));
		g.fillRect(50,0,50,50);
		g.setColor(returnColor(colors[2]));
		g.fillRect(100,0,50,50);
		g.setColor(returnColor(colors[3]));
		g.fillRect(150,0,50,50);
		
		g.setColor(Color.black);
		g.drawString("L", 24, 27);
		g.drawString("R", 74, 27);
		g.drawString("O", 124, 27);
		g.drawString("U", 174, 27);

		g.setColor(Color.white);
		g.drawRect(0,50,498,349);

		drawBox(g, xB, yB, colors[currentBox]);
	}

	public boolean inB(int x, int y) {
		if ((x >= xB) && (y >= yB) && (x <= xB+50) && (y <= yB+50))
			return true;
		else
			return false;		
	}
	
	public boolean inIntim(int x, int y) {
		if ((x >= xB-50) && (y >= yB-50) && (x <= xB+100) && (y <= yB+100))
			return true;
		else
			return false;		
	}

	public boolean rightIntim(int box, int x, int y) {
		switch(box) {
			case 0: if ((x < xB) && ((y > yB) && (y < yB +50))) 
				return true;
				break;
			case 1: if ((x > xB + 50) && ((y > yB) && (y < yB +50))) 
				return true;
				break;
			case 2: if ((y < yB) && ((x > xB) && (x < xB +50)))
				return true;
				break;
			case 3: if ((y > yB + 50) && ((x > xB) && (x < xB +50))) 
				return true;
				break;
		}
		return false;
	}
	
	private void randomCoord() {
		xO = xB;
		yO = yB;
		
		// Schleife so dass der neue Box nicht entsteht zu nah zu den alten.
		
		while(((-50<xB-xO)&&(xB-xO<100))&&((-50<yB-yO)&&(yB-yO<100))){		
			xB=(int)(random.nextFloat() * 449)+1; 		
			yB=(int)(random.nextFloat() * 299)+51;
		}
	}

	public void drawBox(Graphics g, int x, int y, int c) {
		g.setColor(returnColor(c));
		g.fillRect(x, y, 50, 50);
	}

	public void deleteBox(Graphics g, int x, int y) {
		g.setColor(Color.gray);
		g.fillRect(x, y, 50, 50);
	}

	private void randomBox(){
		currentBox=((int)(random.nextFloat()*4));
	}

	public void drawLine(Graphics g, int xO, int yO, int x, int y) {
		g.setColor(Color.black);
		g.drawLine(xO + 25, yO + 25, x + 25, y + 25);
	}

	private void deleteLine(Graphics g, int xOld, int yOld, int x, int y) {
		g.setColor(Color.gray);
		g.drawLine(xOld + 25, yOld + 25, x + 25, y + 25);
	}

	public boolean mouseDrag(Event e, int x, int y) {
		return mouseMove(e,x,y);
	}

	public boolean mouseMove(Event evt, int x, int y) {
		if (gameTime < System.currentTimeMillis()) {
			System.out.println("Ihr Ergebnis: " + result + " Punkte in " + tempTime.longValue() + " Sekunden.");
			System.exit(0);
			return true;
		}
		if (!threadOn){
			if (inB(x,y)) {
				happyFace=true;
				animationThread=new Thread(this);
				animationThread.start();
			}
			else if ((inIntim(x,y)) && !(rightIntim(currentBox,x,y))) {
				happyFace=false;
				animationThread=new Thread(this);
				animationThread.start();
			}
		}
		return true;
	}

	public boolean handleEvent(Event e) {
   	switch (e.id) {
		case Event.WINDOW_DESTROY:
			System.exit(0);
            break;
      }  
      return super.handleEvent(e);
   }

	private Color returnColor(int i) {
		switch(i) {
			case 1: return Color.blue; 
			case 2: return Color.cyan; 
			case 3: return Color.green;
			case 4: return Color.magenta;
			case 5: return Color.orange; 
			case 6: return Color.pink; 
			case 7: return Color.red; 
			case 8: return Color.yellow;
		}
		return Color.white;
	}

	private void setColors() {
		for(int i=0;i<4;i++){
			colors[i]=(int)(random.nextFloat()*8);
			for(int j=0;j<i;j++){
				if (colors[j]==colors[i]){
					i--;
					break;
				}
			}
		}
	}

	static public void main(String[] args) {
		Frame f = new Jumpbox("java Jumpbox von Xxxxxx Xxxxxx #943151", args);
		f.setBackground(Color.gray);
		f.resize(508,428);
		f.show();
	}
}