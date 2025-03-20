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
            private String entpName;
            private String itemName;
            private String efcyQesitm;
            private String useMethodQesitm;
            private String atpnQesitm;
            private String depositMethodQesitm;
        }
    }
}
