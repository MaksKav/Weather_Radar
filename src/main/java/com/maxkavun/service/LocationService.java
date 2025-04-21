package com.maxkavun.service;

import com.maxkavun.dto.LocationWithWeatherDto;
import com.maxkavun.dto.UserLocationsDto;
import com.maxkavun.entity.Location;
import com.maxkavun.entity.Session;
import com.maxkavun.entity.User;
import com.maxkavun.exception.HttpClientException;
import com.maxkavun.exception.LocationServiceException;
import com.maxkavun.exception.RepositoryException;
import com.maxkavun.exception.SessionNotFoundException;
import com.maxkavun.repository.LocationRepository;
import com.maxkavun.repository.SessionRepository;
import com.maxkavun.service.client.OpenWeatherClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class LocationService {

    private final OpenWeatherClient openWeatherClient;
    private final LocationRepository locationRepository;
    private final SessionRepository sessionRepository;

    public LocationService(OpenWeatherClient openWeatherClient, LocationRepository locationRepository, SessionRepository sessionRepository) {
        this.openWeatherClient = openWeatherClient;
        this.locationRepository = locationRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public UserLocationsDto getUserWithLocations(String sessionStringUUID) {
        try {
            var user = getUserBySession(sessionStringUUID);
            var userLocations = locationRepository.findAllUserLocations(user);
            return enrichLocationsWithWeather(user.getLogin(), userLocations);
        } catch (RepositoryException | HttpClientException | SessionNotFoundException e) {
            log.error("Error while getting all user locations with sessionUUID: {}", sessionStringUUID, e);
            throw new LocationServiceException("Error while getting all user locations", e);
        }
    }


    @Transactional
    public void saveLocationIfAbsent(String cityName, BigDecimal lat, BigDecimal lon, String sessionStringUUID) {
        try {
            var user = getUserBySession(sessionStringUUID);
            var userLocation = locationRepository.findByUserAndLatitudeAndLongitude(user, lat, lon);

            if (userLocation.isPresent()) {
                log.warn("Location already exists for user: {}", user.getLogin());
                return;
            }

            var location = openWeatherClient.getLocationByCoordinates(lat, lon);
            if (location.isPresent()) {
                var loc = Location.builder()
                        .locationName(cityName)
                        .latitude(lat)
                        .longitude(lon)
                        .user(user)
                        .build();
                locationRepository.save(loc);
                log.info("Location: {} wad added successfully to user: {} ", loc, user.getLogin());
            }
        } catch (HttpClientException | RepositoryException | SessionNotFoundException e) {
            log.error("Error while adding location");
            throw new LocationServiceException("Error while added location for user", e);
        }
    }


    @Transactional
    public void deleteLocationIfExists(BigDecimal lat, BigDecimal lon, String sessionStringUUID) {
        try {
            var user = getUserBySession(sessionStringUUID);
            var userLocation = locationRepository.findByUserAndLatitudeAndLongitude(user, lat, lon);
            log.info("ВОТ ТАКАЯ ВОТ ЛОКАЦИЯ ЮЗЕРА: {}", userLocation); //TODO pum pum pum ... ne rabotaet
            log.info("ВОТ ТАКАЯ lat: {}, lon: {}", lat, lon);
            if (userLocation.isEmpty()) {
                log.warn("Location does not exist for user: {}", user.getLogin());
                throw new LocationServiceException("Failed to delete location: The location does not exist for the user.");
            } else {
                user.getLocations().remove(userLocation.get());
                locationRepository.delete(userLocation.get());
            }
            log.info("Location: {} was deleted successfully from user: {} ", userLocation.get(), user.getLogin());
        } catch (RepositoryException | SessionNotFoundException e) {
            log.error("Error while deleting location");
            throw new LocationServiceException("Error while deleting location for user", e);
        }
    }


    private UserLocationsDto enrichLocationsWithWeather(String username, List<Location> locations) {
        List<LocationWithWeatherDto> result = new ArrayList<>();
        for (Location location : locations) {
            var loc = openWeatherClient.getLocationByCoordinates(location.getLatitude(), location.getLongitude());
            if (loc.isPresent()) {
                loc.get().setLatitude(location.getLatitude());
                loc.get().setLongitude(location.getLongitude());
                result.add(loc.get());
            }

        }
        return new UserLocationsDto(username, result);
    }

    private Session getSession(String sessionStringUUID) {
        UUID sessionUUID = UUID.fromString(sessionStringUUID);
        var session = sessionRepository.findById(sessionUUID);
        if (session.isPresent()) {
            log.info("Session with UUID: {} was found successfully ", sessionStringUUID);
            return session.get();
        }
        throw new SessionNotFoundException("Session with UUID: " + sessionStringUUID + " not found");
    }

    private User getUserBySession(String sessionStringUUID) {
        var session = getSession(sessionStringUUID);
        return session.getUser();
    }
}
