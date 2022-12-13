package whu.wz.jwtwork.dao;

import org.apache.ibatis.annotations.Mapper;
import whu.wz.jwtwork.domain.Commodity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cl
 * @since 2022-10-30
 */
@Mapper
public interface CommodityDao extends BaseMapper<Commodity> {

}
