package com.hun.market.backoffice.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class OneFileUpload {

    public OneFileUpload(MultipartFile file) throws IOException {

        String uploadPath = getUploadPath();
        String originalFileName = file.getOriginalFilename();
        File newFile = new File(uploadPath + getSavedFileName(originalFileName));
        file.transferTo(newFile);

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
