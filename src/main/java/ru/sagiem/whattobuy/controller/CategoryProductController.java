package ru.sagiem.whattobuy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.sagiem.whattobuy.dto.*;
import ru.sagiem.whattobuy.service.CategoryProductService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.sagiem.whattobuy.utils.ResponseUtils.*;

@RestController
@RequestMapping("/api/v1/category-product")
@RequiredArgsConstructor
@Tag(name = "Работа с категоирями продуктов")
public class CategoryProductController {
    private final CategoryProductService service;

    @Operation(
            summary = "Возвращает все категории продуктов",
            description = "Возвращает все категории продуктов для группы"
            //tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CategoryProductDtoResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    @GetMapping("/show-all-for-group/{id}")
    public ResponseEntity<List<CategoryProductDtoResponse>> showAllForGroup(@PathVariable("id") @Min(1) Integer familyGroupId,
                                                                            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.showAllForGroup(familyGroupId, userDetails));

    }

    @Operation(
            summary = "Добавление категории",
            description = "Добавление происходит в группу"

    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Integer.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    @PostMapping("/add/")
    public ResponseEntity<Integer> add(@RequestBody CategoryProductDtoRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.create(request, userDetails));
    }

    @Operation(
            summary = "Поиск категории по id",
            description = "Пользователь должен быть в группе к которой принадлежит категория"
            //tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CategoryProductDtoResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    @GetMapping("/search/{id}")
    public ResponseEntity<CategoryProductDtoResponse> searchId(@PathVariable("id") @Min(1) Integer id,
                                                               @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(service.searchId(id, userDetails));
    }

    @Operation(
            summary = "Обновление категории",
            description = "Пользователь должен быть в группе к которой принадлежит категория"
            //tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = SuccessResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    @PatchMapping("/update")
    public ResponseEntity<SuccessResponse> update(CategoryProductDtoRequest categoryProductDtoRequest,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        String сategoryProductName = service.update(categoryProductDtoRequest, userDetails);
        return ResponseEntity.ok(getSuccessResponse(CATEGORY_PRODUCT_UPDATE_MESSAGE, сategoryProductName));

    }

    @Operation(
            summary = "Удаляет категорию",
            description = "Удаляет категорию по id"
            //tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = SuccessResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    @DeleteMapping("/{Id}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable("Id") @Min(1) Integer Id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        String сategoryProductName = service.delete(Id, userDetails);
        return ResponseEntity.ok(getSuccessResponse(CATEGORY_PRODUCT_DELETE_MESSAGE, сategoryProductName));

    }
}
