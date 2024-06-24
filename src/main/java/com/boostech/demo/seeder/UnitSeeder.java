package com.boostech.demo.seeder;

import com.boostech.demo.entity.Unit;
import com.boostech.demo.repository.IUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UnitSeeder {
    private IUnitRepository repository;

    public void seed() {
        // Create sample Unit objects
        Unit length = new Unit();
        length.setId(UUID.randomUUID());
        length.setUnitName("mm");
        length.setUnitType("length");

        Unit weight = new Unit();
        weight.setId(UUID.randomUUID());
        weight.setUnitName("gram");
        weight.setUnitType("weight");

        Unit volume = new Unit();
        volume.setId(UUID.randomUUID());
        volume.setUnitName("L");
        volume.setUnitType("volume");

        // Save the Units to the database
        repository.save(length);
        repository.save(weight);
        repository.save(volume);
    }
}
