package com.flicker.bff.dto.user.photocard;

import lombok.Data;

import java.util.List;

@Data
public class PhotoCardListDto {
    private List<PhotoCardDto> photoCardDtoList;
}
