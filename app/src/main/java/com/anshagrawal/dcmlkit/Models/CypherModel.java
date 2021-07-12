package com.anshagrawal.dcmlkit.Models;

public class CypherModel {




    private String _id, stringtoDecode, decodedAt;

    public CypherModel(String id, String stringtoDecode, String decodedAt) {
        this._id = _id;
        this.stringtoDecode = stringtoDecode;
        this.decodedAt = decodedAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStringtoDecode() {
        return stringtoDecode;
    }

    public void setStringtoDecode(String stringtoDecode) {
        this.stringtoDecode = stringtoDecode;
    }

    public String getDecodedAt() {
        return decodedAt;
    }

    public void setDecodedAt(String decodedAt) {
        this.decodedAt = decodedAt;
    }
}
