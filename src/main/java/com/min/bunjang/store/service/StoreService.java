package com.min.bunjang.store.service;

import com.min.bunjang.aws.s3.service.S3UploadService;
import com.min.bunjang.common.validator.RightRequesterChecker;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.security.MemberAccount;
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

    //memberAccount
    @Transactional
    public StoreCreateResponse createStore(StoreCreateOrUpdateRequest storeCreateOrUpdateRequest, MemberAccount memberAccount) throws IOException {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        Member member = memberRepository.findByEmail(memberAccount.getEmail()).orElseThrow(NotExistMemberException::new);
        StoreThumbnail storeThumbnail = refineStoreThumbnail(storeCreateOrUpdateRequest.getStoreThumbnail(), null);
        Store store = Store.createStore(storeCreateOrUpdateRequest.getStoreName(), storeCreateOrUpdateRequest.getIntroduceContent(), storeThumbnail, member);
        return StoreCreateResponse.of(storeRepository.save(store));
    }

    @Transactional
    public void updateIntroduceContent(MemberAccount memberAccount, StoreIntroduceUpdateRequest storeIntroduceUpdateRequest) {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        Store store = storeRepository.findByMemberEmail(memberAccount.getEmail()).orElseThrow(NotExistStoreException::new);
        store.updateIntroduceContent(storeIntroduceUpdateRequest.getUpdateIntroduceContent());
    }

    //? 컨트롤러 없는데?
    @Transactional
    public void updateStore(StoreCreateOrUpdateRequest storeCreateOrUpdateRequest, Long storeNum, MemberAccount memberAccount) throws IOException {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        Store store = storeRepository.findById(storeNum).orElseThrow(NotExistStoreException::new);
        RightRequesterChecker.verifyMemberAndStoreMatchByEmail(memberAccount.getEmail(), store);

        store.updateStore(storeCreateOrUpdateRequest);
        store.updateThumbnail(refineStoreThumbnail(storeCreateOrUpdateRequest.getStoreThumbnail(), store));
    }

    private StoreThumbnail refineStoreThumbnail(MultipartFile multipartFile, Store store) throws IOException {
        if (multipartFile == null || store == null) {
            return null;
        }

        if (store.checkExistThumbnail()) {
            storeThumbnailRepository.delete(store.getStoreThumbnail());
            //기존 파일 삭제
//                s3UploadService.
        }
        StoreThumbnail updatedThumbnail = StoreThumbnail.createStoreThumbnail(s3UploadService.uploadForMultiFile(multipartFile), store.getNum());
        return storeThumbnailRepository.save(updatedThumbnail);
    }

    @Transactional
    public void updateStoreName(StoreNameUpdateRequest storeNameUpdateRequest, MemberAccount memberAccount) {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        Store store = storeRepository.findByMemberEmail(memberAccount.getEmail()).orElseThrow(NotExistStoreException::new);
        store.updateStoreName(storeNameUpdateRequest.getUpdatedStoreName());
    }

    @Transactional
    public void plusVisitor(VisitorPlusDto visitorPlusDto, MemberAccount memberAccount) {
        RightRequesterChecker.verifyLoginRequest(memberAccount);
        Store owner = storeRepository.findById(visitorPlusDto.getOwnerNum()).orElseThrow(NotExistStoreException::new);
        Store visitor = storeRepository.findByMemberEmail(memberAccount.getEmail()).orElseThrow(NotExistStoreException::new);
        owner.plusVisitor(visitor.getNum());
    }

}
