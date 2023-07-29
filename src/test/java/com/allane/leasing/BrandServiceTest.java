package com.allane.leasing;

import com.allane.leasing.model.Brand;
import com.allane.leasing.repository.BrandRepository;
import com.allane.leasing.service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BrandServiceTest {

    @Autowired
    private BrandRepository brandRepository;

    @Mock
    private TestEntityManager testEntityManager;

    @InjectMocks
    private BrandService brandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveBrand() {
        Brand brand = new Brand();
        brand.setName("BMW");

        when(brandRepository.save(any(Brand.class))).thenAnswer(invocation -> {
            Brand brandToSave = invocation.getArgument(0);
            brandToSave.setId(1L);
            return brandToSave;
        });

        Brand savedBrand = brandService.createBrand(brand);

        assertNotNull(savedBrand.getId());
        assertEquals(brand.getName(), savedBrand.getName());

        verify(brandRepository, times(1)).save(any(Brand.class));
    }


}
