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

		Simulator simulator = new Simulator();

		Scanner scanner = new Scanner(System.in);
		boolean exit = false;
		
		String command = args[0];
		
		System.out.println(">command: "+command);
		if (command.compareToIgnoreCase("resume") == 0) {
			System.out.println("loading cached Records");
			simulator.loadCache();
		}
		else {
			
			simulator.firstCycle();
			System.out.println("loading Records");
			simulator.loadRecords();
		}
		
		System.out.println("loading Instructor Assigments for Semester "+simulator.getCycle());
        boolean moreCycles= simulator.readAssignments();
     
        if (!moreCycles) {
            System.out.println("> stopping simulation - no more cycles");
            exit = true;
        }
        else
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
		
			case "submit":
				System.out.println("selections finalized- now processing requests for Semester "+simulator.getCycle());
				
				simulator.readRequests();
				simulator.validateCourseRequests();
				simulator.displaySemesterStatistics();
				simulator.addValidRecords();
				simulator.displayRecords();
				simulator.displayWaitlist();
				simulator.incrementWaitlistCycle();
				simulator.resume();
				simulator.writeCycle();
				simulator.writeRecords();
				simulator.writeWaitlist();
				simulator.writeStatistics();
				System.out.print("continue simulation? [yes/no]: ");
				break;
				
			case "quit": 
			case "no": 
				exit = true;
				System.out.println("> stopping the command loop");
				scanner.close();
				break;
			case "yes":
				System.out.println("loading Instructor Assigments for Semester "+simulator.getCycle());
		        moreCycles= simulator.readAssignments();
				

 
		        if (!moreCycles){
		            System.out.println("> stopping simulation - no more cycles");
		            exit = true;
		        }
		        else{
		        	System.out.println("association analysis of previous course selections");
					simulator.analizeHistory();
		        }
				
				break;
			case "show_suggestions":
			case "WEKA":
				simulator.analizeHistory();
				break;
			case "help":			
			default:
				
				System.out.println("help             - show this help screen.");
				System.out.println("add,row          - add uid from the roster to the assignment.");
				System.out.println("delete,row       - delete uid from the assignment and put it back on the roster.");
				System.out.println("show_roster      - show the selected and unselected rosters");
				System.out.println("show_suggestions - show suggestions from WEKA");
				System.out.println("submit           - submit the selection and complete the instructor assignments.");
				System.out.println("quit             - quit the simulation.");
				
				break;
			}
		}
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
