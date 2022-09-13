package com.example.demo;

import com.example.demo.Model.Product.Product;
import com.example.demo.Repository.ProductRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// 參考
// https://chikuwa-tech-study.blogspot.com/2021/05/spring-boot-mockmvc-integration-test-1.html
public class ProductTest {

    private HttpHeaders httpHeaders;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo productRepo;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @After
    public void clear() {
        productRepo.deleteAll();
    }

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

    @Test
    public void testGetProduct() throws Exception {

        Product product = initProduct("testGet", 100);
        productRepo.insert(product);

        // 建立請求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/" + product.getId())
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void testReplaceProduct() throws Exception {

        Product product = initProduct("testReplace", 101);
        productRepo.insert(product);

        JSONObject request = new JSONObject()
                .put("name", "replace?")
                .put("price", 101101);

        // 建立請求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/" + product.getId())
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(request.getString("name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(request.getString("price")));
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteProduct() throws Exception {

        Product product = initProduct("testDelete", 102);
        productRepo.insert(product);

        // 建立請求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/" + product.getId())
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        productRepo.findById(product.getId())
                .orElseThrow(RuntimeException::new);
    }

    @Test
    public void testSearchProductsSortByPriceAsc() throws Exception {

        Product p1 = initProduct("1111", 1000);
        Product p2 = initProduct("1222", 2000);
        Product p3 = initProduct("1333", 3000);
        Product p4 = initProduct("1444", 4000);
        Product p5 = initProduct("1555", 5000);
        productRepo.insert(Arrays.asList(p1, p2, p3, p4, p5));

        // 建立請求
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/")
                .headers(httpHeaders)
                .param("keyword", "1")
                .param("orderBy", "price")
                .param("sortRule", "asc");

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String responseJSONString = mockHttpServletResponse.getContentAsString();
        JSONArray productJSONArray = new JSONArray(responseJSONString);

        List<String> productIds = new ArrayList<>();
        for (int i = 0; i < productJSONArray.length(); i ++) {
            JSONObject productJSON = productJSONArray.getJSONObject(i);
            productIds.add(productJSON.getString("id"));
        }

        // 驗證資料
        Assert.assertEquals(5, productIds.size());
        Assert.assertEquals(p1.getId(), productIds.get(0));
        Assert.assertEquals(p2.getId(), productIds.get(1));
        Assert.assertEquals(p3.getId(), productIds.get(2));
        Assert.assertEquals(p4.getId(), productIds.get(3));
        Assert.assertEquals(p5.getId(), productIds.get(4));

        Assert.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE,
                mockHttpServletResponse.getHeader(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    public void getBadRequestWtihEmptyName() throws Exception {

        JSONObject request = new JSONObject()
                .put("name", "")
                .put("price", 123);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getBadRequestWtihNegativePrice() throws Exception {

        JSONObject request = new JSONObject()
                .put("name", "Test")
                .put("price", -123);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .headers(httpHeaders);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private Product initProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
