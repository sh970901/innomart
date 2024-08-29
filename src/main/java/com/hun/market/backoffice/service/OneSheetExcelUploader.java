package com.hun.market.backoffice.service;

import static java.lang.Long.parseLong;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class OneSheetExcelUploader<T> {

    private final XSSFWorkbook workbook;

    //실제 반환 되어야 하는 결과 리스트(excel -> dto)
    @Getter
    private final List<T> resultDtoList = new ArrayList<>();

    public OneSheetExcelUploader(Class<T> type, MultipartFile file) throws IOException {
        this.workbook = new XSSFWorkbook(file.getInputStream());
        excelToDto(type);
    }

    /*핵심 로직*/
    public void excelToDto(Class<T> type) throws IOException, RuntimeException {
        XSSFSheet xssfSheet = readExcel();
        //return 되어야 하는 리스트
        List<T> list = new ArrayList<>();
        try {
            /*해당 Dto의 모든 filed 가져오기*/

            for (int i = 1; i < xssfSheet.getPhysicalNumberOfRows(); i++) {
                /*인자로 받은 타입의 실제 Dto Instance*/
                Object object = type.getDeclaredConstructor().newInstance();
                /*인자로 받은 타입의 필드들*/
                Field[] fields = type.getDeclaredFields();

                DataFormatter dataFormatter = new DataFormatter();
                XSSFRow row = xssfSheet.getRow(i);

                for (int j = 0; j < fields.length; j++) {
                    // TODO
                    //이미지 필드에는 값을 할당 하지 않게(어노 테이션) 혹은 엑셀 파일에 빈 값으로 넣어놓기
                    /*Excel 의 column 수와 Dto의 Field 수는 일치*/
                    fields[j].setAccessible(true);
                    //타입체크 먼저
                    if ("java.lang.Long".equals(getFieldTypeName(fields[j]))) {
                        fields[j].set(object, parseLong(dataFormatter.formatCellValue(row.getCell(j))));
                    } else if ("java.lang.String".equals(getFieldTypeName(fields[j]))) {
                        fields[j].set(object, dataFormatter.formatCellValue(row.getCell(j)));
                    }
                }
                this.resultDtoList.add((T)object);
            }

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            /*에러 핸들링 어떻게 할지 고민*/
            throw new RuntimeException(e);
        }
    }

    private String getFieldTypeName(Field field) {
        return field.getType().getName();
    }

    private XSSFSheet readExcel() throws IOException {
        return getFirstSheet(this.workbook);
    }

    private XSSFSheet getFirstSheet(XSSFWorkbook workbook) {
        /*첫번째 시트 고정 읽기*/
        return workbook.getSheetAt(0);
    }

}
