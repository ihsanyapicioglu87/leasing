package com.allane.leasing.config;

import com.allane.leasing.repository.BrandRepository;
import com.allane.leasing.service.BrandService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.allane.leasing.repository")
@PropertySource("classpath:application-test.properties")
@EnableTransactionManagement
@Import(BrandRepository.class)
public class BrandTestConfig {

    @Bean
    public BrandService brandService(BrandRepository brandRepository) {
        return new BrandService(brandRepository);
    }

}
