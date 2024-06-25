package cloud.macca.microservices.grades;

import cloud.macca.microservices.grades.repository.GradeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("cloud.macca")
@EnableJpaRepositories
public class GradesApplication {

	private final GradeRepository grades;
	public GradesApplication(GradeRepository grades) {
		this.grades = grades;
	}
	public static void main(String[] args) {
		SpringApplication.run(GradesApplication.class, args);
	}

}
