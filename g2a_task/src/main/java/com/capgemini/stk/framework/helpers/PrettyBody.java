package com.capgemini.stk.framework.helpers;

import io.restassured.internal.support.Prettifier;
import io.restassured.response.Response;

public class PrettyBody {
    public static String get(Response response) {
        Prettifier prettifier = new Prettifier();
        return prettifier.getPrettifiedBodyIfPossible(response, response);
    }
}