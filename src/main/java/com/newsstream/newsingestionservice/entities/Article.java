package com.newsstream.newsingestionservice.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Article {
    private String title;
    private String source_id;
    private List<String> category;
    private String publishDate;
    private String link;
    private List<String> keywords;
    private String description;
    private String content;

    public Article(JsonNode rawArticle) {
        this.title = rawArticle.get("title").toString();
        this.source_id = rawArticle.get("source_id").toString();
        this.category = this.parseStringList(rawArticle, "category"); // array
        this.publishDate = rawArticle.get("pubDate").toString();
        this.link = rawArticle.get("link").toString();
        this.keywords = this.parseStringList(rawArticle, "keywords"); // array
        this.description = rawArticle.get("description").toString();
        this.content = rawArticle.get("content").toString();
    }

    private List<String> parseStringList(JsonNode rawArticle, String key) {
        List<String> output = new ArrayList<>();
        JsonNode articleNode = rawArticle.get(key);
        if (!(articleNode instanceof NullNode)) {
            ArrayNode elementsToAdd = (ArrayNode) articleNode;

            for (JsonNode string : elementsToAdd) {
                output.add(string.toString());
            }
        }
        return output;
    }
}
