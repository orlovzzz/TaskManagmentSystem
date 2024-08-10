package org.example.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.example.entity.Task;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class CustomerSpecs implements Specification<Task> {

    private SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchCriteria.getField().equals("priority") || searchCriteria.getField().equals("status")) {
            return criteriaBuilder.like(
                    root.get(searchCriteria.getField()).as(String.class),
                    "%" + searchCriteria.getPattern() + "%"
            );
        }
        return criteriaBuilder.like(root.get(searchCriteria.getField()), "%" + searchCriteria.getPattern() + "%");
    }
}