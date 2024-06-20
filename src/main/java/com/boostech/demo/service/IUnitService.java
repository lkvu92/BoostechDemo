package com.boostech.demo.service;

import com.boostech.demo.dto.UnitDto;
import com.boostech.demo.entity.Unit;

import java.util.List;
import java.util.UUID;

public interface IUnitService {
    List<Unit> getAll();

    Unit getById(UUID id);

    Unit create(UnitDto unitDto);

    Unit update(UUID id, UnitDto unitDto);

    void delete(UUID id);
}
