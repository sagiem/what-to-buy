package ru.sagiem.whattobuy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.sagiem.whattobuy.dto.ShoppingDtoRequest;
import ru.sagiem.whattobuy.dto.ShoppingSetDtoRequest;
import ru.sagiem.whattobuy.mapper.ShoppingMapper;
import ru.sagiem.whattobuy.model.shopping.Shopping;
import ru.sagiem.whattobuy.model.shopping.ShoppingStatus;
import ru.sagiem.whattobuy.model.user.User;
import ru.sagiem.whattobuy.repository.FamilyGroupRepository;
import ru.sagiem.whattobuy.repository.UserRepository;
import ru.sagiem.whattobuy.repository.poroduct.PointShoppingRepository;
import ru.sagiem.whattobuy.repository.poroduct.ProductRepository;
import ru.sagiem.whattobuy.repository.poroduct.ShoppingProjectRepository;
import ru.sagiem.whattobuy.repository.poroduct.ShoppingRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShoppingService {

    public final ShoppingRepository shoppingRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PointShoppingRepository pointShoppingRepository;
    private final FamilyGroupRepository familyGroupRepository;


    //пользователь приобрел товар и сам добавляет его.
    public Integer addShopping(ShoppingDtoRequest shoppingDtoRequest, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Shopping shopping = Shopping.builder()
                .executorDate(LocalDateTime.now())
                .product(productRepository.getReferenceById(shoppingDtoRequest.getProductId()))
                .volume(shoppingDtoRequest.getVolume())
                .pointShopping(pointShoppingRepository.getReferenceById(shoppingDtoRequest.getPointShoppingId()))
                .familyGroup(familyGroupRepository.getReferenceById(shoppingDtoRequest.getFamilyGroup()))
                .userCreator(user)
                .userExecutor(user)
                .shoppingStatus(ShoppingStatus.EXECUTED)
                .build();

        Shopping saveShopping = shoppingRepository.save(shopping);
        return saveShopping.getId();
    }

    // пользователь назначает другому пользователю из семьи купить товар.
    public Integer addSetShopping(ShoppingSetDtoRequest shoppingSetDtoRequest, UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        Shopping shopping = Shopping.builder()
                .product(productRepository.getReferenceById(shoppingSetDtoRequest.getProductId()))
                .volume(shoppingSetDtoRequest.getVolume())
                .pointShopping(pointShoppingRepository.getReferenceById(shoppingSetDtoRequest.getPointShoppingId()))
                .familyGroup(familyGroupRepository.getReferenceById(shoppingSetDtoRequest.getFamilyGroup()))
                .userCreator(user)
                .userExecutor(userRepository.getReferenceById(shoppingSetDtoRequest.getUserExecutorId()))
                .shoppingStatus(ShoppingStatus.ASSIGNED)
                .build();

        Shopping saveShopping = shoppingRepository.save(shopping);
        return saveShopping.getId();
    }

    public Integer executedShopping(Integer id, UserDetails userDetails) {

        Shopping shopping = shoppingRepository.getReferenceById(id);
        shopping.setExecutorDate(LocalDateTime.now());
        shopping.setShoppingStatus(ShoppingStatus.EXECUTED);
        shoppingRepository.save(shopping);
        return shopping.getId();
    }

    public Integer notExecutedShopping(Integer id, UserDetails userDetails) {

        Shopping shopping = shoppingRepository.getReferenceById(id);
        shopping.setExecutorDate(LocalDateTime.now());
        shopping.setShoppingStatus(ShoppingStatus.NOT_EXECUTED);
        shoppingRepository.save(shopping);
        return shopping.getId();
    }

}
