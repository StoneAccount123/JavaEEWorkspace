package whu.wz.jwtwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whu.wz.jwtwork.domain.Commodity;
import whu.wz.jwtwork.domain.Supplier;
import whu.wz.jwtwork.service.CommodityService;

import java.util.List;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cl
 * @since 2022-10-30
 */



@Api(description = "商品管理")
@RestController
@RequestMapping("/commodity")
public class CommodityController {
    @Autowired
    CommodityService commodityService;

    @ApiOperation("根据Hello World测试")
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                "Hello, World", headers, HttpStatus.OK);
    }
    @ApiOperation("根据Id查询商品")
    @GetMapping("/{id}")
    public ResponseEntity<Commodity> getCommodity(@ApiParam("商品Id") @PathVariable Integer id) {
        Commodity result = commodityService.getCommodity(id);
        System.out.println(result);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if(result == null){
            return new ResponseEntity<>(headers,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);

    }
    @ApiOperation("根据名称查找商品")
    @GetMapping("")
    public ResponseEntity<List<Commodity>> findCommodity(@ApiParam("商品名称")@RequestParam(value = "name") String name) {
        List<Commodity> result = commodityService.findCommodities(name);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);
    }
    @ApiOperation("添加商品")
    @PostMapping("")
    public ResponseEntity<Commodity> addCommodity(@RequestBody Commodity commodity){
        Commodity result = commodityService.addCommodity(commodity);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);
    }
    @ApiOperation("修改商品")
    @PutMapping("/{id}")
    public ResponseEntity<Commodity> updateCommodity(@ApiParam("商品Id") @PathVariable Integer id,@ApiParam("修改后的商品") @RequestBody Commodity commodity){
        Commodity result = commodityService.updateCommodity(id,commodity);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);
    }
    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommodity(@ApiParam("商品Id") @PathVariable Integer id){
        String result = commodityService.deleteCommodity(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);
    }
    @ApiOperation("查找商品id对应的供货商详细信息")
    @GetMapping("/supplier/{id}")
    public ResponseEntity<Supplier>getSupplierById(@ApiParam("商品Id") @PathVariable Integer id)
    {
        Supplier result = commodityService.findSupplier(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if(result == null){
            return new ResponseEntity<>(headers,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);

    }
}


