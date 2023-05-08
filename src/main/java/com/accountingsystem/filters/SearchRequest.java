package com.accountingsystem.filters;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    @Valid
    private Set<FilterRequest> filters;

    @Positive
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
