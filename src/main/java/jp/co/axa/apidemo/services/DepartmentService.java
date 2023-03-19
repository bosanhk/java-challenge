package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ServiceException;

import java.util.List;

public interface DepartmentService {

    List<Department> retrieveDepartments();

    Department getDepartment(Long departmentId);

    Department getDepartmentByName(String departmentName);

    Department createDepartment(Department department) throws ServiceException;

    void deleteDepartment(Long departmentId);

    Department updateDepartment(Department department) throws ServiceException;

    List<Employee> retrieveDepartmentEmployees(Long departmentId);
}