package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.converters.dto2entity.DepartmentDto2DepartmentConverter;
import jp.co.axa.apidemo.converters.entity2dto.Department2DepartmentDtoConverter;
import jp.co.axa.apidemo.converters.entity2dto.Employee2EmployeeDtoConverter;
import jp.co.axa.apidemo.dto.DepartmentDto;
import jp.co.axa.apidemo.dto.EmployeeDto;
import jp.co.axa.apidemo.dto.common.ResponseDTO;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ApiException;
import jp.co.axa.apidemo.exceptions.ServiceException;
import jp.co.axa.apidemo.services.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private Department2DepartmentDtoConverter department2DepartmentDtoConverter;
    @Autowired
    private DepartmentDto2DepartmentConverter departmentDto2DepartmentConverter;
    @Autowired
    private Employee2EmployeeDtoConverter employee2EmployeeDtoConverter;

    @GetMapping("")
    public ResponseDTO<List<Department>> getDepartments() {
        List<Department> departments = departmentService.retrieveDepartments();

        //Convert entities into DTO
        List<DepartmentDto> departmentDtoList = department2DepartmentDtoConverter.convert(departments);

        return new ResponseDTO(departmentDtoList);
    }

    @GetMapping("/{departmentId}")
    public ResponseDTO<Department> getDepartment(@PathVariable(name="departmentId")Long departmentId) throws ApiException {
        Department department = departmentService.getDepartment(departmentId);

        if (department == null) throw new ApiException("Department not found");

        //Convert entities into DTO
        DepartmentDto departmentDto = new DepartmentDto();
        department2DepartmentDtoConverter.convert(department, departmentDto);

        return new ResponseDTO(departmentDto);
    }

    @GetMapping("/{departmentId}/employee")
    public ResponseDTO<List<EmployeeDto>> getDepartmentEmployee(@PathVariable(name="departmentId")Long departmentId) {
        List<Employee> employees = departmentService.retrieveDepartmentEmployees(departmentId);

        //Convert entities into DTO
        List<EmployeeDto> employeeDtoList = employee2EmployeeDtoConverter.convert(employees);

        return new ResponseDTO(employeeDtoList);
    }

    @PostMapping("")
    public ResponseDTO<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) throws ApiException, ServiceException {

        //Check if duplicate department name
        if (departmentService.getDepartmentByName(departmentDto.getName()) != null)
            throw new ApiException("Department name has been used.");

        //Convert DTO into entity
        Department department = new Department();
        departmentDto2DepartmentConverter.convert(departmentDto, department);

        department = departmentService.createDepartment(department);
        logger.debug("Department Saved Successfully");

        //Convert entity into DTO
        DepartmentDto resultDto = new DepartmentDto();
        department2DepartmentDtoConverter.convert(department, resultDto);

        return new ResponseDTO(resultDto);
    }

    @DeleteMapping("/{departmentId}")
    public ResponseDTO deleteDepartment(@PathVariable(name="departmentId")Long departmentId) throws ApiException {

        //Check if department exists
        if (departmentService.getDepartment(departmentId) == null)
            throw new ApiException("Department not found");

        departmentService.deleteDepartment(departmentId);
        logger.debug("Department Deleted Successfully");
        return new ResponseDTO();
    }

    @PutMapping("/{departmentId}")
    public ResponseDTO<DepartmentDto> updateDepartment(@RequestBody DepartmentDto departmentDto,
                               @PathVariable(name="departmentId")Long departmentId) throws ApiException, ServiceException {

        Department department = departmentService.getDepartment(departmentId);

        if (department == null) throw new ApiException("Department not found");

        //Convert DTO into entity
        departmentDto2DepartmentConverter.convert(departmentDto, department);
        department = departmentService.updateDepartment(department);

        //Convert entity into DTO
        DepartmentDto resultDto = new DepartmentDto();
        department2DepartmentDtoConverter.convert(department, resultDto);

        return new ResponseDTO(resultDto);
    }

}
