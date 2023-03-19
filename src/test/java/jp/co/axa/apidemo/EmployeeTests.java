package jp.co.axa.apidemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.dto.EmployeeDto;
import jp.co.axa.apidemo.dto.common.ResponseDTO;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ServiceException;
import jp.co.axa.apidemo.services.DepartmentService;
import jp.co.axa.apidemo.services.EmployeeService;
import net.sf.ehcache.CacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
		locations = "classpath:application.properties")
public class EmployeeTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	Employee employeeForDelete = new Employee();
	Employee employeeForUpdate = new Employee();

	EmployeeDto employeeDtoForCreate = new EmployeeDto();
	EmployeeDto employeeDtoForUpdate = new EmployeeDto();

	Department department = new Department();
	Department departmentForUpdate = new Department();

	@Before
	public void setup() throws ServiceException {

		//Pre-init department for employee test
		department.setName("IT");

		if (departmentService.getDepartmentByName(department.getName()) == null) {
			department = departmentService.createDepartment(department);
		} else {
			department = departmentService.getDepartmentByName(department.getName());
		}

		//Pre-init department for employee modification test
		departmentForUpdate.setName("IT2");

		if (departmentService.getDepartmentByName(departmentForUpdate.getName()) == null) {
			departmentForUpdate = departmentService.createDepartment(departmentForUpdate);
		} else {
			departmentForUpdate = departmentService.getDepartmentByName(departmentForUpdate.getName());
		}

		//Pre-init entity for modification test
		employeeForUpdate.setDepartment(department);
		employeeForUpdate.setUsername("username_for_update");
		employeeForUpdate.setFirstName("first name_for_update");
		employeeForUpdate.setLastName("last name_for_update");
		employeeForUpdate.setSalary(10000);

		if (employeeService.getEmployeeByUsername(employeeForUpdate.getUsername()) == null) {
			employeeForUpdate = employeeService.createEmployee(employeeForUpdate);
		} else {
			employeeForUpdate = employeeService.getEmployeeByUsername(employeeForUpdate.getUsername());
		}

		//Pre-init entity for deletion test
		employeeForDelete.setDepartment(department);
		employeeForDelete.setUsername("username_for_delete");
		employeeForDelete.setFirstName("first name_for_delete");
		employeeForDelete.setLastName("last name_for_delete");
		employeeForDelete.setSalary(10000);

		if (employeeService.getEmployeeByUsername(employeeForDelete.getUsername()) == null) {
			employeeForDelete = employeeService.createEmployee(employeeForDelete);
		} else {
			employeeForDelete = employeeService.getEmployeeByUsername(employeeForDelete.getUsername());
		}

		//Pre-init employee DTO for creation test
		employeeDtoForCreate.setDepartment(department.getName());
		employeeDtoForCreate.setFirstName("firstname_new_created");
		employeeDtoForCreate.setLastName("lastname_new_created");
		employeeDtoForCreate.setUsername("username_new_created");
		employeeDtoForCreate.setSalary(10000);

		//Pre-init employee DTO for modification test
		employeeDtoForUpdate.setDepartment(departmentForUpdate.getName());
		employeeDtoForUpdate.setFirstName("firstname_modified");
		employeeDtoForUpdate.setLastName("lastname_modified");
		employeeDtoForUpdate.setUsername("username_modified");
		employeeDtoForUpdate.setSalary(20000);
	}

	@Test
	public void listEmployee() throws Exception {

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/employee")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<List<EmployeeDto>>> responseType = new TypeReference<ResponseDTO<List<EmployeeDto>>>() {};

		ResponseDTO<List<EmployeeDto>> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(responseDTO.getData().size() > 0);

		//Test if the second level cache works properly
		int size = CacheManager.ALL_CACHE_MANAGERS.get(0)
				.getCache("jp.co.axa.apidemo.entities.Employee").getSize();
		assert(size > 0);
	}

	@Test
	public void listDepartmentEmployee() throws Exception {

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/department/" + department.getId() + "/employee")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<List<EmployeeDto>>> responseType = new TypeReference<ResponseDTO<List<EmployeeDto>>>() {};

		ResponseDTO<List<EmployeeDto>> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(responseDTO.getData().size() > 0);

		//Test if the second level cache works properly
		int size = CacheManager.ALL_CACHE_MANAGERS.get(0)
				.getCache("jp.co.axa.apidemo.entities.Employee").getSize();
		assert(size > 0);
	}

	@Test
	public void getEmployee() throws Exception {

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/" + employeeForUpdate.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<EmployeeDto>> responseType = new TypeReference<ResponseDTO<EmployeeDto>>() {};

		ResponseDTO<EmployeeDto> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(responseDTO.getData().getUsername().equals(employeeForUpdate.getUsername()));
	}

	@Test
	public void createEmployee() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/employee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeDtoForCreate)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<EmployeeDto>> responseType = new TypeReference<ResponseDTO<EmployeeDto>>() {};

		ResponseDTO<EmployeeDto> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(employeeService.getEmployeeByUsername(employeeDtoForCreate.getUsername()) != null);
	}

	@Test
	public void updateEmployee() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();

		MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/employee/" + employeeForUpdate.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeDtoForUpdate)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<EmployeeDto>> responseType = new TypeReference<ResponseDTO<EmployeeDto>>() {};

		ResponseDTO<EmployeeDto> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(responseDTO.getData().getFirstName().equals(employeeDtoForUpdate.getFirstName()));
		assert(responseDTO.getData().getLastName().equals(employeeDtoForUpdate.getLastName()));
		assert(responseDTO.getData().getSalary().equals(employeeDtoForUpdate.getSalary()));
		assert(responseDTO.getData().getUsername().equals(employeeDtoForUpdate.getUsername()));
		assert(responseDTO.getData().getDepartment().equals(employeeDtoForUpdate.getDepartment()));
	}

	@Test
	public void deleteEmployee() throws Exception {

		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/api/v1/employee/" + employeeForDelete.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO> responseType = new TypeReference<ResponseDTO>() {};
		ResponseDTO responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(employeeService.getEmployeeByUsername(employeeForDelete.getUsername()) == null);
	}
}
