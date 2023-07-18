package com.suryansh.model;

import lombok.*;

import jakarta.validation.constraints.NotEmpty;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandModel {
    @NotEmpty(message = "Brand name can't be empty")
    private String name;
}
