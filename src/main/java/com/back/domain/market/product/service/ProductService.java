package com.back.domain.market.product.service;

import com.back.domain.market.product.entity.Product;
import com.back.domain.market.product.repository.ProductRepository;
import com.back.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public long count() {
        return productRepository.count();
    }

    public Product make(Member author, String relTypeCode, int relId, String originName, String name, int price, int salePrice) {
        Product product = new Product(author, relTypeCode, relId, originName, name, price, salePrice);

        return productRepository.save(product);
    }

    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }
}