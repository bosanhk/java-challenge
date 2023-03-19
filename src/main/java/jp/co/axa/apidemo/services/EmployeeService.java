package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ServiceException;

import java.util.List;

public interface EmployeeService {

    List<Employee> retrieveEmployees();

    Employee getEmployee(Long employeeId);

    Employee getEmployeeByUsername(String username);

    Employee createEmployee(Employee employee) throws ServiceException;

    void deleteEmployee(Long employeeId);

    Employee updateEmployee(Employee employee) throws ServiceException;
}