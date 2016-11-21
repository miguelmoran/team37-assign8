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

		Test test = new Test();
		test.readFiles();
		test.displayDigest();
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
			case "display_requests":
				test.displayRequests();
				break;
			case "display_seats":
				test.displaySeats();
				break;
			case "display_records":
				test.displayRecords();
				break;
			case "check_request":
				checkRequest(test);
				break;	
			case "add_record":
				addRecord(test);
				break;	
			case "add_seats":
				addSeats(test);
				break;	
			case "quit":
				exit = true;
				System.out.println("> stopping the command loop");
				scanner.close();
				break;
			default:


				break;
			}

		}



	}
	
	private static void checkRequest(Test test) {
		int studentId=0;
		int courseId=0;

		if (attributes == null || attributes.length==0  || !isStringInt(attributes[0])) {
			System.out.println("> Missing student Id");
			return;
		}
		if (attributes == null || attributes.length<2){
			System.out.println("> Missing course Id");
			return;
		}
		studentId = Integer.parseInt(attributes[0]);
		courseId = Integer.parseInt(attributes[1]);


		test.checkRequest(studentId,courseId);
	}

	private static void addRecord(Test test) {

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


		test.addRecord(attributes);
	}

	private static void addSeats(Test test) {

		if (attributes == null || attributes.length==0 || !isStringInt(attributes[0])){
			System.out.println("> Missing or invalid course Id");
			return;	
		}else if (attributes.length<2 || !isStringInt(attributes[1])){
			System.out.println("> Missing or invalid number of seats");
			return;
		}


		test.addSeats(attributes);
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
