package com.hun.market.backoffice.controller;

import com.hun.market.backoffice.dto.CoinProvideRequestDto;
import com.hun.market.backoffice.dto.EmployeeExcelUploadDto;
import com.hun.market.backoffice.dto.ItemModifyDto;
import com.hun.market.backoffice.dto.OrderPossYnResponse;
import com.hun.market.backoffice.enums.ExcelUploadType;
import com.hun.market.backoffice.service.ExcelService;
import com.hun.market.backoffice.service.ImageService;
import com.hun.market.backoffice.service.S3UploadService;
import com.hun.market.item.dto.ItemDto;
import com.hun.market.item.dto.ItemDto.ItemCreateRequestDto;
import com.hun.market.item.service.ItemService;
import com.hun.market.member.dto.MemberDto;
import com.hun.market.member.service.MemberService;
import com.hun.market.order.order.repository.OrderPossRepository;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/backoffice")
public class BackOfficeApiController {

    private final ExcelService excelService;
    private final ImageService imageService;
    private final ItemService itemService;
    private final MemberService memberService;
    private final S3UploadService s3UploadService;

    private final OrderPossRepository orderPossRepository;

    @PostMapping("/upload/employees")
    public RedirectView uploadExcel(@RequestParam("employees") MultipartFile file) throws IOException {
        excelService.uploadExcel(file, ExcelUploadType.EMPLOYEE);

        return new RedirectView("/backoffice/home");
    }

    @PostMapping("/upload/image")
    public void uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        imageService.uploadImage(file);
    }

    @PostMapping("/upload/images")
    public void uploadMultiImage(@RequestParam("images") ArrayList<MultipartFile> file) throws IOException {
        imageService.uploadImages(file);
    }

    @PostMapping("/upload/items")
    public RedirectView uploadMultiItems(@RequestParam("items") MultipartFile file) throws IOException {
        excelService.uploadExcel(file, ExcelUploadType.ITEM);

        return new RedirectView("/backoffice/home");
    }

    @PostMapping("/modify/item")
    public void updateItem(@Valid @RequestBody ItemModifyDto itemModifyDto) {
        itemService.updateItem(itemModifyDto);
    }

    // 코인 지급은 단독, 일괄 관련 없음(리스트로 받음)
    @PostMapping("/provide/coin")
    public void provideCoin(@Valid @RequestBody CoinProvideRequestDto coinProvideRequestDto) {
        memberService.provideCoin(coinProvideRequestDto);
    }

    @DeleteMapping("/employee/{memberId}")
    public void deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
    }

    @DeleteMapping("/item/{itemNo}")
    public void deleteItem(@PathVariable Long itemNo) {

        itemService.deleteItem(itemNo);
    }

    @DeleteMapping("/item/delete")
    public void deleteAllItem() {
        itemService.deleteAllItem();
    }

    @PostMapping("/item/one")
    public void createOneItem(@RequestBody ItemCreateRequestDto itemCreateRequestDto) {
        itemService.createOneItem(itemCreateRequestDto);
    }

    @PostMapping("/employee/one")
    public void createOneMember(@RequestBody EmployeeExcelUploadDto excelUploadDto) {
        memberService.createOneMember(excelUploadDto);
    }


    @GetMapping("/employee/{memberId}")
    public MemberDto.MemberResponseDto getMember(@PathVariable Long memberId) {
        return memberService.getMember(memberId);
    }

    @GetMapping("/items")
    public List<ItemDto.ItemCreatResponseDto> getAllItems() {
        return itemService.getAllItems();
    }

    /**
     * S3 TEST
     * @param multipartFile
     * @throws IOException
     */
    @PostMapping("/s3/upload")
    public void s3Upload(@RequestParam("file") MultipartFile multipartFile,  @RequestParam("itemId") Long itemId) throws IOException {
        s3UploadService.saveFile(multipartFile, itemId);
    }

    @GetMapping("/orderposs")
    public OrderPossYnResponse checkOrderPoss() {
        return OrderPossYnResponse.builder().
                                   possYn(orderPossRepository.findByIdAndOrderPossYn(1L, "Y").get().getOrderPossYn()).build();
    }

    @PostMapping("/change/{state}")
    public void changeOrderPoss(@PathVariable String state) {
        orderPossRepository.findById(1L)
                           .ifPresent(orderPoss -> {
                               orderPoss.setState(state);
                               orderPossRepository.save(orderPoss);
                           });
    }

}
