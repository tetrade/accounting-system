package com.accountingsystem.filters;

import com.accountingsystem.entitys.enums.ERole;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchRequest {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String login;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ERole role;

    private Set<FilterRequest> filters;

    private Integer page;

    private Integer size;

    public Set<FilterRequest> getFilters() {
        if (Objects.isNull(this.filters)) return new HashSet<>();
        return this.filters;
    }

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
}
