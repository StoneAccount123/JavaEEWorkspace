package edu.whu.boot;


import edu.whu.entity.Goods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getAllTest() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/goods",
                Goods.class));
    }

    @Test
    public void getOneTest() throws Exception {
        Integer id = 1;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/goods"+id,
                Goods.class));
    }

    @Test
    public void saveOneTest() throws Exception {
        Goods goods = new Goods();
        goods.setName("新商品");
        goods.setDescription("10.17上架");
        goods.setPrice(12);
        assertThat(restTemplate.postForObject("http://localhost:" + port + "/goods",goods, Boolean.class));
    }

    @Test
    public void putTest() throws Exception {
        Goods goods = new Goods();
        goods.setId(1L);
        goods.setName("改变的商品");
        goods.setDescription("信息被修改");
        goods.setPrice(12);
        Assertions.assertDoesNotThrow(()->this.restTemplate.put("http://localhost:" + port + "/goods", goods));
    }

    @Test
    public void deleteTest() throws Exception {
        Assertions.assertDoesNotThrow(()->this.restTemplate.delete("http://localhost:" + port + "/goods/2"));
    }





}