package com.min.bunjang.store.dto.request;

import com.min.bunjang.store.common.StoreRequestValidatorMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreNameUpdateRequest {
    @NotBlank(message = StoreRequestValidatorMessages.STORE_BLANK_NAME)
    private String updatedStoreName;
}
