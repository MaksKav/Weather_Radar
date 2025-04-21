package com.maxkavun.controller;

import com.maxkavun.dto.CitySearchForm;
import com.maxkavun.service.LocationService;
import com.maxkavun.service.client.OpenWeatherClient;
import com.maxkavun.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@Controller
public class WeatherPageController {

    private final OpenWeatherClient openWeatherClient;
    private final LocationService locationService;

    public WeatherPageController(OpenWeatherClient openWeatherClient, LocationService locationService) {
        this.openWeatherClient = openWeatherClient;
        this.locationService = locationService;
    }


    @GetMapping("/home")
    public String homePage(HttpServletRequest request, Model model) {
        var sessionId = CookieUtil.getSessionIdFromCookie(request);
        var userWithLocations = locationService.getUserWithLocations(sessionId);
        model.addAttribute("user", userWithLocations.username());
        model.addAttribute("locations", userWithLocations.locations());
        model.addAttribute("cityNameForm", new CitySearchForm());
        return "home";

    }


    @GetMapping("/search")
    public String searchLocationByName(@Valid @ModelAttribute("cityNameForm") CitySearchForm cityName, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        var citiesList = openWeatherClient.getLocationsByCityName(cityName.getCityName());
        model.addAttribute("citiesList", citiesList);
        model.addAttribute("cityNameForm", new CitySearchForm());
        return "search-page";
    }


    @PostMapping("/modifyLocation")
    public String addLocation(
            @RequestParam("action") String action,
            @RequestParam("cityName") String cityName,
            @RequestParam("latitude") BigDecimal latitude,
            @RequestParam("longitude") BigDecimal longitude,
            HttpServletRequest request) {
        String cookieStringUUID = CookieUtil.getSessionIdFromCookie(request);

        if (cookieStringUUID != null) {
            switch (action) {
                case "add" -> locationService.saveLocationIfAbsent(cityName, latitude, longitude, cookieStringUUID);
                case "delete" -> locationService.deleteLocationIfExists(latitude, longitude, cookieStringUUID);
                default -> {
                    return "error";
                }
            }
        }
        return "redirect:/home";
    }


}
