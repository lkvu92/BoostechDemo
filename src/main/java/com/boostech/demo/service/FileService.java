package com.boostech.demo.service;

import com.boostech.demo.dto.FileDto;
import com.boostech.demo.entity.File;
import com.boostech.demo.repository.IFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService implements IFileService {
    private final IFileRepository repository;

    public List<File> getAll() {
        return List.of();
    }

    @Override
    public File getById(UUID id) {
        return null;
    }

    public File create(FileDto fileDto) {
        return null;
    }

    public File update(UUID id, FileDto fileDto) {
        return null;
    }

    public void delete(UUID id) {

    }
}
