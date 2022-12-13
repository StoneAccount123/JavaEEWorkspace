package whu.wz.jwtwork.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import whu.wz.jwtwork.domain.User;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
