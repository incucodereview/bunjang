package com.min.bunjang.storeinquire.service;

import com.min.bunjang.common.exception.WrongWriterException;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.storeinquire.dto.InquireCreateRequest;
import com.min.bunjang.storeinquire.dto.InquireCreateResponse;
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
    private final MemberRepository memberRepository;

    @Transactional
    public InquireCreateResponse createStoreInquiry(String memberEmail, InquireCreateRequest inquireCreateRequest) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotExistMemberException::new);
        Store owner = storeRepository.findById(inquireCreateRequest.getOwnerNum()).orElseThrow(NotExistStoreException::new);
        Store writer = member.getStore();
        if (writer == null) {
            throw new NotExistStoreException();
        }

        StoreInquire storeInquire = StoreInquire.of(owner.getNum(), writer, null, inquireCreateRequest.getInquireContent());
        StoreInquire savedStoreInquire = storeInquiryRepository.save(storeInquire);
        return new InquireCreateResponse(savedStoreInquire.getNum(), writer.getStoreName(), savedStoreInquire.getContent());
    }

    @Transactional
    public void deleteStoreInquire(String email, Long inquireNum) {
        StoreInquire storeInquire = storeInquiryRepository.findById(inquireNum).orElseThrow(NotExistStoreInquireException::new);
        Member member = memberRepository.findByEmail(email).orElseThrow(NotExistMemberException::new);
        Store writer = storeRepository.findByMember(member).orElseThrow(NotExistStoreException::new);
        if (!storeInquire.getWriter().getNum().equals(writer.getNum())) {
            throw new WrongWriterException("작성자가 아닌 사용자가 삭제를 요청했습니다. 잘못된 접근입니다.");
        }

        if (!storeInquiryRepository.existsByNum(inquireNum)) {
            throw new NotExistStoreInquireException();
        }
        storeInquiryRepository.deleteById(inquireNum);
    }
}
