package com.example.demo3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApiResponse {
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
            private String itemName; // 제품명
            private String efcyQesitm; // 효능
            private String useMethodQesitm; // 사용법
            private String atpnQesitm; // 주의사항
            private String seQesitm; // 부작용
        }
    }
}
