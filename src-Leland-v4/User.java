import java.util.List;

/**
 * 
 */

/**
 * @author team37
 *
 */
public class User {

	protected int uuid;
	protected String name;
	protected String address;
	protected List<PhoneNumber> phoneNumbers;
	
	public int getId(){
		return uuid;
	}
	
}
