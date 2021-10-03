package com.laioffer.staybooking.service;

import com.laioffer.staybooking.exception.StayDeleteException;
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
import com.laioffer.staybooking.repository.LocationRepository;
import com.laioffer.staybooking.repository.ReservationRepository;
@Service
public class StayService {

    private StayRepository stayRepository;
    private ImageStorageService imageStorageService;
    private LocationRepository locationRepository;
    private GeoEncodingService geoEncodingService;
    private ReservationRepository reservationRepository;

    @Autowired
    public StayService(StayRepository stayRepository, LocationRepository locationRepository, ReservationRepository reservationRepository, ImageStorageService imageStorageService, GeoEncodingService geoEncodingService) {
        this.stayRepository = stayRepository;
        this.locationRepository = locationRepository;
        this.imageStorageService = imageStorageService;
        this.geoEncodingService = geoEncodingService;
        this.reservationRepository = reservationRepository;
    }


    public Stay findByID(Long stayId) {
        return stayRepository.findById(stayId).orElse(null);
//        return stayRepository.getById(StayID);
    }
//lecture 33 修改 ， 因为有可能reserve 了

    public void delete(Long stayId) throws StayDeleteException {
        List<Reservation> reservations = reservationRepository.findByStayAndCheckoutDateAfter(new Stay.Builder().setId(stayId).build(), LocalDate.now());
        if (reservations != null && reservations.size() > 0) {
            throw new StayDeleteException("Cannot delete stay with active reservation");
        }
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

        Location location = geoEncodingService.getLatLng(stay.getId(), stay.getAddress());
        locationRepository.save(location);
    }

}
