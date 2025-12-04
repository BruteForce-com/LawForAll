package com.bruteforce.lawforall.dto;

import java.time.LocalDate;

public class RagMetaDataInfo {

    private String fileName;
    private LocalDate dateOfUpload;

    public RagMetaDataInfo() {
    }

    public RagMetaDataInfo(String fileName, LocalDate dateOfUpload) {
        this.fileName = fileName;
        this.dateOfUpload = dateOfUpload;
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDate getDate() {
        return dateOfUpload;
    }

    public void setDate(LocalDate dateOfUpload) {
        this.dateOfUpload = dateOfUpload;
    }

    public static RagMetaDataInfo.RagMetaDataInfoBuilder builder() {
        return new RagMetaDataInfo.RagMetaDataInfoBuilder();
    }

    // Builder Pattern
    public static class RagMetaDataInfoBuilder {
        private String fileName;
        private LocalDate dateOfUpload;

        public RagMetaDataInfoBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public RagMetaDataInfoBuilder dateOfUpload(LocalDate dateOfUpload) {
            this.dateOfUpload = dateOfUpload;
            return this;
        }

        public RagMetaDataInfo build() {
            return new RagMetaDataInfo(fileName, dateOfUpload);
        }
    }
}
