package com.TicketXChange.TicketXChange.auth.dtos;


import com.TicketXChange.TicketXChange.auth.model.UserProfileData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    private  boolean success;

    private  String message;

    private String error;

    private UserProfileData data;
}
