import java.util.Scanner;


/**
 * 
 */

/**
 * @author team37
 * 
 */
public class Assignment8 {


	static String[] commandLine;
	static String[] attributes;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Simulator simulator = new Simulator("");

		Scanner scanner = new Scanner(System.in);
		boolean exit = false;
		while (!exit){

			System.out.print("$main:");
			commandLine = scanner.nextLine().split("[,]");

			String command = commandLine[0];

			if (commandLine.length >1){
				attributes = new String[commandLine.length-1]; 
				for(int i=1;i<commandLine.length;i++){
					attributes[i-1]=commandLine[i];
				}

			}


			switch(command){
			case "initial":
				simulator.loadRecords();
				// remove cached files, and initialize them to empty
				simulator.selectInstructors();
				break;
			case "resume":
				//simulator.resume();
				// read cached files and rebuild the dataset for WEKA
				break;
			case "quit":
				exit = true;
				System.out.println("> stopping the command loop");
				// save cache files for resume
				scanner.close();
				break;
			default:


				break;
			}

		}



	}
	

	private static void addRecord(Simulator simulator) {

		if (attributes == null || attributes.length==0 || !isStringInt(attributes[0])){
			System.out.println("> Missing or invalid student Id");
			return;
		}else if (attributes.length<2 || !isStringInt(attributes[1])){
			System.out.println("> Missing or invalid course Id");
			return;
		}else if(attributes.length<3 || !isStringInt(attributes[2])){
			System.out.println("> Missing or invalid instructor Id");
			return;
		}else if (attributes.length<4){
			System.out.println("> Missing comments");
			return;
		}else if (attributes.length<5 || !attributes[4].matches("[ABCDF]")){
			System.out.println("> Missing or invalid grade");
			return;
		}


		simulator.addRecord(attributes);
	}



	private static boolean isStringInt(String s){
		try{
			Integer.parseInt(s);
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}

	}
}
