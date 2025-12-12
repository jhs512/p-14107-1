package com.back.domain.market.product.entity;

import com.back.domain.member.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
public class Product extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member author;
    private int relId;
    private String relTypeCode;
    private String originName;
    private String name;
    private int price;
    private int salePrice;

    public Product(Member author, String relTypeCode, int relId, String originName, String name, int price, int salePrice) {
        this.author = author;
        this.relTypeCode = relTypeCode;
        this.relId = relId;
        this.originName = originName;
        this.name = name;
        this.price = price;
        this.salePrice = salePrice;
    }
}