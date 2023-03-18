package jp.co.axa.apidemo.converters.dto2entity;

import jp.co.axa.apidemo.dto.EmployeeDto;
import jp.co.axa.apidemo.entities.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDto2EmployeeConverter {

    public void convert(EmployeeDto source, Employee target) {
        target.setSalary(source.getSalary());
        target.setLastName(source.getLastName());
        target.setFirstName(source.getFirstName());
        target.setDepartment(source.getDepartment());
        target.setNickname(source.getNickname());
    }
}
