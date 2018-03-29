import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *This Class has the methods to compute the bag difference of the sorted files
 */
public class BagDifference {
	
	/**
	 * Reads the Sorted input file and creates a file with the items and its corresponding count 
	 * @param filename
	 * @param outputFilename
	 * @return
	 */
    public HashMap<String, Integer> readFile(String filename, String outputFilename) {

        HashMap<String, Integer> map = new LinkedHashMap<String, Integer>();

        try {
            BufferedReader inputFile = new BufferedReader(new FileReader(filename));
            String line = null;
            String prev = "s";
            while ((line = inputFile.readLine()) != null) {
                if (prev.equals(line)) {
                    map.put(line, map.get(line) + 1);
                } else {
                	if (map.size()>10000*Constants.getBagDiffMultiplier()){
                		writeFile(outputFilename,map);
                		map.clear();
                	}
                	//System.out.println("Memory :: "+Runtime.getRuntime().freeMemory()+"Map size :: "+map.size() + " Counter " + counter);
                    map.put(line, 1);
                }
                prev = line;
            }
            writeFile(outputFilename, map);
            inputFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File error");
        }
        return map;
    }

    
    public void writeFile(String fileName,HashMap<String,Integer> tupleList){
		BufferedWriter bw=null;
        try {
            try {
            	String fullPathToFile = Constants.DATA_DIR + fileName;
                bw = new BufferedWriter(new FileWriter(fullPathToFile,true));
                for (Map.Entry<String, Integer> entry : tupleList.entrySet()){
                    bw.write(entry.getKey().toString()+"###"+entry.getValue().toString());
                    bw.newLine();
                }
            }finally {
                bw.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
	}
    
    private  void writeBagDifferenceFile(ArrayList<String> buffer)  {
        BufferedWriter bw=null;
        try {
            try {
            	bw = new BufferedWriter(new FileWriter(Constants.DATA_DIR + Constants.BAG_OUTPUT_FILE,true));
                for (String student : buffer) {
                    bw.write(student);
                    bw.newLine();
                }
            }finally {
                bw.close();
                //Performance start
                Performance.BagDifferenceDiskIO++;
               //Performance end
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Compares both the files and find the difference of the occurences of each tuple.
     * Occurences in Input 1 - Occurences in Input 2
     * Dumps all the bag difference information to a Bag difference file
     * @param opFile1
     * @param opFile2
     */
   public void compareTuple(String opFile1,String opFile2){
	File file1=new File(Constants.DATA_DIR + opFile1);
   	File file2=new File(Constants.DATA_DIR + opFile2);
   	try {
			Scanner s1=new Scanner(file1);
			Scanner s2=new Scanner(file2);
			String line1=s1.nextLine(),line2=s2.nextLine();
			while(s1.hasNextLine()&&s2.hasNextLine()){
				if(Integer.parseInt(line1.substring(0,8))==Integer.parseInt(line2.substring(0,8))){
					calculateDifference(line1,line2);
					line1=s1.nextLine();
					line2=s2.nextLine();
				}else if(Integer.parseInt(line1.substring(0,8))>Integer.parseInt(line2.substring(0,8))){
					line2=s2.nextLine();
				}else if(Integer.parseInt(line1.substring(0,8))<Integer.parseInt(line2.substring(0,8))){
					line1=s1.nextLine();
				}
			}
			if(Integer.parseInt(line1.substring(0,8))==Integer.parseInt(line2.substring(0,8))){
				calculateDifference(line1,line2);
			}
			s1.close();
			s2.close();
			writeBagDifferenceFile(BagDiff);
   	}catch(Exception e){
   		System.out.println(e.getMessage());
   	}
  }
   
   
   private ArrayList<String> BagDiff = new ArrayList<>();
   /**
    * Compares and find the difference of the occurences of each tuple.
     * Occurences of line1 - Occurences of line2
    * @param line1
    * @param line2
    */
   public void calculateDifference(String line1,String line2){
	    String[] splitStr1=line1.split("###");
		String[] splitStr2=line2.split("###");
		if(Integer.parseInt(splitStr1[1])>Integer.parseInt(splitStr2[1])){
			//System.out.println(splitStr1[0]+" "+(Integer.parseInt(splitStr1[1])-Integer.parseInt(splitStr2[1])));
			BagDiff.add(splitStr1[0]+" "+(Integer.parseInt(splitStr1[1])-Integer.parseInt(splitStr2[1])));
		}else{
			BagDiff.add("0");
		}
		if(BagDiff.size() > 10000) {
			writeBagDifferenceFile(BagDiff);
			BagDiff.clear();
		}
   }
   
}
