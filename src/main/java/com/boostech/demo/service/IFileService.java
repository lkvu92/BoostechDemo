package com.boostech.demo.service;

import com.boostech.demo.dto.FileDto;
import com.boostech.demo.entity.File;

import java.util.List;
import java.util.UUID;

public interface IFileService {
    List<File> getAll();

    File getById(UUID id);

    File create(FileDto fileDto);

    File update(UUID id, FileDto fileDto);

    void delete(UUID id);
}
