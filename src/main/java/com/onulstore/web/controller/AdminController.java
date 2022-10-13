package com.onulstore.web.controller;

import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.service.AuthService;
import com.onulstore.service.MemberService;
import com.onulstore.service.OrderService;
import com.onulstore.service.ProductService;
import com.onulstore.service.QuestionService;
import com.onulstore.service.ReviewService;
import com.onulstore.web.dto.DailyStatisticDto;
import com.onulstore.web.dto.DashboardDto;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.RequestTimeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Api(tags = {"Admin-Controller"})
public class AdminController {

    private final AuthService authService;
    private final MemberService memberService;
    private final ProductService productService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final QuestionService questionService;

    @ApiOperation(value = "입점사 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberDto.MemberResponse> sellerRegistration(
        @RequestBody MemberDto.SellerRequest request) {
        return ResponseEntity.ok(authService.sellerRegistration(request));
    }

    @ApiOperation(value = "전체 회원 조회 / 인증 필요(관리자)")
    @GetMapping("/find")
    public ResponseEntity<Map<String, List<Member>>> viewAllMember() {
        return ResponseEntity.ok().body(authService.viewAllMember());
    }

    @ApiOperation(value = "대쉬보드 전체 / 인증 필요(관리자)")
    @PostMapping("/dashboard")
    public ResponseEntity<DashboardDto> getEntireDashboard(
        @RequestBody RequestTimeDto requestTimeDto) {
        LocalDateTime localDateTime = requestTimeDto.getLocalDateTime();
        List<Long> members = memberService.memberDashBoard(localDateTime);
        List<Long> products = productService.productDashBoard(
            localDateTime);
        List<Long> categoryCounts = orderService.salesByCategory(localDateTime);
        List<Long> orders = orderService.salesAmount(localDateTime);
        Long order = orderService.orderDashBoard(localDateTime);
        Long question = questionService.questionDashBoard(localDateTime);
        Long review = reviewService.reviewDashBoard(localDateTime);
        List<Long> customerPosts = Arrays.asList(order, question, review);
        List<Long> paidOrders = orderService.paidAndDeliver(requestTimeDto.getLocalDateTime());
        DashboardDto dashboard = DashboardDto.builder()
            .members(members)
            .products(products)
            .categorycounts(categoryCounts)
            .orders(orders)
            .customerPosts(customerPosts)
            .paidOrders(paidOrders)
            .build();

        return ResponseEntity.ok(dashboard);
    }

    @ApiOperation(value = "대쉬보드 회원 / 인증 필요(관리자)")
    @PostMapping("/dashboard/members")
    public ResponseEntity<List<Long>> getMemberInfo(@RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(memberService.memberDashBoard(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대쉬보드 상품 / 인증 필요(관리자)")
    @PostMapping("/dashboard/products")
    public ResponseEntity<List<Long>> getProductInfo(
        @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(
            productService.productDashBoard(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대쉬보드 카테고리 / 인증 필요(관리자)")
    @PostMapping("/dashboard/categories")
    public ResponseEntity<List<Long>> getCategoryInfo(
        @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(orderService.salesByCategory(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대쉬보드 판매금액 아이템 수 / 인증 필요(관리자)")
    @PostMapping("/dashboard/orders")
    public ResponseEntity<List<Long>> getOrderInfo(@RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(orderService.salesAmount(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대쉬보드 질문과 리뷰 / 인증 필요(관리자)")
    @PostMapping("dashboard/customers")
    public ResponseEntity<List<Long>> getCustomerPostInfo(
        @RequestBody RequestTimeDto requestTimeDto) {
        List<Long> reviewAndQuestion = Arrays.asList(
            orderService.orderDashBoard(requestTimeDto.getLocalDateTime()),
            questionService.questionDashBoard(requestTimeDto.getLocalDateTime()),
            reviewService.reviewDashBoard(requestTimeDto.getLocalDateTime())
        );
        return ResponseEntity.ok(reviewAndQuestion);
    }

    @ApiOperation(value = "대쉬보드 주문 / 인증 필요(관리자)")
    @PostMapping("dashboard/paymentorders")
    public ResponseEntity<List<Long>> getOrderAmounts(@RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(orderService.paidAndDeliver(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대쉬보드 통계 / 인증 필요(관리자)")
    @PostMapping("dashboard/dailystatistic")
    public ResponseEntity<List<Long>> getDailyStatistic(
        @RequestBody DailyStatisticDto dailyStatisticDto) {
        return ResponseEntity.ok(
            orderService.dailyOrderStatistic(dailyStatisticDto.getLocalDateTime(),
                dailyStatisticDto.getOrderStatus()));
    }
}
