/**
 * Represent the Student Object for a tuple
 */
public class Student {

	/**
	 * 
	 * @param studentTuple
	 * Parses the tuple based on the size of each field and 
	 * updates the corresponding properties in the Student Object.
	 */
    public Student(String studentTuple) {
        ID = Integer.parseInt(studentTuple.substring(0,8));
        firstName = studentTuple.substring(8,18);
        lastName = studentTuple.substring(18,28);
        department = Integer.parseInt(studentTuple.substring(28,31));
        program = Integer.parseInt(studentTuple.substring(31,34));
        sinNumber = Integer.parseInt(studentTuple.substring(34,43));
        address = studentTuple.substring(43);      
    }

    public int ID;
    public String firstName;
    public String lastName;
    public String address;
    public int department;
    public int program;
    public int sinNumber;

    
    
    public String toString(){
        return ID + firstName + lastName + department + String.format ("%03d", program) + String.format ("%09d", sinNumber) + address;
    }
    
    public int getStudentID(){
    	return this.ID;
    }
}
