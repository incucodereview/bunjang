package com.min.bunjang.member.repository;

import com.min.bunjang.member.model.JoinTempMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinTempMemberRepository extends JpaRepository<JoinTempMember, String> {
}
