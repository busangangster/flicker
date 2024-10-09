package com.flicker.logger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActorDto {

    @JsonProperty("actor_name")
    private String actorName;
}
