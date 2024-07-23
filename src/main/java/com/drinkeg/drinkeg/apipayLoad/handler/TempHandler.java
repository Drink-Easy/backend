package com.drinkeg.drinkeg.apipayLoad.handler;

import com.drinkeg.drinkeg.apipayLoad.code.BaseCode;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseCode errorCode) { super((ErrorStatus) errorCode); }
}