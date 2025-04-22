package com.maxkavun.test;

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
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

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
        LocationWithWeatherDto locationWithWeatherDto = mock(LocationWithWeatherDto.class);
        UUID sessionUUID  = UUID.randomUUID();
        String sessionId   = sessionUUID.toString();
        User user  = User.builder().login("max").build();
        Session session = Session.builder().id(sessionUUID).user(user).build();

        String cityName = "Warsaw";
        BigDecimal lat = new BigDecimal("52.237049");
        BigDecimal lon = new BigDecimal("21.017532");

        when(sessionRepository.findById(sessionUUID)).thenReturn(Optional.of(session));
        when(locationRepository.findByUserAndLatitudeAndLongitude(user, lat, lon)).thenReturn(Optional.empty());
        when(openWeatherClient.getLocationByCoordinates(lat , lon))
                .thenReturn(Optional.of(locationWithWeatherDto));

        locationService.saveLocationIfAbsent(cityName, lat , lon , sessionId);

        verify(locationRepository , times(1)).save(any(Location.class));
    }


    @Test
    void shouldDeleteLocationIfExists() {
        UUID sessionUUID  = UUID.randomUUID();
        String sessionId   = sessionUUID.toString();
        BigDecimal lat = new BigDecimal("52.237049");
        BigDecimal lon = new BigDecimal("21.017532");

        User user  = mock(User.class);
        Session session = Session.builder().id(sessionUUID).user(user).build();
        Location location = Location.builder().locationName("Warsaw").build();

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
