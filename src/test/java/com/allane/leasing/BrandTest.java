package com.allane.leasing;

import com.allane.leasing.model.Brand;
import com.allane.leasing.repository.BrandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BrandTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void testSaveBrand() {
        Brand brand = new Brand();
        brand.setName("BMW");

        Brand savedBrand = brandRepository.save(brand);

        assertNotNull(savedBrand.getId());
        assertEquals(brand.getName(), savedBrand.getName());
    }


    @Test
    public void testDeleteBrand() {
        Brand brand = new Brand();
        brand.setName("Audi");
        brandRepository.save(brand);

        brandRepository.delete(brand);

        assertFalse(brandRepository.findById(brand.getId()).isPresent());
    }

    @Test
    public void testUniqueConstraintOnBrandName() {
        Brand brand1 = new Brand();
        brand1.setName("Lamborghini");
        brandRepository.save(brand1);

        Brand brand2 = new Brand();
        brand2.setName("Lamborghini");

        assertThrows(DataIntegrityViolationException.class, () -> brandRepository.save(brand2));
    }

    @Test
    public void testGetAllBrands() {
        Brand brand1 = new Brand();
        brand1.setName("Nissan");
        brandRepository.save(brand1);

        Brand brand2 = new Brand();
        brand2.setName("Honda");
        brandRepository.save(brand2);

        List<Brand> allBrands = brandRepository.findAll();

        assertEquals(2, allBrands.size());
    }

}
