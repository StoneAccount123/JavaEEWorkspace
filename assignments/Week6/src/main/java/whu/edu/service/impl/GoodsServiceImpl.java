package whu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import whu.edu.entity.Goods;
import whu.edu.entity.GoodsDto;
import whu.edu.entity.Supplier;
import whu.edu.service.GoodsService;
import whu.edu.mapper.GoodsMapper;
import org.springframework.stereotype.Service;
import whu.edu.service.SupplierService;

import java.util.List;

/**
* @author myAdministrator
* @description 针对表【t_goods】的数据库操作Service实现
* @createDate 2022-10-25 18:46:49
*/
@Transactional
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

    @Autowired
    SupplierService supplierService;

    @Override
    public GoodsDto getWithSuppliers(Long id) {
        //查询商品数据 并赋值给GoodsDto
        Goods goods = this.getById(id);
        if(goods==null){
            return null;
        }
        GoodsDto goodsDto = new GoodsDto();
        BeanUtils.copyProperties(goods,goodsDto);

        //根据GoodId 查询Supplierlist
        LambdaQueryWrapper<Supplier> supplierQueryWrapper = new LambdaQueryWrapper<>();
        supplierQueryWrapper.eq(Supplier::getGoodId,goods.getId());
        List<Supplier> list = supplierService.list(supplierQueryWrapper);
        goodsDto.setSuppliers(list);
        return goodsDto;
    }
}




