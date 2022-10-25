package edu.whu.service.impl;

import edu.whu.dao.SupplierDao;
import edu.whu.entity.Supplier;
import edu.whu.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SupplierServiceImpl implements SupplierService {
    @Autowired
    SupplierDao dao;

    @Override
    public List<Supplier> getAll() {
        return dao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        dao.deleteById(id);
    }

    @Override
    public void saveOne(Supplier supplier) {
        dao.save(supplier);
    }

    @Override
    public void updateById(Supplier supplier) {
        dao.updateById(supplier);
    }

    @Override
    public Supplier getById(Long id) {
        return dao.getOne(id);
    }
}
