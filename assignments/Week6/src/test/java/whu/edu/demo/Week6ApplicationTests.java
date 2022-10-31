package whu.edu.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import whu.edu.entity.GoodsDto;
import whu.edu.service.GoodsService;

@SpringBootTest
class Week6ApplicationTests {

    @Autowired
    GoodsService goodsService;

    @Test
    void testGetOne() {
        Assertions.assertNotNull(goodsService.getById(1L));
    }

    @Test
    void testGetWithSuppliers(){
        GoodsDto goodsDto = goodsService.getWithSuppliers(1L);
        Assertions.assertNotNull(goodsDto);
        Assertions.assertEquals(3,goodsDto.getSuppliers().size());
    }

}
