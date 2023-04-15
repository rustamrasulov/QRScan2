package com.miirrr.qrscan.services.web;

import java.io.UnsupportedEncodingException;

public interface WebService {
    WebService getInstance();
    String request(String method) throws UnsupportedEncodingException;

}
