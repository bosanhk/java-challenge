package jp.co.axa.apidemo.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="EMPLOYEE")
public class Employee {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @NotNull(message="Employee username is required.")
    @Size(min=1, message="Employee username is required.")
    @Column(name="EMPLOYEE_USERNAME", unique=true)
    private String username;


    @Getter
    @Setter
    @NotNull(message="Employee first name is required.")
    @Size(min=1, message="Employee first name is required.")
    @Column(name="EMPLOYEE_FIRST_NAME")
    private String firstName;


    @Getter
    @Setter
    @NotNull(message="Employee last name is required.")
    @Size(min=1, message="Employee last name is required.")
    @Column(name="EMPLOYEE_LAST_NAME")
    private String lastName;

    @Getter
    @Setter
    @NotNull(message="Employee salary is required.")
    @Column(name="EMPLOYEE_SALARY")
    private Integer salary;

    @Getter
    @Setter
    @NotNull(message="Employee department is required.")
    @Size(min=1, message = "Employee department is required.")
    @Column(name="DEPARTMENT")
    private String department;

}
