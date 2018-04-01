
public class StudentCourse {
	
	public int ID;
	public String CourseID;
    public int Semester;
    public int Year;
    public int Credits;
    public String Grade;
    
	public StudentCourse(String studentCourseTuple){
			ID=Integer.parseInt(studentCourseTuple.substring(0,8));
//			CourseID=studentCourseTuple.substring(8,16);
//	    	Semester=Integer.parseInt(studentCourseTuple.substring(16,17));
//	    	Year=Integer.parseInt(studentCourseTuple.substring(17,21));
//	    	Credits=Integer.parseInt(studentCourseTuple.substring(21,23));
//	    	Grade=studentCourseTuple.substring(23,27);
	}
	
	public String toString(){
		return ID+CourseID+Semester+Year+Credits+Grade;
	}
}
