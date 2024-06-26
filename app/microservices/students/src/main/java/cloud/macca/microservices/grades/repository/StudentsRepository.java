package cloud.macca.microservices.grades.repository;

import cloud.macca.microservices.grades.model.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsRepository extends CrudRepository<Student, Integer> {
    @Modifying
    @Query(
            value = "insert into students (name, surname) values (:name, :surname)",
            nativeQuery = true
    )
    @Transactional
    void create(@Param("name") String name, @Param("surname") String surname);
}
