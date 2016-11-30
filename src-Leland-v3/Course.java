import java.util.List;

/**
 * 
 */

/**
 * @author team37
 *
 */
public class Course {

	protected int id;
	protected String title;
	protected String comments;
	protected List<Term> terms;
	protected List<Course> prerequisites;
	protected int availableSeats;
	
	public int getId(){
		return id;
	}
	
	// Leland
	public void addSeats (int s){
		if (s > 0) {
			availableSeats += s;
		}
	}
	
	// Leland
	public void removeSeats (int s) {
		if (((availableSeats - s) >=0) && s > 0) {
			availableSeats -= s;
		}
	}
	
}

