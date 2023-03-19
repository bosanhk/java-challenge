package jp.co.axa.apidemo.controllers;

import io.swagger.annotations.*;
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


    @GetMapping(value = "", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = API_KEY_PARAM, value = "Custom API Key Header", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, defaultValue = "mCplueTT3T9vSgyrytEqRPHI4")
    })
    @ApiOperation(value = "List All Employees")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Service Exception")
    })
    public ResponseDTO<List<EmployeeDto>> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();

        //Convert entities into DTO
        List<EmployeeDto> employeeDtoList = employee2EmployeeDtoConverter.convert(employees);

        return new ResponseDTO(employeeDtoList);
    }


    @GetMapping(value = "/{employeeId}", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = API_KEY_PARAM, value = "Custom API Key Header", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, defaultValue = "mCplueTT3T9vSgyrytEqRPHI4")
    })
    @ApiOperation(value = "Get Employee Details By ID")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Service Exception")
    })
    public ResponseDTO<EmployeeDto> getEmployee(@PathVariable(name="employeeId")Long employeeId) throws ApiException {
        Employee employee = employeeService.getEmployee(employeeId);

        if (employee == null) throw new ApiException("Employee not found");

        //Convert entities into DTO
        EmployeeDto employeeDto = new EmployeeDto();
        employee2EmployeeDtoConverter.convert(employee, employeeDto);

        return new ResponseDTO(employeeDto);
    }


    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = API_KEY_PARAM, value = "Custom API Key Header", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, defaultValue = "mCplueTT3T9vSgyrytEqRPHI4")
    })
    @ApiOperation(value = "Create New Employee", notes = "Username must be unique")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Service Exception")
    })
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


    @DeleteMapping(value = "/{employeeId}", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = API_KEY_PARAM, value = "Custom API Key Header", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, defaultValue = "mCplueTT3T9vSgyrytEqRPHI4")
    })
    @ApiOperation(value = "Delete Employee By ID")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Service Exception")
    })
    public ResponseDTO deleteEmployee(@PathVariable(name="employeeId")Long employeeId) throws ApiException {

        //Check if employee exists
        if (employeeService.getEmployee(employeeId) == null)
            throw new ApiException("Employee not found");

        employeeService.deleteEmployee(employeeId);
        logger.debug("Employee Deleted Successfully");
        return new ResponseDTO();
    }


    @PutMapping(value = "/{employeeId}", produces = "application/json", consumes = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = API_KEY_PARAM, value = "Custom API Key Header", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, defaultValue = "mCplueTT3T9vSgyrytEqRPHI4")
    })
    @ApiOperation(value = "Update Employee By ID", notes = "Username must be unique")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Service Exception")
    })
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
