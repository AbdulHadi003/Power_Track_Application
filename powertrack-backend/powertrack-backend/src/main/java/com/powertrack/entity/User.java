package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.powertrack.enums.UserRole;
import com.powertrack.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be 11 digits")
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role = UserRole.CUSTOMER;

    @Column(columnDefinition = "TEXT")
    private String address;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    // ========== RELATIONSHIPS ==========

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Meter> meters;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<MeterRequest> meterRequests;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Bill> bills;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<InstallmentRequest> installmentRequests;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ChatConversation> customerConversations;

    @OneToMany(mappedBy = "csr")
    @JsonIgnore
    private List<ChatConversation> csrConversations;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<SystemNotification> notifications;
}