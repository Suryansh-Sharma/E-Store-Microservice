package com.suryansh.controller;

import com.suryansh.dto.BrandDto;
import com.suryansh.model.BrandModel;
import com.suryansh.service.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/brand")
@AllArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public void createBrand(@RequestBody BrandModel brandModel) {
        brandService.save(brandModel);
    }

    @GetMapping
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        return new ResponseEntity<>(brandService.getAllBrands(), HttpStatus.OK);
    }
    @GetMapping("by-name/{name}")
    public BrandDto getProductByBrandName(@PathVariable String name) {
        return brandService.findByName(name);
    }

}
