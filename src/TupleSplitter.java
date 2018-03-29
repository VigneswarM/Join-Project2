import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TupleSplitter {
    ArrayList<String> _chunkFileList=new ArrayList<>();
    public TupleSplitter(ArrayList<String> chunkFileList){
        _chunkFileList=chunkFileList;
    }

    public void execute(){
        try {
            for(String fileName:_chunkFileList){
                //Open the file
                String fullPathtoFile = Constants.DATA_DIR + fileName;
                File file=new File(fullPathtoFile);
                Scanner sortScan=new Scanner(file);
                while(sortScan.hasNextLine()){
                    Student student=new Student(sortScan.nextLine());
                    //System.out.println(student.toString());
                }
                sortScan.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
