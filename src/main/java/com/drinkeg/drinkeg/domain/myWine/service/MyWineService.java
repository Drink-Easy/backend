package com.drinkeg.drinkeg.domain.myWine.service;

import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineUpdateRequest;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWineResponse;

import java.time.LocalDate;
import java.util.List;

public interface MyWineService {

    Long saveMyWine(MyWineRequest myWineRequest, String username);

    MyWineResponse getMyWineById(Long myWineId, String username, LocalDate now);

    List<MyWineResponse> getMyWinesByUsername(String username, LocalDate now);

    void updateMyWine(Long wineWishlistId, MyWineUpdateRequest myWineUpdateRequest, String username);

    void deleteMyWineById(Long myWineId, String username);
}
