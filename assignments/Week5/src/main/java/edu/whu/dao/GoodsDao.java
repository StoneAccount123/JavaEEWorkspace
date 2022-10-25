package edu.whu.dao;

import edu.whu.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsDao extends JpaRepository<Goods,Long>, JpaSpecificationExecutor<Goods> {
    //SQL查询
    @Query("update Goods g set g.name = #{#goods.name} ,g.description = #{#goods.description },g.price = #{#goods.price} where g.id=#{#goods.id}")
    public void updateById(Goods goods);

    /**
     * 按照方法名生成的查询语句
     * @param name 商品名
     */
    public Goods findGoodsByName(String name);

    /**
     * 按照方法名生成的模糊查询语句
     * @param name 商品名
     */
    public List<Goods> findByNameContaining(String name);

    //JPQL查询
    @Query("select g from Goods g where g.name like %?1% and g.price>?2")
    public List<Goods> findWitJPQLByNameAndMinprice(String name,double price);

    //分页查询
    public Page<Goods> findByNameContaining(String name, Pageable pageable);

    public Page<Goods>findAll(String name, Pageable pageable);

}