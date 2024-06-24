package com.boostech.demo.seeder;

import com.boostech.demo.entity.Unit;
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

public class UnitSeeder {
    private final IUnitRepository repository;

    @Bean // This method creates a Spring bean that executes the seed logic
    public List<Unit> seedUnits() throws Exception {
        // Check if any units already exist (optional)
        List<Unit> units = repository.findAll();

        if (units.isEmpty()) {
            // Create sample Unit objects
            Unit length = new Unit();
            length.setId(UUID.randomUUID());
            length.setUnitName("millimetre");
            length.setUnitType("mm");

            Unit weight = new Unit();
            weight.setId(UUID.randomUUID());
            weight.setUnitName("gram");
            weight.setUnitType("g");

            Unit volume = new Unit();
            volume.setId(UUID.randomUUID());
            volume.setUnitName("liter");
            volume.setUnitType("l");

            // Save the Units to the database
            repository.save(length);
            repository.save(weight);
            repository.save(volume);
        }

        return List.of();
    }
}
