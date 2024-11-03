package com.software_engineering.weather_clothes.controller;

import com.software_engineering.weather_clothes.model.Location;
import com.software_engineering.weather_clothes.service.LocationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 위치와 관련된 정보를 처리.
 * 사용자로부터 지역을 선택받고, 선택된 지역정보를 쿠키에 저장.
 */
@Controller
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * 위치 선택 페이지를 반환한다.
     */
    @GetMapping("/select")
    public String selectLocationPage(Model model) {
        model.addAttribute("regions", locationService.getAllRegions());
        return "location/select-location";
    }

    /**
     * 특정 지역에 해당하는 구(district) 목록을 반환.
     *
     * @param region 지역 이름
     * @return 해당 지역의 구 목록
     */
    @GetMapping("/districts")
    @ResponseBody
    public List<String> getDistricts(@RequestParam String region) {
        return locationService.getDistrictsByRegion(region);
    }

    /**
     * 특정 지역과 구에 해당하는 동(town) 목록을 반환.
     *
     * @param region  지역 이름
     * @param district 구 이름
     * @return 해당 지역과 구의 동 목록
     */
    @GetMapping("/towns")
    @ResponseBody
    public List<String> getTowns(@RequestParam String region, @RequestParam String district) {
        return locationService.getTownsByRegionAndDistrict(region, district);
    }

    /**
     * 사용자가 선택한 위치 정보를 쿠키에 저장.
     *
     * @param region 지역
     * @param district 구
     * @param town    동
     * @param response HTTP 응답 객체
     * @return 위치 정보 저장 성공 메시지를 포함한 응답
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<?> saveLocation(@RequestParam String region,
                                          @RequestParam String district,
                                          @RequestParam String town,
                                          HttpServletResponse response) {
        Location location = locationService.getLocation(region, district, town);

        // 쿠키 생성 및 저장
        Cookie regionCookie = new Cookie("userRegion", region);
        Cookie districtCookie = new Cookie("userDistrict", district);
        Cookie townCookie = new Cookie("userTown", town);
        Cookie nxCookie = new Cookie("userNx", String.valueOf(location.getNx()));
        Cookie nyCookie = new Cookie("userNy", String.valueOf(location.getNy()));

        // 쿠키 설정
        int maxAge = 60 * 60 * 24 * 30; // 30일
        regionCookie.setMaxAge(maxAge);
        districtCookie.setMaxAge(maxAge);
        townCookie.setMaxAge(maxAge);
        nxCookie.setMaxAge(maxAge);
        nyCookie.setMaxAge(maxAge);

        // 쿠키의 Path 설정
        regionCookie.setPath("/"); // 모든 경로에서 유효하도록 설정
        districtCookie.setPath("/");
        townCookie.setPath("/");
        nxCookie.setPath("/");
        nyCookie.setPath("/");

        response.addCookie(regionCookie);
        response.addCookie(districtCookie);
        response.addCookie(townCookie);
        response.addCookie(nxCookie);
        response.addCookie(nyCookie);

        return ResponseEntity.ok().body(Map.of("message", "Location saved successfully"));
    }
}
