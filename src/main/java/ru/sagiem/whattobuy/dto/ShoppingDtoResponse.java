package ru.sagiem.whattobuy.dto;

import lombok.Data;
import ru.sagiem.whattobuy.model.product.Product;
import ru.sagiem.whattobuy.model.shopping.PointShopping;
import ru.sagiem.whattobuy.model.shopping.ShoppingStatus;
import ru.sagiem.whattobuy.model.user.FamilyGroup;
import ru.sagiem.whattobuy.model.user.User;

import java.time.LocalDateTime;

@Data
public class ShoppingDtoResponse {
    private Integer id;
    private LocalDateTime createDate;
    private LocalDateTime lastModified;
    private LocalDateTime executorDate;
    private Integer productId;
    private Integer Volume;
    private Integer pointShoppingId;
    private Integer shoppingProjectId;
    private Integer familyGroupId;
    private Integer userCreatorId;
    private Integer userExecutorId;
    private String shoppingStatus;

}
