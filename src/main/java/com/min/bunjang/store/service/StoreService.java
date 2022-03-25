package com.min.bunjang.store.service;

import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.dto.request.StoreCreateRequest;
import com.min.bunjang.store.dto.response.StoreCreateResponse;
import com.min.bunjang.store.dto.request.StoreIntroduceUpdateRequest;
import com.min.bunjang.store.dto.request.StoreNameUpdateRequest;
import com.min.bunjang.store.dto.VisitorPlusDto;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public StoreCreateResponse createStore(StoreCreateRequest storeCreateRequest, String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotExistMemberException::new);
        Store store = Store.createStore(storeCreateRequest.getStoreName(), storeCreateRequest.getIntroduceContent(), null, member);
        Store savedStore = storeRepository.save(store);
        return StoreCreateResponse.of(savedStore);
    }

    @Transactional
    public void updateIntroduceContent(String memberEmail, StoreIntroduceUpdateRequest storeIntroduceUpdateRequest) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotExistMemberException::new);
        if (member.getStore() == null) {
            throw new NotExistStoreException();
        }

        Store store = storeRepository.findById(member.getStore().getNum()).orElseThrow(NotExistStoreException::new);
        store.updateIntroduceContent(storeIntroduceUpdateRequest.getUpdateIntroduceContent());
    }

    @Transactional
    public void updateStoreName(StoreNameUpdateRequest storeNameUpdateRequest, String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotExistMemberException::new);
        if (member.getStore() == null) {
            throw new NotExistStoreException();
        }

        Store store = storeRepository.findById(member.getStore().getNum()).orElseThrow(NotExistStoreException::new);
        store.updateStoreName(storeNameUpdateRequest.getUpdatedStoreName());
    }

    @Transactional
    public void plusVisitor(VisitorPlusDto visitorPlusDto, String memberEmail) {
        Member visitorMember = memberRepository.findByEmail(memberEmail).orElseThrow(NotExistMemberException::new);
        if (visitorMember.getStore() == null) {
            throw new NotExistStoreException();
        }

        Store owner = storeRepository.findById(visitorPlusDto.getOwnerNum()).orElseThrow(NotExistStoreException::new);
        Store visitor = storeRepository.findByMember(visitorMember).orElseThrow(NotExistStoreException::new);
        owner.plusVisitor(visitor.getNum());
    }

}
