import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

public class LineCounter {
	public LineCounter(){
		
	}
	public int count(String fileName){
		File file =new File(Constants.DATA_DIR+fileName);
		FileReader fr;
	    int lineNumber = 0;
		try {
			fr = new FileReader(file);
			LineNumberReader lnr = new LineNumberReader(fr);
		    while (lnr.readLine() != null){
	            	lineNumber++;
	        }
		    lnr.close();
			return lineNumber;
		} catch (Exception e) {
				System.out.println(e.getMessage());
		}
		return 0;
	}

}
