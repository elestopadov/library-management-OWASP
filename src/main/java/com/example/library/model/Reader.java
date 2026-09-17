package com.example.library.model;

import java.util.Objects;

/**
 * Represents a library reader.
 */
public final class Reader {
    private final String id;
    private final String name;
    private final String email;

    /**
     * Creates a reader.
     *
     * @param id unique reader identifier
     * @param name reader name
     * @param email reader email address
     */
    public Reader(String id, String name, String email) {
        this.id = requireText(id, "id");
        this.name = requireText(name, "name");
        this.email = requireEmail(email);
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    private static String requireEmail(String value) {
        requireText(value, "email");
        if (!value.matches("^[^\\s@]+@[^\\s@]+$")) {
            throw new IllegalArgumentException("email has invalid format");
        }
        return value;
    }

    /**
     * Returns the unique reader identifier.
     *
     * @return reader identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the reader name.
     *
     * @return reader name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the reader email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Compares readers by their unique identifiers.
     *
     * @param other object to compare
     * @return {@code true} when both objects represent the same reader id
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Reader reader)) {
            return false;
        }
        return Objects.equals(id, reader.id);
    }

    /**
     * Returns the hash code derived from the unique reader identifier.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a readable representation of the reader without exposing the email address.
     *
     * @return string representation containing non-sensitive identifying fields
     */
    @Override
    public String toString() {
        return "Reader{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
