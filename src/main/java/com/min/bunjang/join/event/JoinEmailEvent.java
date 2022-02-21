package com.min.bunjang.join.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JoinEmailEvent extends ApplicationEvent {
    private String email;

    public JoinEmailEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
