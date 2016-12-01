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
		
		String command = args[0];
		
		System.out.println(">command: "+command);
		if (command =="resume") 
			simulator.resume();
		System.out.println("loading Records");
		simulator.loadRecords();
		
		System.out.println("loading Instructor Assigments for Semester "+simulator.getCycle());
		simulator.readAssignments();
		simulator.analizeHistory();
		
		while (!exit){

			System.out.print("$roster selection >");
			commandLine = scanner.nextLine().split("[,]");

			command = commandLine[0];

			if (commandLine.length >1){
				attributes = new String[commandLine.length-1]; 
				for(int i=1;i<commandLine.length;i++){
					attributes[i-1]=commandLine[i];
				}

			}


			switch(command){
			case "add":
				addInstructor(simulator);
				break;
			case "delete":
				deleteInstructor(simulator);
				break;
			case "display":
				simulator.displayRoster();
				break;
			case "submit":
				System.out.println("selections finalized- now processing requests for Semester "+simulator.getCycle());
				
				simulator.validateCourseRequests();
				simulator.displaySemesterStatistics();
				simulator.addRecords();
				simulator.displayWaitlist();
				
								System.out.println("continue simulation? [yes/no]");
				break;
				
			case "quit": 
			case "no": 
				exit = true;
				System.out.println("> stopping the command loop");
				scanner.close();
				break;
			case "yes":
				System.out.println("association analysis of previos course selections");
				simulator.resume();
				
			default:


				break;
			}

			
		}



	}
	
	private static void checkRequests(Simulator simulator) {
		int studentId=0;
		int courseId=0;

		if (attributes == null || attributes.length==0  || !Utils.isStringInt(attributes[0])) {
			System.out.println("> Missing student Id");
			return;
		}
		if (attributes == null || attributes.length<2){
			System.out.println("> Missing course Id");
			return;
		}
		studentId = Integer.parseInt(attributes[0].trim());
		courseId = Integer.parseInt(attributes[1].trim());


		simulator.checkRequest(studentId,courseId);
	}
	
	
	private static void addInstructor(Simulator simulator) {

		if (attributes == null || attributes.length==0 || !Utils.isStringInt(attributes[0])){
			System.out.println("> Missing or invalid instructor Id");
			return;
		}

		simulator.addInstructor(Integer.parseInt(attributes[0].trim()));
	}

	private static void deleteInstructor(Simulator simulator) {

		if (attributes == null || attributes.length==0 || !Utils.isStringInt(attributes[0])){
			System.out.println("> Missing or invalid intstructor Id");
			return;
		}

		simulator.deleteInstructor(Integer.parseInt(attributes[0].trim()));
	}



	
}
