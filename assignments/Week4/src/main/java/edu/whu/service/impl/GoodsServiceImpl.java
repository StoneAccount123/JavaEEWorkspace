package edu.whu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whu.entity.Goods;
import edu.whu.mapper.GoodsMapper;
import edu.whu.service.GoodsIService;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements GoodsIService {
}