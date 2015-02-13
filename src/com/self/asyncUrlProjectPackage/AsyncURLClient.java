/**
 * Main class - demonstrates asynchronous requests and a gets a response back for further processing.
 * @author Navneet
 */

package com.self.asyncUrlProjectPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;



public class AsyncURLClient {
	
	private static final Object lock = new Object();
	
	
	public static void main(String args[])
	{
		final AsyncURLClient newObject = new AsyncURLClient();
		final ContextWrapper wrapper = new ContextWrapper();
		System.out.println("Please enter 5 urls in csv format");
		long startTime = System.currentTimeMillis();
		String newString = new String();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in)))
		{
			try
			{
			newString = br.readLine();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(newString==null||newString.equals(""))
		{
			System.out.println("Please check URL Entry-either null or empty");
			return;
		}
		else
		{
			try {
				newObject.asyncGetResponse(newString.trim(),wrapper);
			} catch (Exception e) {
				e.printStackTrace();
			}
			wrapper.setStatisticMap(new URLUtility().sortMapByValue(wrapper.getStatisticMap()));
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Total Time Taken: "+(endTime-startTime));
		}
	
	
	
	public void asyncGetResponse(String urls, final ContextWrapper wrapper ) throws Exception
	{
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
		try
		{
			httpClient.start();
			String requests[] = urls.split(",");
			final HttpGet [] requestArray = new HttpGet[requests.length]; 
			for(int i =0;i<requests.length;i++)
			{
				requestArray[i] = new HttpGet(requests[i].trim());
			}
			final CountDownLatch latch = new CountDownLatch(requests.length);
			for( final HttpGet request : requestArray)
			{
				httpClient.execute(request,  new FutureCallback<HttpResponse>() {

					@Override
					public void cancelled() {
						latch.countDown();
						System.out.println(request.getRequestLine()+" cancelled");
						
					}

					@Override
					public void completed(HttpResponse response) {
						latch.countDown();
						HttpEntity entity = response.getEntity();
						try {
							parseResponse(EntityUtils.toString(entity),wrapper.getStatisticMap());
						} catch (ParseException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					@Override
					public void failed(Exception ex) {
						latch.countDown();
						System.out.println(request.getRequestLine()+" : "+ex);
					}
					
				});
			}
			latch.await();
		}
		finally
		{
			
				httpClient.close();
			
		}
		
	}
	
	public void parseResponse(String line,HashMap<String,Integer>statisticMap)
	{
		String newline;
		try(BufferedReader br = new BufferedReader(new StringReader(line)))
		{
			while((newline=br.readLine())!=null)
			{
				newline = newline.trim();
				String tempBuffer[] = newline.split("\\s+");
				
				for(String key:tempBuffer)
				{
					if(!key.equals((String)("")))
					{
						synchronized(lock)
						{
							if(statisticMap.containsKey(key))
							{
								statisticMap.put(key, statisticMap.get(key)+1);
							}
							else
							{
								statisticMap.put(key, 1);
							}
						}
					
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("Size of Map is "+statisticMap.size());
		
	}
	
	
	
	
}
