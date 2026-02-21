package kr.tour.global.log.mask.strategy;


import kr.tour.global.log.mask.MaskingType;
import org.springframework.stereotype.Component;

@Component
public class FullMaskingStrategy implements MaskingStrategy {

    @Override
    public MaskingType type() {
        return MaskingType.FULL;
    }

    @Override
    public String mask(String value) {
        return "*****";
    }
}
