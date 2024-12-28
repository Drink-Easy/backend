package com.drinkeg.drinkeg.global.exception;


import com.drinkeg.drinkeg.global.apipayLoad.code.ReasonDTO;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public GeneralException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ReasonDTO getErrorStatus() {
        return this.errorStatus.getReason();
    }

}