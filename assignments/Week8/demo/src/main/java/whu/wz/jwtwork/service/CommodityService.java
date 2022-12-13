package whu.wz.jwtwork.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.wz.jwtwork.dao.CommodityDao;
import whu.wz.jwtwork.dao.SupplierDao;
import whu.wz.jwtwork.domain.Commodity;
import whu.wz.jwtwork.domain.Supplier;

import java.util.List;

@Service
public class CommodityService {
    @Autowired
    CommodityDao commodityMapper;
    @Autowired
    SupplierDao supplierMapper;
    public Commodity addCommodity(Commodity commodity) {
        int id = commodityMapper.insert(commodity);
        return commodity;
    }

    public Commodity getCommodity(int id) {
        return commodityMapper.selectById(id);
    }


    public Commodity updateCommodity(int id, Commodity commodity) {
        commodityMapper.updateById(commodity);
        return commodity;
    }

    public String deleteCommodity(int id) {
        commodityMapper.deleteById(id);
        return "删除成功";
    }

    public List<Commodity> findCommodities(String name) {
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        return commodityMapper.selectList(queryWrapper);
    }
    public Supplier findSupplier(Integer id){
        Commodity commodity = commodityMapper.selectById(id);
        Supplier supplier = supplierMapper.selectById(commodity.getSupplier());
        return supplier;
    }


}
