package me.jongwoo.spring.testing.repository;

import me.jongwoo.spring.testing.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
