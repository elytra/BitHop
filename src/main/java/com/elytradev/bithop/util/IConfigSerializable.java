package com.elytradev.bithop.util;

public interface IConfigSerializable {
    public String toConfigString();
    public boolean matches(String configName);
}
