package whu.edu.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.edu.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author myAdministrator
* @description 针对表【t_goods】的数据库操作Mapper
* @createDate 2022-10-25 18:46:49
* @Entity whu.edu.entity.Goods
*/
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}