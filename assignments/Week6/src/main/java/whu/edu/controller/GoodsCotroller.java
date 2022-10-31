package whu.edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import whu.edu.common.MyException;
import whu.edu.entity.Goods;
import whu.edu.entity.GoodsDto;
import whu.edu.service.GoodsService;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsCotroller {
    @Autowired
    GoodsService service;

    /**
     * 通过商品名模糊查询
     *
     * @param name 商品名
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<Goods>>getByName(String name){
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(name),Goods::getName,name);
        wrapper.orderByAsc(Goods::getPrice);
        List<Goods> list = service.list(wrapper);
        if(list.size()==0){
            throw new MyException("没有记录");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goods> getByID(Long id){
        Goods goods = service.getById(id);
        if(goods==null){
            throw new MyException("没有记录");
        }
        return ResponseEntity.ok(goods);
    }

    @GetMapping("")
    public ResponseEntity<GoodsDto> getWithSuppliers(Long id){
        GoodsDto goodsDto = service.getWithSuppliers(id);
        if(goodsDto==null){
            throw new MyException("没有记录");
        }
        return ResponseEntity.ok(goodsDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByID(Long id){
        service.removeById(id);
        return ResponseEntity.ok("删除成功");
    }

    @PostMapping
    public ResponseEntity<String> save(Goods goods){
        service.save(goods);
        return ResponseEntity.ok("保存成功");
    }

    @PutMapping
    public ResponseEntity<String> update(Goods goods){
        service.updateById(goods);
        return ResponseEntity.ok("更新成功");
    }
}