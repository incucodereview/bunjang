package com.min.bunjang.store.service;

import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.dto.StoreCreateRequest;
import com.min.bunjang.store.dto.StoreCreateResponse;
import com.min.bunjang.store.dto.StoreIntroduceDto;
import com.min.bunjang.store.dto.VisitorPlusDto;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public StoreCreateResponse createStore(StoreCreateRequest storeCreateRequest) {
        Member member = memberRepository.findById(storeCreateRequest.getMemberId()).orElseThrow(NotExistMemberException::new);
        Store store = Store.createStore(storeCreateRequest.getStoreName(), storeCreateRequest.getIntroduceContent(), member);
        Store savedStore = storeRepository.save(store);
        return StoreCreateResponse.of(savedStore);
    }

    @Transactional
    public void updateIntroduceContent(StoreIntroduceDto storeIntroduceDto) {
        Store store = storeRepository.findById(storeIntroduceDto.getStoreNum()).orElseThrow(NotExistStoreException::new);
        store.updateIntroduceContent(storeIntroduceDto.getUpdateIntroduceContent());
    }

    @Transactional
    public void plusVisitor(VisitorPlusDto visitorPlusDto) {
        Store owner = storeRepository.findById(visitorPlusDto.getOwnerNum()).orElseThrow(NotExistStoreException::new);
        owner.plusVisitor(visitorPlusDto.getVisitorNum());
    }
}
