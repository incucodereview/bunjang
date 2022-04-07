package com.min.bunjang.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageDto {
    private int startPage;
    private int endPage;
    private boolean prev,next;
    private long total;
    private int realEndPage;
    private int currentPage;

    //TODO 이거 로직 개선해야됨. 3/2
    public PageDto(int pageSize, int currentPage, long total){
        this.total = total;
        this.endPage =  (int) (Math.ceil(2) / 10 * 10);
        this.startPage = endPage - 9;
        this.realEndPage = (int) total / pageSize;

        if (realEndPage < this.endPage) {
            this.endPage = realEndPage;
        }

        this.prev = this.startPage > 1;
        this.next = this.endPage < realEndPage;
        this.currentPage = currentPage;
    }
}
