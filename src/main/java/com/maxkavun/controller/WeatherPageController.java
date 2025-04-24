package com.maxkavun.controller;

import com.maxkavun.dto.CitySearchFormDto;
import com.maxkavun.service.LocationService;
import com.maxkavun.util.CookieUtils;
import com.maxkavun.validator.CitySearchValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Slf4j
@Controller
public class WeatherPageController {

    private final LocationService locationService;
    private final CitySearchValidator citySearchValidator;


    @GetMapping("/home")
    public String homePage(HttpServletRequest request, Model model) {
        var sessionId = CookieUtils.getSessionIdFromCookie(request);
        var userWithLocations = locationService.getUserWithLocations(sessionId);
        log.info("Successfully logged in user with locations: {}", userWithLocations);

        model.addAttribute("user", userWithLocations.username());
        model.addAttribute("locations", userWithLocations.locations());
        model.addAttribute("cityNameForm", new CitySearchFormDto());
        return "home";

    }


    @GetMapping("/search")
    public String searchLocationByName(@ModelAttribute("cityNameForm") CitySearchFormDto cityName, Model model, HttpServletRequest request) {
        var validationResult = citySearchValidator.validate(cityName.getCityName());
        if (validationResult.isPresent()) {
            log.warn("GET /search form error");
            model.addAttribute("cityNameError", validationResult.get());
            return "home";
        }

        var sessionId = CookieUtils.getSessionIdFromCookie(request);
        var userWithCitiesList = locationService.getUserWithLocationsWithInfo(sessionId, cityName.getCityName());
        model.addAttribute("user", userWithCitiesList.username());
        model.addAttribute("citiesList", userWithCitiesList.locations());
        model.addAttribute("cityNameForm", new CitySearchFormDto());
        return "search-page";
    }


    @PostMapping("/modifyLocation")
    public String addLocation(
            @RequestParam("action") String action,
            @RequestParam("cityName") String cityName,
            @RequestParam("latitude") BigDecimal latitude,
            @RequestParam("longitude") BigDecimal longitude,
            HttpServletRequest request) {

        var sessionStringUUID = CookieUtils.getSessionIdFromCookie(request);
        log.info("POST /modifyLocation: {} for city: {} with latitude: {} , longitude: {}  for session with UUID: {}" , action , cityName , latitude, longitude , sessionStringUUID);

        if (sessionStringUUID != null) {
            switch (action) {
                case "add" -> locationService.saveLocationIfAbsent(cityName, latitude, longitude, sessionStringUUID);
                case "delete" -> locationService.deleteLocationIfExists(latitude, longitude, sessionStringUUID);
                default -> {
                    log.error("Problem with client unbelievable action: {}" , action);
                    return "error";
                }
            }
        }
        return "redirect:/home";
    }


}
