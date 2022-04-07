package com.min.bunjang.common.validator;

import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.common.exception.WrongWriterException;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.storeinquire.model.StoreInquire;

public class MemberAndStoreValidator {

    public static void verifyMemberAndStoreMatchByEmail(String email, Store store) {
        if (email == null) {
            throw new ImpossibleException("요청자가 로그인을 하지 않았거나 잘못된 접근입니다.");
        }

        if (!store.verifyMatchMember(email)) {
            throw new ImpossibleException("상점오너와 요청한 회원의 정보가 틀립니다. 잘못된 요청입니다.");
        }
    }

    public static void verifyMemberAndStoreInquireWriterMatchByEmail(String email, StoreInquire storeInquire) {
        if (storeInquire.getWriter() == null) {
            throw new ImpossibleException("작성자가 없는 문의 입니다. 잘못된 데이터 입니다.");
        }

        if (!storeInquire.getWriter().verifyMatchMember(email)) {
            throw new WrongWriterException("작성자가 아닌 사용자가 삭제를 요청했습니다. 잘못된 접근입니다.");
        }
    }

    public static String verifyLoginRequest(MemberAccount memberAccount) {
        if (memberAccount == null) {
            return null;
        }

        return memberAccount.getEmail();
    }
}
