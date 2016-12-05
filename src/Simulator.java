import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;




import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ArffLoader;


public class Simulator {
	protected  String folderPath;
	protected  List<Student> studentList = new ArrayList<Student>();
	protected  List<Instructor> instructorList = new ArrayList<Instructor>();
	protected  List<Course> courseList = new ArrayList<Course>();
	protected  List<AcademicRecord> academicRecords = new ArrayList<AcademicRecord>();
	protected  List<Assignment> assignmentList = new ArrayList<Assignment>();
	protected  List<RequestRecord> requestList = new ArrayList<RequestRecord>();
	protected  List<RequestRecord> waitlist = new ArrayList<RequestRecord>();

    protected List<Integer> selectedInstructors = new ArrayList<Integer>();
	protected List<Boolean> assignmentSelection = new ArrayList<Boolean>();
	
    
	
	protected  int cycle;
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
	
	public void firstCycle() {
		this.writeCycle();
		this.writeRecords();
	}
	
	public void writeCycle() {
		

		String cycleFileToWrite = folderPath + "cycle.csv";
        List<String> lines = new ArrayList<String>();
		    
        System.out.println("Wrting cycle.csv for cycle: "+cycle);
        lines.add(Integer.toString(cycle));
        Utils.writeFile(cycleFileToWrite,lines);
	
	}
	
	public void writeRecords() {
		String recordsFileToWrite = folderPath + "cache_records.csv";
        List<String> lines = new ArrayList<String>();
        String line = new String();
        AcademicRecord a;
        
        System.out.println("Writing academic records: "+academicRecords.size()+" records");
        if (academicRecords.size()>0)
        	for(int i=0; i< academicRecords.size(); i++) {
        		line = new String();
        		a = academicRecords.get(i);

        		line = Integer.toString(a.student.uuid);
        		line = line.concat(",");
        		line = line.concat(Integer.toString(a.course.getId()));
        		line = line.concat(",");
        		line = line.concat(Integer.toString(a.instructor.getId()));
        		line = line.concat(",");
        		line = line.concat(a.comments);
        		line = line.concat(",");
        		line = line.concat(a.grade.toString());
  
        		lines.add(line);
        	}
        else
        	lines.add("");

		    
        Utils.writeFile(recordsFileToWrite,lines);

	
	}

	
	public void loadCache() {
		readStudents();
		readCourses();
		readInstructors();
		readCycle();
		readAcademicRecords();
		readPrerequisites();
		
	}
	
	private  void readCycle() {
		String csvFileToRead = folderPath + "cycle.csv";
		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{
			InputStream i =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(i));

			while ((line = br.readLine())!=null){
				String[] nextCycle = line.split(splitBy);	

				cycle = Integer.parseInt(nextCycle[0]);
				//DEBUG
				System.out.println(cycle);
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
		
		String csvFileToRead;
		
		if (cycle==1) {
			csvFileToRead = folderPath + "records.csv";
		} else {
			csvFileToRead = folderPath + "cache_records.csv";
		}
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


protected boolean readAssignments(){
        String csvFileToRead = folderPath + "assignments_"+cycle+".csv";
        
        BufferedReader br=null;
        String line =  "";
        String splitBy = ",";
		boolean moreCycles = false;

		if (!selectedInstructors.isEmpty())
			selectedInstructors.clear();;
		
		if (!assignmentSelection.isEmpty())
			assignmentSelection.clear();;
		
		if (!assignmentList.isEmpty())
			assignmentList.clear();

        try{
            InputStream i =this.getClass().getResourceAsStream(csvFileToRead);
            br = new BufferedReader(new InputStreamReader(i));
			moreCycles= true;
			
            while ((line = br.readLine())!=null){
                String[] assignments = line.split(splitBy);    

                Assignment assignment = new Assignment();
                if (assignments.length > 0) {
                    assignment.instructor = findInstructorById(Integer.parseInt(assignments[0]));
                    assignment.course = findCourseById(Integer.parseInt(assignments[1]));
                    assignment.seats = Integer.parseInt(assignments[2]);

                    assignmentList.add(assignment);
                    assignmentSelection.add(false);
                }
            }

        }
		catch(FileNotFoundException e){
			System.out.println("the test file: "+ csvFileToRead+ "doesn't exist. ");
		} catch(IOException e) {
			e.printStackTrace();
		} /*finally{
			if (br !=null) {
				try{
					br.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}*/
        

		return moreCycles;
}


	protected  void readRequests() {
		
		requestList.clear();
		
		String csvFileToRead = folderPath + "requests_"+cycle+".csv";
		BufferedReader br=null;
		String line =  "";
		String splitBy = ",";

		try{
			//todo breaks when there are no more files
			InputStream i =this.getClass().getResourceAsStream(csvFileToRead);
			br = new BufferedReader(new InputStreamReader(i));

			while ((line = br.readLine())!=null){
				String[] requests = line.split(splitBy);	

				RequestRecord request = new RequestRecord(cycle);
				if (requests.length > 0) {

					if (findStudentById(Integer.parseInt(requests[0])) ==null ||
							findCourseById(Integer.parseInt(requests[1])) == null)
						continue;

					request.addStudent(findStudentById(Integer.parseInt(requests[0])));
					request.addCourse(findCourseById(Integer.parseInt(requests[1])));

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
		
		System.out.println("Waitlist records");
		//validate waitlisted courses
		for(RequestRecord request : waitlist){
			checkRequest(request);
			
			//mark to remove from waitlist if no longer on waitlist
			if(request.requestResolution != RequestResolution.Invalid_NoAvailableSeats)
			{
				toRemove.add(request);
			}
		}
		
		System.out.println("RequestList records");
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
		System.out.print("request (" + request.student.uuid + ", " + request.course.id + "): ");
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

			//add the item to the waitlist for next waitlist cycle if not already there
			boolean exists = false;
				for(RequestRecord record : waitlist)
				{
					if(record.student.uuid == request.student.uuid && record.course.id == request.course.id)
						exists = true;
				}
			if(!exists){
				waitlist.add(request);
			}
		}
	}
	
	public void displaySemesterStatistics(){
		
		int semesterTotalExamined = validRequests + invalid_hasAlreadyTaken + invalid_missingPrerequisites + invalid_noAvailableSeats;
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nSemester Statistics\n");
		sb.append("Examined: " +  semesterTotalExamined + " Granted: " + validRequests);
		sb.append(" Failed: " + (invalid_missingPrerequisites + invalid_hasAlreadyTaken) + " Wait Listed: " + invalid_noAvailableSeats + "\n"); 
		
		
		totalExamined += semesterTotalExamined;
		totalValid += validRequests;
		totalFailed += invalid_missingPrerequisites;
		totalFailed += invalid_hasAlreadyTaken;
		totalWaitlisted += invalid_noAvailableSeats;
		
		sb.append("Total Statistics\n");
		sb.append("Examined: " +  totalExamined + " Granted: " + totalValid);
		sb.append(" Failed: " + totalFailed + " Wait Listed: " + totalWaitlisted); 
		
		System.out.println(sb.toString());
		
	}
	
	public void addValidRecords(){
	
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
		System.out.println("\nWait Listed Requests");
		for(RequestRecord request : waitlist){
			sb.delete(0, sb.length());
			
			sb.append(request.student.uuid + ", " + request.student.name + ", ");
			sb.append(request.course.id + ", " + request.course.title);
			System.out.println(sb.toString());
		}
	}
	
	//used to allow waitlisted courses to take the next cycle of classes
	public void incrementWaitlistCycle(){
		
		for(RequestRecord request: waitlist){
			request.cycle++;
		}
	}
	
	protected  void analizeHistory() {
		
		//readAcademicRecords();
	
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
            
        ArffLoader loader = new ArffLoader();
		Instances data;
		try {
			
			loader.setSource(new File(arffFileToWrite));
			data = loader.getDataSet();
			Apriori apriori = new Apriori();
		    apriori.buildAssociations(data);
		    System.out.println(apriori);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("***Error:"+ e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("***Error:"+ e.getMessage());
		}
		System.out.print("$roster selection >");
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
		if ( selectedInstructors.size() < MAXInstructors) {
			// The add command takes the argument of the index (row) of the assignment, starting at 0.
			// Each indexed item contains the instructor id, course id, and the seating capacity of the course
			
			// first check to see if this row is in the un-assigned roster
			if (selection < assignmentList.size() && selection >=0 ) {
				
				if (!assignmentSelection.get(selection)){		// this is a valid row or index
						Assignment a =  assignmentList.get(selection);
				
						if (!selectedInstructors.contains(a.getInstructor().getId())) {	
							assignmentSelection.set(selection,true);
							selectedInstructors.add(a.getInstructor().getId());
							
							System.out.println("The Instructor "+a.getInstructor().getId()+" selected!");
							
							for (int j=0; j<courseList.size(); j++) {
								Course c = courseList.get(j);
								if (c.getId() == a.course.getId()){
									c.assignSeats(cycle,a.getCapacity());
									break;
								}
							}
						} 
						else {
							System.out.println("The instructor "+a.getInstructor().getId()+" has already been assigned.");
						}
				} 
				else {
					System.out.println("The row "+selection+", has already been added");
				}
				
			} 
			else {
				System.out.println("Invalid selection, please choose the row number from the unselected roster to add");
			}
			
		}
		else
			System.out.println("No more budget to hire new instructor. You may delete an assignment to reallocate the budget.");

		System.out.print("$roster selection >");
	}
	
	protected void deleteInstructor(int selection){
		// this is the opposite of add
		// need to determine if the assignment was previously added. If so, remove it, and subtract the seats from the course

		if (selectedInstructors.size() > 0){
			
			if (selection < assignmentList.size() && selection >=0 ) {
				if (assignmentSelection.get(selection)){		// this row was previously added
					assignmentSelection.set(selection,false);
					
					Assignment a =  assignmentList.get(selection);
					
					for (int i=0;i<selectedInstructors.size();i++){
						if (selectedInstructors.get(i) == a.getInstructor().getId()){
							selectedInstructors.remove(i);
							System.out.println("Instructor "+a.getInstructor().getId()+" released!");
							break;
						}
					}
					
					// now remove the seats from the course
					for (int j=0; j<courseList.size(); j++) {
						Course c = courseList.get(j);
						if (c.getId() == a.course.getId()){
							c.removeSeats(cycle,a.getCapacity());
							break;
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
		System.out.print("$roster selection >");
	}
	
	public void displayRequests(){

		for (int i=0; i< requestList.size();i++){
			RequestRecord request = requestList.get(i);
			if(request.status)
				System.out.println(request.student.getId()+", "+request.student.name+", "+request.course.getId()+", "+request.course.title);
		}

	}
	
	public void displayRecords(){
		System.out.println("\nAcademic Records");
		for(AcademicRecord record : academicRecords){

			System.out.println(record.student.uuid + ", " + record.course.id + ", " + record.instructor.uuid + ", " + record.comments + ", " + record.grade);
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
		return courseFound ? courseList.get(i) : null;

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
	
		public void showRoster () {
			System.out.println("Instructors available for selection: ");
			System.out.println("row: instructor_id, course_id, seats");
			
			System.out.println("--------------[Selected] -------------------------");	
			System.out.println("Instructors with assignment: ");
			
			for (int i=0;i< assignmentList.size(); i++){
				Assignment a = assignmentList.get(i);
					if (assignmentSelection.size()>0 && assignmentSelection.get(i))	{
						System.out.println(i+": "+a.getInstructor().getId()+", "+a.getCourse().getId()+", "+a.getCapacity());
					}						
			}
			System.out.println("-----------[Unselected]----------------------------");	
			
			for (int i=0;i< assignmentList.size(); i++){
				Assignment a = assignmentList.get(i);
				if (!assignmentSelection.get(i) || assignmentSelection.size()==0)	{
						System.out.println(i+": "+a.getInstructor().getId()+", "+a.getCourse().getId()+", "+a.getCapacity());
					}						
			}
			
			System.out.println("-------End of List--------------------------------");
			System.out.print("$roster selection >");
		}
	
}
