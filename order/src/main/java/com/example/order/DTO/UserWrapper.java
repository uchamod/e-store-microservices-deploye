package com.example.order.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper {


    private String userName;
    private String userEmail;
    private String userRole;
    private String userStatus;
    private String userPhone;
    private String userAddress;
}
