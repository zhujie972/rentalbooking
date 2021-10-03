package com.laioffer.staybooking.repository;

import com.laioffer.staybooking.model.StayAvailability;
import com.laioffer.staybooking.model.StayAvailabilityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

public interface StayAvailabilityRepository extends JpaRepository<StayAvailability, StayAvailabilityKey> {
    // 因为在Mysql里，spring 可以直接翻译这个方法， 否则需要像 CustomLocationRepo 和 CustomLocationRepoImpl 一样需要单独实现
    @Query(value = "SELECT sa.id.stay_id FROM StayAvailability sa WHERE sa.id.stay_id IN ?1 AND sa.state = 0 AND sa.id.date BETWEEN ?2 AND ?3 GROUP BY sa.id.stay_id HAVING COUNT(sa.id.date) = ?4")
    List<Long> findByDateBetweenAndStateIsAvailable(List<Long> stayIds, LocalDate startDate, LocalDate endDate, long duration);

    // 10/02/ 上午课程添加的 新方法 ， 什么作用？ 根据输入查看可询问的日期
    @Query(value = "SELECT sa.id.date FROM StayAvailability sa WHERE sa.id.stay_id = ?1 AND sa.state = 0 AND sa.id.date BETWEEN ?2 AND ?3")
    List<LocalDate> countByDateBetweenAndId(Long stayId, LocalDate startDate, LocalDate endDate);

    @Modifying // 提示hibernate 这是modify语句
    @Query(value = "UPDATE StayAvailability sa SET sa.state = 1 WHERE sa.id.stay_id = ?1 AND sa.id.date BETWEEN ?2 AND ?3")
    void reserveByDateBetweenAndId(Long stayId, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query(value = "UPDATE StayAvailability sa SET sa.state = 0 WHERE sa.id.stay_id = ?1 AND sa.id.date BETWEEN ?2 AND ?3")
    void cancelByDateBetweenAndId(Long stayId, LocalDate startDate, LocalDate endDate);

}