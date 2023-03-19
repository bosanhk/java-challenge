package jp.co.axa.apidemo.services.impl;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ServiceException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    @Transactional(readOnly = true)
    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        return optEmp.orElse(null);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeByUsername(String username) {
        Optional<Employee> optEmp = employeeRepository.findByUsername(username);
        return optEmp.orElse(null);
    }

    @Transactional
    public Employee createEmployee(Employee employee) throws ServiceException {
        if (employee.getId() != null)
            throw new ServiceException("Employee should not contain id.");

        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

    @Transactional
    public Employee updateEmployee(Employee employee) throws ServiceException {

        if (employee.getId() == null)
            throw new ServiceException("Employee not found");

        if (!employeeRepository.existsById(employee.getId()))
            throw new ServiceException("Employee not found");

        try {
            return employeeRepository.save(employee);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
}