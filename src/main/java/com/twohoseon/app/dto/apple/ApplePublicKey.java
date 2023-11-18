package com.twohoseon.app.dto.apple;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ApplePublicKey
 * @date : 11/17/23 4:38 PM
 * @modifyed : $
 **/
public record ApplePublicKey(String kty,
                             String kid,
                             String alg,
                             String n,
                             String e) {
}
