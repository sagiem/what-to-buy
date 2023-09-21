package ru.sagiem.whattobuy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.sagiem.whattobuy.dto.auth.ProductAddRequest;
import ru.sagiem.whattobuy.exceptions.ProductAddError;
import ru.sagiem.whattobuy.model.product.Product;
import ru.sagiem.whattobuy.repository.poroduct.CategoryProductRepository;
import ru.sagiem.whattobuy.repository.poroduct.ProductRepository;
import ru.sagiem.whattobuy.repository.poroduct.SubcategoryProductRepository;
import ru.sagiem.whattobuy.repository.poroduct.UnitOfMeasurementProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final SubcategoryProductRepository subcategoryProductRepository;
    private final UnitOfMeasurementProductRepository unitOfMeasurementProductRepository;

    public ResponseEntity<?> addProduct(ProductAddRequest request) {

        if (request.getName() != null &&
                request.getCategoryId() != null &&
                request.getSubcategoryId() != null &&
                request.getUnitOfMeasurementId() != null) {

            var product = Product.builder()
                    .name(request.getName())
                    .category(categoryProductRepository.findById(request.getCategoryId()).orElseThrow())
                    .subcategory(subcategoryProductRepository.findById(request.getSubcategoryId()).orElseThrow())
                    .unitOfMeasurement(unitOfMeasurementProductRepository.findById(request.getUnitOfMeasurementId()).orElseThrow())
                    .build();
            var saveProduct = productRepository.save(product);

            return ResponseEntity.ok(saveProduct.getId());

        }
        return new ResponseEntity<>(new ProductAddError(HttpStatus.BAD_REQUEST.value(),
                "Неверные данные"), HttpStatus.BAD_REQUEST);

    }
}
