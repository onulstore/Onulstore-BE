package com.onulstore.web.controller;

import com.onulstore.service.PaymentService;
import com.onulstore.web.dto.PaymentDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Api(tags = {"Payment-Controller"})
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ApiOperation(value = "결제하기")
    public ResponseEntity<PaymentDto.PaymentResponse> paying(
        @RequestBody PaymentDto.PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.paying(paymentRequest));
    }

}
