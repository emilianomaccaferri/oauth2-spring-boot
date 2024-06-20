package cloud.macca.microservices.students.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class ExampleController {
    @GetMapping(value = "/")
    public String test(){
        return "hello cinglerio quangle!!!";
    }
}
