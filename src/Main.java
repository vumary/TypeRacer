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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

/**
 * @author Anushree Chaudhuri
 *
 */
class TimeProgress {
	double timeElapsed;
	double percentCompletion;

	public TimeProgress(double timeElapsed, double percentCompletion) {
		this.percentCompletion = percentCompletion;
		this.timeElapsed = timeElapsed;
	}
}

public class Main extends JFrame {

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
	/**
	 * words per minute current
	 */
	int wpm;
	/**
	 * record high words per minute
	 */
	int rec;
	/**
	 * total words in sentence
	 */
	int totalWords = 0;
	/**
	 * Program start time
	 */
	double startTime;
	/**
	 * Timer
	 */
	Timer timer;
	/**
	 * percent of total words completed
	 */
	double percentileWord;
	/**
	 * all previous time progresses at every instant (time and percent completed) to make competing race 
	 */
	ArrayList<TimeProgress> prevRecord = new ArrayList<TimeProgress>();
	/**
	 * buttons for display
	 */
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
	/**
	 * array list of cars for moving races
	 */
	ArrayList<Car> cars2 = new ArrayList<Car>(); // list of cars

	/**
	 * constructor Initializes the frame on top of which panel and all fields will
	 * be placed. Start time begins here (for now). Typing field is added so it is
	 * an interactive JFrame.
	 */
	public Main() {

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
		readRec(new File("prev_record"));

	}

	/**
	 * continuous update method for car movement
	 */
	public void refreshScreen() {
		timer = new Timer(0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cars2.get(0).moveCur(percentileWord);
				cars2.get(1).moveRec(prevRecord, System.currentTimeMillis() - startTime);
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

		JTextArea textArea = new JTextArea(8, 40);
		textArea.setText(dictionary.getSentence());

		// testing below

		Highlighter highlighter = textArea.getHighlighter();
		HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.green);

		// testing above

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
		textField.getDocument().addDocumentListener(/**
		 * @author Anushree Chaudhuri
		 *
		 */
		/**
		 * @author Anushree Chaudhuri
		 *
		 */
		new DocumentListener() {

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

					prevRecord.add(new TimeProgress(System.currentTimeMillis() - startTime, percentileWord));
				}
				System.out.println(percentileWord);

				wpm = (int) (((double) words * 60000.0) / ((double) (System.currentTimeMillis() - startTime)));
				label.setText("WPM: " + wpm);

				if (textField.getText().equals(textArea.getText())) {
					writeRecord();
					gameOver();
				}

			}

			public boolean update() {
				if (!textArea.getText().startsWith(textField.getText())) {
					textField.setBorder(badBorder);
					return false;
				} else {
					try {
						highlighter.addHighlight(0, textField.getText().length(), painter);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

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

	/**
	 * save the high record arraylist to a text file
	 */
	public void writeRecord() {
		try {
			BufferedReader f = new BufferedReader(new FileReader("prev_record"));
			// input file name goes above

			if (Integer.parseInt(f.readLine()) < wpm) {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("prev_record")));
				out.println(wpm);
				for (TimeProgress tp : prevRecord)
					out.println(tp.timeElapsed + " " + tp.percentCompletion);
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// testing below

	/**
	 * create a car object for arraylist
	 */
	public void newCar() {
		// creates as many cars in one line as needed
		for (int i = 0; i < 4; i++) {
			Car newCar = new Car("green_102x28.png", i);

			cars2.add(newCar);
		}
	}

	/**
	 * displays final wpm and compares to high record. terminates program after 5 seconds.
	 */
	public void gameOver() {

		try {
			BufferedReader f = new BufferedReader(new FileReader("prev_record"));
			// input file name goes above

			rec = Integer.parseInt(f.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		label.setText("Game Over. WPM: " + wpm + ". High Record: " + rec + ".");
		Executors.newSingleThreadScheduledExecutor().schedule(() -> System.exit(0), 5, TimeUnit.SECONDS);

	}

	/**
	 * creates the racetrack and displays images on top of the JPanel to make the cars move and stuff
	 * @return
	 */
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

	/**
	 * moves car across the screen by vx call this to update position
	 * 
	 * @param File record of past percentWords
	 */
	public void readRec(File record) {
		ArrayList<TimeProgress> arr = new ArrayList<TimeProgress>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(record));
			String line;
			in.readLine();
			while ((line = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				arr.add(new TimeProgress(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.prevRecord = arr;
	}
	// testing above

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// GUIS should be constructed on the EDT.
		JFrame tt = new Main();
	}

}
