package com.bruteforce.userasaservice.model;


public enum VerificationStatus {
    NOT_VERIFIED,  // Default state, no verification started
    REQUESTED,     // Verification request submitted by the lawyer
    VERIFIED,      // Successfully verified by admin
    REJECTED,      // Verification request  denied by admin
    CLOSED         // Lawyer withdrew the verification request
}