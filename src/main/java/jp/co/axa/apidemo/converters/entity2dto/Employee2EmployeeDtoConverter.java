package jp.co.axa.apidemo.converters.entity2dto;

import jp.co.axa.apidemo.dto.EmployeeDto;
import jp.co.axa.apidemo.entities.Employee;
import org.springframework.stereotype.Component;

@Component
public class Employee2EmployeeDtoConverter {

    public void convert(Employee source, EmployeeDto target) {
        target.setId(source.getId());
        target.setSalary(source.getSalary());
        target.setLastName(source.getLastName());
        target.setFirstName(source.getFirstName());
        target.setDepartment(source.getDepartment());
        target.setUsername(source.getUsername());
    }
}
