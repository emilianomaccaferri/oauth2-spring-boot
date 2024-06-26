package cloud.macca.microservices.grades.controller;

import cloud.macca.microservices.grades.dto.request.AddStudentRequest;
import cloud.macca.microservices.grades.dto.response.SuccessResponse;
import cloud.macca.microservices.grades.error.StudentNotFoundError;
import cloud.macca.microservices.grades.model.Student;
import cloud.macca.microservices.grades.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/")
public class MainController {

    @Autowired
    StudentsRepository students;

    @GetMapping(value = "/{studentId}")
    public SuccessResponse<Student> getStudentById(@PathVariable String studentId){
        final int id = Integer.parseInt(studentId);
        Student student = students.findById(id).orElseThrow(() -> new StudentNotFoundError(id));
        return new SuccessResponse<Student>(student);
    }

    @PostMapping(value = "/")
    public SuccessResponse<String> addStudent(@RequestBody AddStudentRequest student){
        students.create(student.getName(), student.getSurname());
        return new SuccessResponse<String>("student added");
    }

    @GetMapping(value = "/")
    public SuccessResponse<Iterable<Student>> getAllStudents(){
        return new SuccessResponse<Iterable<Student>>(students.findAll());
    }

}
