package whu.edu.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.edu.entity.Supplier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author myAdministrator
* @description 针对表【supplier】的数据库操作Mapper
* @createDate 2022-10-25 18:54:55
* @Entity whu.edu.entity.Supplier
*/
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {

}




