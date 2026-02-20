package kr.tour.global.log.mask.strategy;


import kr.tour.global.log.mask.MaskingType;

public interface MaskingStrategy {

    MaskingType type();

    String mask(String value);
}
