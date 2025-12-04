package com.bruteforce.lawforall.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "rag_metadata")
public class RagMetaData {

    @Id
    @Column(name = "file_name", nullable = false, length = 30)
    private String fileName; // Primary key

    @Column(name = "date_of_upload", nullable = false, updatable = false)
    private LocalDate dateOfUpload;

    @ElementCollection
    @CollectionTable(
            name = "rag_document_ids",
            joinColumns = @JoinColumn(name = "file_name")
    )
    @Column(name = "document_id")
    private List<String> documentIds;



    public RagMetaData() {

    }

    public RagMetaData(String fileName, List<String> documentIds) {
        this.fileName = fileName;
        this.documentIds = documentIds;
    }

    @PrePersist
    protected void onCreated() { // This method is called before the new entity is persisted
        this.dateOfUpload = LocalDate.now();
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDate getDateOfUpload() {
        return dateOfUpload;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }
    public void setDocumentIds(List<String> documentIds) {
        this.documentIds = documentIds;
    }


    public static RagMetaData.Builder builder() {
        return new RagMetaData.Builder();
    }

    public static class Builder {

        private String fileName;
        private List<String> documentIds;

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder documentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return this;
        }

        public RagMetaData build() {
            return new RagMetaData(fileName, documentIds);
        }
    }

}
