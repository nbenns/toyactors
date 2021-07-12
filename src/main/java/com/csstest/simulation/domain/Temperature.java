package com.csstest.simulation.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public enum Temperature implements Serializable {
    @JsonProperty("hot") HOT,
    @JsonProperty("cold") COLD,
    @JsonProperty("frozen") FROZEN;
}
