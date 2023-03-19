package jp.co.axa.apidemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.dto.DepartmentDto;
import jp.co.axa.apidemo.dto.common.ResponseDTO;
import jp.co.axa.apidemo.entities.Department;
import jp.co.axa.apidemo.exceptions.ServiceException;
import jp.co.axa.apidemo.services.DepartmentService;
import net.sf.ehcache.CacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class DepartmentTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private DepartmentService departmentService;

	@Value("${api.security.apiKey}")
	private String apikey;

	private static final String API_KEY_PARAM = "X-Custom-ApiKey";

	Department departmentForDelete = new Department();
	Department departmentForUpdate = new Department();

	DepartmentDto departmentDtoForCreate = new DepartmentDto();
	DepartmentDto departmentDtoForUpdate = new DepartmentDto();

	@Before
	public void setup() throws ServiceException {

		//Pre-init entity for modification test
		departmentForUpdate.setName("DP1");

		if (departmentService.getDepartmentByName(departmentForUpdate.getName()) == null) {
			departmentForUpdate = departmentService.createDepartment(departmentForUpdate);
		} else {
			departmentForUpdate = departmentService.getDepartmentByName(departmentForUpdate.getName());
		}

		//Pre-init entity for deletion test
		departmentForDelete.setName("DP2");

		if (departmentService.getDepartmentByName(departmentForDelete.getName()) == null) {
			departmentForDelete = departmentService.createDepartment(departmentForDelete);
		} else {
			departmentForDelete = departmentService.getDepartmentByName(departmentForDelete.getName());
		}

		//Pre-init department DTO for creation test
		departmentDtoForCreate.setName("DP3");

		//Pre-init department DTO for modification test
		departmentDtoForUpdate.setName("DP4");
	}

	@Test
	public void listDepartment() throws Exception {

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/department")
				.contentType(MediaType.APPLICATION_JSON)
				.header(API_KEY_PARAM, apikey))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<List<DepartmentDto>>> responseType = new TypeReference<ResponseDTO<List<DepartmentDto>>>() {};

		ResponseDTO<List<DepartmentDto>> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(responseDTO.getData().size() > 0);

		//Test if the second level cache works properly
		int size = CacheManager.ALL_CACHE_MANAGERS.get(0)
				.getCache("jp.co.axa.apidemo.entities.Department").getSize();
		assert(size > 0);
	}

	@Test
	public void getDepartment() throws Exception {

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/department/" + departmentForUpdate.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.header(API_KEY_PARAM, apikey))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<DepartmentDto>> responseType = new TypeReference<ResponseDTO<DepartmentDto>>() {};

		ResponseDTO<DepartmentDto> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(responseDTO.getData().getName().equals(departmentForUpdate.getName()));
	}

	@Test
	public void createDepartment() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/department")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(departmentDtoForCreate))
				.header(API_KEY_PARAM, apikey))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<DepartmentDto>> responseType = new TypeReference<ResponseDTO<DepartmentDto>>() {};

		ResponseDTO<DepartmentDto> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(departmentService.getDepartmentByName(departmentDtoForCreate.getName()) != null);
	}

	@Test
	public void updateDepartment() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();

		MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/department/" + departmentForUpdate.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(departmentDtoForUpdate))
				.header(API_KEY_PARAM, apikey))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO<DepartmentDto>> responseType = new TypeReference<ResponseDTO<DepartmentDto>>() {};

		ResponseDTO<DepartmentDto> responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(responseDTO.getData().getName().equals(departmentDtoForUpdate.getName()));
	}

	@Test
	public void deleteDepartment() throws Exception {

		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/api/v1/department/" + departmentForDelete.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.header(API_KEY_PARAM, apikey))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		String json = result.getResponse().getContentAsString();

		TypeReference<ResponseDTO> responseType = new TypeReference<ResponseDTO>() {};
		ResponseDTO responseDTO = new ObjectMapper().readValue(json, responseType);

		assert(responseDTO.isSuccess());
		assert(departmentService.getDepartmentByName(departmentForDelete.getName()) == null);
	}
}
