package cloud.macca.microservices.grades.dto;

public class Student {
    int id;
    String name,
        surname;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }
}
