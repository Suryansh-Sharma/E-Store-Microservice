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
    public ResponseEntity<String> createBrand(@RequestBody BrandModel brandModel) {
        try {
            brandService.save(brandModel);
            return new ResponseEntity<>("Brand save successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to save.", HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        return new ResponseEntity<>(brandService.getAllBrands(), HttpStatus.OK);
    }

    @GetMapping("by-name/{name}")
    public ResponseEntity<BrandDto> getBrandByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(brandService.findByName(name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}