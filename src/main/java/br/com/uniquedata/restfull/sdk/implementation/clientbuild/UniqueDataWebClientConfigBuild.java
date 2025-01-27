package br.com.uniquedata.restfull.sdk.implementation.clientbuild;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UniqueDataWebClientConfigBuild {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueDataWebClientConfigBuild.class);

	
	private static Map<Class<?>, WebClient> webClients;
	
	static {
		webClients = new HashMap<>();
	}
	
	public synchronized static boolean hasWebClientCreated(final Class<?> classType) {
		return webClients.containsKey(classType);
	}
	
	public synchronized static WebClient getWebClient(final Class<?> classType) {
		if(hasWebClientCreated(classType)) {
			return webClients.get(classType);
		}

		return createWebClientBuild(classType, null);
	}
	
	public synchronized static WebClient createWebClientBuild(final Class<?> classType, final ExchangeFilterFunction interceptorFilter) {
		if(interceptorFilter == null) {
			 webClients.put(classType, WebClient.builder().build());
		}else {
			webClients.put(classType, WebClient.builder().filter(interceptorFilter)
				.exchangeStrategies(createExchangeStrategies()).build());
		}
		
        LOGGER.info("The Spring Web Client process has been successfully customized and initialized by UniqueData RestFull SDK!");
		
		return webClients.get(classType);
	}

    private static ExchangeStrategies createExchangeStrategies() {
    	final ObjectMapper mapper = UniqueDataJacksonConfigBuild.getCustomObjectMapper();

        return ExchangeStrategies.builder().codecs(clientCodecConfigurer -> {
            clientCodecConfigurer.defaultCodecs() .jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
            clientCodecConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
        }).build();
    }
	
}
