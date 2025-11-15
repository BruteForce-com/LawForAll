package com.bruteforce.lawforall.model;

/// Represents the status of a legal case in the system.
///
/// The CaseStatus enumeration defines the possible lifecycle states of a legal case.
///
///   - [#DRAFT] - Initial draft submitted by the public user.
///   - [#OPEN] - Open status, visible to the public user.
///   - [#IN_PROGRESS] - the lawyer is working on Case.
///   - [#CLOSED] - Case is closed by the lawyer.
///   - [#APPEALED] - Case is appealed to the higher court.
///
public enum CaseStatus {
    /// Initial draft submitted by the public user.
    DRAFT,

    /// Open status, visible to the public user.
    OPEN,

    /// The lawyer is working on the case.
    IN_PROGRESS,

    /// The lawyer closes the Case.
    CLOSED,

    /// The Case is appealed to the higher court.
    APPEALED
}

