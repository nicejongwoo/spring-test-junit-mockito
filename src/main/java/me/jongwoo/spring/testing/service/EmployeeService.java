package me.jongwoo.spring.testing.service;

import me.jongwoo.spring.testing.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

}
