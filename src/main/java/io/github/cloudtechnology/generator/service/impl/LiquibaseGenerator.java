package io.github.cloudtechnology.generator.service.impl;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.stereotype.Component;

import io.github.cloudtechnology.generator.service.SchemaVersioning;
import io.github.cloudtechnology.generator.vo.SchemaVo;
import liquibase.CatalogAndSchema;
import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.serializer.core.yaml.YamlChangeLogSerializer;
import liquibase.structure.core.Column;
import liquibase.structure.core.Data;
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Index;
import liquibase.structure.core.PrimaryKey;
import liquibase.structure.core.Sequence;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;
import liquibase.structure.core.View;

/**
 * https://www.liquibase.com/blog/3-ways-to-run-liquibase
 * https://stackoverflow.com/questions/33563763/could-not-find-databasechangelog-node-with-include/55604522
 * https://www.programcreek.com/java-api-examples/?code=chifei%2Fjweb-cms%2Fjweb-cms-master%2Fjweb-main%2Fsrc%2Ftest%2Fjava%2FMain.java
 * https://stackoverflow.com/questions/25490596/how-to-generate-change-log-file-in-liquibase-using-java
 * https://tolkiana.com/managing-database-migrations-with-liquibase-and-spring-boot/
 * 2023/04/20 generateChangeLog Deprecated 被棄用 待修改
 * https://stackoverflow.com/questions/76047207/liquibase-core-update-method-does-not-create-databasechangeloglock-table
 */
@Component
public class LiquibaseGenerator implements SchemaVersioning {

    @Override
    public void generate(SchemaVo schemaVo) throws Exception {
        Connection connection = null;
        String catalogName;
        String schemaName;

        try {

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                schemaVo.dbUrl(), schemaVo.dbUsername(), schemaVo.dbPassword());

            // database = new PostgresDatabase();

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            // Liquibase liquibase = new liquibase.Liquibase(
            //         "",
            //         new ClassLoaderResourceAccessor(),
            //         database);
            // liquibase.update(new Contexts(), new LabelExpression());

            // CatalogAndSchema catalogAndSchema = new CatalogAndSchema(catalogName,
            // schemaName);
            DiffToChangeLog changeLogWriter = new DiffToChangeLog(new DiffOutputControl(false, false, true, null));

            Path path = Paths.get(schemaVo.projectTempPath() + "/src/main/resources/db/changelog/history");
            Path pathCreate = Files.createDirectories(path);

            CommandScope generateChangelogCommand = new CommandScope("generateChangeLog");
            generateChangelogCommand.addArgumentValue("database", database);
            generateChangelogCommand.addArgumentValue("changelogFile", pathCreate.toAbsolutePath() + "/changelog-init.yaml");
            generateChangelogCommand.execute();

            // liquibase.generateChangeLog(CatalogAndSchema.DEFAULT, changeLogWriter,
            //         new PrintStream(new FileOutputStream(
            //                 pathCreate.toAbsolutePath() + "/changelog-init.yaml")),
            //         new YamlChangeLogSerializer(),
            //         this.snapshotTypes());

            connection.close();
        } catch (Exception e) {
            throw e;
        }

    }

    private Class[] snapshotTypes() {
        return new Class[] { UniqueConstraint.class, Sequence.class, Table.class, View.class, ForeignKey.class,
                PrimaryKey.class, Index.class, Column.class, Data.class };
    }

}