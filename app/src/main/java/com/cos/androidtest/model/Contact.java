package com.cos.androidtest.model;


import android.util.Log;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Contact {
    private Long id;
    private String name;
    private String tel;

    @Override
    public String toString() {
        return "Contact{" +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
