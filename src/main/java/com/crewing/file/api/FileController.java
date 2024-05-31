package com.crewing.file.api;

import com.crewing.file.service.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/file")
@Slf4j
@RequiredArgsConstructor
public class FileController {
    private final FileServiceImpl fileService;

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam String imgUrl){
        fileService.deleteFile(imgUrl);
        return "delete successful";
    }
}
