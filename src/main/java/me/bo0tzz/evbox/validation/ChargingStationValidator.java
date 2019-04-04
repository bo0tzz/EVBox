package me.bo0tzz.evbox.validation;

import me.bo0tzz.evbox.model.ChargingStation;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChargingStationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ChargingStation.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {

        ChargingStation chargingStation = (ChargingStation) target;
        if (chargingStation.getStationId() <= 0) {
            e.rejectValue("stationId", "stationId.invalid");
        }

    }
}
