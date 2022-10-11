package com.onulstore.web.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardDto {
    List<Long> members = new ArrayList<>();
    List<Long> products = new ArrayList<>();
    List<Long> categorycounts = new ArrayList<>();
    List<Long> orders = new ArrayList<>();
    List<Long> customerPosts = new ArrayList<>();
    List<Long> paidOrders = new ArrayList<>();
}
