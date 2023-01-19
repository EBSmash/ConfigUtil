package github.ebsmash.maroon.util.misc;


import github.ebsmash.maroon.MaroonCore;
import github.ebsmash.maroon.module.Module;
import github.ebsmash.maroon.module.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.JsonException;
import org.json.JSONObject;
import org.lwjgl.Sys;

import java.io.*;

public class ConfigUtil {

    static JSONObject config = new JSONObject();

    static File configFile = new File(getDir() + new File("modules.json"));


    public static void saveConfig() {


        for (Module m : MaroonCore.moduleManager.getModules()) {
            JSONObject settings = new JSONObject();

            for (Setting setting : m.getSettings()) {


                if (setting.isBVal()) {
                    settings.put(setting.getName(), setting.getBVal());
                } else if (setting.isDVal()) {
                    settings.put(setting.getName(), setting.getDVal());
                } else if (setting.isMVal()) {
                    settings.put(setting.getName(), setting.getMVal());
                }

            }
            settings.put("key", m.getKey());
            settings.put("toggled", m.isToggled());

            config.put(m.getName(), settings);
        }

        System.out.println(config.toString());

        try {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();


            FileWriter file = new FileWriter(configFile);
            file.write(config.toString());
            file.close();
            System.out.println("Saved config successfully");
        } catch (IOException e) {
            System.out.println("Failed to save config");
            e.printStackTrace();
        }
    }

    public static void loadConfig() {


        try {
            configFile.createNewFile();
        } catch (IOException ignored) {

        }

        JSONObject config = readJSONFile(configFile.getAbsolutePath());

        if (config == null) {
            return;
        }

        for (Module m : MaroonCore.moduleManager.getModules()) {

            JSONObject settings = null;

            try {
                settings = config.getJSONObject(m.getName());
            } catch (Exception e) {
                System.out.println(String.format("Module %s has not been initialized yet", m.getName()));
            }

            if (settings == null){
                continue;
            }

            for (Setting setting : m.getSettings()) {
                String settingName = setting.getName();

                if (setting.isBVal()) {
                    setting.setBVal(settings.getBoolean(settingName));
                } else if (setting.isDVal()) {
                    setting.setDVal(settings.getDouble(settingName));
                } else if (setting.isMVal()) {
                    setting.setMVal(settings.getInt(settingName));
                }
            }
            //avoid nullpointers for things defined on enabled
            m.setToggled(true);

            m.setToggled(settings.getBoolean("toggled"));
            m.setKey(settings.getInt("key"));

        }
    }


    private static JSONObject readJSONFile(String filePath) {
        try {
            FileReader file = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(file);
            String jsonString = reader.readLine();
            JSONObject json = new JSONObject(jsonString);
            reader.close();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getDir() {
        return Minecraft.getMinecraft().gameDir.getAbsoluteFile() + "/maroon";
    }
}
