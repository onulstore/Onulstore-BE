package com.onulstore.web.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class DashboardDto {

    private DashboardMemberResponse dashboardMemberResponse;
    private DashboardProductResponse dashboardProductResponse;
    private DashboardCategoryResponse dashboardCategoryResponse;
    private TotalSaleAmounts totalSaleAmounts;
    private CustomerPosts customerPosts;
    private PaidAndDeliveredOrders paidAndDeliveredOrders;
    private DailyStatistic dailyStatistic;
    private List<OrderDto.OrderResponse> orderResponseList;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DashboardMemberResponse {
        private Long memberCounts;
        private Long sellerCounts;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DashboardProductResponse {
        private Long onSaleProducts;
        private Long entireProducts;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DashboardCategoryResponse {
        private Long fashionCounts;
        private Long livingCounts;
        private Long beautyCounts;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TotalSaleAmounts {
        private Long totalPrice;
        private Long totalCount;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CustomerPosts {
        private Long questions;
        private Long reviews;
        private Long orders;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaidAndDeliveredOrders {
        private Long paidOrders;
        private Long deliveredOrders;
        private Long entireOrders;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DailyStatistic {
        private List<Long> statistic;
    }
}
