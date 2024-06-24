package com.boostech.demo.seeder;

import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.Unit;
import com.boostech.demo.repository.AttributeRepository;
import com.boostech.demo.repository.IUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Configuration
@Profile("dev")

public class AttributeSeeder {
    private final AttributeRepository repository;
    private final IUnitRepository unitRepository;

    @Bean // This method creates a Spring bean that executes the seed logic
    public List<Attribute> seedAttributes() {

        List<Attribute> attributes = repository.findAll();

        if(attributes.isEmpty()){
            Attribute length = new Attribute();
            length.setId(UUID.randomUUID());
            length.setAttributeName("Length");
            length.setActive(true);

            Attribute weight = new Attribute();
            weight.setId(UUID.randomUUID());
            weight.setAttributeName("Weight");
            weight.setActive(true);

            Attribute volume = new Attribute();
            volume.setId(UUID.randomUUID());
            volume.setAttributeName("Volume");
            volume.setActive(true);

            repository.save(length);
            repository.save(weight);
            repository.save(volume);
        }

        return List.of();
    }

}
