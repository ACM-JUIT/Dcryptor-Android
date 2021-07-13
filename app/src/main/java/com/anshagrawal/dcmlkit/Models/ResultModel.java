package com.anshagrawal.dcmlkit.Models;

public class ResultModel {
    private String[] decoded_Text;

    public ResultModel(String[] decoded_Text) {
        this.decoded_Text = decoded_Text;
    }

    public String[] getDecoded_Text() {
        return decoded_Text;
    }

    public void setDecoded_Text(String[] decoded_Text) {
        this.decoded_Text = decoded_Text;
    }
}
