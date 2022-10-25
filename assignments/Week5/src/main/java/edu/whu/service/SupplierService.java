package edu.whu.service;

import edu.whu.entity.Supplier;

import java.util.List;

public interface SupplierService {
    public List<Supplier> getAll();

    public void deleteById(Long id);

    public void saveOne(Supplier supplier);

    public void updateById(Supplier supplier);

    public Supplier getById(Long id);
}
