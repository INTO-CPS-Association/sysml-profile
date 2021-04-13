package org.modelio.module.intocps.traceability;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by CasperThule on 2015-10-15.
 */
public class ServerRequester {
    final static ObjectMapper mapper = new ObjectMapper();

    final static String daemonURL = "http://localhost:8083/";

    public static JsonNode PerformPostRequest(byte[] data, URL url) throws Exception {

        HttpURLConnection urlConn;
        urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setDoOutput(true); //Makes it a POST request
        urlConn.addRequestProperty("Content-Type", "application/json");
        urlConn.setRequestProperty("Content-Length", Integer.toString(data.length));
        urlConn.getOutputStream().write(data);

        BufferedReader br = new BufferedReader(new InputStreamReader((urlConn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);}

        urlConn.disconnect();

        //Removing [} around json data
        String response = sb.substring(1, sb.length()-1);
        return mapper.readTree(response);
    }



    public static JsonNode InvokeSimulate(double startTime, double endTime, int sessionId) throws Exception {
        Map<String,Double> startEndTime = new HashMap<>();

        startEndTime.put("startTime", startTime);

        startEndTime.put("endTime", endTime);

        String startEndTimeJson = new ObjectMapper().writeValueAsString(startEndTime);

        byte[] data = startEndTimeJson.getBytes();

        URL url = new URL(daemonURL + "simulate/" +sessionId);

        return PerformPostRequest(data, url);
    }


    public static String invokeResult(int sessionId) throws Exception {

        HttpURLConnection urlConn;
        URL mUrl = new URL(daemonURL + "result/" + sessionId);

        urlConn = (HttpURLConnection) mUrl.openConnection();

        urlConn.addRequestProperty("Content-Type", "text/plain");

        BufferedReader br = new BufferedReader(new InputStreamReader((urlConn.getInputStream())));
        ArrayList<String> outputArray = new ArrayList<>();
        String output;
        while ((output = br.readLine()) != null) {
            outputArray.add(output);
        }

        urlConn.disconnect();
        //Csv format
        return String.join("\n", outputArray);
    }

    public static void invokeStop(int sessionId) throws Exception {

        HttpURLConnection urlConn;
        URL mUrl = new URL(daemonURL + "destroy/" + sessionId);

        urlConn = (HttpURLConnection) mUrl.openConnection();

        urlConn.addRequestProperty("Content-Type", "text/plain");

        urlConn.disconnect();

    }

    public static void sendMessage(String content) throws Exception {

        HttpURLConnection urlConn;
        URL mUrl = new URL(daemonURL + "destroy/" + content);

        urlConn = (HttpURLConnection) mUrl.openConnection();

        urlConn.addRequestProperty("Content-Type", "text/plain");

        urlConn.disconnect();

    }
}
