package com.drinkeg.drinkeg.exception;


import com.drinkeg.drinkeg.apipayLoad.code.ReasonDTO;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
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