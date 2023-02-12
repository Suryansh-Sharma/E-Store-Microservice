package com.suryansh.controller;

import com.suryansh.dto.BrandDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.model.BrandModel;
import com.suryansh.service.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/brand")
@CrossOrigin("*")
@AllArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public void createBrand(@RequestBody @Valid BrandModel brandModel) {
        brandService.save(brandModel);
    }

    @GetMapping("by-name/{name}")
    public ProductPagingDto getProductByBrandName(@PathVariable String name,
                                                  @RequestParam(name = "pageNo",defaultValue = "0",required = false) int page
    ) {
        Pageable pageable = PageRequest.of(page,6);
        return brandService.findByName(name,pageable);
    }
    @GetMapping("nameLike/{name}")
    public List<BrandDto> getBrandNameLike(@PathVariable String name){
        return brandService.findByNameLike(name);
    }

}
