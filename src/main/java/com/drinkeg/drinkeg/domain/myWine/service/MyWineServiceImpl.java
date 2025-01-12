package com.drinkeg.drinkeg.domain.myWine.service;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.myWine.domain.MyWine;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineUpdateRequest;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWineResponse;
import com.drinkeg.drinkeg.domain.myWine.repository.MyWineRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyWineServiceImpl implements MyWineService{

    private final MemberRepository memberRepository;
    private final WineRepository wineRepository;
    private final MyWineRepository myWineRepository;

    @Override
    public Long saveMyWine(MyWineRequest myWineRequest, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        Long wineId = myWineRequest.getWineId();
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        MyWine myWine = MyWine.create(member, wine, myWineRequest.getPurchaseDate(), myWineRequest.getPurchasePrice());

        MyWine savedMyWine = myWineRepository.save(myWine);
        return savedMyWine.getId();
    }

    @Override
    public MyWineResponse getMyWineById(Long myWineId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        MyWine myWine = myWineRepository.findById(myWineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MY_WINE_NOT_FOUND));

        if (!member.equals(myWine.getMember())) {
            throw new GeneralException(ErrorStatus.MY_WINE_UNAUTHORIZED);
        } else {
            return MyWineResponse.of(myWine);
        }
    }

    @Override
    public List<MyWineResponse> getMyWinesByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        List<MyWine> myWineList = myWineRepository.findByMemberOrderByCreatedAt(member);

        return myWineList.stream().map(MyWineResponse::of).toList();
    }


    @Override
    public void updateMyWine(Long myWineId, MyWineUpdateRequest myWineUpdateRequest, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        MyWine myWine = myWineRepository.findById(myWineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MY_WINE_NOT_FOUND));

        if (!member.equals(myWine.getMember())) {
            throw new GeneralException(ErrorStatus.MY_WINE_UNAUTHORIZED);
        }

        myWine.update(myWineUpdateRequest.getPurchaseDate(), myWineUpdateRequest.getPurchasePrice());

        myWineRepository.save(myWine);
    }

    @Override
    public void deleteMyWineById(Long myWineId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        MyWine myWine = myWineRepository.findById(myWineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MY_WINE_NOT_FOUND));

        if(myWine.getMember().equals(member)) {
            myWineRepository.delete(myWine);
        } else {
            throw new GeneralException(ErrorStatus.MY_WINE_UNAUTHORIZED);
        }
    }
}
