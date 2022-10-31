package whu.edu.service;

import whu.edu.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import whu.edu.entity.GoodsDto;

/**
* @author myAdministrator
* @description 针对表【t_goods】的数据库操作Service
* @createDate 2022-10-25 18:46:49
*/
public interface GoodsService extends IService<Goods> {

    GoodsDto getWithSuppliers(Long id);
}
