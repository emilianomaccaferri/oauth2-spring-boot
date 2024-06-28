package cloud.macca.microservices.grades.service;

import cloud.macca.microservices.grades.dto.GetAllGradesResponse;
import cloud.macca.microservices.grades.dto.GetAllStudentsResponse;
import cloud.macca.microservices.grades.dto.Grade;
import cloud.macca.microservices.grades.dto.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GradesService {

    @Value("${microservices.students_endpoint}")
    private String gradesEndpoint;
    private final RestClient http;

    public GradesService(
            RestClient.Builder builder
    ){
        this.http = builder.build();
    }

    public Grade[] getAllGrades(String accessToken){
        RestClient.ResponseSpec response = this.http.get()
                .uri(gradesEndpoint + "/")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve();
        GetAllGradesResponse grades = response.body(GetAllGradesResponse.class);
        return grades.result;
    }
}
