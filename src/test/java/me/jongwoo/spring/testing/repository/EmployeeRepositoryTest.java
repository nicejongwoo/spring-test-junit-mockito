package me.jongwoo.spring.testing.repository;

import me.jongwoo.spring.testing.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @DisplayName("JUnit test for save employee operation")
    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        //given - precondition ro setup
        Employee employee = Employee.builder()
                .firstName("jongwoo")
                .lastName("lee")
                .email("jongwoo@email.com")
                .build();

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }


    @DisplayName("JUnit test for get all employees operation")
    @Test
    void givenEmployeesList_whenFindAll_thenEmployeesList(){
        //given - precondition ro setup
        Employee employee = Employee.builder()
                .firstName("jongwoo")
                .lastName("lee")
                .email("jongwoo@email.com")
                .build();

        Employee employee1 = Employee.builder()
                .firstName("gildong")
                .lastName("hong")
                .email("hong@email.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

}