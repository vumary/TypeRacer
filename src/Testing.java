import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Testing extends JFrame {
	JTextField textField;
	JPanel panel;
	JButton button1;
	JButton button2;

	public Testing() {

		JFrame frame = new JFrame("huh");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(800,800);
		frame.setResizable(false);

		
		frame.getContentPane().add(typingField());
		frame.setVisible(true);
		// pack();


	}
	
	
	
	public JPanel typingField() {
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); //new flow layout to hold text field
		//textField.setFont(textField.getFont().deriveFont(50f)); //set font
		
		Font typeFont = new Font("Comic Sans MS", Font.BOLD, 40);
		textField = new JTextField(20); //8 columns wide
		textField.setFont(typeFont);

		panel.add(textField); //set listener here
		panel.setVisible(true);
		
		return panel;
	}
	

	public static void main(String[] args) {
		// GUIS should be constructed on the EDT.
		JFrame tt = new Testing();
	}
}
