package com.csstest.simulation.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
@ToString(includeFieldNames = true)
@JsonDeserialize(builder = Order.OrderBuilder.class)
public class Order {
    @JsonProperty
    @NonNull
    public String id;

    @JsonProperty
    @NonNull
    public String name;

    @JsonProperty
    @NonNull
    public Temperature temp;

    @JsonProperty
    @NonNull
    public Long shelfLife;

    @JsonProperty
    @NonNull
    public Double decayRate;
}
