package ru.sagiem.whattobuy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.sagiem.whattobuy.dto.auth.ProductDtoRequest;
import ru.sagiem.whattobuy.dto.auth.ProductDtoResponse;
import ru.sagiem.whattobuy.model.product.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = Product.class)
public interface ProductMapper {


    @Mapping(source = "category.id", target = "categoryId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(source = "subcategory.id", target = "subcategoryId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(source = "unitOfMeasurement.id", target = "unitOfMeasurementId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    ProductDtoResponse convertToDTO(Product product);
}