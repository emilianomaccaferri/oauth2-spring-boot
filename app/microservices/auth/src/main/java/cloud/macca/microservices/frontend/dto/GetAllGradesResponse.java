package cloud.macca.microservices.frontend.dto;

public class GetAllGradesResponse {
    public final Grade[] result;
    public final boolean success;
    public GetAllGradesResponse(
            Grade[] students,
            boolean success
    ){
        this.result = students;
        this.success = success;
    }
}
