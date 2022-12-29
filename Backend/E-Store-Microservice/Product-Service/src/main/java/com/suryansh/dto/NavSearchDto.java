package com.suryansh.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class NavSearchDto {
    private Long id;
    private String productName;
}
