package com.management.appuser;

import com.management.appuser.model.User;
import com.management.appuser.model.UserPatchDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.time.Month.JANUARY;

public class TestUtil {

    private final static LocalDateTime TIME_TEST = LocalDateTime.of(1900, JANUARY, 1, 1, 1, 0, 0);

    public User aUser(int id, String name, String date) {
        return User.builder()
                .id(id)
                .name(name)
                .birthDate(LocalDate.parse(date))
                .createdAt(TIME_TEST).build();
    }

    public UserPatchDTO aModifiedUser(String name, String birthDate) {
        return UserPatchDTO.builder()
                .name(name)
                .birthDate(birthDate)
                .build();
    }

    public List<User> aListOfUsers() {
        return Arrays.asList(
                aUser(1, "name1", "1980-01-01"),
                aUser(2, "name2", "1980-02-02")
        );
    }

    public String aListJSonResponse() {
        return "[{\"id\":\"1\",\"name\":\"name1\",\"birthDate\":\"1980-01-01\",\"createdAt\":\"1900-01-01 01:01:00\"}," +
                "{\"id\":\"2\",\"name\":\"name2\",\"birthDate\":\"1980-01-02\",\"createdAt\":\"1900-01-01 01:01:00\"}]";
    }

    public String aJSonResponse() {
        return "{\"id\":\"1\",\"name\":\"name1\",\"birthDate\":\"1980-01-01\",\"createdAt\":\"1900-01-01 01:01:00\"}";
    }
}
