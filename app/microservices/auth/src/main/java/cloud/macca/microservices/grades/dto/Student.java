package cloud.macca.microservices.grades.dto;

public class Student {
    public final int id;
    public final String name;
    public final String surname;

    public Student(
            int id,
            String name,
            String surname
    ) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }
}
