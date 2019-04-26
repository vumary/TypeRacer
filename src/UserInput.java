import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UserInput {
	public static void main(String[] args){
		File file = new File("user.input"); // point to file
		try{
			Scanner sc = new Scanner(file); // set up scanner

			while(sc.hasNextLine()){ //  grab a line at a time
				String aLine = sc.nextLine();
				for(int i = 0; i < aLine.length(); i++){
					System.out.println(aLine.charAt(i));
				}
				
			}
			sc.close(); // close scanner
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
}
