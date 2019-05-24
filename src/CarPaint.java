import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Michelle Wu
 * wip class for creating a road and cars
 * should ultimately make a jpanel that can be added to Testing class
 */
public class CarPaint extends JPanel implements ActionListener {

	int screen_width = 800;
	int screen_size = 400;
	String bg = "bg_800_400.png";
	JLabel background;
	Font font = new Font ("Courier New", 1, 50);


	ArrayList<Car> cars2 = new ArrayList<Car>(); //list of cars


	/** (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paintComponent(g);
		g.setFont(font);
		g.setColor(Color.WHITE);
	}//end of paint method - put code above for anything dealing with drawing -



	/* do not draw here */
	/**
	 * updates 40/sec
	 * wip, should change car velocities and move cars
	 */
	public void update() {
		System.out.println("smt");
	}//end of update method -  code above for any updates on variable


	//==================code above ===========================

	@Override
	public void actionPerformed(ActionEvent arg0) {
		update();
		repaint();
	}

	/**
	 * @param arg = ignore
	 * starts up CarPaint
	 * delete when finished
	 */
	public static void main(String[] arg) {
		CarPaint d = new CarPaint();
	}


	
	/**
	 * Creates 4 new cars in their respective lanes
	 * adds cars to arraylist cars2
	 */
	public void newCar() {
		//creates as many cars in one line as needed
		for(int i = 0; i < 4; i ++) {
			Car newCar = new Car("green_102x28.png", i);

			cars2.add(newCar);
		}
	}

	/**Constructor class
	 * uses temporary jframe
	 * adds all cars, backgrounds, etc. to panel
	 */
	public CarPaint(){

		JFrame f = new JFrame();
		f.setTitle("CARPAINt");
		f.setSize(screen_width, screen_size);
		

		f.getContentPane().setBackground(new Color(144,198,111)); //set background to match lane border

		String src = new File("").getAbsolutePath()+"/src/"; //path to image setup
		ImageIcon backg = new ImageIcon(src+bg);    //setups icon image
		background = new JLabel(backg);
		background.setBounds(0,0, 800, 400); //set location and size of icon


		//traverse every car in the array
		newCar();


		for(int j = 0; j < cars2.size(); j ++) {
			//display
			f.add(cars2.get(j).img);
		}
		//add bg after
		f.add(background);

		//frame actions
		f.setResizable(false);
		f.setLayout(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	Timer t;





}