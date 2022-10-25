package edu.whu.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_goods")
public class Goods {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private Double price;

    private String description;
}
