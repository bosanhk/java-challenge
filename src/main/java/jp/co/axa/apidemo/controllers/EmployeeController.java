package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.converters.dto2entity.EmployeeDto2EmployeeConverter;
import jp.co.axa.apidemo.converters.entity2dto.Employee2EmployeeDtoConverter;
import jp.co.axa.apidemo.dto.EmployeeDto;
import jp.co.axa.apidemo.dto.common.ResponseDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ApiException;
import jp.co.axa.apidemo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private Employee2EmployeeDtoConverter employee2EmployeeDtoConverter;
    @Autowired
    private EmployeeDto2EmployeeConverter employeeDto2EmployeeConverter;

    @GetMapping("/employees")
    public ResponseDTO<List<Employee>> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();

        List<EmployeeDto> employeeDtoList = employees.stream().map(employee -> {
            EmployeeDto employeeDto = new EmployeeDto();
            employee2EmployeeDtoConverter.convert(employee, employeeDto);
            return employeeDto;
        }).collect(Collectors.toList());

        return new ResponseDTO(employeeDtoList);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseDTO<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) throws ApiException {
        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) throw new ApiException("Employee not found");

        EmployeeDto employeeDto = new EmployeeDto();
        employee2EmployeeDtoConverter.convert(employee, employeeDto);

        return new ResponseDTO(employeeDto);
    }

    @PostMapping("/employees")
    public ResponseDTO<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) throws ApiException {

        Employee duplicateEmployee = employeeService.getEmployeeByNickname(employeeDto.getNickname());

        if (duplicateEmployee != null) throw new ApiException("Employee nickname has been used.");

        Employee employee = new Employee();
        employeeDto2EmployeeConverter.convert(employeeDto, employee);

        employeeService.saveEmployee(employee);
        logger.debug("Employee Saved Successfully");

        EmployeeDto resultDto = new EmployeeDto();
        employee2EmployeeDtoConverter.convert(employee, resultDto);

        return new ResponseDTO(resultDto);
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseDTO deleteEmployee(@PathVariable(name="employeeId")Long employeeId) throws ApiException {
        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) throw new ApiException("Employee not found");

        employeeService.deleteEmployee(employeeId);
        logger.debug("Employee Deleted Successfully");
        return new ResponseDTO();
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseDTO<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto,
                               @PathVariable(name="employeeId")Long employeeId) throws ApiException {
        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) throw new ApiException("Employee not found");

        employeeDto2EmployeeConverter.convert(employeeDto, employee);
        employee = employeeService.updateEmployee(employee);

        EmployeeDto resultDto = new EmployeeDto();
        employee2EmployeeDtoConverter.convert(employee, resultDto);

        return new ResponseDTO(resultDto);
    }

}
