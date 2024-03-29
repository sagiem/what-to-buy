package ru.sagiem.whattobuy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sagiem.whattobuy.dto.ShoppingDtoRequest;
import ru.sagiem.whattobuy.dto.ShoppingDtoResponse;
import ru.sagiem.whattobuy.dto.ShoppingSetDtoRequest;
import ru.sagiem.whattobuy.exception.FamilyGroupNotUserException;
import ru.sagiem.whattobuy.exception.ShoppingProjectNotFoundException;
import ru.sagiem.whattobuy.mapper.ShoppingMapper;
import ru.sagiem.whattobuy.model.shopping.Shopping;
import ru.sagiem.whattobuy.model.shopping.ShoppingProject;
import ru.sagiem.whattobuy.model.shopping.ShoppingStatus;
import ru.sagiem.whattobuy.model.user.FamilyGroup;
import ru.sagiem.whattobuy.model.user.User;
import ru.sagiem.whattobuy.repository.FamilyGroupRepository;
import ru.sagiem.whattobuy.repository.UserRepository;
import ru.sagiem.whattobuy.repository.poroduct.PointShoppingRepository;
import ru.sagiem.whattobuy.repository.poroduct.ProductRepository;
import ru.sagiem.whattobuy.repository.poroduct.ShoppingProjectRepository;
import ru.sagiem.whattobuy.repository.poroduct.ShoppingRepository;
import ru.sagiem.whattobuy.utils.FamalyGroupAndUserUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingService {

    public final ShoppingRepository shoppingRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PointShoppingRepository pointShoppingRepository;
    private final FamilyGroupRepository familyGroupRepository;
    private final ShoppingMapper shoppingMapper;
    private final FamalyGroupAndUserUtils famalyGroupAndUserUtils;
    private final ShoppingProjectRepository shoppingProjectRepository;


    // получить все покупки пользователя из всех групп и всех проектов
    public List<ShoppingDtoResponse> showAllMyExecutor(UserDetails userDetails) {
        User user = famalyGroupAndUserUtils.getUser(userDetails);
        List<Shopping> shoppings = shoppingRepository.findByUserCreator(user).orElse(null);
        if (shoppings != null) {
            return shoppings.stream()
                    .map(shoppingMapper::convertToDto)
                    .toList();
        } else
            return null;
    }

    // получить все покупки из проекта покупок для пользователя
    public List<ShoppingDtoResponse> showMyExecutorinShoppingProgect(Integer shoppingProjectId, UserDetails userDetails) {
        User user = famalyGroupAndUserUtils.getUser(userDetails);
        ShoppingProject shoppingProject = shoppingProjectRepository.findById(shoppingProjectId).orElse(null);
        if (shoppingProject == null)
            throw new ShoppingProjectNotFoundException();

        List<Shopping> shoppings = shoppingRepository.findAllByShoppingProjectAndUserExecutor(shoppingProject, user).orElse(null);
        if (shoppings != null) {
            return shoppings.stream()
                    .map(shoppingMapper::convertToDto)
                    .toList();
        } else
            return null;
    }

    //Возвращает все покупки в проекте покупок
    public List<ShoppingDtoResponse> showAllInShoppingProgect(Integer shoppingProgectId, UserDetails userDetails) {
        User user = famalyGroupAndUserUtils.getUser(userDetails);
        ShoppingProject shoppingProject = shoppingProjectRepository.findById(shoppingProgectId).orElse(null);
        if (shoppingProject == null)
            throw new ShoppingProjectNotFoundException();

        if (!famalyGroupAndUserUtils.isUserInFamilyGroup(userDetails, shoppingProject.getFamilyGroup().getId()))
            throw new FamilyGroupNotUserException();

        List<Shopping> shoppings = shoppingRepository.findAllByShoppingProjectAndUserExecutor(shoppingProject, user).orElse(null);
        if (shoppings != null) {
            return shoppings.stream()
                    .map(shoppingMapper::convertToDto)
                    .toList();
        } else
            return null;
    }


    public Integer addSet(ShoppingDtoRequest shoppingSetDtoRequest, UserDetails userDetails) {

        FamilyGroup familyGroup = familyGroupRepository.findById(shoppingSetDtoRequest.getFamilyGroupId()).orElse(null);
        if (familyGroup == null && shoppingSetDtoRequest.getFamilyGroupId() != null)
            throw new FamilyGroupNotUserException();

        ShoppingProject shoppingProject = shoppingProjectRepository.findById(shoppingSetDtoRequest.getShoppingProjectId()).orElse(null);
        User userExecutor = userRepository.findById(shoppingSetDtoRequest.getUserExecutorId()).orElse(null);
        if (userExecutor == null && shoppingSetDtoRequest.getUserExecutorId() != null)
            throw new UsernameNotFoundException("неверно указан пользователь исполнитель");

        if (shoppingProject != null) {
            FamilyGroup shopingProjectFamilyGroup = shoppingProject.getFamilyGroup();
            if (famalyGroupAndUserUtils.isUserInFamilyGroup(userDetails, shopingProjectFamilyGroup.getId()) && userExecutor != null &&
                    userExecutor.getFamilyGroups() == shopingProjectFamilyGroup) {
                Shopping shopping = Shopping.builder()
                        .product(productRepository.findById(shoppingSetDtoRequest.getProductId()).orElse(null))
                        .volume(shoppingSetDtoRequest.getVolume())
                        .pointShopping(pointShoppingRepository.findById(shoppingSetDtoRequest.getPointShoppingId()).orElse(null))
                        .familyGroup(shopingProjectFamilyGroup)
                        .shoppingProject(shoppingProject)
                        .userExecutor(userExecutor)
                        .build();

                Shopping entityShopping = shoppingRepository.save(shopping);
                return entityShopping.getId();
            }
            if (famalyGroupAndUserUtils.isUserInFamilyGroup(userDetails, shopingProjectFamilyGroup.getId()) && userExecutor == null) {
                Shopping shopping = Shopping.builder()
                        .product(productRepository.findById(shoppingSetDtoRequest.getProductId()).orElse(null))
                        .volume(shoppingSetDtoRequest.getVolume())
                        .pointShopping(pointShoppingRepository.findById(shoppingSetDtoRequest.getPointShoppingId()).orElse(null))
                        .familyGroup(shopingProjectFamilyGroup)
                        .shoppingProject(shoppingProject)
                        .userExecutor(null)
                        .build();

                Shopping entityShopping = shoppingRepository.save(shopping);
                return entityShopping.getId();
            } else
                throw new FamilyGroupNotUserException();
        }

        if (userExecutor != null && familyGroup != null && famalyGroupAndUserUtils.isUserInFamilyGroup(userDetails, familyGroup.getId())) {
            Shopping shopping = Shopping.builder()
                    .product(productRepository.findById(shoppingSetDtoRequest.getProductId()).orElse(null))
                    .volume(shoppingSetDtoRequest.getVolume())
                    .pointShopping(pointShoppingRepository.findById(shoppingSetDtoRequest.getPointShoppingId()).orElse(null))
                    .familyGroup(familyGroup)
                    .shoppingProject(null)
                    .userExecutor(userExecutor)
                    .build();

            Shopping entityShopping = shoppingRepository.save(shopping);
            return entityShopping.getId();
        }
        if (userExecutor == null && familyGroup != null && famalyGroupAndUserUtils.isUserInFamilyGroup(userDetails, familyGroup.getId())) {
            Shopping shopping = Shopping.builder()
                    .product(productRepository.findById(shoppingSetDtoRequest.getProductId()).orElse(null))
                    .volume(shoppingSetDtoRequest.getVolume())
                    .pointShopping(pointShoppingRepository.findById(shoppingSetDtoRequest.getPointShoppingId()).orElse(null))
                    .familyGroup(familyGroup)
                    .shoppingProject(null)
                    .userExecutor(null)
                    .build();

            Shopping entityShopping = shoppingRepository.save(shopping);
            return entityShopping.getId();
        } else
            throw new FamilyGroupNotUserException();

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
