import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


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

	
}