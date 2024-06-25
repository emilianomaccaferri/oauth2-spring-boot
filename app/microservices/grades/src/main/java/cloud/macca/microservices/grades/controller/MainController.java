package cloud.macca.microservices.grades.controller;

import cloud.macca.microservices.grades.dto.request.AddGradeRequest;
import cloud.macca.microservices.grades.dto.response.SuccessResponse;
import cloud.macca.microservices.grades.model.Grade;
import cloud.macca.microservices.grades.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/")
public class MainController {

    @Autowired
    private GradeRepository grades;

    @PostMapping(value = "/{studentId}")
    public SuccessResponse<String> addGradeToStudentId(
            @PathVariable String studentId,
            @RequestBody AddGradeRequest body
    ){
        // check if the student exists!
        grades.insertGrade(
                body.getGrade(),
                Integer.valueOf(studentId)
        );

        return new SuccessResponse("ok");
    }

    @GetMapping(value = "/{studentId}")
    public Iterable<Grade> getGradesByStudentId(@PathVariable String studentId){
        return grades.findByStudentId(Integer.valueOf(studentId));
    }
}
