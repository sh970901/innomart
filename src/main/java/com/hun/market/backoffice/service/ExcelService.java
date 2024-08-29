package com.hun.market.backoffice.service;

import com.hun.market.backoffice.enums.ExcelUploadType;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {

    void uploadExcel(MultipartFile file, ExcelUploadType uploadType) throws IOException;
}
