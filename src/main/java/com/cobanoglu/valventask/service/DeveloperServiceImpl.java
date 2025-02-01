package com.cobanoglu.valventask.service;

import com.cobanoglu.valventask.model.Developer;
import com.cobanoglu.valventask.repository.DeveloperRepository;
import com.cobanoglu.valventask.service.DeveloperService;
import org.springframework.stereotype.Service;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    public DeveloperServiceImpl(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    @Override
    public Developer getDeveloper() {
        return developerRepository.findAll().stream().findFirst().orElse(null);
    }
}
