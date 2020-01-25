/*
 *THIS TEST FILE WAS TAKEN FROM:
 *“What is JPlag,” Institute for Program Structures and Data Organization, [Online].  *Available: https://jplag.ipd.kit.edu/. [Accessed 18 May 2018].
 */

import java.util.;
import java.awt.;
import java.awt.image.;

public class Jumpbox extends Frame implements Runnable {
	
	Image smile, nosmile, offImage;
	Graphics offGraphics = null;
	int sizeImg;
   int nrChange = 0, xOldBox, yOldBox, xBox = 0, yBox = 0, 

typeBox, points = 0, cBox[] = new int[4];
	Long gTime;
	long gameTime;
	Random rnd = new Random(System.currentTimeMillis());
	Thread animationThread;
	boolean bSmile, threadRunning = false;

	public Jumpbox(String title, String[] args) {
		super(title);
		if (args.length == 0) {
			gameTime = 15;
			gTime = new Long(15);
		} 

else {
			try {
				gTime = new Long(args[0]);
				gameTime = gTime.longValue();
			} catch (NumberFormatException ioe) {
				System.out.println(Jumpbox usage Jumpbox integer);
				System.exit(0);
			}
		}
		
		gameTime = System.currentTimeMillis() + gameTime1000;
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
      smile = toolkit.getImage(smile.gif);
      nosmile = toolkit.getImage(wince.gif);

		randomBoxColor();
		randomBoxType();
		randomBoxCoordinates();
	}

	private void startThread() {
		animationThread = new Thread(this);
		animationThread.start();
	}

	public void run() {
		threadRunning = true;
		nrChange++;
		if (nrChange = 3) {
			nrChange = 0;
			randomBoxColor();
		}
		if (bSmile) { 			 SMILE
			drawSmile(this.getGraphics(), xBox, yBox);
			try {
				animationThread.sleep(500);
			} catch (InterruptedException ignored) {

			}		
			points++;
		} else { 				 NO SMILE
			sizeImg = 26;
			while (sizeImg  1) {
				sizeImg--;
				repaint();
				try {
					animationThread.sleep(40);
				} catch (InterruptedException ignored) {
				}		
			}
			points

--;
		}
		randomBoxCoordinates();
		randomBoxType();
		drawLine(this.getGraphics(), xOldBox, yOldBox, xBox, yBox);
		try {
			animationThread.sleep(500);
		} catch (InterruptedException ignored) {		}		
		deleteBox(this.getGraphics(), xOldBox, yOldBox);
		deleteLine(this.getGraphics(), xOldBox, yOldBox, xBox, yBox);
		

drawBox(this.getGraphics(), xBox, yBox, cBox[typeBox]);
		threadRunning = false;
		repaint();
		animationThread.yield();
	}

	public void update(Graphics g) {
		if (threadRunning) {
			if (bSmile) {
				paint(g);
			} else {  Animation NoSmile
				if (offGraphics == null) {
					offImage = createImage(50,50);
					offGraphics = offImage.getGraphics();
				}
				offGraphics.setColor(

getBackground());
				offGraphics.fillRect(0,0,50,50);
				offGraphics.drawImage(nosmile,25-sizeImg,25-sizeImg,sizeImg2,sizeImg2, this);
				g.drawImage(offImage, xBox, yBox, this);
			}
		} else {
			paint(g);
		}
	}

	public void paint(Graphics g) {
		g.setColor(number2color(cBox[0]));
		g.fillRect(0,0,50,50);
		g.setColor(number2color(cBox[1]));
		g.fillRect(50,0,50,50);
		g.setColor(number2color(cBox[2]));
		g.fillRect(100,0,50,50);
		g.setColor(number2color(cBox[3]));
		g.fillRect(150,0,50,50);
		
		g.setColor(Color.black);
		g.drawString(L, 24, 27);
		g.drawString(R, 74, 27);
		g.drawString(O, 124, 27);
		g.drawString(U, 174, 27);

		g.setColor(Color.white);
		g.drawRect(0,49,499,400-49);

		drawBox(g, xBox, yBox, cBox[typeBox]);
	}

	private boolean inBox(int x, int y) {
		if ((x = xBox) && (y = yBox) && (x = xBox+50) && (y = yBox+50))
			return true;
		else
			return false;		
	}
	
	private boolean inBigBox(int x, int y) {
		if ((x = xBox-50) && (y = yBox-50) && (x = xBox+100) && (y = yBox+100))
			return true;
		else
			return false;		
	}

	private boolean inEntre(int box, int x, int y) {
		switch(box) {
			case 0 if ((x  xBox) && !((y  yBox)  (y  yBox +50))) return true; break;
			case 1 if ((x  xBox + 50) && !((y  yBox)  (y  yBox +50))) return true; break;
			case 2 if ((y  yBox) && !((x  xBox)  (x  xBox +50))) return true; break;
			case 3 if ((y  yBox + 50) && !((x  xBox)  (x  xBox +50))) return true; break;
		}
		return false;
	}
	
	private void randomBoxCoordinates() {
		xOldBox = xBox;
		yOldBox = yBox;
		xBox = (int)(rnd.nextFloat()  449) + 1; 
		yBox = (int)(rnd.nextFloat()  300) + 50;
		

while ( ((xBox  xOldBox-50) && (xBox  xOldBox+100)) && ((yBox  yOldBox-50) && (yBox  yOldBox+100)) ) {
			xBox = (int)(rnd.nextFloat()  449) + 1; 
			yBox = (int)(rnd.nextFloat()  300) + 50;
		}
	}

	private void drawBox(Graphics g, int x, int y, int c) {
		g.setColor(number2color(c));
		g.fillRect(x, y, 50, 50);
	}

	private void deleteBox(Graphics g, int x, int y) {
		g.setColor(Color.gray);
		g.fillRect(x, y, 50, 50);
	}
	
	private void randomBoxType() {
		while ((typeBox = (int)(rnd.nextFloat()4)) == 4) {}
	}

	private void drawLine(Graphics g, int xOld, int yOld, int x, int y) {
		g.setColor(Color.white);
		g.

drawLine(xOld + 25, yOld + 25, x + 25, y + 25);
	}

	private void deleteLine(Graphics g, int xOld, int yOld, int x, int y) {
		g.setColor(Color.gray);
		g.drawLine(xOld + 25, yOld + 25, x + 25, y + 25);
	}

	private void drawSmile(Graphics g, int xOld, int yOld) {
		g.

drawImage(smile, xOld, yOld, this);
	}

	public boolean mouseDrag(Event evt, int x, int y) {
		return mouseMove(evt, x,y);
	}

	public boolean mouseMove(Event evt, int x, int y) {
		if (gameTime  System.currentTimeMillis()) {
			System.out.println(Ergebnis  + points +  Punkte in  + gTime.longValue() +  Sekunden.);
			System.exit(0);
			return true;
		}
		if (!threadRunning) {
			if (inBox(x,y)) {
				bSmile = true;
				startThread();
			} 
			else if ((inBigBox(x,y)) && !(inEntre(typeBox, x, y))) {
				bSmile = false;
				startThread();
			}
		}
		return true;
	}

	public boolean handleEvent(Event e) {
   	switch (e.id) {
          case Event.WINDOW_ICONIFY
            break;
          case Event.WINDOW_DEICONIFY
            repaint();
				break;
			

case Event.WINDOW_DESTROY
         	System.exit(0);
            break;
      }  
      return super.handleEvent(e);
   }

	private Color number2color(int i) {
		switch(i) {
			case 1 return Color.blue; 
			case 2 return Color.cyan; 
			case 3 return Color.green;
			case 4 return Color.magenta;
			case 5 return Color.orange; 
			case 6 return Color.pink; 
			case 7 return Color.red; 
			case 8 return Color.yellow;
		}
		return Color.white;
	}

	private void randomBoxColor() {
		boolean cCorrect;
		int i = 0, c;
		while (i  4) {
			c = (int)(rnd.nextFloat()8 + 1);
			cBox[i] = c;
			cCorrect = true;
			if (cBox[i] == 9)
				cCorrect = false;
			else {
				for (int j=0;j  i; j++) {
					if (cBox[i] == cBox[j]) 
						cCorrect = false;
				}
			}
			if (cCorrect)
				i++;
		}
	

}

	static public void main(String[] args) {
		Frame f = new Jumpbox(Jumpbox - Xxxx Xxxxxx, #942261, args);
		f.setBackground(Color.gray);
		f.resize(508,430);
		f.show();
	}
}