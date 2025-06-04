package org.modelio.module.intocps.traceability;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * Created by kel on 03/11/16.
 */
public class WebClient
{
	public static String get(String url) throws IOException
	{
		// Créer un client HTTP
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			// Créer une requête HTTP GET
			HttpGet request = new HttpGet(url);

			// Exécuter la requête
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				// Gérer la réponse
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, "UTF-8");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";

		//		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		//		try
		//		{
		//			HttpGet request = new HttpGet(url);
		//			
		//			
		//			HttpResponse response = httpClient.execute(request);
		//
		//			HttpEntity entity = response.getEntity();
		//
		//			return EntityUtils.toString(entity, "UTF-8");
		//		} finally
		//		{
		//			httpClient.getConnectionManager().shutdown(); // Deprecated
		//		}
	}

	public static void post(String url, String... data) throws IOException

	{
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try
		{

			for (String message : data)
			{
				HttpPost request = new HttpPost(url);
				StringEntity params = new StringEntity(message);
				request.addHeader("content-type", "application/json");
				request.setEntity(params);
				
				// Exécuter la requête
				try (CloseableHttpResponse response = httpClient.execute(request)) {
					// Gérer la réponse
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						System.out.println(EntityUtils.toString(entity, "UTF-8"));
					}
				}
				
//				HttpResponse response = httpClient.execute(request);
//
//				HttpEntity entity = response.getEntity();
//
//				String responseString = EntityUtils.toString(entity, "UTF-8");
//
//				System.out.println(responseString);

			}

		} finally
		{
			httpClient.getConnectionManager().shutdown(); // Deprecated
		}
	}


}
