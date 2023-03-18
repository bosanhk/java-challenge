package jp.co.axa.apidemo.dto;

import lombok.Getter;
import lombok.Setter;

public class EmployeeDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String nickname;


    @Getter
    @Setter
    private String firstName;


    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private Integer salary;

    @Getter
    @Setter
    private String department;
}
