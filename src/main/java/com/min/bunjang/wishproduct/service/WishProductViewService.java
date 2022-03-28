package com.min.bunjang.wishproduct.service;

import com.min.bunjang.common.dto.PageDto;
import com.min.bunjang.common.exception.ImpossibleException;
import com.min.bunjang.member.exception.NotExistMemberException;
import com.min.bunjang.member.model.Member;
import com.min.bunjang.member.repository.MemberRepository;
import com.min.bunjang.store.exception.NotExistStoreException;
import com.min.bunjang.store.model.Store;
import com.min.bunjang.store.repository.StoreRepository;
import com.min.bunjang.wishproduct.dto.WishProductResponse;
import com.min.bunjang.wishproduct.dto.WishProductResponses;
import com.min.bunjang.wishproduct.model.WishProduct;
import com.min.bunjang.wishproduct.repository.WishProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishProductViewService {
    private final WishProductRepository wishProductRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    /**
     * 요청자와 상점오너의 비교를 왜 서비스 로직에 그냥 넣었는가? -> 다른곳에서 검증해도 어쩌피 디비에 쿼리를 날려 상점과 회원정보를 가져와야하기에 서비스 로직에선 무조건 쿼리를 날리므로 서비스에 구현하는것이 효율적이라 판단
     * */
    @Transactional(readOnly = true)
    public WishProductResponses findWishProductsByStore(String ownerEmail, Long storeNum, Pageable pageable) {
        Store store = storeRepository.findById(storeNum).orElseThrow(NotExistStoreException::new);
        Member member = memberRepository.findByEmail(ownerEmail).orElseThrow(NotExistMemberException::new);
        verifyImpossibleException(store, member);

        Page<WishProduct> wishProductPages = wishProductRepository.findByStore(store, pageable);
        return new WishProductResponses(
                WishProductResponse.listOf(wishProductPages.getContent()),
                new PageDto(pageable.getPageSize(), pageable.getPageNumber(), wishProductPages.getTotalElements())
        );
    }

    //TODO 좋은 메서드 이름이 아니다... 또 해당 로직이 여기에 있어야 하는지 엔티티에 있어야하는지도 불확정함. 변경 가능성 다분함.
    private void verifyImpossibleException(Store store, Member member) {
        if (store.verifyMatchMember(member.getEmail())) {
            throw new ImpossibleException("상점오너의 조회 요청이 아니므로 찜목록을 반환할 필요가 없습니다.");
        }
    }

}
