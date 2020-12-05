package com.alan.databee.common.token;

import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    public String genToken(String userName){
        return userName;
    }

    public String parseToken(String token){
        return token;
    }
}
