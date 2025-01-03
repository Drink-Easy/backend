package com.drinkeg.drinkeg.domain.myWine.service;

import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineUpdateRequest;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWineResponse;

import java.util.List;

public interface MyWineService {

    Long saveMyWine(MyWineRequest myWineRequest, String username);

    List<MyWineResponse> getMyWinesByUsername(String username);

    void updateMyWine(Long wineWishlistId, MyWineUpdateRequest myWineUpdateRequest, String username);
}
