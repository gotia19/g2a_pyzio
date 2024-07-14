package com.capgemini.stk.categories;

public interface Status {
    interface GUI {
        interface Development {}

        interface Done {}

        interface Blocked {}
    }

    interface REST_API {
        interface Development {}

        interface Done {}
    }
}