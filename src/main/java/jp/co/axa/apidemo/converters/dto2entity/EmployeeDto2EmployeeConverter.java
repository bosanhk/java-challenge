package jp.co.axa.apidemo.converters.dto2entity;

import jp.co.axa.apidemo.dto.EmployeeDto;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDto2EmployeeConverter {

    @Autowired
    private DepartmentService departmentService;

    public void convert(EmployeeDto source, Employee target) {
        if (source.getSalary() != null) target.setSalary(source.getSalary());
        if (source.getLastName() != null) target.setLastName(source.getLastName());
        if (source.getFirstName() != null) target.setFirstName(source.getFirstName());
        if (source.getDepartment() != null) {
            Department department = departmentService.getDepartmentByName(source.getDepartment());
            target.setDepartment(department);
        }
        if (source.getUsername() != null) target.setUsername(source.getUsername());
    }
}
