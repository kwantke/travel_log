package kr.tour.travelogue.application;

import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueCountry;
import kr.tour.travelogue.domain.enums.CountryCode;
import kr.tour.travelogue.dto.request.TravelogueRequest;
import kr.tour.travelogue.infrastructure.TravelogueCountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TravelogueCountryService {

  private final TravelogueCountryRepository travelogueCountryRepository;

  @Transactional
  public List<TravelogueCountry> createTravelogueCountries(Travelogue travelogue, TravelogueRequest request) {
    Map<CountryCode, Long> countryCounts = countCountries(request);

    return countryCounts.entrySet().stream()
            .map(entry -> travelogueCountryRepository.save(
                    new TravelogueCountry(travelogue, entry.getKey(), entry.getValue().intValue()))
            )
            .toList();
  }

  private Map<CountryCode, Long> countCountries(TravelogueRequest request) {
    return request.days().stream()
            .flatMap(day -> day.places().stream())
            .map(place -> CountryCode.valueOf(place.countryCode()))
            .filter(countryCode -> countryCode != CountryCode.NONE)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

}
