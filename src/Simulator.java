import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
<<<<<<< HEAD
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
=======
>>>>>>> origin/master


public class Simulator {
	protected  String folderPath;
	protected  List<Student> studentList = new ArrayList<Student>();
	protected  List<Instructor> instructorList = new ArrayList<Instructor>();
	protected  List<Course> courseList = new ArrayList<Course>();
	protected  List<AcademicRecord> academicRecords = new ArrayList<AcademicRecord>();
	protected  List<Assignment> assignmentList = new ArrayList<Assignment>();
	protected  List<RequestRecord> requestList = new ArrayList<RequestRecord>();
	protected  List<RequestRecord> waitlist = new ArrayList<RequestRecord>();

	final int MAXSize = 10000;
	int numInstructorsSelected = 0;
	int[] selectedInstructors = new int[MAXSize];
	boolean[] unSelectedAssignments = new boolean[MAXSize];
	boolean[] selectedAssignments = new boolean[MAXSize];

	
	
	protected  int cycle;
	protected  int fallCourses;
	protected  int springCourses;
	protected  int summerCourses;
	protected  int validRequests;
	protected  int invalid_missingPrerequisites;
	protected  int invalid_hasAlreadyTaken;
	protected  int invalid_noAvailableSeats;
	private final int MAXInstructors = 5; //maximum number of instructors to hire per cycle
	
	private int totalValid;
	private int totalFailed;
	private int totalWaitlisted;
	private int totalExamined;
	
	private Random random;
	
	private int totalValid;
	private int totalFailed;
	private int totalWaitlisted;
	private int totalExamined;
	
	private Random random;
	
	public Simulator(){

		folderPath = "";

		initialize();
	}

	

	public Simulator(String path){

		folderPath = path;

		initialize();
	} 

	
	private void initialize() {
		cycle=1;
		fallCourses =0;
		springCourses=0;
		summerCourses=0;
		validRequests=0;
		invalid_missingPrerequisites=0;
		invalid_hasAlreadyTaken=0;
		invalid_noAvailableSeats=0;
		
		totalValid = 0;
		totalFailed = 0;
		totalWaitlisted = 0;
		totalExamined = 0;
		
		random = new Random();
	}
	
	public void resume(){
		cycle++;
	}
	
	public void loadRecords() {
		
		readStudents();
		readCourses();
		readInstructors();
		readAcademicRecords();
		readPrerequisites();
		
		
		
	}
		
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
					student.uuid = Integer.parseInt(students[0].trim());
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
				//Term term = new Term();
				//List<Term> terms = new ArrayList<Term>();

				if (courses.length > 0) {
					course.id = Integer.parseInt(courses[0]);
					course.title = courses[1];

					/*for (int i=2; i< courses.length; i++)
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
					*/
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

	protected void readAssignments(){
		String csvFileToRead = folderPath + "assignments_"+cycle+".csv";
		
		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{
			InputStream i =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(i));

			while ((line = br.readLine())!=null){
				String[] assignments = line.split(splitBy);	

				Assignment assignment = new Assignment();
				if (assignments.length > 0) {
					assignment.instructor = findInstructorById(Integer.parseInt(assignments[0]));
					assignment.course = findCourseById(Integer.parseInt(assignments[1]));
					assignment.seats = Integer.parseInt(assignments[2]);

					if (assignment.course != null) {
						assignment.course.assignSeats(cycle, assignment.seats);
						
					}

					assignmentList.add(assignment);
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
		
		//initialize 
		for (int i=0; i < assignmentList.size(); i++) {
			unSelectedAssignments[i] = true;	// true means this element is used
			selectedAssignments[i] = false;		// false means this element is empty
		}


		
	}
	
	protected  void readRequests() {
		String csvFileToRead = folderPath + "requests_"+cycle+".csv";
		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{
			InputStream i =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(i));

			RequestResolution resolution;
			while ((line = br.readLine())!=null){
				String[] requests = line.split(splitBy);	

				RequestRecord request = new RequestRecord(cycle);
				if (requests.length > 0) {

					if (findStudentById(Integer.parseInt(requests[0])) ==null ||
							findCourseById(Integer.parseInt(requests[1])) == null)
						continue;

					request.addStudent(findStudentById(Integer.parseInt(requests[0])));
					request.addCourse(findCourseById(Integer.parseInt(requests[1])));
					resolution = request.calculateStatus();

					switch (resolution){
					case Valid:
						validRequests++;
						break;
					case Invalid_MissingPrerequisites: 
						invalid_missingPrerequisites++;
						break;
					case Invalid_HasAlreadyTaken:
						invalid_hasAlreadyTaken++;
						break;
					case Invalid_NoAvailableSeats:
						invalid_noAvailableSeats++;
						break;
					default:
						break;

					}

					requestList.add(request);
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

	public void validateCourseRequests(){
		validRequests=0;
		invalid_missingPrerequisites=0;
		invalid_hasAlreadyTaken=0;
		invalid_noAvailableSeats=0;
		
		List<RequestRecord> toRemove = new ArrayList<RequestRecord>();
		
		//validate waitlisted courses
		for(RequestRecord request : waitlist){
			checkRequest(request);
			
			//remove from waitlist if no longer on waitlist
			if(request.requestResolution != RequestResolution.Invalid_NoAvailableSeats)
			{
				toRemove.add(request);
			}
		}
		
		//validate newly loaded courses
		for(RequestRecord request : requestList){
			checkRequest(request);
		}
		
		for(RequestRecord request: toRemove){
			waitlist.remove(request);
			requestList.add(request);
		}
		
	}
	
	private void checkRequest(RequestRecord request){
		
		request.calculateStatus();
		
		if(request.requestResolution == RequestResolution.Valid){
			System.out.println("valid");
			validRequests++;
		}
		else if(request.requestResolution == RequestResolution.Invalid_MissingPrerequisites){
			System.out.println("student is missing one or more prerequisites");
			invalid_missingPrerequisites++;
		}
		else if(request.requestResolution == RequestResolution.Invalid_HasAlreadyTaken){
			System.out.println("student has already taken the course with a grade of C or higher");
			invalid_hasAlreadyTaken++;
		}
		else if(request.requestResolution == RequestResolution.Invalid_NoAvailableSeats){
			System.out.println("no remaining seats at this time: (re-)added to waitlist");
			invalid_noAvailableSeats++;

			//add the item to the waitlist for future processing if not already there
			if(!waitlist.contains(request))
				waitlist.add(request);
		}
	}
	
	public void displaySemesterStatistics(){
		System.out.println("Semester Statistics");
		StringBuilder sb = new StringBuilder();
	
		sb.append("Examined: " +  waitlist.size() + requestList.size() + " Granted: " + validRequests);
		sb.append(" Failed: " + (invalid_missingPrerequisites + invalid_hasAlreadyTaken) + "Wait Listed: " + invalid_noAvailableSeats); 
		
		System.out.println(sb.toString());
		
		totalExamined += waitlist.size() + requestList.size();
		totalValid += validRequests;
		totalFailed += invalid_missingPrerequisites;
		totalFailed += invalid_hasAlreadyTaken;
		totalWaitlisted += invalid_noAvailableSeats;
		
		System.out.println("Total Statistics");
		sb.append("Examined: " +  totalExamined + " Granted: " + totalValid);
		sb.append(" Failed: " + totalFailed + " Wait Listed: " + totalWaitlisted); 
		
	}
	
	public void addRecords(){
	
		for(RequestRecord request : requestList){
			if(request.requestResolution == RequestResolution.Valid){
				addToRecords(request);
			}
		}
	}
	
	private void addToRecords(RequestRecord validRequest){
		AcademicRecord record = new AcademicRecord();
		record.student = validRequest.student;
		record.course = validRequest.course;
		record.instructor = findInstructorByCourse(validRequest.course);
		record.comments = "random comment";
		record.grade = computeGrade();

		Student student = validRequest.student;
		
		if (student.academicRecords == null)
			student.academicRecords = new ArrayList<AcademicRecord>();

		student.academicRecords.add(record);
		academicRecords.add(record);
	}
	
	private CourseGrade computeGrade(){
		int score = random.nextInt(100);
		
		if(score < 100 && score > 70)
			return CourseGrade.A;
		else if(score <= 70 && score > 30)
			return CourseGrade.B;
		else if(score <= 30 && score > 20)
			return CourseGrade.C;
		else if(score <= 20 && score > 10)
			return CourseGrade.D;
		else
			return CourseGrade.F;
	}
	
	//step 9
	public void displayWaitlist(){
		//if on requestList and waitlist, output waitlist info
		StringBuffer sb = new StringBuffer();
		for(RequestRecord request : waitlist){
			sb.delete(0, sb.length());
			
			sb.append(request.student.uuid + ", " + request.student.name + ", ");
			sb.append(request.course.id + ", " + request.course.title);
			System.out.println(sb.toString());
		}
	}
	
	protected  void analizeHistory() {
		
		readAcademicRecords();
<<<<<<< HEAD
		
		String arffFileToWrite = folderPath + "history.arff";
		BufferedWriter output = null;
        try {
            File file = new File(arffFileToWrite);
            output = new BufferedWriter(new FileWriter(file));
            
            output.write("@relation university\r\n");
        	for(int i=0; i< courseList.size(); i++) {
        		output.write("@attribute 'course"+courseList.get(i).getId()+"' { taken, none}\r\n");
        	}
            
			output.write("@data\r\n");
            
            for (int i=0; i< studentList.size();i++){
    			Student student = studentList.get(i);
    				
    			for(int j=0; j< courseList.size(); j++) {
    				Course course = courseList.get(j);
    				
    				if (student.hasTakenCourse(course.getId()) == CourseGrade.CourseNotTaken)
    					output.write("none");
    				else
    					output.write("taken");
    				
    				if (j< courseList.size()-1)
    					output.write(",");
    			}
    			output.write("\r\n");
    		}
            
            
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
          if ( output != null ) {
            try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
          }
        }
		
=======
	
		String arffFileToWrite = folderPath + "history.arff";
        List<String> lines = new ArrayList<String>();
		    
        lines.add("@relation university");
    	for(int i=0; i< courseList.size(); i++) {
    		lines.add("@attribute 'course"+courseList.get(i).getId()+"' { taken, none}");
    	}
          
		lines.add("@data");
        
        for (int i=0; i< studentList.size();i++){
			Student student = studentList.get(i);
			String line ="";	
			for(int j=0; j< courseList.size(); j++) {
				Course course = courseList.get(j);
				
				if (student.hasTakenCourse(course.getId()) == CourseGrade.CourseNotTaken)
					line +="none";
				else
					line +="taken";
				
				if (j< courseList.size()-1)
					line +=",";
			}
			lines.add(line);
		}
            
        Utils.writeFile(arffFileToWrite,lines);
            
>>>>>>> origin/master
        ArffLoader loader = new ArffLoader();
		Instances data;
		try {
			
			loader.setSource(new File(arffFileToWrite));
			data = loader.getDataSet();
			Apriori apriori = new Apriori();
			apriori.setUpperBoundMinSupport(0.65);
		    apriori.buildAssociations(data);
<<<<<<< HEAD

=======
>>>>>>> origin/master
		    System.out.println(apriori);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("***Error:"+ e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("***Error:"+ e.getMessage());
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
	
	protected void addInstructor(int selection){
		if ( numInstructorsSelected < MAXInstructors) {
			// The add command takes the argument of the index (row) of the assignment, starting at 0.
			// Each indexed item contains the instructor id, course id, and the seating capacity of the course

			
			// first check to see if this row is in the un-assigned roster
			if (selection < assignmentList.size() && selection >=0 ) {
				if (unSelectedAssignments[selection]){		// this is a valid row or index
					
						
						boolean found = false;
						for (int i=0; i<=selection && i<assignmentList.size() && !found; i++) {
							Assignment a =  assignmentList.get(i);
					
							if (selectedInstructors[a.getInstructor().getId()] == 0) {	// 0 means this instructor was not selected
									selectedInstructors[a.getInstructor().getId()] = 1;
									unSelectedAssignments[selection] = false;
									selectedAssignments[selection] = true;
							} else {
								System.out.println("This instructor, "+a.getInstructor().getId()+", has been assigned.");
							}
							
							for (int j=0; j<courseList.size(); j++) {
								Course c = courseList.get(j);
								if (c.getId() == a.course.getId()){
									c.assignSeats(cycle,a.getCapacity());
									found = true;
									break;
								}
							}
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

	}
	
	protected void deleteInstructor(int selection){
		// this is the opposite of add
		// need to determine if the assignment was previously added. If so, remove it, and subtract the seats from the course

		if (numInstructorsSelected > 0){
			
			if (selection < assignmentList.size() && selection >=0 ) {
				if (selectedAssignments[selection]){		// this row was previously added
					
					selectedAssignments[selection] = false;
					unSelectedAssignments[selection] = true;
					numInstructorsSelected --;
					
					// now remove the seats from the course
					boolean found = false;
					for (int i=0; i<=selection && i<assignmentList.size() && !found; i++) {
						Assignment a =  assignmentList.get(i);
						
						for (int j=0; j<courseList.size(); j++) {
							Course c = courseList.get(j);
							if (c.getId() == a.course.getId()){
								c.removeSeats(cycle,a.getCapacity());
								found = true;
								break;
							}
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

	}
	
	public void displayRequests(){

		for (int i=0; i< requestList.size();i++){
			RequestRecord request = requestList.get(i);
			if(request.status)
				System.out.println(request.student.getId()+", "+request.student.name+", "+request.course.getId()+", "+request.course.title);
		}

	}
	
	
	public void checkRequest(int studentId,int courseId){
		RequestRecord request = new RequestRecord(cycle);
		request.student = findStudentById(studentId);
		request.course = findCourseById(courseId);
		request.calculateStatus();
		requestList.add(request);
		switch (request.requestResolution){
		case Valid:
			System.out.println("> request is valid");
			break;
		case Invalid_MissingPrerequisites:
			System.out.println("> student is missing one or more prerequisites");		
			break;
		case Invalid_HasAlreadyTaken:
			System.out.println("> student has already taken the course with a grade of C or higher");
			break;
		case Invalid_NoAvailableSeats:
			System.out.println("> no remaining seats available for the course at this time");
			break;
		}
		
		
	}
	
	protected Instructor findInstructorByCourse(Course course){
		for(Assignment assignment: assignmentList){
			if(assignment.course.id == course.id){
				return assignment.instructor;
			}
		}
		return null;
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
	
	protected String getCycle(){
		return new Integer(cycle).toString();
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
			
			// unselected rosters = assignments file
			// selected roster = begin with an empty list

			// copy the Assignment's index to unSelectedAssignment;
			
			for (int i=0; i < assignmentList.size(); i++) {
				unSelectedAssignments[i] = true;	// true means this element is used
				selectedAssignments[i] = false;		// false means this element is empty
			}

			
	}
		public void showRoster () {


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
			
			System.out.println("-----                                       ------");	
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
			System.out.println("-------End of List--------------------------------");

			
		}
	
}
