package org.dontpanic.spanners.springmvc.services;

import org.dontpanic.spanners.springmvc.domain.Spanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * Client of the Spanners-API REST service
 * Created by stevie on 08/06/16.
 */
@Service
public class SpannersService {

    @Autowired
    private RestTemplate restTemplate;

    private String resourceUrl;
    private String itemUrl;

    @Autowired
    public SpannersService(@Value("${app.service.url.spanners}") String resourceUrl) {
        this.resourceUrl = resourceUrl.startsWith("http") ?
                resourceUrl : "http://" + resourceUrl;
        this.itemUrl = this.resourceUrl + "/{0}";
    }


    public Collection<Spanner> findAll() {
        ResponseEntity<PagedResources<Spanner>> response = restTemplate.exchange(resourceUrl, HttpMethod.GET, null,
                                                            new ParameterizedTypeReference<PagedResources<Spanner>>(){});
        PagedResources<Spanner> pages = response.getBody();
        return pages.getContent();

    }


    public Spanner findOne(Long id) {
        return restTemplate.getForObject(itemUrl, Spanner.class, id);
    }


    public void delete(Spanner spanner) {
        restTemplate.delete(itemUrl, spanner.getId());
    }


    public void create(Spanner spanner) {
        restTemplate.postForObject(resourceUrl, spanner, Spanner.class);
    }


    public void update(Spanner spanner) {
        restTemplate.put(itemUrl, spanner, spanner.getId());
    }
}


