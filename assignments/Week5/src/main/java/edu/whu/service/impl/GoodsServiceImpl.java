package edu.whu.service.impl;

import edu.whu.dao.GoodsDao;
import edu.whu.entity.Goods;
import edu.whu.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsDao dao;

    @Override
    public List<Goods> getAll() {
        return dao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        dao.deleteById(id);
    }

    @Override
    public void add(Goods goods) {
        dao.save(goods);
    }

    @Override
    public void updateById(Goods goods) {
        dao.updateById(goods);
    }

    @Override
    public List<Goods> getSelective(String name, double price) {
        return dao.findAll((root,query,criteriaBuilder)->criteriaBuilder.and(
                criteriaBuilder.like(root.get("name"),name),
                criteriaBuilder.gt(root.get("price"),price)));
    }

    @Override
    public Goods getById(Long id) {
        return dao.getOne(id);
    }

    @Override
    public void saveOne(Goods goods) {
        dao.save(goods);
    }
}
