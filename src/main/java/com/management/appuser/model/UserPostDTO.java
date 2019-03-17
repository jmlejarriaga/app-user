package com.management.appuser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPostDTO {

    String id;

    @Size(max = 50)
    String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate;
}
