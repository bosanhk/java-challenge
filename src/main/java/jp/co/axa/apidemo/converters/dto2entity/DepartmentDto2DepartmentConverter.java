package jp.co.axa.apidemo.converters.dto2entity;

import jp.co.axa.apidemo.dto.DepartmentDto;
import jp.co.axa.apidemo.entities.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentDto2DepartmentConverter {

    public void convert(DepartmentDto source, Department target) {
        if (source.getName() != null) target.setName(source.getName());
    }
}
