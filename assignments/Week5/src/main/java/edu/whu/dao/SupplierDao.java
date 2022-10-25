package edu.whu.dao;

import edu.whu.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SupplierDao  extends JpaRepository<Supplier,Long>, JpaSpecificationExecutor<Supplier> {
    @Query("update Supplier s set s.name = #{#supplier.name} where s.id=#{#supplier.id}")
    public void updateById(Supplier supplier);

}
