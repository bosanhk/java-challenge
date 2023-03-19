package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.converters.dto2entity.EmployeeDto2EmployeeConverter;
import jp.co.axa.apidemo.converters.entity2dto.Employee2EmployeeDtoConverter;
import jp.co.axa.apidemo.dto.EmployeeDto;
import jp.co.axa.apidemo.dto.common.ResponseDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ApiException;
import jp.co.axa.apidemo.exceptions.ServiceException;
import jp.co.axa.apidemo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private Employee2EmployeeDtoConverter employee2EmployeeDtoConverter;
    @Autowired
    private EmployeeDto2EmployeeConverter employeeDto2EmployeeConverter;

    @GetMapping("")
    public ResponseDTO<List<Employee>> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();

        //Convert entities into DTO
        List<EmployeeDto> employeeDtoList = employee2EmployeeDtoConverter.convert(employees);

        return new ResponseDTO(employeeDtoList);
    }

    @GetMapping("/{employeeId}")
    public ResponseDTO<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) throws ApiException {
        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) throw new ApiException("Employee not found");

        //Convert entities into DTO
        EmployeeDto employeeDto = new EmployeeDto();
        employee2EmployeeDtoConverter.convert(employee, employeeDto);

        return new ResponseDTO(employeeDto);
    }

    @PostMapping("")
    public ResponseDTO<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) throws ApiException, ServiceException {

        //Check if duplicate username
        if (employeeService.getEmployeeByUsername(employeeDto.getUsername()) != null)
            throw new ApiException("Employee username has been used.");

        //Convert DTO into entity
        Employee employee = new Employee();
        employeeDto2EmployeeConverter.convert(employeeDto, employee);

        employee = employeeService.createEmployee(employee);
        logger.debug("Employee Saved Successfully");

        //Convert entity into DTO
        EmployeeDto resultDto = new EmployeeDto();
        employee2EmployeeDtoConverter.convert(employee, resultDto);

        return new ResponseDTO(resultDto);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseDTO deleteEmployee(@PathVariable(name="employeeId")Long employeeId) throws ApiException {

        //Check if employee exists
        if (employeeService.getEmployee(employeeId) == null)
            throw new ApiException("Employee not found");

        employeeService.deleteEmployee(employeeId);
        logger.debug("Employee Deleted Successfully");
        return new ResponseDTO();
    }

    @PutMapping("/{employeeId}")
    public ResponseDTO<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto,
                               @PathVariable(name="employeeId")Long employeeId) throws ApiException, ServiceException {

        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) throw new ApiException("Employee not found");

        //Convert DTO into entity
        employeeDto2EmployeeConverter.convert(employeeDto, employee);
        employee = employeeService.updateEmployee(employee);

        //Convert entity into DTO
        EmployeeDto resultDto = new EmployeeDto();
        employee2EmployeeDtoConverter.convert(employee, resultDto);

        return new ResponseDTO(resultDto);
    }

}
