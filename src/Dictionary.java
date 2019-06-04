import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Anushree Chaudhuri
 *
 */
public class Dictionary {

	/**
	 * holds every sentence that's in the Dictionary file
	 */
	private ArrayList<String> sentences = new ArrayList<String>();
	
	/**
	 * default constructor
	 */
	public Dictionary() {
		
		File file = new File("Dictionary");
		
		try {
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()){ //  grab a line at a time
				String temp = sc.nextLine();
				sentences.add(temp);
				
			}
			sc.close();
		}
		
		catch (FileNotFoundException e){
			e.printStackTrace();
		}

		
	}
	
	/**
	 * @return a random sentence
	 */
	public String getSentence() {
		
		int i = (int) (Math.random() * sentences.size());
		return sentences.get(i);
		
	}

	
}
