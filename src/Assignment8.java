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
			case "show_roster":
				simulator.showRoster();
				break;
			case "show_catalogue":
				System.out.println("Course Catalogue: ");
				// call some helper method to display the courses with seats > 0
			
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
				System.out.println("association analysis of previous course selections");
				simulator.resume();
				simulator.analizeHistory();
				
				break;
			case "help":			
			default:
				
				System.out.println("help             - show this help screen.");
				System.out.println("add,row          - add uid from the roster to the assignment.");
				System.out.println("delete,row       - delete uid from the assignment and put it back on the roster.");
				System.out.println("show_catalogue   - show the current course catalogue");
				System.out.println("show_roster      - show the selected and unselected rosters");
				System.out.println("show_suggestions - show suggestions from WEKA");
				System.out.println("submit           - submit the selection and complete the instructor assignments.");
				System.out.println("quit             - quit the simulation.");
				
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
