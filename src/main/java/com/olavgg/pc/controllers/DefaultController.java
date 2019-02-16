package com.olavgg.pc.controllers;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.olavgg.pc.PasswordReader;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.validation.constraints.NotBlank;

@Controller
public class DefaultController {

    @Get("/sha1/{shasum}")
    public HttpStatus sha1(@NotBlank String shasum) {
        if(shasum.length() > 15){
            return checkShasum(shasum.substring(0, 16));
        }
        return HttpStatus.BAD_REQUEST;
    }

    @Get("/password/{password}")
    public HttpStatus password(@NotBlank String password) {
        String shasum =
            Hashing.sha1().hashString(
                password,
                Charsets.UTF_8
            )
            .toString()
            .substring(0,16);
        return checkShasum(shasum);
    }

    private HttpStatus checkShasum(String shasum){
        if(PasswordReader.FILTER.mightContain(
                shasum.toUpperCase().getBytes()
        )){
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

}


