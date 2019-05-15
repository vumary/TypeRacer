import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Testing extends JFrame {
	JTextField textField;
	JPanel panel;
	int words = 0;
	double startTime;
	JButton button1;
	JButton button2;
	Border goodBorder = BorderFactory.createLineBorder(Color.GREEN, 5);
	Border badBorder = BorderFactory.createLineBorder(Color.RED, 5);

	public Testing() {

		JFrame frame = new JFrame("TypeRacer");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(800, 800);
		frame.setResizable(false);

		frame.getContentPane().add(typingField());
		frame.setVisible(true);
		// pack();

		startTime = System.currentTimeMillis();
	}

	public JPanel typingField() {
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // new flow layout to hold text field

		// textField.setFont(textField.getFont().deriveFont(50f)); //set font
		Font typeFont = new Font("Comic Sans MS", Font.BOLD, 40);
		Font textFont = new Font("Comic Sans MS", Font.BOLD, 32);

		JTextArea textArea = new JTextArea(
				"Cheese is a dairy product derived from milk that is produced in a wide range of flavors, textures, and forms by coagulation of the milk protein casein. It comprises proteins and fat from milk, usually the milk of cows, buffalo, goats, or sheep.",
				16, 24);

		textArea.setLineWrap(true);
		textArea.setFont(textFont);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setWrapStyleWord(true); //make words cut off by the whole word

		textField = new JTextField(18); // 18 columns wide
		textField.setFont(typeFont);
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
				if(good && textField.getText().endsWith(" "))
					words = (new StringTokenizer(textField.getText())).countTokens(); // count number of words if new word is typed (count words to reduce cheating)
				System.out.println(((double) words * 60000.0) / ((double)(System.currentTimeMillis() - startTime))); // print wpm to the screen!

			}
			
			public boolean update() {
				if(!textArea.getText().startsWith(textField.getText())){
					textField.setBorder(badBorder);
					return false;
				}else {
					textField.setBorder(goodBorder);
					return true;
				}
			}
		});


		panel.add(textField); // set listener here
		panel.add(textArea);
		panel.setVisible(true);

		return panel;
	}

	public static void main(String[] args) {
		// GUIS should be constructed on the EDT.
		JFrame tt = new Testing();
	}
}
