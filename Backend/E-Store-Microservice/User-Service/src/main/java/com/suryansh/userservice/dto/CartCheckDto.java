package com.suryansh.userservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartCheckDto {
    private boolean isProductInCart;
    private Long cartId;
}
