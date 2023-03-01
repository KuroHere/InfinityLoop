package com.me.infinity.loop.manager;

import com.google.gson.*;
import com.me.infinity.loop.InfinityLoop;
import com.me.infinity.loop.features.command.Command;
import com.me.infinity.loop.features.modules.Module;
import com.me.infinity.loop.features.modules.render.Search;
import com.me.infinity.loop.features.setting.Setting;
import com.me.infinity.loop.features.setting.impl.*;
import com.me.infinity.loop.util.utils.Util;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.me.infinity.loop.manager.ModuleManager.modules;


public class ConfigManager implements Util {

    public static  File MainFolder = new File(mc.gameDir, "loop");
    public static  File ConfigsFolder = new File(MainFolder, "configs");
    public static  File CustomImages = new File(MainFolder, "images");
    public static  File TempFolder = new File(MainFolder, "temp");
    public static  File SkinsFolder = new File(TempFolder, "skins");
    public static  File CapesFolder = new File(TempFolder, "capes");
    public static  File HeadsFolder = new File(TempFolder, "heads");
    public static  File DiscordEmbeds = new File(TempFolder, "embeds");
    public static  File MiscFolder = new File(MainFolder, "misc");
    public static  File KitsFolder = new File(MiscFolder, "kits");
    //friends
    //enemies
    //webhook
    //rpc
    //autoEz
    //currentcfg
    //macro
    //search
    //alts



    public static void init(){
        if (!MainFolder.exists()) MainFolder.mkdirs();
        if (!ConfigsFolder.exists()) ConfigsFolder.mkdirs();
        if (!CustomImages.exists()) CustomImages.mkdirs();
        if (!TempFolder.exists()) TempFolder.mkdirs();
        if (!SkinsFolder.exists()) SkinsFolder.mkdirs();
        if (!CapesFolder.exists()) CapesFolder.mkdirs();
        if (!HeadsFolder.exists()) HeadsFolder.mkdirs();
        if (!MiscFolder.exists()) MiscFolder.mkdirs();
        if (!KitsFolder.exists()) KitsFolder.mkdirs();
        if (!DiscordEmbeds.exists()) DiscordEmbeds.mkdirs();

    }

    public static File currentConfig = null;


    public static void load(String name) {
        File file = new File(ConfigsFolder, name + ".if");
        if (!file.exists()) {
            Command.sendMessage("config " + name + " does not exist!");
            return;
        }

        if(currentConfig != null){
            save(currentConfig);
        }

        InfinityLoop.moduleManager.onUnload();
        InfinityLoop.moduleManager.onUnloadPost();
        load(file);
        InfinityLoop.moduleManager.onLoad();

    }

    public static void load(File config) {
        if (!config.exists()) save(config);
        try {
            FileReader reader = new FileReader(config);
            JsonParser parser = new JsonParser();

            JsonArray array = null;
            try {
                array = (JsonArray) parser.parse(reader);
            } catch (ClassCastException e) {
                save(config);
            }

            JsonArray modules = null;
            try {
                JsonObject modulesObject = (JsonObject) array.get(0);
                modules = modulesObject.getAsJsonArray("Modules");
            } catch (Exception e) {
                System.err.println("Module Array not found, skipping!");
            }
            if (modules != null) {
                modules.forEach(m -> {
                    try {
                        parseModule(m.getAsJsonObject());
                    } catch (NullPointerException e) {
                        System.err.println(e.getMessage());
                    }
                });
            }
            Command.sendMessage("Loaded config " + config.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentConfig = config;
        saveCurrentConfig();
    }


    public static void save(String name) {
        File file = new File(ConfigsFolder, name + ".if");

        if (file.exists()) {
            Command.sendMessage("\n" + "config " + name + " already exists!");
            return;
        }

        save(file);
        Command.sendMessage("\n" + "config " + name + " already exists!");

    }


    public static void save(File config) {
        try {
            if (!config.exists()) {
                config.createNewFile();
            }
            JsonArray array = new JsonArray();

            JsonObject modulesObj = new JsonObject();
            modulesObj.add("Modules", getModuleArray());
            array.add(modulesObj);


            FileWriter writer = new FileWriter(config);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            gson.toJson(array, writer);
            writer.close();
        } catch (IOException e) {
            Command.sendMessage("Cant write to config file!");
        }

    }


    private static void parseModule(JsonObject object) throws NullPointerException {

        Module module = modules.stream()
                .filter(m -> object.getAsJsonObject(m.getName()) != null)
                .findFirst().orElse(null);

        if (module != null) {
            JsonObject mobject = object.getAsJsonObject(module.getName());

            for(Setting setting2 : module.getSettings()){
                try {
                    switch (setting2.getType()) {
                        case "Parent": {
                            break;
                        }
                        case "Boolean": {
                            setting2.setValue(mobject.getAsJsonPrimitive(setting2.getName()).getAsBoolean());
                            break;
                        }
                        case "Double": {
                            setting2.setValue(mobject.getAsJsonPrimitive(setting2.getName()).getAsDouble());
                            break;
                        }
                        case "Float": {
                            setting2.setValue(mobject.getAsJsonPrimitive(setting2.getName()).getAsFloat());
                            break;
                        }
                        case "Integer": {
                            setting2.setValue(mobject.getAsJsonPrimitive(setting2.getName()).getAsInt());
                            break;
                        }
                        case "String": {
                            setting2.setValue(mobject.getAsJsonPrimitive(setting2.getName()).getAsString().replace("_", " "));
                            break;
                        }
                        case "Bind": {
                            JsonArray array4 = mobject.getAsJsonArray("Keybind");
                            setting2.setValue((new Bind.BindConverter()).doBackward(array4.get(0)));
                            ((Bind) setting2.getValue()).setHold(array4.get(1).getAsBoolean());
                            break;
                        }
                        case "Color": {
                            setting2.setValue(new Color(mobject.getAsInt(), true));
                            break;
                        }
                        case "PositionSetting": {
                            JsonArray array3 = mobject.getAsJsonArray(setting2.getName());
                            ((PositionSetting) setting2.getValue()).setX(array3.get(0).getAsFloat());
                            ((PositionSetting) setting2.getValue()).setY(array3.get(1).getAsFloat());
                            break;
                        }
                        case "SubBind": {
                            setting2.setValue((new SubBind.SubBindConverter()).doBackward(mobject.getAsJsonPrimitive(setting2.getName())));
                            break;
                        }
                        case "Enum": {
                            try {
                                EnumConverter converter = new EnumConverter(((Enum) setting2.getValue()).getClass());
                                Enum value = converter.doBackward(mobject.getAsJsonPrimitive(setting2.getName()));
                                setting2.setValue((value == null) ? setting2.getDefaultValue() : value);
                            } catch (Exception ignored) {
                            }
                            break;
                        }
                    }
                } catch (Exception e){
                    System.out.println(module.getName());
                    System.out.println(setting2);
                    e.printStackTrace();
                }
            }
        }
    }

    private static JsonArray getModuleArray() {
        JsonArray modulesArray = new JsonArray();
        for (Module m : modules) {
            modulesArray.add(getModuleObject(m));
        }
        return modulesArray;
    }

    public static JsonObject getModuleObject(Module m) {
        JsonObject attrs = new JsonObject();
        JsonParser jp = new JsonParser();

        for (Setting setting : m.getSettings()) {
            if (setting.getValue() instanceof Color) {
                attrs.add(setting.getName(), jp.parse(String.valueOf(((Color) setting.getValue()).getRGB())));
                continue;
            }
            if (setting.isEnumSetting()) {
                EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                attrs.add(setting.getName(), converter.doForward((Enum) setting.getValue()));
                continue;
            }
            if (setting.isStringSetting()) {
                String str = (String) setting.getValue();
                setting.setValue(str.replace(" ", "_"));
            }
            if(setting.isPositionSetting()){
                JsonArray array = new JsonArray();
                float num2 = ((PositionSetting) setting.getValue()).getX();
                float num1 = ((PositionSetting) setting.getValue()).getY();
                array.add(new JsonPrimitive(num2));
                array.add(new JsonPrimitive(num1));

                attrs.add(setting.getName(), array);
                continue;
            }
            if(setting.isBindSetting()){
                JsonArray array = new JsonArray();
                String key = setting.getValueAsString();
                boolean hold = ((Bind) setting.getValue()).isHold();
                array.add(new JsonPrimitive(key));
                array.add(new JsonPrimitive(hold));

                attrs.add(setting.getName(), array);
                continue;
            }
            try {
                attrs.add(setting.getName(), jp.parse(setting.getValueAsString()));
            } catch (Exception ignored) {
            }
        }

        JsonObject moduleObject = new JsonObject();
        moduleObject.add(m.getName(), attrs);
        return moduleObject;
    }

    public static boolean delete(File file) {
        return file.delete();
    }


    public static boolean delete(String name) {
        File file = new File(ConfigsFolder, name + ".if");
        if (!file.exists()) {
            return false;
        }
        return delete(file);
    }

    public static List<String> getConfigList() {
        if (!MainFolder.exists() || MainFolder.listFiles() == null) return null;

        List<String> list = new ArrayList<>();

        if (ConfigsFolder.listFiles() != null) {
            for(File file : Arrays.stream(ConfigsFolder.listFiles()).filter(f -> f.getName().endsWith(".if")).collect(Collectors.toList())){
                list.add(file.getName().replace(".if",""));
            }
        }
        return list;
    }


    public static void saveCurrentConfig() {
        File file = new File("loop/misc/currentcfg.txt");
        try {
            if (file.exists()) {
                FileWriter writer = new FileWriter(file);
                writer.write(currentConfig.getName().replace(".if",""));
                writer.close();
            } else {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(currentConfig.getName().replace(".if",""));
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File  getCurrentConfig() {
        File file = new File("loop/misc/currentcfg.txt");
        String name = "config";
        try {
            if (file.exists()) {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine())
                    name = reader.nextLine();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentConfig = new File(ConfigsFolder,name + ".if");
        return currentConfig;
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


    public static void loadSearch(){
        try {
            File file = new File("loop/misc/search.txt");

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    while (reader.ready()) {
                        String name = reader.readLine();
                        Search.defaultBlocks.add(getRegisteredBlock(name));
                    }

                }
            }
        } catch (Exception ignored) {}
    }

    public static void saveSearch() {
        File file = new File("loop/misc/search.txt");
        try {
            new File("loop").mkdirs();
            file.createNewFile();
        } catch (Exception e){

        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Block name :  Search.defaultBlocks) {
                writer.write(name.getRegistryName() + "\n");
            }
        } catch (Exception ignored){}
    }

    private static Block getRegisteredBlock(String blockName) {
        return (Block)Block.REGISTRY.getObject(new ResourceLocation(blockName));
    }
}