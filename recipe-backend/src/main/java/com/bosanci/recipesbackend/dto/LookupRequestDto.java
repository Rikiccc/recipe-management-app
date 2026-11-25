package com.bosanci.recipesbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LookupRequestDto {
    @NotBlank(message = "Naziv stavke je obavezan.")
    @Size(min = 2, max = 100, message = "Naziv mora imati između 2 i 100 karaktera.")
    private String name;
}