package com.bruteforce.userasaservice.model;

/// Represents a type of legal case.
///
/// This enumeration is used to categorize legal cases based on their type.
/// It is used to filter and search for cases in the system.
///
/// @author shivaverabandi - BruteForce.com
/// @version 1.0
/// @since 2025-10-22
public enum CaseType {
    /// Civil case type.
    CIVIL,
    /// Criminal case type.
    CRIMINAL,
    /// Family case type.
    FAMILY,
    /// Consumer case type.
    CONSUMER,
    /// Writ case type.
    WRIT,
    /// Other case type, for cases that do not fit into the above categories.
    OTHER
}