package com.wqbill.opendota.json;

import com.wqbill.opendota.util.FieldUtils;
import lombok.Data;

@Data
public class Field {
    private String originalName;
    private String type;
    private String capitalName;

    public String getCapitalName() {
        return FieldUtils.capitalFirst(originalName);
    }

    private String name;

    public String getName() {
        return FieldUtils.underlineToCamel(name);
    }
    private String subType;
    private boolean array;
    private boolean object;
}
