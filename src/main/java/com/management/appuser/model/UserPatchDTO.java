package com.management.appuser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPatchDTO {

    @Size(max = 50)
    String name;

    @Size(max = 10, message = "If you need to change birthday date, expected format is yyyy-MM-dd")
    String birthDate;
}
