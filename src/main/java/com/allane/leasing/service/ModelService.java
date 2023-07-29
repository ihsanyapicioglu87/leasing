package com.allane.leasing.service;

import com.allane.leasing.model.Model;
import com.allane.leasing.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {

    private final ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<Model> getAllModels() {
        return modelRepository.findAll();
    }

    public Model getModelById(Long id) {
        return modelRepository.findById(id).orElse(null);
    }

    public Model createModel(Model model) {
        return modelRepository.save(model);
    }

    public Model updateModel(Model updatedModel) {
        Long id = updatedModel.getId();
        Model existingModel = modelRepository.findById(id).orElse(null);
        if (existingModel != null) {
            return modelRepository.save(updatedModel);
        }
        return null;
    }

    public boolean deleteModel(Long id) {
        Model existingModel = modelRepository.findById(id).orElse(null);
        if (existingModel != null) {
            modelRepository.delete(existingModel);
            return true;
        }
        return false;
    }

    public List<Model> getModelsByBrand(Long brandId) {
        return modelRepository.findByBrandId(brandId);
    }
}
