package com.nus.iss.repository;

import com.nus.iss.model.SkillsAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsAssessmentRepository extends JpaRepository<SkillsAssessment, Long> {
}