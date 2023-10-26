package com.tys.survey.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tys.survey.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public FileUploadService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private boolean fileCheck(MultipartFile file){
        if (file.isEmpty()){
            log.info("파일이 비어잇습니다.");
            return false;
        }
        log.info("파일이 비어있지 않습니다..");
        return true;
    }

    public FileDTO saveFile(MultipartFile file) throws RuntimeException {
        String originalFilename = null;
        String ext;
        String renameFileName = null;
        String path = null;
        if (fileCheck(file)){
            originalFilename = file.getOriginalFilename();
            ext = originalFilename.substring(originalFilename.indexOf("."));
            renameFileName = UUID.randomUUID() + ext;
            log.info("renameFileName : {}",renameFileName);

            try {
                // s3 업로드
                amazonS3.putObject(new PutObjectRequest(bucket, renameFileName, file.getInputStream(), new ObjectMetadata())
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                log.error("S3 파일 업로드 중 오류 발생", e);
                throw new RuntimeException("S3 파일 업로드 중 오류 발생", e);
            }

            // 업로드 경로
            path = amazonS3.getUrl(bucket, renameFileName).toString();
        }
        return FileDTO.builder()
                    .fileName(originalFilename)
                    .rename(renameFileName)
                    .path(path)
                    .build();
    }

    public void deleteFile(String renameFileName){
        if (!amazonS3.doesObjectExist(bucket, renameFileName)) {
            throw new AmazonS3Exception(renameFileName + " 파일이 존재하지 않습니다.");
        }
        amazonS3.deleteObject(bucket, renameFileName);
        log.info("S3 파일이 삭제 되었습니다.");
    }

}
