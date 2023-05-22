package com.newsstream.newsingestionservice.api;

import com.newsstream.newsingestionservice.controller.NewsArticlesController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Utility class for building a request object for an
 * HTTP request.
 */
class ParameterStringBuilder {
    static final Logger logger = LoggerFactory.getLogger(NewsArticlesController.class);
    static String getParamsString(String baseUrl, Map<String, String> params)
        throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        result.append(baseUrl);
        result.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {

            result.append(URLEncoder.encode(
                            entry.getKey(),
                            StandardCharsets.UTF_8.toString()
                    )
            );
            result.append("=");
            result.append(URLEncoder.encode(
                            entry.getValue(),
                            StandardCharsets.UTF_8.toString()
                    )
            );
            result.append("&");
        }

        String resultString = result.toString();
        int length = resultString.length();

        return length > 0
                ? resultString.substring(0, length - 1)
                : resultString;
    }
}
