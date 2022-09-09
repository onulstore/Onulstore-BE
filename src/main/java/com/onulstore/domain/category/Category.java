package com.onulstore.domain.category;

import com.onulstore.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String categoryName;

    @Column(length = 10)
    private String categoryCode;

    @Column(length = 10)
    private String upperCategoryCode;

}
