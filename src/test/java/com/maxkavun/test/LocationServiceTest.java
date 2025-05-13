package com.maxkavun.test;

import com.maxkavun.config.AppConfig;
import com.maxkavun.dto.LocationWithWeatherDto;
import com.maxkavun.entity.Location;
import com.maxkavun.entity.Session;
import com.maxkavun.entity.User;
import com.maxkavun.repository.LocationRepository;
import com.maxkavun.repository.SessionRepository;
import com.maxkavun.service.LocationService;
import com.maxkavun.service.client.OpenWeatherClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {AppConfig.class})
public class LocationServiceTest {

    private LocationService locationService;
    private OpenWeatherClient openWeatherClient;
    private LocationRepository locationRepository;
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        openWeatherClient = mock(OpenWeatherClient.class);
        locationRepository = mock(LocationRepository.class);
        sessionRepository = mock(SessionRepository.class);

        locationService = new LocationService(openWeatherClient, locationRepository, sessionRepository);
    }

    @Test
    void shouldSaveLocationIfNotExists(){
        var locationWithWeatherDto = mock(LocationWithWeatherDto.class);
        var sessionUUID  = UUID.randomUUID();
        var sessionId   = sessionUUID.toString();
        var user  = User.builder().login("max").build();
        var session = Session.builder().id(sessionUUID).user(user).build();

        var cityName = "Warsaw";
        var lat = new BigDecimal("52.237049");
        var lon = new BigDecimal("21.017532");

        when(sessionRepository.findById(sessionUUID)).thenReturn(Optional.of(session));
        when(locationRepository.findByUserAndLatitudeAndLongitude(user, lat, lon)).thenReturn(Optional.empty());
        when(openWeatherClient.getLocationByCoordinates(lat , lon))
                .thenReturn(Optional.of(locationWithWeatherDto));

        locationService.saveLocationIfAbsent(cityName, lat , lon , sessionId);

        verify(locationRepository , times(1)).save(any(Location.class));
    }


    @Test
    void shouldDeleteLocationIfExists() {
        var sessionUUID  = UUID.randomUUID();
        var sessionId   = sessionUUID.toString();
        var lat = new BigDecimal("52.237049");
        var lon = new BigDecimal("21.017532");

        var user  = mock(User.class);
        var session = Session.builder().id(sessionUUID).user(user).build();
        var location = Location.builder().locationName("Warsaw").build();

        Set<Location> userLocations = new HashSet<>();
        userLocations.add(location);

        when(sessionRepository.findById(sessionUUID)).thenReturn(Optional.of(session));
        when(locationRepository.findByUserAndLatitudeAndLongitude(user, lat, lon)).thenReturn(Optional.of(location));
        when(user.getLocations()).thenReturn(userLocations);

        locationService.deleteLocationIfExists(lat, lon, sessionId);

        verify(locationRepository, times(1)).delete(location);
        assertFalse(userLocations.contains(location), "Location should have been removed from the user's locations");
    }

}
