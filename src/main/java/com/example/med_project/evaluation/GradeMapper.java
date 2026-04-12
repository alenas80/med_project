package com.example.med_project.evaluation;

public final class GradeMapper {

    private GradeMapper() {
    }

    public static Grade fromScore(double score) {
        if (score >= 0.80) {
            return Grade.HIGH;
        }
        if (score >= 0.50) {
            return Grade.MEDIUM;
        }
        return Grade.LOW;
    }
}