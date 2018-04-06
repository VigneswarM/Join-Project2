import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CgpaComputer {
	
	private ArrayList<String> grades=new ArrayList<>();
	HashMap<String,Double> gradesList=new HashMap<>();
	BufferedWriter bw;
	
	public CgpaComputer(){
		initGradesList();
		FileWriter writer;
		try {
			writer = new FileWriter(Constants.DATA_DIR + "GPA.txt", true);
	        bw = new BufferedWriter(writer);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void initGradesList(){
		gradesList.put("A+", 4.3);
		gradesList.put("A", 4.0);
		gradesList.put("A-", 3.7);
		gradesList.put("B+", 3.3);
		gradesList.put("B", 3.0);
		gradesList.put("B-", 2.7);
		gradesList.put("C+", 2.3);
		gradesList.put("C", 2.0);
		gradesList.put("C-", 1.7);
		gradesList.put("D+", 1.3);
		gradesList.put("D", 1.0);
		gradesList.put("D-", 0.7);
		gradesList.put("Fail", 0.0);
	}
	
	public void addGrade(String grade){
		grades.add(grade);		
	}
	
	public void compute(String currStudentId){
		if(grades.contains("Fail")){
			grades.clear();
			writeToFile(currStudentId, 0);
			return;
		}
		double cgpa=grades.stream().mapToDouble(x -> gradesList.get(x)).sum()/grades.size();
		cgpa = (double)Math.round(cgpa * 100d) / 100d;
		grades.clear();
		writeToFile(currStudentId,cgpa);
	}
	
	private void writeToFile(String studentId, double cgpa){
		try {
			bw.write(studentId+" "+cgpa);
    		bw.newLine();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}			    
	}
	
	public void finish(String studentID){
		try {
			compute(studentID);
			bw.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
