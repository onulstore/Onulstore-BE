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
    List<Integer> members = new ArrayList<>();
    List<Integer> products = new ArrayList<>();
    List<Integer> categorycounts = new ArrayList<>();
    List<Integer> orders = new ArrayList<>();
    List<Integer> customerPosts = new ArrayList<>();
}
