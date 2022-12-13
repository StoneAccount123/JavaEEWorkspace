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
import whu.wz.jwtwork.service.SupplierService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cl
 * @since 2022-10-30
 */

@Api(description = "供货商管理")
@RestController
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    SupplierService supplierService;

    @ApiOperation("根据Hello World测试")
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                "Hello, World", headers, HttpStatus.OK);
    }
    @ApiOperation("根据Id查询供货商")
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplier(@ApiParam("供货商Id") @PathVariable Integer id) {
        Supplier result = supplierService.getSupplier(id);
        System.out.println(result);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        if(result == null){
            return new ResponseEntity<>(headers,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);

    }
    @ApiOperation("根据名称查找供货商")
    @GetMapping("")
    public ResponseEntity<List<Supplier>> findSupplier(@ApiParam("供货商名称")@RequestParam(value = "name") String name) {
        List<Supplier> result = supplierService.findSuppliers(name);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);
    }
    @ApiOperation("添加供货商")
    @PostMapping("")
    public ResponseEntity<Supplier> addSupplier(@RequestBody Supplier supplier){
        Supplier result = supplierService.addSupplier(supplier);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);
    }
    @ApiOperation("修改供货商")
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@ApiParam("供货商Id") @PathVariable Integer id,@ApiParam("修改后的供货商") @RequestBody Supplier supplier){
        Supplier result = supplierService.updateSupplier(id,supplier);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);
    }
    @ApiOperation("删除供货商")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@ApiParam("供货商Id") @PathVariable Integer id){
        String result = supplierService.deleteSupplier(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);
    }
    @ApiOperation("查找供货商id对应的商品详细信息")
    @GetMapping("/supplier/{id}")
    public ResponseEntity<List<Commodity>>getCommoditiesById(@ApiParam("供货商Id") @PathVariable Integer id)
    {
        List<Commodity> result = supplierService.findCommodities(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        return new ResponseEntity<>(
                result, headers, HttpStatus.OK);

    }
}


