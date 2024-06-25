package cloud.macca.microservices.grades.controller;

import cloud.macca.microservices.grades.dto.Student;
import cloud.macca.microservices.grades.dto.request.AddGradeRequest;
import cloud.macca.microservices.grades.dto.response.SuccessResponse;
import cloud.macca.microservices.grades.model.Grade;
import cloud.macca.microservices.grades.repository.GradeRepository;
import cloud.macca.microservices.grades.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
public class MainController {

    @Autowired
    private GradeRepository grades;

    @Autowired
    private StudentsService studentsService;

    @PostMapping(value = "/{studentId}")
    public SuccessResponse<String> addGradeToStudentId(
            @PathVariable String studentId,
            @RequestBody AddGradeRequest body
    ) {

        Student student = studentsService.getStudent(Integer.parseInt(studentId));
        grades.insertGrade(body.getGrade(), Integer.parseInt(studentId));

        return new SuccessResponse<String>("ok");
    }

    @GetMapping(value = "/{studentId}")
    public Iterable<Grade> getGradesByStudentId(@PathVariable String studentId){
        return grades.findByStudentId(Integer.valueOf(studentId));
    }
}
