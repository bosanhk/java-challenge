package jp.co.axa.apidemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.dto.EmployeeDto;
import jp.co.axa.apidemo.dto.common.ResponseDTO;
import jp.co.axa.apidemo.entities.Employee;
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
public class ApiDemoApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private EmployeeService employeeService;

	Employee employee;

	EmployeeDto employeeDto;

	@Before
	public void setup() {
		employee = new Employee();
		employee.setDepartment("IT");
		employee.setFirstName("first name");
		employee.setLastName("last name");
		employee.setSalary(10000);

		employeeDto = new EmployeeDto();
		employeeDto.setDepartment("IT");
		employeeDto.setFirstName("first name");
		employeeDto.setLastName("last name");
		employeeDto.setSalary(10000);
	}

	@Test
	public void listEmployee() throws Exception {

		employee.setUsername("username1");
		employeeService.saveEmployee(employee);

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
		assert(responseDTO.getData().size() == 1);

		//Test if the second level cache works properly
		int size = CacheManager.ALL_CACHE_MANAGERS.get(0)
				.getCache("jp.co.axa.apidemo.entities.Employee").getSize();
		assert(size > 0);
	}

	@Test
	public void getEmployee() throws Exception {

		employee.setUsername("username2");
		employeeService.saveEmployee(employee);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<EmployeeDto>> responseType = new TypeReference<ResponseDTO<EmployeeDto>>() {};

		ResponseDTO<EmployeeDto> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(responseDTO.getData().getUsername().equals("username2"));
	}

	@Test
	public void saveEmployee() throws Exception {

		employeeDto.setUsername("username3");
		ObjectMapper objectMapper = new ObjectMapper();

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/employee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<EmployeeDto>> responseType = new TypeReference<ResponseDTO<EmployeeDto>>() {};

		ResponseDTO<EmployeeDto> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(employeeService.getEmployeeByUsername("username3") != null);
	}

	@Test
	public void updateEmployee() throws Exception {

		employee.setUsername("username4");
		employeeService.saveEmployee(employee);

		employeeDto.setFirstName("first name2");
		ObjectMapper objectMapper = new ObjectMapper();

		MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/employee/" + employee.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<EmployeeDto>> responseType = new TypeReference<ResponseDTO<EmployeeDto>>() {};

		ResponseDTO<EmployeeDto> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(responseDTO.getData().getFirstName().equals("first name2"));
	}

	@Test
	public void deleteEmployee() throws Exception {

		employee.setUsername("username5");
		employeeService.saveEmployee(employee);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/api/v1/employee/" + employee.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO> responseType = new TypeReference<ResponseDTO>() {};
		ResponseDTO responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(employeeService.getEmployeeByUsername("username5") == null);
	}
}
