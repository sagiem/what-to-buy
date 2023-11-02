package ru.sagiem.whattobuy.dto.auth;

import lombok.Data;

@Data
public class ShoppingSetDtoRequest {
    private Integer productId;
    private Integer Volume;
    private Integer pointShoppingId;
    private Integer userExecutorId;
}
