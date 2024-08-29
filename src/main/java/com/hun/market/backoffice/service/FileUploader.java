package com.hun.market.backoffice.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class FileUploader {

    private final String uploadPath; //업로드 주소는 일정(?)
    private String originalFileName;


    public FileUploader(MultipartFile file) throws IOException {
        //파일 패스읽고 생성하는 부분을 따로 함수로빼야겠음 
        this.uploadPath = getUploadPath();
        this.originalFileName = file.getOriginalFilename();
        file.transferTo(new File(uploadPath + getSavedFileName(this.originalFileName)));
    }
    
    public FileUploader(List<MultipartFile> files) throws IOException {
        this.uploadPath = getUploadPath();
        for (MultipartFile file : files) {
            this.originalFileName = file.getOriginalFilename();
            file.transferTo(new File(uploadPath + getSavedFileName(this.originalFileName)));
        }
    }
    

    private String getUploadPath() {
        //S3저장소 주소가 될것 추후에는 S3로부터 돌려받은 ImagePath만을 DB 에 저장하게 될것
        return "C:\\Users\\kim_sungsoo06\\Desktop\\ImageTest\\";
    }

    private String getSavedFileName(String originalFileName) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + "_" + originalFileName;
    }

}
