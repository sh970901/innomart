package com.hun.market.backoffice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.hun.market.item.domain.Item;
import com.hun.market.item.repository.ItemRepository;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final ItemRepository itemRepository;

    public void saveFile(MultipartFile multipartFile, Long ItemId) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);

        Optional<Item> selectItem = itemRepository.findById(ItemId);

        selectItem.ifPresent(item -> {
            item.setImagePath(amazonS3.getUrl(bucket, originalFilename).toString());
            itemRepository.save(item); // 변경된 아이템을 저장
        });

    }

    public void deleteImage(String originalFilename)  {
        amazonS3.deleteObject(bucket, originalFilename);
    }

    public String getFileUrl(String filename){
        return amazonS3.getUrl(bucket, filename).toString();
    }
}
