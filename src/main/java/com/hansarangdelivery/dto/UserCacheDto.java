package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.DeliveryAddress;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCacheDto implements Serializable {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private List<DeliveryAddress> addressList = new ArrayList<>();

    public UserCacheDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.addressList = user.getAddressList();
    }
}
