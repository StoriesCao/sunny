package com.stories.sunny.db_model;

import org.litepal.crud.DataSupport;

/**
 * Created by Charlottecao on 8/31/17.
 * The DB module of provinces;
 */

public class Province extends DataSupport{

    private int id;

    private int provinceId;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
