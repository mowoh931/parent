package com.baar.parent.model;

import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class State {
    String code;
    String name;
    String population;
}
