package com.example.demo;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// 參考
// https://chikuwa-tech-study.blogspot.com/2021/05/spring-boot-mockmvc-integration-test-1.html
public class ProductTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateProduct() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        JSONObject request = new JSONObject()
                .put("name", "AAA")
                .put("price", 99);

        // 建立請求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .headers(httpHeaders)
                .content(request.toString());

        // 發出模擬請求
        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                // 驗證狀態碼應為"201"
                .andExpect(MockMvcResultMatchers.status().isCreated())

                // jsonPath：獲取JSON指定欄位得值。以『＄』開始，使用『.』前往下一層的路徑。
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").hasJsonPath())

                // 驗證某個JSON欄位值為何。
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(request.getString("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(request.getString("price")))

                // 驗證回應標頭中的某欄位存在
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION))

                // 驗證回應標頭中的某欄位值為何
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }
}
