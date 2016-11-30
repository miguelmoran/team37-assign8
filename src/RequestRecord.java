import java.util.Iterator;



/**
 * @author team37
 *
 */

public class RequestRecord {

	protected Student student;
	protected Course course;
	protected boolean status;
	protected RequestResolution requestResolution;
	protected int cycle;

	
	RequestRecord(int semester) {
		this.cycle = semester;
	}
	
	
	protected RequestResolution calculateStatus(){
		
		CourseGrade grade=null;
		boolean hasTaken=false;
		//check prerequisites
		for (Iterator<Course> cp = this.course.prerequisites.iterator(); cp.hasNext();){
			
			Course c = cp.next();
			
			 grade =student.hasTakenCourse(c.getId());
			 hasTaken = grade != CourseGrade.CourseNotTaken;
			 
			if(!hasTaken || (hasTaken && grade== CourseGrade.F)){			
					this.status = false;
					this.requestResolution = RequestResolution.Invalid_MissingPrerequisites;
					return this.requestResolution;
			}
		}
		
		//check if the student Has Already Taken the course with a grade above D
		grade = student.hasTakenCourse(this.course.getId()) ;
		hasTaken = (grade != CourseGrade.CourseNotTaken);
		
		if(hasTaken && (grade != CourseGrade.D) && (grade != CourseGrade.F)){
					this.status = false;
					this.requestResolution = RequestResolution.Invalid_HasAlreadyTaken;
					return this.requestResolution;
			
		}
		
		// check if there are available seats
		
		int availableSeats = this.course.getAvailableSeats(cycle);
		if(availableSeats>0 )
		{
				this.course.assignSeats(cycle, availableSeats--);
				this.status = true;
				this.requestResolution = RequestResolution.Valid;
				
		}
		else{
			this.status = false;
			this.requestResolution = RequestResolution.Invalid_NoAvailableSeats;
			
		}
			
		
		
		return this.requestResolution;
		
	}
}