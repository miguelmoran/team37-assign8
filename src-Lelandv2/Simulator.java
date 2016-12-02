import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

// Leland
//
import java.util.Scanner;
//
// Leland

public class Simulator {
	protected  String folderPath;
	protected  List<Student> studentList = new ArrayList<Student>();
	protected  List<Instructor> instructorList = new ArrayList<Instructor>();
	protected  List<Course> courseList = new ArrayList<Course>();
	protected  List<AcademicRecord> academicRecords = new ArrayList<AcademicRecord>();
	protected  List<Assignment> assignmentList = new ArrayList<Assignment>();
	protected  List<RequestRecord> requestList = new ArrayList<RequestRecord>();

	
	protected  int fallCourses;
	protected  int springCourses;
	protected  int summerCourses;
	protected  int validRequests;
	protected  int invalid_missingPrerequisites;
	protected  int invalid_hasAlreadyTaken;
	protected  int invalid_noAvailableSeats;
	private final int MAXInstructors = 5;	//maximum number of instructors to hire per cycle
	private int currentCycle = 1;
	
	public Simulator(){

		folderPath = "";

		initialize();
	}

	

	public Simulator(String path){

		folderPath = path;

		initialize();
	} 

	
	private void initialize() {
		fallCourses =0;
		springCourses=0;
		summerCourses=0;
		validRequests=0;
		invalid_missingPrerequisites=0;
		invalid_hasAlreadyTaken=0;
		invalid_noAvailableSeats=0;
	}
	
	public void loadRecords() {
		
		readStudents();
		readCourses();
		readInstructors();
		readAcademicRecords();
		readPrerequisites();
		
		// empty out cache files for writing later during start of initial loop
		// DEBUG
		readAssignments();
		
	}
	
	
	//------------------------------ read assignments --------------------
	private  void readAssignments() {
		String csvFileToRead = folderPath + "assignments_1.csv";	//remember to change the filename to different cycle
		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{
			InputStream i =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(i));

			while ((line = br.readLine())!=null){
				String[] fields = line.split(splitBy);	

				Assignment assignment = new Assignment();
				assignment.instructor = new Instructor();
				assignment.course = new Course();


				if (fields.length > 0) {
					assignment.instructor.uuid= Integer.parseInt(fields[0]);
					assignment.course.id = Integer.parseInt(fields[1]);
					assignment.seats = Integer.parseInt(fields[2]);

					assignmentList.add(assignment);
				}
			}

		}
		catch(FileNotFoundException e){
			System.out.println("the test file: "+ csvFileToRead+ " doesn't exist. ");
		} catch(IOException e) {
			e.printStackTrace();
		} finally{
			if (br !=null) {
				try{
					br.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	//------------------------------end read assignments -----------------
	
	private void readStudents() {
		String csvFileToRead = folderPath + "students.csv";

		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{

			InputStream i =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(i));
			while ((line = br.readLine())!=null){
				String[] students = line.split(splitBy);	

				Student student = new Student();
				PhoneNumber contactNumber = new PhoneNumber();
				List<PhoneNumber> phones = new ArrayList<PhoneNumber>();

				if (students.length > 0) {
					student.uuid = Integer.parseInt(students[0]);
					student.name = students[1];
					student.address = students[2];
					contactNumber.number = students[3];
					phones.add(contactNumber);
					student.phoneNumbers =  phones;

					studentList.add(student);
				}
			}

		}
		catch(FileNotFoundException e){
			System.out.println("the test file: "+ csvFileToRead+ " doesn't exist. ");
		} catch(IOException e) {
			e.printStackTrace();
		} finally{
			if (br !=null) {
				try{
					br.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private  void readInstructors() {
		String csvFileToRead = folderPath + "instructors.csv";
		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{
			InputStream i =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(i));

			while ((line = br.readLine())!=null){
				String[] instructors = line.split(splitBy);	

				Instructor instructor = new Instructor();
				PhoneNumber contactNumber = new PhoneNumber();
				List<PhoneNumber> phones = new ArrayList<PhoneNumber>();

				if (instructors.length > 0) {
					instructor.uuid = Integer.parseInt(instructors[0]);
					instructor.name = instructors[1];
					instructor.address = instructors[2];
					contactNumber.number = instructors[3];
					phones.add(contactNumber);
					instructor.phoneNumbers =  phones;

					instructorList.add(instructor);
				}
			}

		}
		catch(FileNotFoundException e){
			System.out.println("the test file: "+ csvFileToRead+ " doesn't exist. ");
		} catch(IOException e) {
			e.printStackTrace();
		} finally{
			if (br !=null) {
				try{
					br.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private  void readCourses() {
		String csvFileToRead = folderPath + "courses.csv";
		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{
			InputStream inputStream =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(inputStream));

			while ((line = br.readLine())!=null){
				String[] courses = line.split(splitBy);	

				Course course = new Course();
				Term term = new Term();
				List<Term> terms = new ArrayList<Term>();

				if (courses.length > 0) {
					course.id = Integer.parseInt(courses[0]);
					course.title = courses[1];

					for (int i=2; i< courses.length; i++)
					{
						term.name = courses[i];
						terms.add(term);

						switch (term.name){
						case   "Fall": fallCourses++; break;
						case   "Spring": springCourses++; break;
						case   "Summer": summerCourses++; break;


						}

					}
					course.terms = terms;
					course.prerequisites = new ArrayList<Course>();
					courseList.add(course);
				}
			}

		}
		catch(FileNotFoundException e){
			System.out.println("the test file: "+ csvFileToRead+ "doesn't exist. ");
		} catch(IOException e) {
			e.printStackTrace();
		} finally{
			if (br !=null) {
				try{
					br.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private  void readAcademicRecords() {
		String csvFileToRead = folderPath + "records.csv";
		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{
			InputStream i =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(i));

			while ((line = br.readLine())!=null){
				String[] attributes = line.split(splitBy);	

				addRecord(attributes);
			}

		}
		catch(FileNotFoundException e){
			System.out.println("the test file: "+ csvFileToRead+ "doesn't exist. ");
		} catch(IOException e) {
			e.printStackTrace();
		} finally{
			if (br !=null) {
				try{
					br.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private  void readPrerequisites() {
		String csvFileToRead = folderPath + "prereqs.csv";
		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{
			InputStream inputStream =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(inputStream));

			while ((line = br.readLine())!=null){
				String[] prerequisites = line.split(splitBy);	


				if (prerequisites.length > 0) {
					int prerequisiteCourseId = Integer.parseInt(prerequisites[0]);
					int courseId = Integer.parseInt(prerequisites[1]);

					findCourseById(courseId).prerequisites.add(findCourseById(prerequisiteCourseId));
				}
			}

		}
		catch(FileNotFoundException e){
			System.out.println("the test file: "+ csvFileToRead+ "doesn't exist. ");
		} catch(IOException e) {
			e.printStackTrace();
		} finally{
			if (br !=null) {
				try{
					br.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	protected void addRecord(String[] attributes) {
		AcademicRecord record = new AcademicRecord();
		if (attributes.length > 0) {
			record.student = findStudentById(Integer.parseInt(attributes[0]));
			record.course = findCourseById(Integer.parseInt(attributes[1]));
			record.instructor = findInstructorById(Integer.parseInt(attributes[2]));
			record.comments = attributes[3];
			record.grade = CourseGrade.valueOf(attributes[4]);

			Student student = findStudentById(Integer.parseInt(attributes[0]));
			if (student.academicRecords == null)
				student.academicRecords = new ArrayList<AcademicRecord>();

			student.academicRecords.add(record);
			academicRecords.add(record);

		}
	}
	protected  Course findCourseById(int courseId) {
		boolean courseFound = false;

		int i;
		for(i=0; i<courseList.size();i++){

			if (courseList.get(i).getId() == courseId)
			{
				courseFound = true;
				break;
			}
		} 
		return (courseFound)? courseList.get(i) : null;

	}

	protected  Student findStudentById(int studentId) {
		boolean studentFound = false;

		int i;
		for(i=0; i<studentList.size();i++){

			if (studentList.get(i).getId() == studentId)
			{
				studentFound = true;
				break;
			}
		} 


		return (studentFound)? studentList.get(i) : null;
	}

	protected  Instructor findInstructorById(int instructorId) {
		boolean instructorFound = false;

		int i;

		for(i=0; i<instructorList.size();i++){

			if (instructorList.get(i).getId() == instructorId)
			{
				instructorFound = true;
				break;
			}
		} 


		return (instructorFound)? instructorList.get(i) : null;
	}

	
	//Leland -- step 5: Select Instructor Assignments
	// required input or access to these
	// 1) output from WEKA
	// 2) assignmentList filled (from the current cycle)
	// 3) budget or maximum number of instructors to hire (assumed to be 5, using MAXInstructors)
	// 
	// resulting output
	// a) courses with seats, based on hired instructors
	// b) quit command to quit the simulation
	
	public void selectInstructors () {
		final int MAXSize = 10000;
		boolean exit = false;
		Scanner scanner = new Scanner(System.in);
		String[] commandLine = null;
		String[] attributes = null;
		int numInstructorsSelected = 0;
		int[] selectedInstructors = new int[MAXSize];
		boolean[] unSelectedAssignments = new boolean[MAXSize];
		boolean[] selectedAssignments = new boolean[MAXSize];
		
		// unselected rosters = assignments file
		// selected roster = begin with an empty list

		// copy the Assignment's index to unSelectedAssignment;
		
		for (int i=0; i < assignmentList.size(); i++) {
			unSelectedAssignments[i] = true;	// true means this element is used
			selectedAssignments[i] = false;		// false means this element is empty
		}

		
		while (!exit){
			System.out.print("$roster selection > ");
			commandLine = scanner.nextLine().split("[,]");

			String command = commandLine[0];

			if (commandLine.length >1){
				attributes = new String[commandLine.length-1]; 
				for(int i=1;i<commandLine.length;i++){
					attributes[i-1]=commandLine[i];
				}

			}


			switch(command){
			
			case "add":
				if ( numInstructorsSelected < MAXInstructors) {
					// The add command takes the argument of the index (row) of the assignment, starting at 0.
					// Each indexed item contains the instructor id, course id, and the seating capacity of the course
					// 
					int selection = -1;
					if (attributes.length > 0 ) {
						selection = Integer.parseInt(attributes[0]);
					}
					
					// first check to see if this row is in the un-assigned roster
					// method for checking roster is needed
					// method for moving from unassigned to assigned is needed
					// method for adding to course is needed
					if (selection < assignmentList.size() && selection >=0 ) {
						if (unSelectedAssignments[selection]){		// this is a valid row or index
							
							Assignment a = null;
							
							Iterator ita = assignmentList.iterator();
							
							
							for (int i=0; i<=selection && ita.hasNext(); i++)
								a = (Assignment) ita.next();		// get the indexed assignment
							
							if (selectedInstructors[a.getInstructor().getId()] == 0) {	// 0 means this instructor was not selected
								selectedInstructors[a.getInstructor().getId()] = 1;
								unSelectedAssignments[selection] = false;
								selectedAssignments[selection] = true;
								
								Iterator itb = courseList.iterator();
								numInstructorsSelected ++;
								boolean found = false;
								while (itb.hasNext() && !found) {
									Course c = (Course) itb.next();
									if (c.getId() == a.getCourse().getId()){
										c.addSeats(a.getCapacity());
										found = true;
									}
								}

							} else {
								System.out.println("This instructor, "+a.getInstructor().getId()+", has been assigned.");
							}
							
						} else {
							System.out.println("This row, "+selection+", has already been added");
						}
						
					} else {
						System.out.println("Invalid selection, please choose the row number from the unselected roster to add");
					}
					
				}
				else
					System.out.println("No more budget to hire new instructor. You may delete an assignment to reallocate the budget.");
				break;
				
			case "delete":
				// this is the opposite of add
				// need to determine if the assignment was previously added. If so, remove it, and subtract the seats from the course

				if (numInstructorsSelected > 0){
					
					int selection = -1;
					if (attributes.length > 0 ) {
						selection = Integer.parseInt(attributes[0]);
					}
					if (selection < assignmentList.size() && selection >=0 ) {
						if (selectedAssignments[selection]){		// this row was previously added
							
							selectedAssignments[selection] = false;
							unSelectedAssignments[selection] = true;
							numInstructorsSelected --;
							
							// now remove the seats from the course
							Assignment a = null;
							
							Iterator ita = assignmentList.iterator();
							
							
							for (int i=0; i<=selection && ita.hasNext(); i++)
								a = (Assignment) ita.next();		// get the indexed assignment

							Iterator itb = courseList.iterator();
							boolean found = false;
							while (itb.hasNext() && !found) {
								Course c = (Course) itb.next();
								if (c.getId() == a.getCourse().getId()){
									c.removeSeats(a.getCapacity());
									found = true;
								}
							}

						} else {
							System.out.println("Invalid selection. This row has not been added, so it cannot be deleted.");
						}
						
					} else {
						System.out.println("Out of range. Please select the row number from the Selected List.");
					}

					
				} else {
					System.out.println("Cannot delete. The Selection List is empty.");
				}
				break;
				
			case "show_catalogue":
				
				System.out.println("Course Catalogue: ");
				// call some helper method to display the courses with seats > 0
				
				break;
				
			case "show_roster":
				System.out.println("-----------[Unselected]----------------------------");	
				System.out.println("Instructors available for selection: ");
				System.out.println("row: instructor_id, course_id, seats");
				Iterator ita = assignmentList.iterator();
				Assignment a = null;
				int i=0;
				
				while (ita.hasNext()){
					a = (Assignment) ita.next();
					if (i<MAXSize){
						if (unSelectedAssignments[i]== true){
							System.out.println(i+": "+a.getInstructor().getId()+", "+a.getCourse().getId()+", "+a.getCapacity());
						}
						i++;					
					}
				}
				
				System.out.println("--------------------------------------------------");	
				System.out.println("--------------[Selected] -------------------------");	
				System.out.println("Instructors with assignment: ");
				Iterator itb = assignmentList.iterator();
				
				i = 0;
				while (itb.hasNext()){
					a = (Assignment) itb.next();
					if (i<MAXSize){
						if (selectedAssignments[i]== true){
							System.out.println(i+": "+a.getInstructor().getId()+", "+a.getCourse().getId()+", "+a.getCapacity());
						}
						i++;					
					}
				}
				break;
			
			case "show_suggestions":
				
				System.out.println("Suggestions from WEKA: ");
				break;

			case "quit":
				System.out.println("> exiting roster selection");
				//
				//
				// set global quit to true
				// continue to the submit case, and finish this cycle of simulation


			case "submit":
				exit = true;
//				scanner.close();
// don't close the scanner yet, the outer loop still needs it
				
				// process the remaining tasks of select instructors
				
				//simulator.resume();
				// read cached files and rebuild the dataset for WEKA
				break;

			case "help":			
			default:
				
				System.out.println("help - show this help screen.");
				System.out.println("add, uid - add uid from the roster to the assignment.");
				System.out.println("delete, uid - delete uid from the assignment and put it back on the roster.");
				System.out.println("show_catalogue - show the current course catalogue");
				System.out.println("show_roster - show the selected and unselected rosters");
				System.out.println("show_suggestions - show suggestions from WEKA");
				System.out.println("submit - submit the selection and complete the instructor assignments.");
				System.out.println("quit - quit the simulation.");
				
				break;


			}
			
		}

		
	}
	
	//
	// Leland
}
