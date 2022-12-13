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
public class SupplierService {
    @Autowired
    SupplierDao supplierMapper;
    @Autowired
    CommodityDao commodityMapper;
    public Supplier addSupplier(Supplier supplier) {
        supplierMapper.insert(supplier);
        return supplier;
    }

    public Supplier getSupplier(int id) {
        return supplierMapper.selectById(id);
    }


    public Supplier updateSupplier(int id, Supplier supplier) {
        supplierMapper.updateById(supplier);
        return supplier;
    }

    public String deleteSupplier(int id) {
        supplierMapper.deleteById(id);
        return "删除成功";
    }

    public List<Supplier> findSuppliers(String name) {
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        return supplierMapper.selectList(queryWrapper);
    }

    public List<Commodity> findCommodities(Integer id){
        Supplier supplier = supplierMapper.selectById(id);
        System.out.println(id);
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("supplier",supplier.getId());
        System.out.println(supplier.getId());
        List<Commodity> commodities = commodityMapper.selectList(queryWrapper);
        return commodities;
    }
}
