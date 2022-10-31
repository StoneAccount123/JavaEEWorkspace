package whu.edu.entity;

import lombok.Data;

import java.util.List;

@Data
public class GoodsDto extends Goods{
    private List<Supplier> suppliers;
}
