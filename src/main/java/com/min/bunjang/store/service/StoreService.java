package com.min.bunjang.store.service;

import com.min.bunjang.aws.s3.service.S3UploadService;
import com.min.bunjang.common.validator.RightRequesterChecker;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.dto.request.StoreCreateOrUpdateRequest;
import com.min.bunjang.store.dto.response.StoreCreateResponse;
import com.min.bunjang.store.dto.request.StoreIntroduceUpdateRequest;
import com.min.bunjang.store.dto.request.StoreNameUpdateRequest;
import com.min.bunjang.store.dto.request.VisitorPlusDto;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.model.StoreThumbnail;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.store.repository.StoreThumbnailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final StoreThumbnailRepository storeThumbnailRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public StoreCreateResponse createStore(StoreCreateOrUpdateRequest storeCreateOrUpdateRequest, String memberEmail) throws IOException {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotExistMemberException::new);
        StoreThumbnail storeThumbnail = refineStoreThumbnail(storeCreateOrUpdateRequest.getStoreThumbnail(), null);
        Store store = Store.createStore(storeCreateOrUpdateRequest.getStoreName(), storeCreateOrUpdateRequest.getIntroduceContent(), storeThumbnail, member);
        return StoreCreateResponse.of(storeRepository.save(store));
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
    public void updateStore(StoreCreateOrUpdateRequest storeCreateOrUpdateRequest, Long storeNum, String memberEmail) throws IOException {
        Store store = storeRepository.findById(storeNum).orElseThrow(NotExistStoreException::new);
        RightRequesterChecker.verifyMemberAndStoreMatchByEmail(memberEmail, store);

        store.updateStore(storeCreateOrUpdateRequest);
        store.updateThumbnail(refineStoreThumbnail(storeCreateOrUpdateRequest.getStoreThumbnail(), store));
    }

    private StoreThumbnail refineStoreThumbnail(MultipartFile multipartFile, Store store) throws IOException {
        if (multipartFile == null) {
            return null;
        }

        if (store != null && store.checkExistThumbnail()) {
            storeThumbnailRepository.delete(store.getStoreThumbnail());
            //기존 파일 삭제
//                s3UploadService.
        }
        StoreThumbnail updatedThumbnail = StoreThumbnail.createStoreThumbnail(s3UploadService.uploadForMultiFile(multipartFile), store);
        return storeThumbnailRepository.save(updatedThumbnail);
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
