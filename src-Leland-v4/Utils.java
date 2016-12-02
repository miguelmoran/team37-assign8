import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Utils {

	
	public static boolean isStringInt(String s){
		try{
			Integer.parseInt(s.trim());
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}
	}
	
	public static void writeFile(String filename, List<String> lines) {
		BufferedWriter output = null;
		 try {
	            File file = new File(filename);
	            output = new BufferedWriter(new FileWriter(file));
	            
	            for(int i=0; i<  lines.size(); i++){
	            	output.write(lines.get(i)+"\r\n");
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
	
		
	}
	
}
