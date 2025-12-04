package com.bruteforce.lawforall.service;

import com.bruteforce.lawforall.Utils.DtoConverter;
import com.bruteforce.lawforall.dto.RagMetaDataInfo;
import com.bruteforce.lawforall.model.RagMetaData;
import com.bruteforce.lawforall.repo.RagMetaDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * This service is responsible for loading a PDF file into a vector store.
 * This is achieved by using the PagePdfDocumentReader to read the PDF file,
 * a TokenTextSplitter to transform the text into tokens, and then the vector store
 * to load the tokens into the vector store.
 *
 * @author Brute Force
 */
@Component
public class PdfLoaderService {

    private static final Logger log = LoggerFactory.getLogger(PdfLoaderService.class);
    private final VectorStore vectorStore;
    private final RagMetaDataRepository ragMetaDataRepository;
    /**
     * Constructs a new PdfLoaderService with the given vector store.
     *
     * @param vectorStore the vector store to load the PDF file into
     */
    public PdfLoaderService(VectorStore vectorStore, RagMetaDataRepository ragMetaDataRepository) {
        this.vectorStore = vectorStore;
        this.ragMetaDataRepository = ragMetaDataRepository;
    }

    /**
     * Loads a PDF file into the vector store.
     * This is achieved by using the PagePdfDocumentReader to read the PDF file,
     * a TokenTextSplitter to transform the text into tokens, and then the vector store
     * to load the tokens into the vector store.
     *
     * @see PagePdfDocumentReader
     * @see TokenTextSplitter
     * @see VectorStore
     */
    public void loadPdfToVectorStore(String filePath) {
        // 1.Step => Create PDF reader
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
                filePath,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPagesPerDocument(1)
                        .build()
        );

        // 2.Step => Create text-splitter
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();

        // 3.Step => ETL Pipeline: Read -> Transform -> Load
        vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));

        // Alternative method syntax
        // vectorStore.write(tokenTextSplitter.split(pdfReader.read()));
    }


    /**
     * Loads a PDF file from a byte array into the vector store.
     * This is achieved by using the PagePdfDocumentReader to read the PDF file,
     * a TokenTextSplitter to transform the text into tokens, and then the vector store
     * to load the tokens into the vector store.
     *
     * @param extractedBytesFromPdf the byte array containing the PDF file
     * @param fileName the name of the PDF file
     * @return the RagMetaData object containing the document ids and file name
     * @throws IllegalStateException if a PDF file with the same name already exists
     * @throws RuntimeException if there is an error while loading the PDF file into the vector store
     */
    @Transactional
    public RagMetaData loadPdfFromBytes(byte[] extractedBytesFromPdf, String fileName) {

        // convert bytes to resources
        log.info("Entered into loadPdfFromBytes(param(2)=> bytes[],string) method in {}", PdfLoaderService.class.getName());

        Optional<RagMetaData> ragMetaData = ragMetaDataRepository.findById(fileName);
        if (ragMetaData.isPresent()) {
            log.info("PDF already exists with filename: {}", fileName);
            throw new IllegalStateException("PDF already exists with filename: " + fileName);
        }
        log.info("PDF does not exist with filename: {} in the method loadPdfFromBytes(2*param)", fileName);
        Resource resource = new ByteArrayResource(extractedBytesFromPdf);
        log.info("Extracted PDF file name: {}", fileName);
        //1.STEP: Extract => Create a PDF Reader
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPagesPerDocument(1)
                        .build()
        );


        // Store Document ids in the database to track and delete if we need further
        List<String> documentIds = reader.get().stream()
                .map(doc -> doc.getId())
                .toList();
        log.info("Document ids extracted from PDF file: {}", documentIds);


        //2. STEP: Transform => Create text-splitter
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();

        //3. STEP: Load => ETL Pipeline: Read -> Transform -> Load
        try{
            vectorStore.accept(tokenTextSplitter.apply(reader.get()));
        }catch (Exception e){
            throw new RuntimeException("Error while loading PDF file into vectorstore " + e.getMessage());
        }

        // Create ragMetaData to store documentIds and fileName
        RagMetaData newRagMetaData = RagMetaData.builder()
                .fileName(fileName)
                .documentIds(documentIds)
                .build();

        // save ragMetaData to the database
        newRagMetaData = ragMetaDataRepository.save(newRagMetaData);

        log.info("RagMetaData created successfully with filename: {} on Date {}", newRagMetaData.getFileName(),newRagMetaData.getDateOfUpload());


        return newRagMetaData;
    }

    /**
     * This method is responsible for fetching the list of RagMetaDataInfo objects from the database.
     * It uses the RagMetaDataRepository to find all the RagMetaData objects and then converts each RagMetaData object
     * into a RagMetaDataInfo object using the DtoConverter.convertRagMetaDataToRagMetaDataInfo method.
     *
     * @return a list of RagMetaDataInfo objects containing the file name and date of upload for each RagMetaData object
     */
    @Transactional(readOnly = true)
    public List<RagMetaDataInfo> getRagFileNames(){
        log.info("Entered into getRagFileNames() method in {}", PdfLoaderService.class.getName());
        return ragMetaDataRepository.findAll().stream()
                .map(ragMetaData -> DtoConverter.convertRagMetaDataToRagMetaDataInfo(ragMetaData))
                .toList();
    }


    /**
     * This method is responsible for deleting a PDF file from the vector store and the database.
     * It first checks if a PDF file with the given fileName exists in the database. If it does, it proceeds to delete the file.
     *
     * Step 1: Deletes the file from the vectorstore
     * Uses the VectorStore's delete method to delete the file's data from the vectorstore.
     *
     * Step 2: Deletes the file's metadata from the database
     * Uses the RagMetaDataRepository's deleteById method to delete the file's metadata from the database.
     *
     * @param fileName The name of the PDF file to delete.
     * @return A string indicating the success of the deletion operation.
     * @throws IllegalStateException if no PDF file with the given fileName exists in the database.
     * @throws RuntimeException if there is an error while deleting the PDF file from the vectorstore.
     */
    @Transactional
    public String deleteRagFile(String fileName){

        Optional<RagMetaData> ragMetaData = ragMetaDataRepository.findById(fileName);

        if(ragMetaData.isEmpty()){
            log.info("No PDF file found with filename: {}", fileName);
            throw new IllegalStateException("No PDF file found with filename: " + fileName);
        }

        log.info("PDF file found with filename: {} in the method deleteRagFile()", fileName);
        // Get Document ids from the database
        List<String> documentIds = ragMetaData.get().getDocumentIds();

        // delete the file's data from vector_store
        try{

            // Step 1: Delete the file from the vectorstore
            vectorStore.delete(documentIds);
            log.info("PDF file deleted successfully with filename: {}", fileName);

            // Step2: Delete the file's metadata from the database
            ragMetaDataRepository.deleteById(fileName);
            log.info("RagMetaData deleted successfully with filename: {}", fileName);
        }catch (Exception e){
            log.error("Error while deleting PDF file from vectorstore: {}", e.getMessage());
            throw new RuntimeException("Error while deleting PDF file from vectorstore: " + e.getMessage());
        }
        return "PDF file deleted successfully with filename: "+ fileName;
    }
}