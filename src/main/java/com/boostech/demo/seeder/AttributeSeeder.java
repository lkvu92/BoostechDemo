package com.boostech.demo.seeder;

import com.boostech.demo.entity.Attribute;
import com.boostech.demo.repository.AttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AttributeSeeder {
    private final AttributeRepository repository;

    public void seed() {
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
}
