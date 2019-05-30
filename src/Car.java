import java.awt.Rectangle;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


/**
 * @author Michelle Wu
 * Car Class that connects with wpm to move cars across the screen
 */
public class Car {

	private int x_c, y_c; //pos
	private int w, h;
	public JLabel img_c;
	private int x_r, y_r; //pos

	public JLabel img_r;
	private int vx = 2; //velocity
	private int WPM;
	
	//screen settings
	private int screen_width = 800;
	private int screen_height = 400;

	/**
	 * @param filename - which image this car is referencing
	 * @param lane - 1 through 4, which lane this car is in
	 * creates a car (jlabel linked to an img) that will be shown in CarPaint
	 * 
	 */
	public Car(String filename, int lane) {
		//lane =  which lane the car is
		String src = new File("").getAbsolutePath() + "/src/";
		ImageIcon ghost = new ImageIcon(src + filename);
		img_c = new JLabel(ghost); //conect img ot this objects img field
		
		//bound img to object
		h = ghost.getIconHeight();
		w = ghost.getIconWidth();
		//calculate starting
		
		x_c = 0;
		y_c = 75+(lane)*80;
		img_c.setBounds(x_c,y_c,w,h);
	}
	
	
	/**
	 * @param w = new wpm
	 * constantly update this with the new WPM calculated from testing
	 * changes velocity of car
	 */
	public void updateWPM(int w) {
		WPM = w;
		vx = WPM/10; //generate new velocity
	}
	
	
	/**
	 * moves car across the screen by vx
	 * call this to update position
	 */
	public void moveCur(double percentWord) {
		while (x_c < percentWord*(screen_width)) {
			x_c += vx;
		}
		if(x_c >= screen_width - w) {
			System.out.println("reached finish line"); //update when classes are complete
		}
		img_c.setBounds(x_c,y_c,w,h);
	}
	
	public void moveRec(File record) {
		
		
	}

	
	/**
	 * @return Image label
	 * used to add car to visual panels
	 */
	public JLabel getImg() {
		return img_c;
	}

	
	/**
	 * @param img = new img of car
	 * changes appearance of car
	 */
	public void setImg(JLabel img) {
		this.img_c = img;
	}
	
	/**
	 * @return velocity of car
	 */
	public int getVx() {
		return vx;
	}
	
}
