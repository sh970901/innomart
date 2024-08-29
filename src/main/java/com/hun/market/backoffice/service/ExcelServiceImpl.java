package com.hun.market.backoffice.service;

import com.hun.market.backoffice.dto.EmployeeExcelUploadDto;
import com.hun.market.backoffice.enums.ExcelUploadType;
import com.hun.market.item.domain.Item;
import com.hun.market.item.dto.ItemDto;
import com.hun.market.item.dto.ItemDto.ItemCreateRequestDto;
import com.hun.market.item.repository.ItemRepository;
import com.hun.market.member.domain.CoinTransHistory;
import com.hun.market.member.domain.Member;
import com.hun.market.member.dto.MemberDto;
import com.hun.market.member.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ExcelServiceImpl implements ExcelService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public ExcelServiceImpl(ItemRepository itemRepository, MemberRepository memberRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void uploadExcel(MultipartFile file, ExcelUploadType uploadType) throws IOException {

        if (ExcelUploadType.EMPLOYEE.equals(uploadType)) {
            List<EmployeeExcelUploadDto> resultDtoList = new OneSheetExcelUploader<>(EmployeeExcelUploadDto.class, file).getResultDtoList();
            //
            for (EmployeeExcelUploadDto excelUploadDto : resultDtoList) {
                Member member = Member.from(MemberDto.from(excelUploadDto));
                member.setPassword(excelUploadDto.getPassword());

                CoinTransHistory initHistory = CoinTransHistory.registByAdmin(member, excelUploadDto.getCoin().intValue());
                member.getCoinTransHistories().add(initHistory);

                memberRepository.save(member);

            }

        } else if (ExcelUploadType.ITEM.equals(uploadType)) {
            List<ItemDto.ItemCreateRequestDto> resultDtoList = new OneSheetExcelUploader<>(ItemDto.ItemCreateRequestDto.class, file).getResultDtoList();
            for (ItemCreateRequestDto itemCreateRequestDto : resultDtoList) {
                Item item = Item.from(itemCreateRequestDto);
                itemRepository.save(item);
            }
        }

    }

}
