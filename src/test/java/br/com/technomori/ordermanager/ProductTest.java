package br.com.technomori.ordermanager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.logging.Logger;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.technomori.ordermanager.dto.ProductDTO;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Test(groups = "ProductTest" )

public class ProductTest {

	private static Logger log = Logger.getLogger(ProductTest.class.getName());

	private final String BASE_PATH = TestSuite.SERVER_ADDRESS+"/products";

	@BeforeClass
	public void beforeClass() {
	}

	@Test(dataProvider="fetchingProductsProvider")
	public void fetchingProducts(String productName, String categoryId, Integer numberOfProductsExpected) {
		ResponseEntity<PagedResources<ProductDTO>> responseEntity = RestTemplateFactory.getRestTemplateNoProfile().exchange(
				BASE_PATH
				+ "/?"
				+ "productName="+productName
				+ "&"
				+ "categoryId="+categoryId
				, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<ProductDTO>>() {}
				);
		
		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

		Collection<ProductDTO> collectionDTO = responseEntity.getBody().getContent();
		log.info("Paging products in database: " + collectionDTO);
		
		assertThat(collectionDTO).hasSize(numberOfProductsExpected);
	}
	
	@Test
	public void testingAccessControlToEndpoints() {
		// GET - by ID
		RestTemplateFactory.getRestTemplateNoProfile().getForEntity(BASE_PATH+"/1", Object.class);
		RestTemplateFactory.getRestTemplateCustomerProfile().getForEntity(BASE_PATH+"/1", Object.class);
		RestTemplateFactory.getRestTemplateAdminProfile().getForEntity(BASE_PATH+"/1", Object.class);

		// GET - All
		RestTemplateFactory.getRestTemplateNoProfile().getForEntity(BASE_PATH, Object.class);
		RestTemplateFactory.getRestTemplateCustomerProfile().getForEntity(BASE_PATH, Object.class);
		RestTemplateFactory.getRestTemplateAdminProfile().getForEntity(BASE_PATH, Object.class);
	}
	
	@DataProvider
	private Object[][] fetchingProductsProvider() {
		return new Object[][] {
			new Object[] { "Printer", "1", 1},
			new Object[] { "Printer", "2", 1},
			new Object[] { "Printer", "3", 0},
			new Object[] { "Printer", "4", 1},
			new Object[] { "Printer", "1,2,3,4", 1},
			new Object[] { "er", "2", 1},
			new Object[] { "er", "1", 2},
			new Object[] { "er", "3", 0},
			new Object[] { "er", "4", 2},
			new Object[] { "er", "1,2,3,4", 2},
			new Object[] { "er", "", 0},
			new Object[] { "", "1,2,3,4", 7}
		};
	}
	
	@AfterClass
	public void afterClass() {
	}
}
