package com.musafir.foodApi.service.impl;

import com.musafir.foodApi.DTO.FoodRequest;
import com.musafir.foodApi.DTO.FoodResponse;
import com.musafir.foodApi.entity.FoodEntity;
import com.musafir.foodApi.repo.FoodRepo;
import com.musafir.foodApi.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodServiceImpl implements FoodService {
    private final S3Client s3Client;
    private final FoodRepo foodRepo;
    private final ModelMapper mapper;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        log.info("Starting file upload to S3 bucket: {}", bucketName);
        String fileName = file.getOriginalFilename();
        if(fileName == null || !fileName.contains(".")){
            log.error("Invalid file name {}",fileName);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File is not valid");
        }
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);
        String key = UUID.randomUUID().toString()+"."+fileExtension;

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .acl("public-read")
                    .build();
            log.debug("Uploading file with key: {}", key);

            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            if(response.sdkHttpResponse().isSuccessful()){
                log.info("file upload successful {}",fileName);
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            }else{
                log.error("s3 upload failed.HTTP response {}",response.sdkHttpResponse().statusCode());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"file upload was not successful");
            }
        }catch (IOException ex){
            log.error("IOException occurred while uploading file {}",ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occured while uploading the file");
        }catch (Exception ex) {
            log.error("Unexpected error occurred during file upload", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred while uploading the file");
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest foodRequest, MultipartFile file) {
        log.info("adding food in db");
        FoodEntity newFoodEntity = mapper.map(foodRequest, FoodEntity.class);
        String imageUrl = uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);
        FoodEntity savedFoodEntity = foodRepo.save(newFoodEntity);
        log.info("added food successfully in db");
        return mapper.map(savedFoodEntity,FoodResponse.class);
    }

    @Override
    public List<FoodResponse> readFoods() {
        log.info("Fetching all foods from the database");
        List<FoodResponse> foods = foodRepo.findAll()
                .stream()
                .map(foodEntity -> mapper.map(foodEntity,FoodResponse.class))
                .toList();
        log.info("Successfully fetched {} food items", foods.size());
        return foods;
    }

    @Override
    public FoodResponse readFoods(String id) {
      FoodEntity foodEntity = foodRepo.findById(id).orElseThrow(()->new RuntimeException("Food item not found with id: "+id));
        return mapper.map(foodEntity,FoodResponse.class);
    }

    @Override
    public boolean deleteFile(String name) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(name)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        log.info("Deleting file with id {}",id);
        FoodResponse response = readFoods(id);
        String imageUrl = response.getImageUrl();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isFileDeleted = deleteFile(fileName);
        if(isFileDeleted){
            log.info("file deleted successfuly");
            foodRepo.deleteById(id);
        }else{
            log.error("error while deleteing file");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Some issue occured while deleting file");
        }
    }


}
