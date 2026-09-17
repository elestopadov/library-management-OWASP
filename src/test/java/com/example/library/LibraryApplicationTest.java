package com.example.library;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class LibraryApplicationTest {
    @Test
    void shouldCreateLoanAsJson() throws Exception {
        String json = LibraryApplication.createSampleLoanJson();
        JsonNode root = new ObjectMapper().readTree(json);

        assertThat(root.get("book")).isNotNull();
        assertThat(root.get("book").isObject()).isTrue();
        assertThat(root.get("reader")).isNotNull();
        assertThat(root.get("reader").isObject()).isTrue();
        assertThat(root.get("loanDate")).isNotNull();
        assertThat(root.get("dueDate")).isNotNull();
        assertThat(root.has("returnDate")).isTrue();
        assertThat(root.path("returnDate").isNull()).isTrue();
        assertThat(root.path("open").asBoolean()).isTrue();
        assertThat(root.path("book").path("id").asText()).isEqualTo("book-1");
        assertThat(root.path("reader").path("id").asText()).isEqualTo("reader-1");
    }

    @Test
    void mainMethodShouldPrintJson() throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            LibraryApplication.main(new String[0]);
        } finally {
            System.setOut(originalOut);
        }

        JsonNode root = new ObjectMapper().readTree(output.toString(StandardCharsets.UTF_8));
        assertThat(root.has("book")).isTrue();
        assertThat(root.has("loanDate")).isTrue();
        assertThat(root.path("returnDate").isNull()).isTrue();
    }
}
