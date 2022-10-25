package edu.whu.controller;

import edu.whu.entity.Goods;
import edu.whu.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService service;

    @GetMapping
    public ResponseEntity<List<Goods>> getAll(){
        List<Goods> goods = service.getAll();
        if(null == goods){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(goods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goods>getByID(@PathVariable("id")Long id){
        Goods goods = service.getById(id);
        if(null == goods){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(goods);
    }

    @PostMapping
    public ResponseEntity<Void>saveOne(Goods goods){
        service.saveOne(goods);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void>deleteById(Long id){
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void>updateById(Goods goods){
        service.updateById(goods);
        return ResponseEntity.ok().build();
    }
}
