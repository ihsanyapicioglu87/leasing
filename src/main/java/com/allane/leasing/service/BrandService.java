package com.allane.leasing.service;

import com.allane.leasing.model.Brand;
import com.allane.leasing.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand updateBrand(Brand updatedBrand) {
        Long id = updatedBrand.getId();
        Brand existingBrand = brandRepository.findById(id).orElse(null);
        if (existingBrand != null) {
            return brandRepository.save(updatedBrand);
        }
        return null;
    }

    public boolean deleteBrand(Long id) {
        Brand existingBrand = brandRepository.findById(id).orElse(null);
        if (existingBrand != null) {
            brandRepository.delete(existingBrand);
            return true;
        }
        return false;
    }
}
