package com.min.bunjang.productinquire.controller;

import com.min.bunjang.productinquire.repository.ProductInquireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInquireService {
    private final ProductInquireRepository productInquireRepository;


}
