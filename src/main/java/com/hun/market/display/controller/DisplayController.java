package com.hun.market.display.controller;

import com.hun.market.item.dto.ItemDto;
import com.hun.market.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DisplayController {

    private final ItemService itemService;

    @GetMapping({"/", "/main"})
    @PreAuthorize("isAuthenticated()")
    public String display(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "18") int size, Model model){

        List<ItemDto.ItemCreatResponseDto> itemList = itemService.getItemList(page, size,"","");
        model.addAttribute("itemList", itemList);

        return "display/home";
    }
}
