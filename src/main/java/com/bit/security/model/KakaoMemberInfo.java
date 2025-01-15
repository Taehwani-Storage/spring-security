package com.bit.security.model;

import lombok.Data;

import java.util.Map;

@Data
public class KakaoMemberInfo {
    private Map<String, Object> attributes;
    private Map<String, Object> accountAttribute;
    private Map<String, Object> profileAttribute;

    public KakaoMemberInfo(Map<String, Object> paramMap) {
        attributes = paramMap;
        accountAttribute = (Map<String, Object>) paramMap.get("kakao_account");
        profileAttribute = (Map<String, Object>) paramMap.get("profile");
    }

    public String getProviderId() {
        return attributes.get("id").toString();
    }

    public String getProvider() {
        return "kakao";
    }

    public String getName() {
        return accountAttribute.get("nickname").toString();
    }

    public String getEmail() {
        return accountAttribute.get("email").toString();
    }

}
