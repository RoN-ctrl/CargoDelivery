package com.cargo.service.impl;

import com.cargo.dto.CityDto;
import com.cargo.model.City;
import com.cargo.model.enums.Region;
import com.cargo.repository.CityRepository;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.cargo.test.util.TestDataUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @InjectMocks
    private CityServiceImpl cityService;

    @Mock
    private CityRepository cityRepository;

    @Test
    void shouldCreateCity() {
        City city = testCreateCity();
        CityDto cityDto = testCreateCityDto();
        when(cityRepository.save(city)).thenReturn(city);

        CityDto testCityDto = cityService.createCity(cityDto);

        assertThat(testCityDto, getCityMatcher(city));

    }

    @Test
    void shouldGetCityById() {
        City city = testCreateCity();
        when(cityRepository.findById(ID)).thenReturn(Optional.of(city));

        CityDto cityDto = cityService.getCityById(ID);

        assertThat(cityDto, getCityMatcher(city));

    }

    @Test
    void shouldGetCityByName() {
        City city = testCreateCity();
        when(cityRepository.findByName(NAME)).thenReturn(Optional.of(city));

        CityDto cityDto = cityService.getCityByName(NAME);

        assertThat(cityDto, getCityMatcher(city));

    }

    @Test
    void shouldGetCitiesByRegion() {
        List<City> cities = new ArrayList<>();
        City city = testCreateCity();
        cities.add(city);
        when(cityRepository.findAllByRegion(REGION, Sort.by("name"))).thenReturn(cities);

        List<CityDto> cityDtos = cityService.getCitiesByRegion(REGION);

        assertThat(cityDtos.size(), equalTo(cities.size()));

    }

    @Test
    void shouldUpdateCity() {
        City city = testCreateCity();
        CityDto newCity = CityDto.builder()
                .name("New Lviv")
                .region(Region.REGION_SOUTH_AMERICA)
                .longitude(75.1)
                .latitude(23.6)
                .build();

        when(cityRepository.findById(ID)).thenReturn(Optional.of(city));
        when(cityRepository.save(ArgumentMatchers.any(City.class)))
                .thenReturn(cityService.mapCityDtoToCity(newCity));

        CityDto cityDto = cityService.updateCity(0, newCity);

        assertThat(cityDto, allOf(
                hasProperty("name", equalTo(newCity.getName())),
                hasProperty("region", equalTo(newCity.getRegion())),
                hasProperty("longitude", equalTo(newCity.getLongitude())),
                hasProperty("latitude", equalTo(newCity.getLatitude()))
        ));

    }

    @Test
    void shouldDeleteCityById() {
        City city = testCreateCity();
        when(cityRepository.findById(ID)).thenReturn(Optional.of(city));

        cityService.deleteCityById(ID);

        verify(cityRepository).delete(city);

    }

    @Test
    void shouldReturnCityDto_whenGivenCity() {
        City city = testCreateCity();

        CityDto cityDto = cityService.mapCityToCityDto(city);

        assertThat(cityDto, getCityMatcher(city));

    }

    @Test
    void shouldReturnCity_whenGivenCityDto() {
        CityDto cityDto = testCreateCityDto();

        City city = cityService.mapCityDtoToCity(cityDto);

        assertThat(city, allOf(
                hasProperty("id", equalTo(cityDto.getId())),
                hasProperty("name", equalTo(cityDto.getName())),
                hasProperty("region", equalTo(cityDto.getRegion())),
                hasProperty("longitude", equalTo(cityDto.getLongitude())),
                hasProperty("latitude", equalTo(cityDto.getLatitude()))
        ));

    }

    private Matcher<CityDto> getCityMatcher(City city) {
        return allOf(
                hasProperty("id", equalTo(city.getId())),
                hasProperty("name", equalTo(city.getName())),
                hasProperty("region", equalTo(city.getRegion())),
                hasProperty("longitude", equalTo(city.getLongitude())),
                hasProperty("latitude", equalTo(city.getLatitude()))
        );
    }
}