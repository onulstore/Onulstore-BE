package com.onulstore.web.controller;

import com.onulstore.domain.member.Member;
import com.onulstore.service.AuthService;
import com.onulstore.service.MemberService;
import com.onulstore.service.OrderService;
import com.onulstore.service.ProductService;
import com.onulstore.service.QuestionService;
import com.onulstore.service.ReviewService;
import com.onulstore.web.dto.DashboardDto;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.RequestTimeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @ApiOperation(value = "전체 회원 조회")
    @GetMapping("/find")
    public ResponseEntity<Map<String, List<Member>>> viewAllMember() {
        return ResponseEntity.ok().body(authService.viewAllMember());
    }

    @ApiOperation(value = "대쉬보드 전체")
    @PostMapping("/dashboard")
    public ResponseEntity<DashboardDto> getEntireDashboard(@RequestBody RequestTimeDto requestTimeDto) {
        LocalDateTime localDateTime = requestTimeDto.getLocalDateTime();
        List<Integer> members = memberService.memberDashBoard(localDateTime); // 회원 수, 입점사 수
        List<Integer> products = productService.productDashBoard(
            localDateTime); //등록한 아이템 수, 판매중 아이템 수
        List<Integer> categoryCounts = orderService.salesByCategory(localDateTime); // 카테고리별 판매수
        List<Integer> orders = orderService.salesAmount(localDateTime); // 판매금액, 판매된 아이템 수
        Integer order = orderService.orderDashBoard(localDateTime); // 구매확정된 주문 수
        Integer question = questionService.questionDashBoard(localDateTime); // 문의 수
        Integer review = reviewService.reviewDashBoard(localDateTime);
        List<Integer> customerPosts = Arrays.asList(orderService.orderDashBoard(localDateTime),
            questionService.questionDashBoard(localDateTime),
            reviewService.reviewDashBoard(localDateTime)
        );
        DashboardDto dashboard = DashboardDto.builder()
            .members(members)
            .products(products)
            .categorycounts(categoryCounts)
            .orders(orders)
            .customerPosts(customerPosts)
            .build();

        return ResponseEntity.ok(dashboard);
    }

    @ApiOperation(value = "대쉬보드 회원")
    @PostMapping("/dashboard/members")
    public ResponseEntity<List<Integer>> getMemberInfo(@RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(memberService.memberDashBoard(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대쉬보드 상품")
    @PostMapping("/dashboard/products")
    public ResponseEntity<List<Integer>> getProductInfo(
        @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(
            productService.productDashBoard(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대쉬보드 카테고리")
    @PostMapping("/dashboard/categories")
    public ResponseEntity<List<Integer>> getCategoryInfo(
        @RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(orderService.salesByCategory(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대쉬보드 주문")
    @PostMapping("/dashboard/orders")
    public ResponseEntity<List<Integer>> getOrderInfo(@RequestBody RequestTimeDto requestTimeDto) {
        return ResponseEntity.ok(orderService.salesAmount(requestTimeDto.getLocalDateTime()));
    }

    @ApiOperation(value = "대쉬보드 질문과 리뷰")
    @PostMapping("dashboard/customers")
    public ResponseEntity<List<Integer>> getCustomerPostInfo(
        @RequestBody RequestTimeDto requestTimeDto) {
        List<Integer> reviewAndQuestion = Arrays.asList(
            orderService.orderDashBoard(requestTimeDto.getLocalDateTime()),
            questionService.questionDashBoard(requestTimeDto.getLocalDateTime()),
            reviewService.reviewDashBoard(requestTimeDto.getLocalDateTime())
        );
        return ResponseEntity.ok(reviewAndQuestion);
    }
}
