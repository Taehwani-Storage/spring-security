package com.bit.security.controller;

import org.springframework.ui.Model;
import com.bit.security.model.FileDTO;
import com.bit.security.model.UserDTO;
import com.bit.security.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

// RestController -> text 응답으로 보내줘, API 서버를 만들 때 사용(백엔드와 프론트 분리되었을 때 사용)
@Controller
@RequestMapping("/board/")
public class BoardController {
    @Value("${upload.path}")
    private String path;

    @Autowired
    private FileService fileService;

    @ResponseBody
    @GetMapping("showAll")
    public String showAll(@AuthenticationPrincipal UserDTO userDTO) {
        // @AuthenticationPrincipal 은
        // SecurityContextHolder.getContext().getAuthentication(); 와 같음

        return "boardController.showAll()";
    }

    @ResponseBody
    @GetMapping("write")
    public String write(@AuthenticationPrincipal UserDTO userDTO) {
        System.out.println(userDTO);

        return "boardController.write()";
    }

    @GetMapping("upload")
    public String upload() {
        System.out.println(this.path);
        return "board/upload";
    }

    @PostMapping("upload")
    // @ResquestParam -> 파일을 불러올 때 어떤 파일을 넘겨 받아야 하는지 체크
    public String upload(@RequestParam("file") List<MultipartFile> files) throws IOException {
        // 업로드할 폴더를 C드라이브 안에 upload 폴더로 지정
        // String path = "c://upload/";
        // application.properties 안에 upload.path 라는 속성 값을 만들고
        // 필요하면 @Value("${upload.path})로 해당 속성 값을 불러옴

        // 만약 해당 폴더가 존재하지 않으면 해당 폴더를 만듦
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ## 1개 파일만 업로드시키는 코드 ##
        // 파라미터에 @RequestParam("file") MultiparFile f
        // 업로드할 파일을 어떤 이름으로 어디에 저장할지 지정
        // File result = new File(path + f.getOriginalFilename());
        // 지정된 경로에 해당 파일 내용을 MultipartFile f 변수의 내용으로 복사
        // f.transferTo(result);

        // 여러개의 파일을 업로드하는 코드
        // 단, 여러개의 파일을 업로드 할 때, form태크 안에 input file 태크에 반드시 multiple="multiple" 어트리뷰트가 적혀있어야 하고
        // 메소드의 파라미터에는 MultipartFile 파라미터가 아닌 List<MultipartFile> 파라미터가 필요!!

        for (MultipartFile f : files) {
            // UUID: Universal Unique ID (범용 고유 식별자)
            // 프로젝트 전역에서 사용할 수 있는 고유한 식별아이디
            // 우리가 업로드한 파일 이름을 UUID로 바꿔서 저장하면 해당 파일이 중복될 확률 없앰
            // 1. 현재 파일의 확장자 가져오기
            String ext = f.getOriginalFilename().substring(f.getOriginalFilename().lastIndexOf('.'));
            System.out.println(ext);
            // 2. 해당 파일 업로드할 떄 사용할 UUID 생성
            String uuid = UUID.randomUUID().toString();

            // DB에 저장하는 코드
            // 단, 파일 설명에 들어갈 원본 파일 이름도 적음
            FileDTO fileDTO = new FileDTO();
            fileDTO.setFileName(f.getOriginalFilename());
            fileDTO.setFilePath(uuid + ext);

            fileService.insert(fileDTO);

            // 3. 해당 이름으로 파일을 업로드함
            File result = new File(path + uuid + ext);
            f.transferTo(result);
        }

        return "redirect:/board/download";
    }

    @GetMapping("download")
    public String download(Model model) {
        List<FileDTO> list = fileService.selectAll();
        model.addAttribute("list", list);
        model.addAttribute("path", path);

        return "board/download";
    }
}




