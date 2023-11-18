package com.twohoseon.app.dto.apple;

import javax.naming.AuthenticationException;
import java.util.List;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ApplePublicKeyResponse
 * @date : 11/17/23 4:39 PM
 * @modifyed : $
 **/
public record ApplePublicKeyResponse(List<ApplePublicKey> keys) {
    public ApplePublicKey getMatchedKey(String kid, String alg) throws AuthenticationException {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("Not found matched key"));
    }
}
