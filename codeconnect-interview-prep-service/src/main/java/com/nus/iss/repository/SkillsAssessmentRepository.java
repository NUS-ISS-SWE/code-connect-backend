package com.nus.iss;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsAssessmentRepository extends JpaRepository<SkillsAssessment, Long> {
}