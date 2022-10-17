package edu.whu.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_goods")
@Data
public class Goods {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String name;

    private double price;


    private String description;
}
