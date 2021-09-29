package com.laioffer.staybooking.service;

import com.laioffer.staybooking.model.*;
import com.laioffer.staybooking.repository.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StayService {

    private StayRepository stayRepository;
    private ImageStorageService imageStorageService;


    @Autowired
    public StayService(StayRepository stayRepository, ImageStorageService imageStorageService) {
        this.stayRepository = stayRepository;
        this.imageStorageService = imageStorageService;
    }

    public Stay findByID(Long stayId) {
        return stayRepository.findById(stayId).orElse(null);
//        return stayRepository.getById(StayID);
    }

    public void delete(Long stayId) {
        stayRepository.deleteById(stayId);
    }

    public List<Stay> findByHost(String username) {
        return stayRepository.findByHost(new User.Builder()
                .setUsername(username)
                .build());
    }

    public void add(Stay stay, MultipartFile[] images) {
        LocalDate date = LocalDate.now().plusDays(1); // 得到现在时间， 加上一天后可以开始预定。
        List<StayAvailability> availabilities = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            availabilities.add(new StayAvailability.Builder()
                    .setId(new StayAvailabilityKey(stay.getId(), date))
                    .setStay(stay).setState(StayAvailabilityState.AVAILABLE)
                    .build());
            date = date.plusDays(1);
        }
        stay.setAvailabilities(availabilities);
// images 设置 教案写法
        // 平行上传文件
        List<String> mediaLinks = Arrays.stream(images)
                .parallel()
                // lambda 表达式， 输入image， 返回 xxx.save(image);
                .map(image -> imageStorageService.save(image))
                .collect(Collectors.toList());

        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            stayImages.add(new StayImage(mediaLink, stay));
        }
        stay.setImages(stayImages);
// 课上 的写法
//        List<StayImage> stayImages = new ArrayList<>();
//        for(MultipartFile image : images) {
//            String url = imageStorageService.save(image);
//            stayImages.add(new StayImage(url, stay));
//        }
//        stay.setImages(stayImages);


        stayRepository.save(stay); //stay 表格里 oneToMany 有cascade all， 只要更新stay 会同时存另外一个表里对应的
    }

}
