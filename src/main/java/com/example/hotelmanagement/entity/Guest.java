package com.example.hotelmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "guests")
public class Guest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false, length = 60)
    private String firstName;

    @NotBlank @Column(nullable = false, length = 60)
    private String lastName;

    @Email @NotBlank @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 25)
    private String phone;

    public Guest() {}
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    @Transient public String getFullName() { return firstName + " " + lastName; }
}
