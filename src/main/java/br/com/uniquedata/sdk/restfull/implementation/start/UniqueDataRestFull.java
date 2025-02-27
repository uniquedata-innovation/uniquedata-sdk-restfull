package br.com.uniquedata.sdk.restfull.implementation.start;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.uniquedata.sdk.restfull.implementation.beanbuild.UniqueDataRestFullBuiderBean;

/**
 * Class that can be used to bootstrap and launch a Spring application from a Java main
 * method.
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 */
public class UniqueDataRestFull {
	
	private static final String SDK_BANNER = "/sdk-banner.txt";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UniqueDataRestFull.class);
	
	/**
	 * For SpringBoot use.
	 * 
	 * Scan SpringApplication instance. The application context will load
	 * beans from the specified primary sources.
	 * 
	 * <pre>
"@SpringBootApplication"
"@EnableAutoConfiguration"
public class Application implements CommandLineRunner {

	"@Autowired"
	private TestApi testApi; 

	public static void main(final String[] args) {
		UniqueDataRestFull.scan(Application.class);
		SpringApplication.run(Application.class, args);
	}
	
	"@Override"
	public void run(final String... args) throws Exception {
		List<TestResponseBodyDto> produtos = api.get("Testing");
		System.out.println(new ObjectMapper().writeValueAsString(produtos));
	}
}
	 * <pre>
	 * 
	 * @param class start application.
	 */
	public static void startSptringBootScan() {
		LOGGER.info("Starting Unique Data RestFull Sdk + Spring Boot application scan.");
		UniqueDataRestFullPackageIdentifier.packageSimpleScanners();
	}
	
	/**
	 * For simple use.
	 * 
	 * Get Simple instance. The application context will load specific bean.
	 * 
	 * <pre>
"@UniqueDataRestFullClient(baseUrl = "https://webservice.com.br/")"
public interface TestApi {

	"@UniqueDataRestFullGet("/product")"
	public List<TestResponseBodyDto> get(@RestFullParam("name") final String name);
	
	public static void main(final String[] args) {
		final TestApi api = UniqueDataRestFull.getApi(TestApi.class);
	
		// Example normal RequestParam
		List<TestResponseBodyDto> produtos = api.get("Testing");
		System.out.println(new ObjectMapper().writeValueAsString(produtos));
	}
}
	 * </pre>
	 * 
	 * @param classType specific api.
	 */
	public static <T> T getApi(final Class<T> classType) {
		if(UniqueDataRestFullBuiderBean.isNewBuildBean()) {
			LOGGER.info("Starting UniqueData RestFull SDK scan.");
			UniqueDataRestFullPackageIdentifier.packageSimpleScanners();
			new UniqueDataRestFullBuiderBean().build();
		}
		
		return UniqueDataRestFullManagerBean.getBean(classType);
	}
	
	static {
		try {
	        LOGGER.info(IOUtils.toString(UniqueDataRestFull.class.getResourceAsStream(SDK_BANNER), "UTF-8"));
	    } catch (IOException e) {
	        LOGGER.error("Falha ao carregar o banner!", e);
	    }
	}
	
}
