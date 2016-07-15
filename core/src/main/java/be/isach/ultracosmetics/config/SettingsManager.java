package be.isach.ultracosmetics.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.util.CustomConfiguration;

/**
 * Created by sacha on 21/07/15.
 */
public class SettingsManager {

    // Config file.
    // Translation config file.
    private static SettingsManager messages = new SettingsManager("messages");

    private static SettingsManager conf;
    public FileConfiguration fileConfiguration;
    private File file;

    /**
     * Creates a new file and defines fileConfiguration and file.
     *
     * @param fileName
     */
    private SettingsManager(String fileName) {

        if (!UltraCosmetics.getInstance().getDataFolder().exists()) {
            UltraCosmetics.getInstance().getDataFolder().mkdir();
        }

        File f = new File(UltraCosmetics.getInstance().getDataFolder(), "/data");
        if (!f.exists())
            f.mkdirs();

        file = new File(UltraCosmetics.getInstance().getDataFolder(), fileName + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        FileInputStream stream;
		try {
			stream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.err.println("Could not find " + fileName);
			e.printStackTrace();
			return;
		}
        InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
		BufferedReader input = new BufferedReader(reader);
		
		StringBuilder stb = new StringBuilder();
		
		String line;
		try {
			while((line = input.readLine()) != null) {
				stb.append(line + "\n");
			}
		} catch (IOException e1) {
			System.err.println("Could not read " + fileName);
			e1.printStackTrace();
		}
		
		YamlConfiguration yaml = new YamlConfiguration();
		
		try {
			yaml.loadFromString(stb.toString());
		} catch (InvalidConfigurationException e) {
			System.err.println("Could not load " + fileName);
			e.printStackTrace();
		}
		
        fileConfiguration = yaml;
    }

    /**
     * Creates a new file and defines fileConfiguration and file.
     *
     * @param fileName
     */
    private SettingsManager() {
        file = new File(UltraCosmetics.getInstance().getDataFolder(), "config.yml");
        fileConfiguration = UltraCosmetics.config;
    }

    /**
     * Gets the messages SettingsManager.
     *
     * @return the messages SettingsManager.
     */
    public static SettingsManager getMessages() {
        return messages;
    }

    /**
     * Gets the messages SettingsManager.
     *
     * @return the messages SettingsManager.
     */
    public static CustomConfiguration getConfig() {
        return UltraCosmetics.config;
    }

    /**
     * Gets the data settings manager of a player.
     *
     * @param p The player.
     * @return the data settings manager of a player.
     */
    public static SettingsManager getData(Player p) {
        return new SettingsManager("/data/" + p.getUniqueId().toString());
    }

    /**
     * Gets the data settings manager of a uuid.
     *
     * @param p The player.
     * @return the data settings manager of a uuid.
     */
    public static SettingsManager getData(UUID uuid) {
        return new SettingsManager("/data/" + uuid.toString());
    }

    public static boolean hasData(UUID uuid) {
        return Arrays.asList(UltraCosmetics.getInstance().getDataFolder()
                .listFiles()).contains(new File(uuid.toString() + ".yml"));
    }

    public void reload() {
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the value of a given path.
     *
     * @param path
     * @param value
     */
    public void set(String path, Object value) {
        fileConfiguration.set(path, value);
        try {
            fileConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a boolean in config from a path.
     *
     * @param path The path in config to the boolean.
     * @return The boolean found in config from the given path.
     */
    public boolean getBoolean(String path) {
        return (boolean) get(path);
    }

    /**
     * Get a int in config from a path.
     *
     * @param path The path in config to the int.
     * @return The int found in config from the given path.
     */
    public int getInt(String path) {
        return (int) get(path);
    }

    /**
     * Get a double in config from a path.
     *
     * @param path The path in config to the double.
     * @return The double found in config from the given path.
     */
    public double getDouble(String path) {
        return (double) get(path);
    }

    /**
     * Sets a value if the fileConfiguration doesn't contain the path.
     *
     * @param path  The fileConfiguration path.
     * @param value The value for this path.
     */
    public void addDefault(String path, Object value) {
        if (!fileConfiguration.contains(path))
            set(path, value);
    }

    /**
     * Create and get a configuration section for a given path.
     *
     * @param path
     * @return the configuration section created for the given path.
     */
    public ConfigurationSection createConfigurationSection(String path) {
        ConfigurationSection cs = fileConfiguration.createSection(path);
        try {
            fileConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cs;
    }


    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) fileConfiguration.get(path);
    }

    /**
     * @param path
     * @return {@code true} if the fileConfiguration contains the path, {@code false} otherwise.
     */
    public boolean contains(String path) {
        return fileConfiguration.contains(path);
    }

}
