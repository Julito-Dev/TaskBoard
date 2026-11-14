package com.taskboard.repository.sqlite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

abstract class SqliteRepositoryTestBase {

    @TempDir
    protected Path tempDir;

    protected Database database;
    protected String dbUrl;

    @BeforeEach
    void setUpDatabase() {
        dbUrl = "jdbc:sqlite:" + tempDir.resolve("taskboard-test.db");
        database = new Database(dbUrl);
        database.initialize();
    }

    protected Database reopenDatabase() {
        return new Database(dbUrl);
    }
}
