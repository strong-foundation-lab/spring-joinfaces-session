package io.strongfoundation.joinfaces.session;

public enum SessionEnum {
    USERNAME("username"),
    ROLE("role"),
    BEGIN_AT("beginAt"),
    EXPIRATION_AT("expirationAt"),
    UUID("uuid");

    private String key;

    private SessionEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
