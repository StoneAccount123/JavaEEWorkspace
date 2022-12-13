package whu.wz.jwtwork;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import whu.wz.jwtwork.aspect.RestApiAspect;
import whu.wz.jwtwork.domain.Commodity;
import whu.wz.jwtwork.domain.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RestApiAspectTest {

    @LocalServerPort
    private int port;

    @Autowired

    RestApiAspect restApiAspect;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testApi(){
        String baseUrl="http://localhost:" + port;
        Supplier supplier=new Supplier();
        supplier.setName("name1");
        supplier.setTelphone("1234567890");
        supplier.setAddress("address1");
        Supplier supplier2=new Supplier();
        supplier2.setName("name2");
        supplier2.setTelphone("0987654321");
        supplier2.setAddress("address2");
        ResponseEntity<Supplier> response1 = restTemplate.postForEntity(baseUrl+"/supplier", supplier, Supplier.class);
        assertEquals(HttpStatus.OK,response1.getStatusCode());
        ResponseEntity<Supplier> response2 = restTemplate.postForEntity(baseUrl + "/supplier", supplier2, Supplier.class);
        assertEquals(HttpStatus.OK,response2.getStatusCode());
        Commodity commodity1 =new Commodity(null,"commodity1","type1",1.1,2,response1.getBody().getId());
        Commodity commodity2 =new Commodity(null,"commodity2","type2",1.1,3,response2.getBody().getId());

        ResponseEntity<Commodity> response3 = restTemplate.postForEntity(baseUrl + "/commodity", commodity1, Commodity.class);
        assertEquals(HttpStatus.BAD_REQUEST,response3.getStatusCode());


        ResponseEntity<Commodity> response4 = restTemplate.postForEntity(baseUrl + "/commodity", commodity2, Commodity.class);
        assertEquals(HttpStatus.OK,response4.getStatusCode());


        restApiAspect.getRecords().forEach((restApi, record)->{
            System.out.println("RESTAPI:"+ restApi);
            System.out.println(record);
        });
        System.out.println("---------------------------------------------");
    }


}

