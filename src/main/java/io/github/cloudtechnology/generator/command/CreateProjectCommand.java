package io.github.cloudtechnology.generator.command;

import java.nio.file.Path;

import io.github.cloudtechnology.generator.bo.BuildToolEnum;
import io.github.cloudtechnology.generator.bo.RuntimeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class CreateProjectCommand {
    Path projectTempPath;
    /**
     * Build type
     * gradle-project or maven-project
     */
    BuildToolEnum buildTool;
    String groupId;
    String artifactId;
    String name;
    String description;
    String packageName;
    /**
     * JDK 版本 17
     */
    String jvmVersion;
    /**
     * 上傳的 OpenAPI 檔案
     */
    // MultipartFile openAPIFile;
    Path openAPIFIle;
    /**
     * 資料庫DAO產生器使用
     */
    String dbUrl;
    String dbUsername;
    String dbPassword;
    /**
     * 執行環境
     */
    RuntimeEnum runtime;
}
