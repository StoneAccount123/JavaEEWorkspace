package edu.whu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.whu.entity.Goods;
import edu.whu.service.GoodsIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    GoodsIService service;

    @GetMapping("/goods")
    public ResponseEntity<List<Goods>> getAll(){
        List<Goods> list = service.list(null);
        if(list==null||list.size()==0)return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/goods/{id}")
    public ResponseEntity<Goods> getById(@PathVariable("id")Long id){
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getId,id);
        Goods res = service.getOne(wrapper);
        if(res == null)return ResponseEntity.noContent().build();
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/goods/{id}")
    public ResponseEntity<Boolean> removeById(@PathVariable("id")Long id){
        boolean res = service.removeById(id);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/goods")
    public ResponseEntity<Boolean> save(Goods goods){
        boolean res = service.save(goods);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/goods")
    public ResponseEntity<Boolean> update(Goods goods){
        boolean res = service.updateById(goods);
        return ResponseEntity.ok(res);
    }

}
