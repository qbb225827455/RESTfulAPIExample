package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

// 參考
// https://chikuwa-tech-study.blogspot.com/2021/05/spring-boot-jackson-annotation-basic.html
public class ObjectMapperTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Data
    private static class Book {
        private String id;
        private String name;
        private int price;
        private String isbn;
        private Date createTime;
        private Publisher publisher;
    }

    @Data
    private static class Publisher {
        private String companyName;
        private String address;
        private String telephoneNumber;
    }

    @Test
    public void testBookToJSON() throws Exception {

        Book book = new Book();
        book.setId("12345");
        book.setName("Test book");
        book.setPrice(1000);
        book.setIsbn("987-654-321-012-1");
        book.setCreateTime(new Date());

        String bookJSONString = objectMapper.writeValueAsString(book);
        JSONObject bookJSON = new JSONObject(bookJSONString);

        Assert.assertEquals(book.getId(), bookJSON.getString("id"));
        Assert.assertEquals(book.getName(), bookJSON.getString("name"));
        Assert.assertEquals(book.getPrice(), bookJSON.getInt("price"));
        Assert.assertEquals(book.getIsbn(), bookJSON.getString("isbn"));
        Assert.assertEquals(book.getCreateTime().getTime(), bookJSON.getLong("createTime"));
    }

    @Test
    public void testJSONToPublisher() throws Exception {

        JSONObject publisherJSON = new JSONObject()
                .put("companyName", "Taipei Company")
                .put("address", "Taipei")
                .put("telephoneNumber", "02-1234-5678");

        String publisherJSONString = publisherJSON.toString();
        Publisher publisher = objectMapper.readValue(publisherJSONString, Publisher.class);

        Assert.assertEquals(publisherJSON.getString("companyName"), publisher.getCompanyName());
        Assert.assertEquals(publisherJSON.getString("address"), publisher.getAddress());
        Assert.assertEquals(publisherJSON.getString("telephoneNumber"), publisher.getTelephoneNumber());
    }
}
