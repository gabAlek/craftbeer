package com.beerhouse;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IncompleteObject {

    private String category;
    private int id;
    private String name;
}
