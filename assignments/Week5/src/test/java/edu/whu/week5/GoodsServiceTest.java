package edu.whu.week5;

import edu.whu.service.GoodsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GoodsServiceTest {
    @Autowired
    GoodsService service;

    @Test
    void testGet(){
        Assertions.assertNotNull(service.getAll());
        Assertions.assertNotNull(service.getById(1L));
    }
}
