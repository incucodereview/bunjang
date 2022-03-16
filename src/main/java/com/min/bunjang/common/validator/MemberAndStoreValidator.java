package com.min.bunjang.common.validator;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.store.model.Store;

public class MemberAndStoreValidator {

    public static void verifyMemberAndStoreMatchByEmail(String email, Store store) {
        if (email.isEmpty()) {
            throw new ImpossibleException("요청자가 로그인을 하지 않았거나 잘못된 접근입니다.");
        }

        if (!store.verifyMatchMember(email)) {
            throw new ImpossibleException("상점오너와 요청한 회원의 정보가 틀립니다. 잘못된 요청입니다.");
        }
    }
}
