import java.util.List;


public class Student extends User {

	protected Boolean working;
	protected List<AcademicRecord> academicRecords;
	
	
	public CourseGrade hasTakenCourse(int courseId){
		
		
		if (academicRecords == null || academicRecords.isEmpty())
			return CourseGrade.CourseNotTaken;
		
		for (int i=0; i< academicRecords.size();i++){
			AcademicRecord record = academicRecords.get(i);

			if (record.course == null) continue;
			if (record.course.getId() == courseId){
				return record.grade;
			}
		}
		
		return CourseGrade.CourseNotTaken;
		
	}

	

}
