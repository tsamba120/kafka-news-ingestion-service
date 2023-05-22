package com.newsstream.newsingestionservice.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsstream.newsingestionservice.configs.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Client for the newsdata.io API
 * Possible detection parameters
 * Sports -> Dodgers
 * Food -> Michelin
 */
@Service
public class NewsApiClient {

    private final String apiKey;
    static final Logger logger = LoggerFactory.getLogger(NewsApiClient.class);

    private final String baseURL;

    URL url;
    HttpURLConnection con;
    Map<String, String> requestParameters;

    @Autowired
    public NewsApiClient(ApiConfig apiConfig) throws IOException {
        this.requestParameters = null;
        this.apiKey = apiConfig.getApiKey();
        this.baseURL = apiConfig.getApiUrl();
    }

    public void setConnection() throws IOException {
        this.con = (HttpURLConnection) this.url.openConnection();
    }

    public void setGetRequest() throws ProtocolException {
        this.con.setRequestMethod("GET");
    }

    private void setApiKey(String ApiKey) {
        this.addRequestHeader("X-ACCESS-KEY", ApiKey);
    }

    public void addRequestParameter(String key, String value) {
        if (this.requestParameters == null) {
            this.requestParameters = new HashMap<>();
        }

        this.requestParameters.put(key, value);
    }

    public void addParametersFromMap(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.addRequestParameter(entry.getKey(), entry.getValue());
        }
    }

    public void addRequestHeader(String key, String value) {
        this.con.setRequestProperty(key, value);
    }

    public String getRequestHeaderField(String key) {
        return this.con.getHeaderField(key);
    }

    public int executeGetRequest() throws IOException {

        if (this.requestParameters != null) {
            String parameterizedUrl = ParameterStringBuilder.getParamsString(
                    this.baseURL,
                    this.requestParameters
            );
            this.url = new URL(parameterizedUrl);
        }

        this.setConnection();
        this.setApiKey(apiKey);
        this.setGetRequest();
        int status = con.getResponseCode();
        return status;
    }

    public JsonNode readJsonResponse() throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(in);
        in.close();

        return node;
    }


    public void closeConnection() {
        this.con.disconnect();
    }

}
