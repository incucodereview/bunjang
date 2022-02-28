package com.min.bunjang.store.storeinquiry.service;

import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.store.storeinquiry.dto.InquiryCreateRequest;
import com.min.bunjang.store.storeinquiry.dto.InquiryCreateResponse;
import com.min.bunjang.store.storeinquiry.model.StoreInquiry;
import com.min.bunjang.store.storeinquiry.repository.StoreInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreInquiryService {
    private final StoreInquiryRepository storeInquiryRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public InquiryCreateResponse createStoreInquiry(InquiryCreateRequest inquiryCreateRequest) {
        Store owner = storeRepository.findById(inquiryCreateRequest.getOwnerNum()).orElseThrow(NotExistStoreException::new);
        Store writer = storeRepository.findById(inquiryCreateRequest.getWriterNum()).orElseThrow(NotExistStoreException::new);
        StoreInquiry storeInquiry = StoreInquiry.of(owner.getNum(), writer.getNum(), null, inquiryCreateRequest.getInquiryContent());

        StoreInquiry savedStoreInquiry = storeInquiryRepository.save(storeInquiry);
        return new InquiryCreateResponse(savedStoreInquiry.getNum(), writer.getStoreName(), savedStoreInquiry.getContent());
    }
}
