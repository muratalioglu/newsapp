package com.example.newsapp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class NewsInDTO {

    @NotBlank
    @Size(max = 255)
    String title;

    @NotNull
    @Size(max = 65535)
    String content;

    @Size(max = 255)
    String imageUrl;
}