package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.model.Location;
import com.software_engineering.weather_clothes.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    /** 모든 지역 정보를 가져옴. */
    public List<String> getAllRegions() {
        return locationRepository.findDistinctRegion();
    }

    /**
     * @param region 검색할 지역 이름
     * @return 주어진 지역에 속하는 구역 이름의 목록
     */
    public List<String> getDistrictsByRegion(String region) {
        return locationRepository.findDistinctDistrictByRegion(region);
    }

    /**
     * @param region 검색할 지역 이름
     * @param district 검색할 구역 이름
     * @return 주어진 지역 및 구역에 속하는 도시 이름의 목록
     */
    public List<String> getTownsByRegionAndDistrict(String region, String district) {
        return locationRepository.findDistinctTownByRegionAndDistrict(region, district);
    }

    /**
     * 주어진 지역, 구역 및 도시에 해당하는 위치 정보를 가져옴.
     *
     * @param region 검색할 지역 이름
     * @param district 검색할 구역 이름
     * @param town 검색할 도시 이름
     * @return 주어진 지역, 구역 및 도시에 해당하는 {@link Location} 객체
     * @throws RuntimeException 위치를 찾을 수 없는 경우
     */
    public Location getLocation(String region, String district, String town) {
        return locationRepository.findByRegionAndDistrictAndTown(region, district, town)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }
}
