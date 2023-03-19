package jp.co.axa.apidemo.converters.entity2dto;

import jp.co.axa.apidemo.dto.DepartmentDto;
import jp.co.axa.apidemo.entities.Department;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Department2DepartmentDtoConverter {

    public void convert(Department source, DepartmentDto target) {
        target.setId(source.getId());
        target.setName(source.getName());
    }

    public List<DepartmentDto> convert(List<Department> sourceList) {
        return sourceList.stream().map(department -> {
            DepartmentDto departmentDto = new DepartmentDto();
            convert(department, departmentDto);
            return departmentDto;
        }).collect(Collectors.toList());
    }
}
