package com.cobanoglu.valventask.service;

import com.cobanoglu.valventask.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate;

    public ExternalApiService() {
        this.restTemplate = new RestTemplate();
    }

    public String fetchDataFromApi(String url) {
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            throw new ApiException("Failed to fetch data from API: " + e.getMessage());
        }
    }
}
