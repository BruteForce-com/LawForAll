package com.bruteforce.userasaservice.model;



import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
/// Represents a legal case in the system, tracking all case-related information and relationships.
///
/// This entity stores comprehensive case information including case details, status, and relationships
/// with users (public and lawyer). It is mapped to the `legal_cases` table in the database.
///
/// ### Key Features:
///
///     - Case tracking with detailed metadata
///     - Bidirectional relationships with User entities
///     - Support for various case types and statuses
///     - Financial claim tracking with proper decimal precision
///     - Built-in audit fields for tracking case lifecycle
///
/// @see User
/// @see CaseType
/// @see CaseStatus
/// @since 1.0.0
/// @author @shivaverbandi - BruteForce
///
@Entity
@Table(
        name = "legal_cases",
        indexes = {
                @Index(name = "idx_case_status", columnList = "status"),
                @Index(name = "idx_case_public_user", columnList = "public_user_id"),
                @Index(name = "idx_case_lawyer", columnList = "lawyer_id")
        }
)
public class LegalCase {

    /// The unique identifier for the case (UUID).
    ///
    /// This is the primary key of the LegalCase entity and is used to identify
    /// the case in the database. It is generated automatically using the UuidGenerator.
    ///
    /// @see UuidGenerator
    ///
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "case_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID caseId;

    /// The unique case number for external reference (e.g., "CASE-2023-00456").
    ///
    /// This field is unique and is used to identify the case in external systems.
    /// It is stored as a string and has a maximum length of 100 characters.
    ///
    @Column(name = "case_number", unique = true, length = 100)
    private String caseNumber;
    /// The title of the case, typically a brief summary of the case.
    ///
    /// This field is required and has a maximum length of 255 characters.
    ///
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    /// The detailed description of the case, including background and key issues.
    ///
    /// This field is optional and can store a large amount of text (up to 2^31 - 1 characters).
    /// It is stored as a LOB (Large Object) in the database.
    ///
    @Lob
    @Column(name = "description")
    private String description;

    /// The type of the case (e.g., CIVIL, CRIMINAL, FAMILY, etc.).
    ///
    /// This field is required and is stored as a string with a maximum length of 50 characters.
    /// It is mapped to the `case_type` column in the database.
    ///
    @Enumerated(EnumType.STRING)
    @Column(name = "case_type", length = 50, nullable = false)
    private CaseType caseType;

    /// The status of the case (e.g., DRAFT, OPEN, IN_PROGRESS, etc.).
    ///
    /// This field is required and is stored as a string with a maximum length of 50 characters.
    /// It is mapped to the `status` column in the database.
    ///
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private CaseStatus status;

    /// The filing date of the case.
    ///
    /// This field is optional and is stored as a LocalDate.
    /// It is mapped to the `filing_date` column in the database.
    ///
    @Column(name = "filing_date")
    private LocalDate filingDate;

    /// The hearing date of the case.
    ///
    /// This field is optional and is stored as a LocalDate.
    /// It is mapped to the `hearing_date` column in the database.
    ///
    @Column(name = "hearing_date")
    private LocalDate hearingDate;

    /// The public user associated with the case.
    ///
    /// This field is required and is stored as a User entity.
    /// It is mapped to the `public_user_id` column in the database.
    ///
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "public_user_id", referencedColumnName = "user_id",
            nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_CASE_PUBLIC_USER"))
    private User publicUser;

    /// The lawyer associated with the case.
    ///
    /// This field is optional and is stored as a User entity.
    /// It is mapped to the `lawyer_id` column in the database.
    ///
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lawyer_id", referencedColumnName = "user_id",
            foreignKey = @ForeignKey(name = "FK_CASE_LAWYER"))
    private User lawyer;

    /// The name of the court associated with the case.
    ///
    /// This field is optional and is stored as a String.
    /// It is mapped to the `court_name` column in the database.
    ///
    @Column(name = "court_name", length = 255)
    private String courtName;


    /// The name of the judge presiding over the case.
    ///
    /// This field is optional and is stored as a String with a maximum length of 255 characters.
    /// It is mapped to the `judge_name` column in the database.
    @Column(name = "judge_name", length = 255)
    private String judgeName;

    /// The legal jurisdiction where the case is being heard.
    ///
    /// This field is optional and is stored as a String with a maximum length of 255 characters.
    /// It typically includes the geographical or legal authority (e.g., "New Delhi District Court").
    @Column(name = "jurisdiction", length = 255)
    private String jurisdiction;

    /// The legal basis or grounds for the case.
    ///
    /// This field is optional and can store a large amount of text (up to 2^31-1 characters).
    /// It should include applicable laws, regulations, or legal principles relevant to the case.
    @Lob
    @Column(name = "legal_basis")
    private String legalBasis;

    /// The monetary amount being claimed in the case.
    ///
    /// This field is optional and is stored as a decimal with 19 total digits and 2 decimal places.
    /// Example: 100000.00 for one lakh rupees.
    @Column(name = "claimed_amount", precision = 19, scale = 2)
    private BigDecimal claimedAmount;

    /// The timestamp when the case was created in the system.
    ///
    /// This field is automatically set when the case is first persisted and cannot be updated.
    /// It is mapped to the `created_at` column in the database.
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /// The timestamp when the case was last updated in the system.
    ///
    /// This field is automatically updated whenever the case is modified.
    /// It is mapped to the `updated_at` column in the database.
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    // Default constructor
    public LegalCase() {
    }

    // Constructor with all fields
    public LegalCase(CaseType caseType, String caseNumber, String description, CaseStatus status,
                     LocalDate filingDate, LocalDate hearingDate, User publicUser, User lawyer) {
        this.caseType = caseType;
        this.caseNumber = caseNumber;
        this.description = description;
        this.status = status;
        this.filingDate = filingDate;
        this.hearingDate = hearingDate;
        this.publicUser = publicUser;
        this.lawyer = lawyer;
    }

    // Getters & Setters

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getLegalBasis() {
        return legalBasis;
    }

    public void setLegalBasis(String legalBasis) {
        this.legalBasis = legalBasis;
    }

    public BigDecimal getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(BigDecimal claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public CaseStatus getStatus() {
        return status;
    }

    public void setStatus(CaseStatus status) {
        this.status = status;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public LocalDate getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(LocalDate hearingDate) {
        this.hearingDate = hearingDate;
    }

    public User getPublicUser() {
        return publicUser;
    }

    public void setPublicUser(User publicUser) {
        this.publicUser = publicUser;
    }

    public User getLawyer() {
        return lawyer;
    }

    public void setLawyer(User lawyer) {
        this.lawyer = lawyer;
    }


    // Optionally builder-style methods, equals/hashCode, toString


    public static class LegalCaseBuilder {
        private CaseType caseType;
        private String caseNumber;
        private String description;
        private CaseStatus status;
        private LocalDate filingDate;
        private LocalDate hearingDate;
        private User publicUser;
        private User lawyer;

        public LegalCaseBuilder() {
        }

        public LegalCaseBuilder caseType(CaseType caseType) {
            this.caseType = caseType;
            return this;
        }

        public LegalCaseBuilder caseNumber(String caseNumber) {
            this.caseNumber = caseNumber;
            return this;
        }

        public LegalCaseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public LegalCaseBuilder status(CaseStatus status) {
            this.status = status;
            return this;
        }

        public LegalCaseBuilder filingDate(LocalDate filingDate) {
            this.filingDate = filingDate;
            return this;
        }

        public LegalCaseBuilder hearingDate(LocalDate hearingDate) {
            this.hearingDate = hearingDate;
            return this;
        }

        public LegalCaseBuilder publicUser(User publicUser) {
            this.publicUser = publicUser;
            return this;
        }

        public LegalCaseBuilder lawyer(User lawyer) {
            this.lawyer = lawyer;
            return this;
        }

        public LegalCase build() {
            return new LegalCase(caseType, caseNumber, description, status, filingDate, hearingDate, publicUser, lawyer);
        }
    }
}

