package com.wqbill.opendota.commons;

public interface Callback {
    <T> T execute(Object... args);
}
