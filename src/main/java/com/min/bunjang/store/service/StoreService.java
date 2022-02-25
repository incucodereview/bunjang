package com.min.bunjang.store.service;

import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.dto.StoreCreateRequest;
import com.min.bunjang.store.dto.StoreCreateResponse;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public StoreCreateResponse createStore(StoreCreateRequest storeCreateRequest) {
        Member member = memberRepository.findById(storeCreateRequest.getMemberId()).orElseThrow(NotExistMemberException::new);
        Store store = Store.createStore(storeCreateRequest.getStoreName(), storeCreateRequest.getIntroduceContent(), member);
        Store savedStore = storeRepository.save(store);
        return StoreCreateResponse.of(savedStore);
    }
}
