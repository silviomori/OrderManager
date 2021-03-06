package br.com.technomori.ordermanager;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;


public class AuthorizationInterceptor implements ClientHttpRequestInterceptor {
	
	private List<String> authToken;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {

		if( "/login".equals(request.getURI().getPath()) ) {
			ClientHttpResponse clientHttpResponse = execution.execute(request, body);

			authToken = clientHttpResponse.getHeaders().get("Authorization");
			
			return clientHttpResponse;
		}

		if( authToken != null && !authToken.isEmpty() ) {
			request.getHeaders().addAll("Authorization", authToken);
			return execution.execute(request, body);
		}

		return execution.execute(request, body);
	}

}