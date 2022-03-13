package com.example.dnatask.user;

import javax.validation.constraints.NotBlank;

record CreateUserRequest(@NotBlank String name) {
}
