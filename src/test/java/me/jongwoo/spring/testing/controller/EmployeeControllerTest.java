package me.jongwoo.spring.testing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.jongwoo.spring.testing.entity.Employee;
import me.jongwoo.spring.testing.service.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("JUnit test for createEmployee")
    @Test
    void givenEmployeeObject_whenCreateEmployee_then() throws Exception {
        //given - precondition ro setup
        Employee employee = Employee.builder()
                .firstName("jongwoo")
                .lastName("lee")
                .email("jongwoo@email.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        );

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }


    @DisplayName("JUnit test for getAllEmployees RestAPI")
    @Test
    void givenListOfEmployees_whenGetAllEmployees_then() throws Exception {
        //given - precondition ro setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().id(1L).firstName("jongwoo").lastName("lee").email("jongwoo@email.com").build());
        employeeList.add(Employee.builder().id(2L).firstName("gildong").lastName("hong").email("gildong@email.com").build());
        given(employeeService.getAllEmployees()).willReturn(employeeList);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));
    }


    @DisplayName("JUnit test for getEmployeeById RestAPI with valid employeeId")
    @Test
    void givenValidEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition ro setup
        long id = 1L;
        Employee employee = Employee.builder()
                .id(id)
                .firstName("jongwoo")
                .lastName("lee")
                .email("jongwoo@email.com")
                .build();

        given(employeeService.getEmployeeById(id)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", id));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }


    @DisplayName("JUnit test for getEmployeeById RestAPI with invalid employeeId")
    @Test
    void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given - precondition ro setup
        long id = 1L;
        given(employeeService.getEmployeeById(id)).willReturn(Optional.empty());

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", id));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }


    @DisplayName("JUnit test for updateEmployee RestAPI")
    @Test
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        //given - precondition ro setup
        long id = 1L;
        Employee savedEmployee = Employee.builder()
                .id(id)
                .firstName("jongwoo")
                .lastName("lee")
                .email("jongwoo@email.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .id(id)
                .firstName("jw")
                .lastName("kim")
                .email("jw@email.com")
                .build();

        given(employeeService.getEmployeeById(id)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }


    @DisplayName("JUnit test for updateEmployee RestAPI with invalid employeeId")
    @Test
    void givenIfNotExistsEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        //given - precondition ro setup
        long id = 1L;
        Employee updatedEmployee = Employee.builder()
                .id(id)
                .firstName("jw")
                .lastName("kim")
                .email("jw@email.com")
                .build();

        given(employeeService.getEmployeeById(id)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }


    @DisplayName("JUnit test for deleteEmployee RestAPI")
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        //given - precondition ro setup
        long id = 1L;
        willDoNothing().given(employeeService).deleteById(id);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", id));

        //then - verify the output
        MvcResult result = response.andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Assertions.assertThat(content).isEqualTo("Employee deleted successfully!");
    }

}