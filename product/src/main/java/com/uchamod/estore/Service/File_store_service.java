package com.uchamod.estore.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class File_store_service {
    private final S3Client s3Client;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.folder.products:products}")
    private String productsFolder;

//    public File_store_service(S3Client s3Client) {
//        this.s3Client = s3Client;
//    }
    //upload file to s3 and return url
    public String uploadFile(MultipartFile file)throws IOException{
        //validateFile
        validateFile(file);

        //generate quniqe file name
        String fileName=generateUniqueFileName(file.getOriginalFilename());
        String key=productsFolder+"/"+fileName;

        try{
           //upload file to s3
            PutObjectRequest putObjectRequest=PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(),file.getSize()));
            return getPublicUrl(key);
        }catch (Exception e){
            throw new IOException("Failed to upload file to S3: " + e.getMessage(), e);
        }
    }
   //get public url
    private String getPublicUrl(String key) {
        return String.format("https://%s.s3.ap-south-1.amazonaws.com/%s",
                bucketName,key);
    }//https://productsimagedata.s3.amazonaws.com/ap-south-1
    //generate unique file name
    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
   //validate file
    private void validateFile(MultipartFile file)throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // Check file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IOException("File size exceeds maximum limit of 10MB");
        }
        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !isValidImageType(contentType)) {
            throw new IOException("Invalid file type. Only image files are allowed.");
        }
    }
    //validate image type
    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp");
    }
    //delete image file
    public Boolean deleteFile(String imageFile){
        try{
           String key=extractKeyFromUrl(imageFile);
           if(key == null){
               return false;
           }
            DeleteObjectRequest deleteObjectRequest=DeleteObjectRequest.builder().
                    bucket(bucketName).
                    key(key).
                    build();
            s3Client.deleteObject(deleteObjectRequest);
            return true;
        }catch (S3Exception e){
            System.err.println("Failed to delete file from S3: " + e.getMessage());
            return false;
        }
    }
    //extract image key from full url
//https://productsimagedata.s3.ap-south-1.amazonaws.com/ -54 products/e1c3722b-8a58-4360-90d8-614dacb99b31.jpeg
    private String extractKeyFromUrl(String url) {
        if (url == null) {
            return null;
        }
        try{
           return url.substring(54);
        }catch (Exception e){
            System.err.println("Error extracting key from URL: " + e.getMessage());
            return null;
        }
    }
}
