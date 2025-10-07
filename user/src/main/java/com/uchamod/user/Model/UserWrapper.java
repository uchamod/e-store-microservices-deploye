package com.uchamod.user.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserWrapper {

    private String userName;
    private String userEmail;
    private String userRole;
    private String userStatus;
    private String userPhone;
    private String userAddress;

}
