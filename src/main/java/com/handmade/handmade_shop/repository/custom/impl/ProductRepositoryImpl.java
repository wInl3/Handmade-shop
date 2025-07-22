package com.handmade.handmade_shop.repository.custom.impl;

import com.handmade.handmade_shop.dto.ProductFilterRequest;
import com.handmade.handmade_shop.entity.Product;
import com.handmade.handmade_shop.repository.custom.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.*;

@Repository("productRepositoryCustomImpl")
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Product> filterProducts(ProductFilterRequest filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getCategoryId() != null) {
            predicates.add(cb.equal(root.get("category").get("categoryId"), filter.getCategoryId()));
        }

        if (filter.getSellerId() != null) {
            predicates.add(cb.equal(root.get("seller").get("userId"), filter.getSellerId()));
        }

        if (filter.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }

        if (filter.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }

        if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getKeyword().toLowerCase() + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        // Sort
        String sortBy = filter.getSortBy();
        boolean asc = filter.getSortDir().equalsIgnoreCase("asc");
        cq.orderBy(asc ? cb.asc(root.get(sortBy)) : cb.desc(root.get(sortBy)));

        // Pagination
        int page = filter.getPage();
        int size = filter.getSize();

        List<Product> resultList = em.createQuery(cq)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        // count query with separate root and predicates
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);
        List<Predicate> countPredicates = new ArrayList<>();

        if (filter.getCategoryId() != null) {
            countPredicates.add(cb.equal(countRoot.get("category").get("categoryId"), filter.getCategoryId()));
        }

        if (filter.getSellerId() != null) {
            countPredicates.add(cb.equal(countRoot.get("seller").get("userId"), filter.getSellerId()));
        }

        if (filter.getMinPrice() != null) {
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("price"), filter.getMinPrice()));
        }

        if (filter.getMaxPrice() != null) {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("price"), filter.getMaxPrice()));
        }

        if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("name")), "%" + filter.getKeyword().toLowerCase() + "%"));
        }

        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, PageRequest.of(page, size), total);
    }
}
