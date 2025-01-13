package com.drinkeg.drinkeg.global.apipayLoad.handler;

import com.drinkeg.drinkeg.global.apipayLoad.code.BaseCode;

import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseCode errorCode) { super((ErrorStatus) errorCode); }
}