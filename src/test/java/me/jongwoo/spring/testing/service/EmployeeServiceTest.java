package me.jongwoo.spring.testing.service;

import me.jongwoo.spring.testing.entity.Employee;
import me.jongwoo.spring.testing.exception.AlreadyExistsException;
import me.jongwoo.spring.testing.repository.EmployeeRepository;
import me.jongwoo.spring.testing.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("jongwoo")
                .lastName("lee")
                .email("jongwoo@email.com")
                .build();
//        employeeRepository = BDDMockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
    }


    @DisplayName("JUnit test for saveEmployee")
    @Test
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        //given - precondition ro setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }


    @DisplayName("JUnit test for saveEmployee which throws exception")
    @Test
    void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        //given - precondition ro setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        Assertions.assertThrows(AlreadyExistsException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }


    @DisplayName("JUnit test for getAllEmployees")
    @Test
    void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList(){
        //given - precondition ro setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("gildong")
                .lastName("hong")
                .email("gildong@email.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for getAllEmployees - negative scenario")
    @Test
    void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){
        //given - precondition ro setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }


    @DisplayName("JUnit test for getEmployeeById")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given - precondition ro setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        Optional<Employee> savedEmployee = employeeService.getEmployeeById(1L);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.get().getEmail()).isEqualTo("jongwoo@email.com");
    }

    @DisplayName("JUnit test for updateEmployee")
    @Test
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObejct(){
        //given - precondition ro setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("jw@email.com");
        employee.setFirstName("jw");

        //when - action or the behaviour that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("jw@email.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("jw");
    }


    @DisplayName("JUnit test for deleteEmployeeById")
    @Test
    void givenEmployeeId_whenDeleteEmployeeById_thenNothing(){
        //given - precondition ro setup
        willDoNothing().given(employeeRepository).deleteById(1L);

        //when - action or the behaviour that we are going test
        employeeService.deleteById(1L);

        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(1L);
    }

}