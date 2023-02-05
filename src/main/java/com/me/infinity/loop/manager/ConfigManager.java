package com.me.infinity.loop.manager;

import com.google.gson.*;
import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.Feature;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.setting.Bind;
import com.me.infinity.loop.features.setting.ColorSetting;
import com.me.infinity.loop.features.setting.EnumConverter;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.util.interfaces.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigManager implements Util {
    public ArrayList<Feature> features = new ArrayList<>();

    public String config = "loop/config/";

    public JsonObject getModuleObject(Feature feature) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();

        for (Setting setting : feature.getSettings()) {
            if (setting.isEnumSetting()) {
                EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                object.add(setting.getName(), converter.doForward((Enum) setting.getValue()));
                continue;
            }
            if(setting.isColorSetting()){
                JsonArray array = new JsonArray();
                array.add(new JsonPrimitive(((ColorSetting) setting.getValue()).getRawColor()));
                array.add(new JsonPrimitive(((ColorSetting) setting.getValue()).isCycle()));
                array.add(new JsonPrimitive(((ColorSetting) setting.getValue()).getGlobalOffset()));
                object.add(setting.getName(), array);
                continue;
            }
            if (setting.isStringSetting()) {
                String str = (String) setting.getValue();
                setting.setValue(str.replace(" ", "_"));
            }
            try {
                object.add(setting.getName(), jp.parse(setting.getValueAsString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }


    /*----------------- LOAD ---------------*/
    public static void setValueFromJson(Feature feature, Setting setting, JsonElement element) {
        String str;
        JsonArray array = element.getAsJsonArray();
        switch (setting.getType()) {
            case "Boolean":
                setting.setValue(Boolean.valueOf(element.getAsBoolean()));
                return;
            case "Double":
                setting.setValue(Double.valueOf(element.getAsDouble()));
                return;
            case "Float":
                setting.setValue(Float.valueOf(element.getAsFloat()));
                return;
            case "Integer":
                setting.setValue(Integer.valueOf(element.getAsInt()));
                return;
            case "String":
                str = element.getAsString();
                setting.setValue(str.replace("_", " "));
                return;
            case "Bind":
                setting.setValue((new Bind.BindConverter()).doBackward(element));
                return;
            case "ColorSetting":
                ((ColorSetting) setting.getValue()).setColor(array.get(0).getAsInt());
                ((ColorSetting) setting.getValue()).setCycle(array.get(1).getAsBoolean());
                ((ColorSetting) setting.getValue()).setGlobalOffset(array.get(2).getAsInt());
                return;
            case "Enum":
                try {
                    EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                    Enum value = converter.doBackward(element);
                    setting.setValue((value == null) ? setting.getDefaultValue() : value);
                } catch (Exception exception) {
                }
                return;
        }

        InfinityLoop.LOGGER.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
    }

    private static void loadFile(JsonObject input, Feature feature) {
        for (Map.Entry<String, JsonElement> entry : input.entrySet()) {
            String settingName = entry.getKey();
            JsonElement element = entry.getValue();
            if (feature instanceof FriendManager) {
                try {
                    InfinityLoop.friendManager.addFriend(new FriendManager.Friend(element.getAsString(), UUID.fromString(settingName)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            boolean settingFound = false;
            for (Setting setting : feature.getSettings()) {
                if (settingName.equals(setting.getName())) {
                    try {
                        setValueFromJson(feature, setting, element);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    settingFound = true;
                }
            }
            if (settingFound) ;
        }
    }

    public void loadConfig(String name) {
        final List<File> files = Arrays.stream(Objects.requireNonNull(new File("loop").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        if (files.contains(new File("loop/" + name + "/"))) {
            this.config = "loop/" + name + "/";
        } else {
            this.config = "loop/config/";
        }
        InfinityLoop.friendManager.onLoad();
        for (Feature feature : this.features) {
            try {
                loadSettings(feature);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveCurrentConfig();
    }

    public boolean configExists(String name) {
        final List<File> files = Arrays.stream(Objects.requireNonNull(new File("loop").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        return files.contains(new File("loop/" + name + "/"));
    }

    public void saveConfig(String name) {
        this.config = "loop/" + name + "/";
        File path = new File(this.config);
        if (!path.exists())
            path.mkdir();
        InfinityLoop.friendManager.saveFriends();
        for (Feature feature : this.features) {
            try {
                saveSettings(feature);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveCurrentConfig();
    }

    public void saveCurrentConfig() {
        File currentConfig = new File("loop/misc/currentconfig.txt");
        try {
            if (currentConfig.exists()) {
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("loop", ""));
                writer.close();
            } else {
                currentConfig.createNewFile();
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("loop", ""));
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAlts(){
        try {
            File file = new File("loop/misc/alts.txt");

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    while (reader.ready()) {
                        String name = reader.readLine();
                        InfinityLoop.alts.add(name);
                    }

                }
            }
        } catch (Exception ignored) {}
    }

    public static void saveAlts() {
        File file = new File("loop/misc/alts.txt");
        try {
            new File("loop").mkdirs();
            file.createNewFile();
        } catch (Exception e){

        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String name : InfinityLoop.alts) {
                writer.write(name + "\n");
            }
        } catch (Exception ignored){}
    }


    public String loadCurrentConfig() {
        File currentConfig = new File("loop/misc/currentconfig.txt");
        String name = "config";
        try {
            if (currentConfig.exists()) {
                Scanner reader = new Scanner(currentConfig);
                while (reader.hasNextLine())
                    name = reader.nextLine();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public void resetConfig(boolean saveConfig, String name) {
        for (Feature feature : this.features)
            feature.reset();
        if (saveConfig)
            saveConfig(name);
    }

    public void saveSettings(Feature feature) throws IOException {
        JsonObject object = new JsonObject();
        File directory = new File(this.config + getDirectory(feature));
        if (!directory.exists())
            directory.mkdir();
        String featureName = this.config + getDirectory(feature) + feature.getName() + ".json";
        Path outputFile = Paths.get(featureName);
        if (!Files.exists(outputFile))
            Files.createFile(outputFile);
        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
        String json = gson.toJson(getModuleObject(feature));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
        writer.write(json);
        writer.close();
    }

    public void init() {
        this.features.addAll(ModuleManager.modules);
        this.features.add(InfinityLoop.friendManager);
        String name = loadCurrentConfig();
        loadConfig(name);
        InfinityLoop.LOGGER.info("Config loaded.");
    }

    private void loadSettings(Feature feature) throws IOException {
        String featureName = this.config + getDirectory(feature) + feature.getName() + ".json";
        Path featurePath = Paths.get(featureName);
        if (!Files.exists(featurePath))
            return;
        loadPath(featurePath, feature);
    }

    private void loadPath(Path path, Feature feature) throws IOException {
        InputStream stream = Files.newInputStream(path);
        try {
            loadFile((new JsonParser()).parse(new InputStreamReader(stream)).getAsJsonObject(), feature);
        } catch (IllegalStateException e) {
            InfinityLoop.LOGGER.error("Bad Config File for: " + feature.getName() + ". Resetting...");
            loadFile(new JsonObject(), feature);
        }
        stream.close();
    }

    public String getDirectory(Feature feature) {
        String directory = "";
        if (feature instanceof Module)
            directory = directory + ((Module) feature).getCategory().getName() + "/";
        return directory;
    }
}
