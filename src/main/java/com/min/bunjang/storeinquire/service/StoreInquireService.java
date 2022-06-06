package com.min.bunjang.storeinquire.service;

import com.min.bunjang.common.validator.RightRequesterChecker;
import com.min.bunjang.security.MemberAccount;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storeinquire.dto.request.InquireCreateRequest;
import com.min.bunjang.storeinquire.dto.response.InquireCreateResponse;
import com.min.bunjang.storeinquire.exception.NotExistStoreInquireException;
import com.min.bunjang.storeinquire.model.StoreInquire;
import com.min.bunjang.storeinquire.repository.StoreInquireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreInquireService {
    private final StoreInquireRepository storeInquiryRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public InquireCreateResponse createStoreInquiry(MemberAccount memberAccount, InquireCreateRequest inquireCreateRequest) {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        Store owner = storeRepository.findById(inquireCreateRequest.getOwnerNum()).orElseThrow(NotExistStoreException::new);
        Store writer = storeRepository.findById(inquireCreateRequest.getWriterNum()).orElseThrow(NotExistStoreException::new);
        RightRequesterChecker.verifyMemberAndStoreMatchByEmail(memberAccount.getEmail(), writer);
        StoreInquire storeInquire = StoreInquire.of(owner.getNum(), writer, inquireCreateRequest.getInquireContent());
        defineMentionIfExistMentionNum(inquireCreateRequest, storeInquire);

        StoreInquire savedStoreInquire = storeInquiryRepository.save(storeInquire);
        return new InquireCreateResponse(savedStoreInquire.getNum(), writer.getStoreName(), savedStoreInquire.getContent());
    }

    private void defineMentionIfExistMentionNum(InquireCreateRequest inquireCreateRequest, StoreInquire storeInquire) {
        if (inquireCreateRequest.checkExistenceMentionedStoreNum()) {
            Store mentionedStore = storeRepository.findById(inquireCreateRequest.getMentionedStoreNumForAnswer()).orElseThrow(NotExistStoreException::new);
            storeInquire.defineMention(mentionedStore.getNum(), mentionedStore.getStoreName());
        }
    }

    @Transactional
    public void deleteStoreInquire(MemberAccount memberAccount, Long inquireNum) {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        StoreInquire storeInquire = storeInquiryRepository.findById(inquireNum).orElseThrow(NotExistStoreInquireException::new);
        RightRequesterChecker.verifyMemberAndStoreInquireWriterMatchByEmail(memberAccount.getEmail(), storeInquire);

        storeInquiryRepository.deleteById(inquireNum);
    }
}
