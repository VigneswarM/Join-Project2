import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Merge {
    private static ArrayList<String> buff = new ArrayList<>();

    private static String result = "";

    public int execute(int count) {
		int j = 0;
		for(int i=0; i<count; i = i+2){
			String file_one = "sorted_chunk_2_"+i+".txt";
			String file_two = "sorted_chunk_2_"+ (i + 1)  +".txt";
			//System.out.println(file_one + "#####" + file_two);
			merge(file_one, file_two, "sorted_chunk_3_"+ j + ".txt" );
			j++;
		}
		
		if(count %2 == 0){
			Path source = Paths.get(Constants.DATA_DIR + "sorted_chunk_2_" + count + ".txt");
			Path destination = Paths.get(Constants.DATA_DIR + "sorted_chunk_3_" + j + ".txt");
			try {
				Files.copy(source, destination);
				j++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return j;
	}

    private static void updateBuff(String s) {
        if(buff.size() > 10000) {
        	buff.add(s);
            writeBagDifferenceFile(buff);
            buff.clear();
        }
        else{
            buff.add(s);
        }
    }

    public static void merge(String opFile1,String opFile2, String resultFileName){
    	buff.clear();
    	boolean added_s1 = false;
    	boolean added_s2 = false;
        result = resultFileName;
        File file1=new File(Constants.DATA_DIR + opFile1);
        File file2=new File(Constants.DATA_DIR + opFile2);
        try {
            Scanner s1=new Scanner(file1);
            Scanner s2=new Scanner(file2);
            String line1=s1.nextLine(),line2=s2.nextLine();
            while(s1.hasNextLine()&&s2.hasNextLine()){
            	if(Integer.parseInt(line1.substring(0,8)) == Integer.parseInt(line2.substring(0,8))){
                    updateBuff(line1);
                    updateBuff(line2);            	
                    line1=s1.nextLine();
                    line2=s2.nextLine();
               }else if(Integer.parseInt(line1.substring(0,8))>Integer.parseInt(line2.substring(0,8))){
                    updateBuff(line2);
                    line2=s2.nextLine();
                }else if(Integer.parseInt(line1.substring(0,8))<Integer.parseInt(line2.substring(0,8))){
                    updateBuff(line1);
                    line1=s1.nextLine();
                }
                else{
                	System.out.println("Dontknow");
                }
            }
            
            ArrayList<byte[]> temp = new ArrayList<byte[]>();
            
            if(!(s1.hasNextLine() || s2.hasNextLine())) {
            	temp.add(line1.getBytes());
            	temp.add(line2.getBytes());
            }
            
            if(s1.hasNextLine()){
            	temp.add(line1.getBytes());
            	temp.add(line2.getBytes());
            	while(s1.hasNextLine()){
            		temp.add(s1.nextLine().getBytes());
            	}
            }
            
            if(s2.hasNextLine()){
            	temp.add(line1.getBytes());
            	temp.add(line2.getBytes());
            	while(s2.hasNextLine()){
            		temp.add(s2.nextLine().getBytes());
            	}
            }
            
            temp.sort(Comparator.comparing(s -> new String(s)));
            
            for(byte[] k : temp){
            	updateBuff(new String(k));
            }
            
            s1.close();
            s2.close();
            writeBagDifferenceFile(buff);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private  static void writeBagDifferenceFile(ArrayList<String> buffer)  {
        BufferedWriter bw=null;
        try {
            try {
                bw = new BufferedWriter(new FileWriter(Constants.DATA_DIR + result, true));
                for (String student : buffer) {
                    bw.write(student);
                    bw.newLine();
                }
            }finally {
                bw.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
