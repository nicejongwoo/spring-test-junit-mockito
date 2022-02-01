package me.jongwoo.spring.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jongwoo.spring.testing.entity.Employee;
import me.jongwoo.spring.testing.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}