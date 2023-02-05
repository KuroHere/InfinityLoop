package com.me.infinity.loop.manager;

import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.Feature;
import com.me.infinity.loop.features.setting.shaders.SettingShader;
import com.me.infinity.loop.features.modules.Module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager
        extends Feature {
    private final Path base = this.getMkDirectory(this.getRoot(), "loop");
    private final Path config = this.getMkDirectory(this.base, "config");

    private final Path shader = this.getMkDirectory(this.base, "shaders");

    private final List<SettingShader> shaderList = new ArrayList<>();
    private final Map<String, SettingShader> shaders = new ConcurrentHashMap<>();
    public FileManager() {
        this.getMkDirectory(this.base, "pvp");
        for (Module.Category category : InfinityLoop.moduleManager.getCategories()) {
            this.getMkDirectory(this.config, category.getName());
        }
    }

    public static boolean appendTextFile(String data, String file) {
        try {
            Path path = Paths.get(file);
            Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("WARNING: Unable to write file: " + file);
            return false;
        }
        return true;
    }

    public static List<String> readTextFileAllLines(String file) {
        try {
            Path path = Paths.get(file);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("WARNING: Unable to read file, creating new file: " + file);
            FileManager.appendTextFile("", file);
            return Collections.emptyList();
        }
    }

    private String[] expandPath(String fullPath) {
        return fullPath.split(":?\\\\\\\\|\\/");
    }

    private Stream<String> expandPaths(String... paths) {
        return Arrays.stream(paths).map(this::expandPath).flatMap(Arrays::stream);
    }

    private Path lookupPath(Path root, String... paths) {
        return Paths.get(root.toString(), paths);
    }

    private Path getRoot() {
        return Paths.get("");
    }

    private void createDirectory(Path dir) {
        try {
            if (!Files.isDirectory(dir)) {
                if (Files.exists(dir)) {
                    Files.delete(dir);
                }
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleShaderDir(File dir)
    {
        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                try
                {
                    if (!shaders.containsKey(file.getName())
                            && file.getName().endsWith(".shader"))
                    {
                        SettingShader shader = new SettingShader(new FileInputStream(file), file.getName());
                        shaders.put(file.getName(), shader);
                        shaderList.add(shader);
                    }
                }
                catch (IOException e)
                {
                    InfinityLoop.getLogger().error("Failed to shader model: " + file.getName() + "!");
                    e.printStackTrace();
                }
            }
        }
    }

    public List<SettingShader> getShaders()
    {
        return shaderList;
    }

    private Path getMkDirectory(Path parent, String... paths) {
        if (paths.length < 1) {
            return parent;
        }
        Path dir = this.lookupPath(parent, paths);
        this.createDirectory(dir);
        return dir;
    }

    public Path getBasePath() {
        return this.base;
    }

    public Path getBaseResolve(String... paths) {
        String[] names = this.expandPaths(paths).toArray(String[]::new);
        if (names.length < 1) {
            throw new IllegalArgumentException("missing path");
        }
        return this.lookupPath(this.getBasePath(), names);
    }

    public Path getMkBaseResolve(String... paths) {
        Path path = this.getBaseResolve(paths);
        this.createDirectory(path.getParent());
        return path;
    }

    public Path getConfig() {
        return this.getBasePath().resolve("config");
    }

    public Path getCache() {
        return this.getBasePath().resolve("cache");
    }

    public Path getMkBaseDirectory(String... names) {
        return this.getMkDirectory(this.getBasePath(), this.expandPaths(names).collect(Collectors.joining(File.separator)));
    }

    public Path getMkConfigDirectory(String... names) {
        return this.getMkDirectory(this.getConfig(), this.expandPaths(names).collect(Collectors.joining(File.separator)));
    }

    // TODO:
    public SettingShader getInitialShader()
    {
        if (!shaderList.isEmpty())
        {
            return shaderList.get(0);
        }
        else
        {
            return new SettingShader("default");
        }
    }

}

