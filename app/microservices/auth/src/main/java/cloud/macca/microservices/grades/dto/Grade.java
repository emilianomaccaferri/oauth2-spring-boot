package cloud.macca.microservices.grades.dto;

public class Grade {
    public final int id;
    public final int studentId;
    public final int value;
    public Grade(
            int id,
            int studentId,
            int value
    ){
        this.id = id;
        this.studentId = studentId;
        this.value = value;
    }
}
