package cloud.macca.microservices.grades.dto.request;

public class AddStudentRequest {
    String name, surname;

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }
}
