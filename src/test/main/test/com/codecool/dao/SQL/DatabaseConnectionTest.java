package com.codecool.dao.SQL;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void testIfConnectionIsNotNull() {
        assertNotNull(DatabaseConnection.getConntectionToDatabase());
    }

    @Test
    void testIfConnectionIsValid() {
        try {
            assertTrue(DatabaseConnection.getConntectionToDatabase().isValid(10));
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    @Test
    void testIfConnectionIsNotClosed() {
        try {
            assertFalse(DatabaseConnection.getConntectionToDatabase().isClosed());
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}