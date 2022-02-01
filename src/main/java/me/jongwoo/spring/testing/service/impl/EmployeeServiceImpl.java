package me.jongwoo.spring.testing.service.impl;

import me.jongwoo.spring.testing.entity.Employee;
import me.jongwoo.spring.testing.exception.AlreadyExistsException;
import me.jongwoo.spring.testing.repository.EmployeeRepository;
import me.jongwoo.spring.testing.service.EmployeeService;

import java.util.List;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (savedEmployee.isPresent()) { // 이미 DB에 존재할경우
            throw new AlreadyExistsException("이미 존재합니다.");
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
