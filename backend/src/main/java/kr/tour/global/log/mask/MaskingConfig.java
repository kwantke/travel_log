package kr.tour.global.log.mask;


import kr.tour.global.log.mask.strategy.MaskingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class MaskingConfig {

    @Bean
    public Map<MaskingType, MaskingStrategy> maskingStrategyMap(List<MaskingStrategy> strategies) {
        return strategies.stream()
                .collect(Collectors.toUnmodifiableMap(MaskingStrategy::type, Function.identity()));
    }
}
