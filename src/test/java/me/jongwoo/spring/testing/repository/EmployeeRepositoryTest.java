package me.jongwoo.spring.testing.repository;

import me.jongwoo.spring.testing.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("jongwoo")
                .lastName("lee")
                .email("jongwoo@email.com")
                .build();
    }

    @DisplayName("JUnit test for save employee operation")
    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        //given - precondition ro setup
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

    @DisplayName("JUnit test for get employee by id operation")
    @Test
    void givenEmployeeObject_whenFindById_thenReturnEmployee(){
        //given - precondition ro setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Optional<Employee> employeeDb = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(employeeDb).isNotNull();
    }

    @DisplayName("JUnit test for get employee by email operation")
    @Test
    void givenEmployeeObject_whenFindByEmail_thenReturnEmployee(){
        //given - precondition ro setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Optional<Employee> employeeDb = employeeRepository.findByEmail(employee.getEmail());

        //then - verify the output
        assertThat(employeeDb).isNotNull();
    }


    @DisplayName("JUnit test for update employee operation")
    @Test
    void givenEmployeeObject_whenUpdate_thenReturnUpdatedEmployee(){
        //given - precondition ro setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("jw@email.com");
        savedEmployee.setFirstName("jw");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("jw@email.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("jw");
    }


    @DisplayName("JUnit test for delete employee operation")
    @Test
    void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        //given - precondition ro setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        employeeRepository.delete(employee);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(optionalEmployee).isEmpty();
    }


    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObejct(){
        //given - precondition ro setup
        employeeRepository.save(employee);
        String firstName = "jongwoo";
        String lastName = "lee";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for custom query using JPQL with name parameters")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){
        //given - precondition ro setup
        employeeRepository.save(employee);
        String firstName = "jongwoo";
        String lastName = "lee";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for custom native query with index parameters")
    @Test
    void givenFirstNameAndLastName_whenFindByNativeQuery_thenReturnEmployeeObject(){
        //given - precondition ro setup
        employeeRepository.save(employee);
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeQuery(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for custom native query with named parameters")
    @Test
    void givenFirstNameAndLastName_whenFindByNativeQueryWithNamedParams_thenReturnEmployeeObject(){
        //given - precondition ro setup
        employeeRepository.save(employee);
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeQueryWithNamedParams(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

}
