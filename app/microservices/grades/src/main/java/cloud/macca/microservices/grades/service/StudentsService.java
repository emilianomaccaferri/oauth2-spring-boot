package cloud.macca.microservices.grades.service;

import cloud.macca.microservices.grades.dto.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.logging.Logger;

@Service
public class StudentsService {

    private final RestClient http;

    @Value("${students-uri}")
    private String studentsEndpoint;

    public StudentsService(RestClient.Builder builder){
        DefaultUriBuilderFactory f = new DefaultUriBuilderFactory(studentsEndpoint);
        this.http = builder.uriBuilderFactory(f).build();
    }

    public Student getStudent(int studentId){

        return this.http
                .get()
                .uri("/{id}", studentId)
                .retrieve()
                .body(Student.class);

    }

}
