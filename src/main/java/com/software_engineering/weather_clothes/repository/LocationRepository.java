package com.software_engineering.weather_clothes.repository;

import com.software_engineering.weather_clothes.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    /**
     * 모든 지역의 중복되지 않는 지역(시/도) 목록을 반환.
     *
     * @return 중복되지 않는 시/도 목록
     */
    @Query("SELECT DISTINCT l.region FROM Location l WHERE TRIM(l.region) != '' ORDER BY l.region")
    List<String> findDistinctRegion();


    /**
     * 특정 지역에 해당하는 중복되지 않는 군/구 목록을 반환.
     *
     * @param region 조회할 지역 이름
     * @return 해당 지역의 중복되지 않는 군/구 목록
     */
    @Query("SELECT DISTINCT l.district FROM Location l WHERE l.region = :region AND TRIM(l.district) != '' ORDER BY l.district")
    List<String> findDistinctDistrictByRegion(@Param("region") String region);


    /**
     * 특정 지역과 구에 해당하는 중복되지 않는 읍/면/동 목록을 반환.
     *
     * @param region   조회할 지역 이름
     * @param district 조회할 구 이름
     * @return 지역과 구에 해당하는 중복되지 않는 읍/면/동 목록
     */
    @Query("SELECT DISTINCT l.town FROM Location l WHERE l.region = :region AND l.district = :district AND TRIM(l.town) != '' ORDER BY l.town")
    List<String> findDistinctTownByRegionAndDistrict(@Param("region") String region, @Param("district") String district);


    /**
     * 해당 지역의 위치 정보를 반환.
     *
     * @param region   조회할 지역 이름
     * @param district 조회할 구 이름
     * @param town     조회할 동 이름
     * @return 일치하는 위치 정보가 포함된 객체
     */
    @Query("SELECT l FROM Location l " +
            "WHERE l.region = :region AND l.district = :district AND l.town = :town AND TRIM(l.region) != '' AND TRIM(l.district) != '' AND TRIM(l.town) != ''")
    Optional<Location> findByRegionAndDistrictAndTown(@Param("region") String region, @Param("district") String district, @Param("town") String town);
}
