package com.boostech.demo.controller;

import com.boostech.demo.entity.File;
import com.boostech.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<?> getFiles() {
        List<File> fileList = fileService.getAll();
        return ResponseEntity.ok(fileList);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getFile(@PathVariable UUID id) {
        File filePath = fileService.getById(id);
        if (filePath == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(filePath);
    }

//    @PostMapping
//    public ResponseEntity<?> createFile() {
//        File newFile = new File();
//    }
}
