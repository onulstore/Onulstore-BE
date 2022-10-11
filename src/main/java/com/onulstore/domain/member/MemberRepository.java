package com.onulstore.domain.member;

import com.onulstore.domain.enums.Authority;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNum(String phoneNum);

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    Optional<Member> findByPhoneNum(String phoneNum);

    List<Member> findAllByAuthorityAndCreatedDateAfter(
        Authority roleUser, LocalDateTime localDateTime);

    Long countByAuthorityAndCreatedDateAfter(Authority roleUser, LocalDateTime localDateTime);
}
