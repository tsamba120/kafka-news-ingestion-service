package com.newsstream.newsingestionservice.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProduceNewsEventsRequest {
    String country = "us";
    String language = "en";
    String category = "sports"; // alt. food // NEED TO HANDLE IF MULTI-WORD
//    String q = "baseball"; // alt. michelin
//    String page = "1";
}
