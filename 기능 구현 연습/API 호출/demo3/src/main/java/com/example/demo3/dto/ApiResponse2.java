package com.example.demo3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApiResponse2 {
    private Body body;

    @Getter
    @AllArgsConstructor
    public static class Body {
        private int pageNo;
        private int totalCount;
        private int numOfRows;
        private List<Item> items;

        @Getter
        @AllArgsConstructor
        public static class Item {
            private String ITEM_NAME; // 제품명
            private String ITEM_ENG_NAME;
        }
    }
}
