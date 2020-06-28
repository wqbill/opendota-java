package com.wqbill.opendota.commons;

import lombok.Data;

@Data
public class Url {
    String url;
    Boolean noRetry;
    Boolean raw;
    Boolean json;
}
