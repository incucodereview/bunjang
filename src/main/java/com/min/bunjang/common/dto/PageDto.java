package com.min.bunjang.common.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
public class PageDto {
    private int startPage;
    private int endPage;
    private boolean prev,next;
    private long total;
    private int realEndPage;

    //TODO 이거 로직 개선해야됨. 3/2
    public PageDto(int contentAmount, int currentPage, long total){
        this.total = total;
        this.endPage = (int)(Math.ceil(currentPage/10.0)) * 10;
        this.startPage = endPage - 9;
        this.realEndPage = (int) total / contentAmount;

        if (realEndPage < this.endPage) {
            this.endPage = realEndPage;
        }

        this.prev = this.startPage > 1;
        this.next = this.endPage < realEndPage;
    }
}
