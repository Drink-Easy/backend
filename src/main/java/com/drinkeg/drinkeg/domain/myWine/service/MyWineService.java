package com.drinkeg.drinkeg.domain.myWine.service;

import com.drinkeg.drinkeg.domain.myWine.dto.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWineResponse;

import java.util.List;

public interface MyWineService {

    void saveMyWine(MyWineRequest myWineRequest, String username);

    List<MyWineResponse> getMyWinesByUsername(String username);
}
