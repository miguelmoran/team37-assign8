import java.util.ArrayList;
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
	
	
	public int getId(){
		return id;
	}
	
	
	public void assignSeats(int cycle, int seats){
		Term term;
		
		if (terms == null) {
			term = new Term();
			term.id = cycle;	
			term.availableSeats = seats;
			this.terms = new ArrayList<Term>();
			this.terms.add(term);
		}
		else {
			for (int i=0; i< terms.size();i++){
				term = terms.get(i);
				if (term.id == cycle) {
					term.availableSeats += seats;
					break;
				}
			}
		}
	}


	public void removeSeats(int cycle, int seats){
		Term term;
		
		if (terms == null) {
			term = new Term();
			term.id = cycle;	
			term.availableSeats = seats;
			this.terms = new ArrayList<Term>();
			this.terms.add(term);
		}
		else {
			for (int i=0; i< terms.size();i++){
				term = terms.get(i);
				if (term.id == cycle) {
					term.availableSeats -= seats;
					break;
				}
			}
		}
	}
	
	
	public int getAvailableSeats(int cycle){
		
		if (terms != null) {
			Term term;
			for (int i=0; i< terms.size();i++){
				term = terms.get(i);
				if (term.id == cycle) {
					return term.availableSeats;
				}
			}
		}
		return 0;
		
	}

}

