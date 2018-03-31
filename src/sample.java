import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class sample {

	String SORTED_FILE_PREFIX = "sorted_chunk_";
	String min = "0";
	
	 ArrayList<String> blocksnames1 = new ArrayList<>();

     ArrayList<String> blocksnames2 = new ArrayList<>();
     ArrayList<String> outputblock = new ArrayList<>();
	 
	public void execute() {
    	
    	ArrayList<Integer> blockscount = new ArrayList<>();
    	blockscount.add(0,3);
    	blockscount.add(1,75);
           	
        
    	for(int i=0;i<blockscount.size();i++)
    	{
    		for(int j=0;j<blockscount.get(i);j++)
    		{
    			if(i==0)
    			blocksnames1.add(SORTED_FILE_PREFIX+(i+1)+"_"+j+".txt");
    			if(i==1)
    			blocksnames2.add(SORTED_FILE_PREFIX+(i+1)+"_"+j+".txt");
    			
    		}
    		
    	}
    	//min val
    	for(int i=0;i<blockscount.size()-1;i++)
    	{
    		try {

    		   		for(int j=0;j<blockscount.get(i);j++)
    		{
    			File file1=new File(Constants.DATA_DIR +SORTED_FILE_PREFIX+(i+1)+"_"+j+".txt");
    			System.out.println(file1);
		   	
					Scanner s1=new Scanner(file1);
					String line1=s1.nextLine();	
    	    			
    		if(min.equals("0"))
    		min = line1;
			else
    		{
					if(Integer.parseInt(min.substring(0,8)) > Integer.parseInt(line1.substring(0,8)))
						min = line1;
				}
    		
    		s1.close();	
    		}
    		
    	}
    	catch (Exception e) {
			System.out.println(e.getMessage());
		}

    		
    	System.out.println("min value"+min);		
    	
    	boolean flag = Tuplecompare(min);
    	System.out.println("end");
    	
    	System.out.println(outputblock);
    	
    	
    	}
    	
    	

    	}

	private boolean Tuplecompare(String comp) {
		boolean flag = false;
		ArrayList<Integer> blockscount = new ArrayList<>();
    	blockscount.add(0,3);
    	blockscount.add(1,75);
        

   		for(int j=0;j<blockscount.get(1);j++)
    		{
  	
   			File file1 = new File(Constants.DATA_DIR + Constants.SORTED_FILE_PREFIX +2+"_"+j+".txt");
   			System.out.println(file1);
		try
		{
			
			Scanner s1 = new Scanner(file1);
			String line1 = s1.nextLine();
			//System.out.println(line1);

			while (s1.hasNextLine()) {
				//System.out.println(line1);

				if (Integer.parseInt(line1.substring(0, 8)) == Integer.parseInt(comp.substring(0, 8))) {
					System.out.println("match");
					outputblock.add((comp+line1));
					System.out.println(outputblock);
					
					line1 = s1.nextLine();
					flag = true;
				} else if (Integer.parseInt(line1.substring(0, 8)) > Integer.parseInt(comp.substring(0, 8))) {
					 line1=s1.nextLine();
				} else if (Integer.parseInt(line1.substring(0, 8)) < Integer.parseInt(comp.substring(0, 8))) {
					line1 = s1.nextLine();
				}
			}

			if (Integer.parseInt(line1.substring(0, 8)) == Integer.parseInt(comp.substring(0, 8))) {
				flag = true;
			}
			s1.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		
    }
		return flag;
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		sample obj = new sample();
		obj.execute();

	}

}
