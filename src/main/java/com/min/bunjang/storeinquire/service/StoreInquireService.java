package com.min.bunjang.storeinquire.service;

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

    @Transactional
    public InquireCreateResponse createStoreInquiry(InquireCreateRequest inquireCreateRequest) {
        Store owner = storeRepository.findById(inquireCreateRequest.getOwnerNum()).orElseThrow(NotExistStoreException::new);
        Store writer = storeRepository.findById(inquireCreateRequest.getWriterNum()).orElseThrow(NotExistStoreException::new);
        StoreInquire storeInquire = StoreInquire.of(owner.getNum(), writer, null, inquireCreateRequest.getInquireContent());

        StoreInquire savedStoreInquire = storeInquiryRepository.save(storeInquire);
        return new InquireCreateResponse(savedStoreInquire.getNum(), writer.getStoreName(), savedStoreInquire.getContent());
    }

    @Transactional
    public void deleteStoreInquire(Long inquireNum) {
        if (!storeInquiryRepository.existsByNum(inquireNum)) {
            throw new NotExistStoreInquireException();
        }
        storeInquiryRepository.deleteById(inquireNum);
    }
}
