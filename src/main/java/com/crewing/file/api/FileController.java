package com.crewing.file.api;

import com.crewing.file.service.FileServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "File", description = "파일 API(백엔드 전용,프론트 x)")
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
