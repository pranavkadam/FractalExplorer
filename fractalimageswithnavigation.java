package fractalExplorer;

/**
 *  Fractal images 
@author Pranav Kadam

**/

import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;


//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x
public class FractalExplorer extends JFrame {
	static final int WIDTH = 600;
	static final int HEIGHT =600;
	
	//instance variables for width and height
	
	Canvas canvas; //creates canvas
	BufferedImage fractalImage;   //creates something to 'draw' on the canvas
	
	static final int MAX_ITER=100; 
	
	static final double DEFAULT_ZOOM= 100.0;
	static final double DEFAULT_TOP_LEFT_X = -3.0;
	static final double DEFAULT_TOP_LEFT_Y = +3.0;
	
	double zoomFactor = DEFAULT_ZOOM;
	double topLeftX   = DEFAULT_TOP_LEFT_X;
	double topLeftY   = DEFAULT_TOP_LEFT_Y;
	
	//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x
	
	public FractalExplorer(){
		setInitialGUIProperties();
		addCanvas();
		canvas.addKeyStrokeEvents();
		updateFractal(); 
	}
	
	//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x
	
	public static void main(String[] args){
		new FractalExplorer();
	}
	
	//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x
	
	private void addCanvas(){
		canvas = new Canvas();
		fractalImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		canvas.setVisible(true);
		this.add(canvas,BorderLayout.CENTER);
	}
	
	//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x	
	
	public void setInitialGUIProperties(){
		this.setTitle("Fractal Explorer"); //sets Title for GUI
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //defines how the window is closed. We chose 'x' button option
		this.setSize(WIDTH,HEIGHT);
		this.setResizable(false); //cannot be resized
		this.setLocationRelativeTo(null); //GUI is placed in the center of the screen 
		this.setVisible(true);
		
	}
	
	private double getXPos(double x){
		//transforms x co-ordinate into a point or rather a value on our complex plane (canvas)
		
		return x/zoomFactor + topLeftX; 
	}
	
	private double getYPos(double y){
		return y/zoomFactor - topLeftY; //negative because y-axis is flipped
		
	}
	
//the above methods return the values of c
	
//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x
	
	
	
		public void updateFractal(){
			//going to go through every single pixel on the canvas and change the color
			//loops through pixels
			
			for(int x=0; x<WIDTH; x++){
				for(int y=0; y<HEIGHT; y++){
					
					double c_r=getXPos(x); //real values are on the x-axis, imaginary y-axis
					double c_i=getYPos(y);
					
					int iterCount = computeIterations(c_r, c_i);
					
					//returns number of iterations	
					
					//the next thing we need to do is create a color based on the number of iterations with make color method
					
					int pixelColor = makeColor(iterCount);
					fractalImage.setRGB(x,y,pixelColor);
				}
					
			}
			canvas.repaint();
			
		}
		
//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x
		private int makeColor(int iterCount){
			
			int color = 0b011011100001100101101000; 
			
			int mask  = 0b000000000000010101110111; 
			int shiftMag = iterCount / 13;
			
		
			if(iterCount==MAX_ITER){
				return Color.BLACK.getRGB();
			
			
			
				//return Color.BLUE.getRGB();
			
			}return color | (mask << shiftMag);
		} 
	
//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x

		
	private int computeIterations(double c_r, double c_i){
		/*
		 Let c=c_r +c_i
		 	
		 Let z=z_r +z_i;
		 
		 z'=z*z + c
		   =(z_r +z_i)*(z_r +z_i) + c_r +c_i;
		   =z_r^2 + 2*z_r*z_i-z_1^2 + c_r +c_i
		   
		   Thus, simplified
		   
		   z_r'=z_r^2-z_i^2 +c_r
		   z_i'=2*z_r*z_i + c_i
		   
		   What we need to check, is whether z' distance from the origin is less than or equal to a certain value
		 		 */
		double z_r=0.0;
		double z_i=0.0;
		
		int iterCount=0;
		/*to calculate modulus (distance from origin) we use sqrt of(a^2+b^2)<=2 (w is the value we decided)
		 i.e a^2+b^2 <=4 (squaring both sides) */
		
		while(z_r*z_r + z_i*z_i <= 4.0){
			
			double z_r_tmp = z_r;
			
			z_r = z_r*z_r - z_i*z_i + c_r;
			z_i = 2*z_i*z_r_tmp + c_i;  //tmp because the value of z_r on line 62 is not the value of z_r on line 63
			
			//Point was inside the Mandelbrot set 
			if(iterCount >= MAX_ITER){
				return MAX_ITER;
				
			}
			iterCount++;
		}
		//if the while loop breaks early, then we know that the complex point c is outside the mandelbrot set
		// And we are interested in the number of iterations it did before it broke out of the while loop
		
		return iterCount;
	}
	
//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x

	private void moveUp(){
		double curHeight = HEIGHT/zoomFactor;
		
		topLeftY+=curHeight / 16;
		
		updateFractal();
	}
	
	private void moveDown(){
		double curHeight = HEIGHT/zoomFactor;
		
		topLeftY-=curHeight / 16;
		
		updateFractal();
	}
	
	private void moveLeft(){
		double curHeight = WIDTH/zoomFactor;
		
		topLeftX-=curHeight / 16;
		
		updateFractal();
	}
	
	private void moveRight(){
		double  curHeight = WIDTH/zoomFactor;
		
		topLeftX+=curHeight / 16;
		
		updateFractal();
	}
	
//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x
	
	private void adjustZoom(double newX, double newY, double newZoomFactor){
		topLeftX += newX/zoomFactor;
		topLeftY -= newY/zoomFactor;
		
		zoomFactor=newZoomFactor; 
		
		topLeftX -= (WIDTH/2) /	zoomFactor;
		topLeftY += (HEIGHT/2) / 	zoomFactor;
		
		updateFractal();
	}

//---------------------x-----------------------x-------------------------x----------------x-----------------------------x--------------------------x
	

	
	private class Canvas extends JPanel implements MouseListener {
		
		
		public Canvas(){
			addMouseListener(this);	
		}
		
		
		public void addKeyStrokeEvents() {
			KeyStroke wKey=KeyStroke.getKeyStroke(KeyEvent.VK_W, 0);
			KeyStroke aKey=KeyStroke.getKeyStroke(KeyEvent.VK_A, 0);
			KeyStroke sKey=KeyStroke.getKeyStroke(KeyEvent.VK_S, 0);
			KeyStroke dKey=KeyStroke.getKeyStroke(KeyEvent.VK_D, 0);
			
			Action wPressed= new AbstractAction(){
				@Override public void actionPerformed(ActionEvent e){
					moveUp(); 
				}
			};
			
			Action aPressed= new AbstractAction(){
				@Override public void actionPerformed(ActionEvent e){
					moveRight(); 
				}
			};
			
			Action sPressed= new AbstractAction(){
				@Override public void actionPerformed(ActionEvent e){
					moveDown(); 
				}
			};
			
			Action dPressed= new AbstractAction(){
				@Override public void actionPerformed(ActionEvent e){
					moveLeft(); 
				}
			};
			this.getInputMap().put(wKey, "w_key");
			this.getInputMap().put(aKey, "a_key");
			this.getInputMap().put(sKey, "s_key");
			this.getInputMap().put(dKey, "d_key");
			
			this.getActionMap().put("w_key", wPressed);
			this.getActionMap().put("a_key", aPressed);
			this.getActionMap().put("s_key", sPressed);
			this.getActionMap().put("d_key", dPressed);
		}


		@Override
		public Dimension getPreferredSize(){
			return new Dimension(WIDTH, HEIGHT);
		}
		@Override
		public void paintComponent(Graphics drawingObj){
			drawingObj.drawImage( fractalImage, 0, 0, null); 
			
			//this method would be responsible for drawing image 	 			
		}
		@Override
		public void mouseClicked(MouseEvent mouse) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent mouse) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent mouse) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mousePressed(MouseEvent mouse) {
			double x = (double) mouse.getX();
			double y = (double) mouse.getY();
			
			switch(mouse.getButton()){
			//left
			
			case MouseEvent.BUTTON1:
				adjustZoom(x,y,zoomFactor*2);
				break; 
				
			case MouseEvent.BUTTON3:	
				adjustZoom(x,y,zoomFactor/2);
			break;
			
			}
			
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent mouse) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	

	
	

}
