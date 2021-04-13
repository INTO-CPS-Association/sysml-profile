package org.modelio.module.intocps.traceability;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.modelio.api.modelio.Modelio;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.impl.INTOCPSPeerModule;

/**
 * Created by kel on 03/11/16.
 */
public class WebClient
{
	public static String get(String url) throws IOException
	{
		HttpClient httpClient = HttpClientBuilder.create().build();
		try
		{
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);

			HttpEntity entity = response.getEntity();

			return EntityUtils.toString(entity, "UTF-8");
		} finally
		{
			httpClient.getConnectionManager().shutdown(); // Deprecated
		}
	}

	public static void post(String url, String... data) throws IOException

	{
		HttpClient httpClient = HttpClientBuilder.create().build();
		try
		{

			for (String message : data)
			{
				HttpPost request = new HttpPost(url);
				StringEntity params = new StringEntity(message);
				request.addHeader("content-type", "application/json");
				request.setEntity(params);
				HttpResponse response = httpClient.execute(request);

				HttpEntity entity = response.getEntity();

				String responseString = EntityUtils.toString(entity, "UTF-8");

				System.out.println(responseString);

			}

		} finally
		{
			httpClient.getConnectionManager().shutdown(); // Deprecated
		}
	}


}
