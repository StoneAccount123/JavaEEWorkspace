package edu.whu.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Supplier{
    @Id
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    List<Goods> goods;

}
