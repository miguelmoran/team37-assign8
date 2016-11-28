/**
 * 
 */

/**
 * @author team37
 *
 */
public class AcademicRecord {
	protected CourseGrade grade;
	protected String comments;
	protected Course course;
	protected Student student;
	protected Instructor instructor;
	
	
	public boolean courseCompletedSuccesfully(){
		
		return this.grade != CourseGrade.F;
	}
	
}
