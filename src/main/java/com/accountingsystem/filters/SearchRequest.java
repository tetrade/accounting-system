package com.accountingsystem.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    @Valid
    private Set<FilterRequest> filters;

    @PositiveOrZero
    private Integer page;

    @Positive
    private Integer size;

    public Set<FilterRequest> getFilters() { return Optional.ofNullable(filters).orElse(new HashSet<>()); }
    public Integer getPage() { return Optional.ofNullable(page).orElse(0); }
    public Integer getSize() { return Optional.ofNullable(size).orElse(10); }


    public void addUserLoginFilter(String login) {
        FilterRequest userFilter = new FilterRequest(EPublicKey.LOGIN, ETargetEntity.USER, EOperator.EQUAL, login);
        if (filters == null) filters = new HashSet<>();
        filters.add(userFilter);
    }

    public void addEntityIdFilter(ETargetEntity targetEntity, Integer id) {
        FilterRequest userFilter = new FilterRequest(EPublicKey.ID, targetEntity, EOperator.EQUAL, id.toString());
        if (filters == null) filters = new HashSet<>();
        filters.add(userFilter);
    }

    public void addFilter(FilterRequest filterRequest) {
        if (filters == null) filters = new HashSet<>();
        filters.add(filterRequest);
    }
}
