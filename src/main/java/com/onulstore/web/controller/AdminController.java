package com.onulstore.web.controller;

import com.onulstore.domain.member.Member;
import com.onulstore.service.AuthService;
import com.onulstore.service.MemberService;
import com.onulstore.service.OrderService;
import com.onulstore.service.ProductService;
import com.onulstore.service.QuestionService;
import com.onulstore.service.ReviewService;
import com.onulstore.web.dto.DashboardDto;
import com.onulstore.web.dto.DashboardDto.PaidAndDeliveredOrders;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.OrderDto;
import com.onulstore.web.dto.RequestTimeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @ApiOperation(value = "대시보드 전체 / 인증 필요(관리자)")
    @PostMapping("/dashboard")
    public ResponseEntity<DashboardDto> getEntireDashboard(
            @RequestBody RequestTimeDto requestTimeDto) {
        LocalDateTime localDateTime = requestTimeDto.getLocalDateTime();
        DashboardDto.DashboardMemberResponse memberDashBoard = memberService.memberDashBoard(
                localDateTime);
        DashboardDto.DashboardProductResponse productDashBoard = productService.productDashBoard(
                localDateTime);
        DashboardDto.DashboardCategoryResponse categoryDashboard = orderService.salesByCategory(
                localDateTime);
        DashboardDto.TotalSaleAmounts totalSaleAmounts = orderService.salesAmount(localDateTime);
        Long order = orderService.orderDashBoard(localDateTime);
        Long question = questionService.questionDashBoard(localDateTime);
        Long review = reviewService.reviewDashBoard(localDateTime);
        DashboardDto.CustomerPosts customerPosts = DashboardDto.CustomerPosts.builder()
                .orders(order)
                .questions(question)
                .reviews(review)
                .build();
        DashboardDto.PaidAndDeliveredOrders paidAndDeliveredOrders = orderService.paidAndDeliveredOrders(
                requestTimeDto.getLocalDateTime());
        DashboardDto.DailyStatistic dailyStatistic = orderService.dailyOrderStatistic(
                requestTimeDto.getLocalDateTime(), requestTimeDto.getOrderStatus());
        List<OrderDto.OrderResponse> orderResponse = orderService.recentOrders();

        DashboardDto dashboard = DashboardDto.builder()
                .dashboardMemberResponse(memberDashBoard)
                .dashboardProductResponse(productDashBoard)
                .dashboardCategoryResponse(categoryDashboard)
                .totalSaleAmounts(totalSaleAmounts)
                .customerPosts(customerPosts)
                .paidAndDeliveredOrders(paidAndDeliveredOrders)
                .dailyStatistic(dailyStatistic)
                .orderResponseList(orderResponse)
                .build();

        return ResponseEntity.ok(dashboard);
    }

    @ApiOperation(value = "대시보드 회원 / 인증 필요(관리자)")
    @PostMapping("/dashboard/members")
    public ResponseEntity<DashboardDto.DashboardMemberResponse> getMemberInfo(
            @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(memberService.memberDashBoard(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대시보드 상품 / 인증 필요(관리자)")
    @PostMapping("/dashboard/products")
    public ResponseEntity<DashboardDto.DashboardProductResponse> getProductInfo(
            @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(
                productService.productDashBoard(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대시보드 카테고리 / 인증 필요(관리자)")
    @PostMapping("/dashboard/categories")
    public ResponseEntity<DashboardDto.DashboardCategoryResponse> getCategoryInfo(
            @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(orderService.salesByCategory(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대시보드 판매금액 아이템 수 / 인증 필요(관리자)")
    @PostMapping("/dashboard/orders")
    public ResponseEntity<DashboardDto.TotalSaleAmounts> getOrderInfo(
            @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(orderService.salesAmount(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대시보드 질문과 리뷰 / 인증 필요(관리자)")
    @PostMapping("dashboard/customers")
    public ResponseEntity<DashboardDto.CustomerPosts> getCustomerPostInfo(
            @RequestBody RequestTimeDto requestTimeDto) {
        Long orders = orderService.orderDashBoard(requestTimeDto.getLocalDateTime());
        Long questions = questionService.questionDashBoard(requestTimeDto.getLocalDateTime());
        Long reviews = reviewService.reviewDashBoard(requestTimeDto.getLocalDateTime());
        DashboardDto.CustomerPosts customerPosts = DashboardDto.CustomerPosts.builder()
                .orders(orders)
                .questions(questions)
                .reviews(reviews)
                .build();
        return ResponseEntity.ok(customerPosts);
    }

    @ApiOperation(value = "대시보드 주문 / 인증 필요(관리자)")
    @PostMapping("dashboard/paymentOrders")
    public ResponseEntity<PaidAndDeliveredOrders> getOrderAmounts(
            @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(
                orderService.paidAndDeliveredOrders(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대시보드 통계 / 인증 필요(관리자)")
    @PostMapping("dashboard/dailyStatistic")
    public ResponseEntity<DashboardDto.DailyStatistic> getDailyStatistic(
            @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(
                orderService.dailyOrderStatistic(requestTimeDto.getLocalDateTime(),
                        requestTimeDto.getOrderStatus()));
    }
}
