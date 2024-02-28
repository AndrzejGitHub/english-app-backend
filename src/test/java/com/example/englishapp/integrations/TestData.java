package com.example.englishapp.integrations;

public class TestData {
    public static final String POST_ADD_USER = """
            {
                "firstName": "Basia",
                "lastName": "Nowak",
                "email": "basia@wp.pl",
                "password" : "#$Basia1234",
                "roles": [
                    "ROLE_USER"
                ]
            }
    """;

    public static final String ADDED_USER_SINGLE = """
            {
                "id": 1,
                "firstName": "Basia",
                "lastName": "Nowak",
                "email": "basia@wp.pl",
                "roles": ["ROLE_USER"]
            }
    """;

    public static final String ADDED_USER_ARRAY = """
            [
                {
                    "id": 1,
                    "firstName": "Basia",
                    "lastName": "Nowak",
                    "email": "basia@wp.pl",
                    "roles": ["ROLE_USER"]
                }
            ]
    """;


    public static final String PUT_EDIT_USER = """
            {
                "id": 1,
                "firstName": "Jan",
                "lastName": "Nowak",
                "email": "jan@wp.pl",
                "roles": ["ROLE_ADMIN"]
            }
    """;

    public static final String PUTTED_USER = """
           {
                "id": 1,
                "firstName": "Jan",
                "lastName": "Nowak",
                "email": "jan@wp.pl",
                "roles": ["ROLE_ADMIN"]
            }
    """;
}
