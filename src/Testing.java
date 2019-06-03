import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class TimeProgress {
	double timeElapsed;
	double percentCompletion;

	public TimeProgress(double timeElapsed, double percentCompletion) {
		this.percentCompletion = percentCompletion;
		this.timeElapsed = timeElapsed;
	}
}

public class Testing extends JFrame {

	/**
	 * This is where the text is collected and compared to the contents of textArea.
	 */
	JTextField textField;
	/**
	 * This is where the WPM is displayed on top of the textField and textArea
	 */
	JLabel label;
	/**
	 * Entire JPanel where all the other objects are displayed.
	 */
	JPanel panel;
	/**
	 * number of words typed
	 */
	int words = 0;
	int wpm;
	int totalWords = 0;
	/**
	 * Program start time
	 */
	double startTime;
	/**
	 * 
	 */

	Timer timer;
	double percentileWord;
	ArrayList<TimeProgress> arr = new ArrayList<TimeProgress>();
	JButton button1;
	/**
	 * 
	 */
	JButton button2;
	/**
	 * Green border when the contents of textField match textArea
	 */
	Border goodBorder = BorderFactory.createLineBorder(Color.GREEN, 5);
	/**
	 * Red border when contents of textField don't match textArea
	 */
	Border badBorder = BorderFactory.createLineBorder(Color.RED, 5);
	/**
	 * textArea border is default blue.
	 */
	Border typeBorder = BorderFactory.createLineBorder(Color.BLUE, 5);
	/**
	 * Way of storing every value of the textArea and comparing to textField. Any
	 * mismatches which are not null --> badBorder
	 */
	Dictionary dictionary = new Dictionary();
	ArrayList<Car> cars2 = new ArrayList<Car>(); // list of cars

	/**
	 * constructor Initializes the frame on top of which panel and all fields will
	 * be placed. Start time begins here (for now). Typing field is added so it is
	 * an interactive JFrame.
	 */
	public Testing() {

		JFrame frame = new JFrame("TypeRacer");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(800, 1000);
		frame.setResizable(false);
		frame.setLayout(new GridLayout(2, 1));

		startTime = System.currentTimeMillis();// we should start counting only as soon as person starts typing??? TEST

		// testing below

		frame.getContentPane().add(raceTrack());

		// testing above

		frame.getContentPane().add(typingField());

		frame.setVisible(true);
		refreshScreen();

	}

	public void refreshScreen() {
		timer = new Timer(0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Car c : cars2) {
					c.moveCur(percentileWord);
				}
			}
		});
		timer.setRepeats(true);
		// Aprox. 60 FPS
		timer.setDelay(17);
		timer.start();
	}

	/**
	 * The typing field includes all labels, textFields, textAreas, etc which are
	 * necessary for the game.
	 * 
	 * @return
	 */
	public JPanel typingField() {
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // new flow layout to hold text field

		// textField.setFont(textField.getFont().deriveFont(50f)); //set font
		Font typeFont = new Font("Comic Sans MS", Font.BOLD, 40);
		Font textFont = new Font("Courier", Font.PLAIN, 32);

		label = new JLabel("WPM: ", SwingConstants.CENTER);

		JTextArea textArea = new JTextArea(dictionary.getSentence(), 8, 40);

		textArea.setBorder(typeBorder);
		textArea.setLineWrap(true);
		textArea.setFont(textFont);
		label.setFont(textFont);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setWrapStyleWord(true); // make words cut off by the whole word
		totalWords = (new StringTokenizer(textArea.getText())).countTokens();

		textField = new JTextField(22); // 22 columns wide
		textField.setFont(typeFont); // player types in this
		// create a line border with the specified color and width
		// set the border of this component
		textField.setBorder(goodBorder);
		textField.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {
				update();

			}

			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void insertUpdate(DocumentEvent e) {
				boolean good = update();
				if (good && textField.getText().endsWith(" ")) {
					words = (new StringTokenizer(textField.getText())).countTokens(); // count number of words if new
																						// word is typed (count words to
																						// reduce cheating)
					percentileWord = ((double) words) / ((double) totalWords);

					arr.add(new TimeProgress(System.currentTimeMillis() - startTime, percentileWord));
				}
				System.out.println(percentileWord);

				wpm = (int) (((double) words * 60000.0) / ((double) (System.currentTimeMillis() - startTime)));
				label.setText("WPM: " + wpm);

				if (textField.getText().equals(textArea.getText()))
					writeRecord();
			}

			public boolean update() {
				if (!textArea.getText().startsWith(textField.getText())) {
					textField.setBorder(badBorder);
					return false;
				} else {
					textField.setBorder(goodBorder);
					return true;
				}
			}

		});

		panel.add(label);
		panel.add(textField); // set listener here
		panel.add(textArea);
		panel.setVisible(true);

		return panel;
	}

	public void writeRecord() {
		try {
			BufferedReader f = new BufferedReader(new FileReader("prev_record"));
			// input file name goes above

			if (Integer.parseInt(f.readLine()) < wpm) {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("prev_record")));
				out.println(wpm);
				for (TimeProgress tp : arr)
					out.println(tp.timeElapsed + " " + tp.percentCompletion);
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// testing below

	public void newCar() {
		// creates as many cars in one line as needed
		for (int i = 0; i < 4; i++) {
			Car newCar = new Car("green_102x28.png", i);

			cars2.add(newCar);
		}
	}

	public JPanel gameOver() {
		String bg = "bg_800_400.png";

		panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // new flow layout to hold text field
		panel.setSize(800, 400);
		panel.setBackground(new Color(144, 198, 111)); // set background to match lane border

		String src = new File("").getAbsolutePath() + "/src/"; // path to image setup
		ImageIcon backg = new ImageIcon(src + bg); // setups icon image
		JLabel background = new JLabel(backg);
		background.setBounds(0, 0, 800, 400); // set location and size of icon
		panel.add(background);

		return panel;

	}

	public JPanel raceTrack() {

		String bg = "bg_800_400.png";

		panel = new JPanel(null); // new flow layout to hold text field
		panel.setSize(800, 400);
		panel.setBackground(new Color(144, 198, 111)); // set background to match lane border

		String src = new File("").getAbsolutePath() + "/src/"; // path to image setup
		ImageIcon backg = new ImageIcon(src + bg); // setups icon image
		JLabel background = new JLabel(backg);
		background.setBounds(0, 0, 800, 400); // set location and size of icon

		// traverse every car in the array
		newCar();
		for (int j = 0; j < cars2.size(); j++) {
			// display
			panel.add(cars2.get(j).img_c);
			// cars2.get(j).img
		}

		panel.add(background);

		return panel;
	}

	// testing above

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// GUIS should be constructed on the EDT.
		JFrame tt = new Testing();
	}

}
