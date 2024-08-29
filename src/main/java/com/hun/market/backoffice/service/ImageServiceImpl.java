package com.hun.market.backoffice.service;

import java.io.IOException;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService{


    @Override public void uploadImage(MultipartFile file) throws IOException {
       FileUploader fileUploader = new FileUploader(file);
    }

    @Override public void uploadImages(ArrayList<MultipartFile> file) throws IOException {
       FileUploader fileUploader = new FileUploader(file);
    }

}
