package com.codegym.jira.profile.internal.web;

import com.codegym.jira.profile.ContactTo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class ProfilePostRequest {
    private Set<@NotBlank String> mailNotifications;

    private @Valid ContactTo[] contacts;
}
