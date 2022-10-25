package edu.whu.service;

import edu.whu.entity.Goods;

import java.util.List;

public interface GoodsService {
    public List<Goods>getAll();

    public void deleteById(Long id);

    public void add(Goods goods);

    public void updateById(Goods goods);

    /**
     * 查找商品名含有name 且价格大于price的商品
     * @param name 商品名
     * @param price 价格
     * @return
     */
    public List<Goods> getSelective(String name, double price);

    Goods getById(Long id);

    void saveOne(Goods goods);

}
