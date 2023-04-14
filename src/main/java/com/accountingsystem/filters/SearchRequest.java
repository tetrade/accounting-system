package com.accountingsystem.filters;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchRequest {
    private Set<FilterRequest> filters;

    private Integer page;

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
