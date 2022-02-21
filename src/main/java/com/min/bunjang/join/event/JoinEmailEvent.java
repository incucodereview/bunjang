package com.min.bunjang.join.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JoinEmailEvent extends ApplicationEvent {
    private Long memberId;
    private String email;

    public JoinEmailEvent(Object source, Long memberId, String email) {
        super(source);
        this.memberId = memberId;
        this.email = email;
    }
}
