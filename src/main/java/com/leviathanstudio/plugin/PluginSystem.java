package com.leviathanstudio.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The Plugin System is the entity that handles loading, initialization and coordination of plugins.<br/>
 * Plugins can be loaded from a folder as ".jar" files or from the classpath
 */
public class PluginSystem
{

    /**
     * Used as a placeholder while loading the plugins. Prevents crashes if a plugin tries to access its logger before
     * being initialized or after an exception preventing said plugin to properly load
     */
    private final Logger temporaryLogger;
    private File pluginFolder;
    private static PluginSystem instance;
    private List<MineIDEPlugin> loadedPlugins;

    /**
     * Creates a new plugin system
     * @param pluginFolder
     */
    private PluginSystem(File pluginFolder)
    {
        loadedPlugins = new ArrayList<>();
        this.pluginFolder = pluginFolder;
        temporaryLogger = LogManager.getLogger(this);
    }

    /**
     * Returns the folder used to search for plugins
     * @return
     *      The folder to search plugins in
     */
    public File getPluginFolder()
    {
        return pluginFolder;
    }

    /**
     * Starts the Plugin System, loads the plugins and returns the created instance.<br/>
     * <b>Warning:</b> It is only possible to have one instance of a Plugin System at a time
     * @param pluginFolder
     *      The folder to search plugins in, can be null for none
     * @return
     *      The created instance
     *
     * @throws IllegalStateException
     *      If an instance already exists
     */
    public static PluginSystem kickoff(File pluginFolder)
    {
        if(instance == null)
        {
            PluginSystem system = new PluginSystem(pluginFolder);
            instance = system;
            system.loadPluginFromFolder();
            system.loadPluginFromClasspath();
            return system;
        }
        else
        {
            throw new IllegalStateException("The plugin system is a singleton, don't try to start it twice!");
        }
    }

    /**
     * Checks if a given plugin has been found by the system
     * @param id
     *      The ID of the plugin
     * @return
     *      <code>true</code> if the plugin is present, <code>false</code> otherwise
     */
    public boolean isPluginPresent(String id)
    {
        return getPlugin(id).isPresent();
    }

    /**
     * Checks if a given plugin has been found by the system and loaded
     * @param id
     *      The ID of the plugin
     * @return
     *      <code>true</code> if the plugin is loaded, <code>false</code> otherwise
     */
    public boolean isPluginLoaded(String id)
    {
        Optional<MineIDEPlugin> plugin = getPlugin(id);
        if(plugin.isPresent())
        {
            return plugin.get().infos.getState() == PluginInfos.PluginState.LOADED;
        }
        return false;
    }

    /**
     * Returns a potential plugin with the given id
     * @param id
     *      The id of the plugin
     * @return
     *      The potential plugin
     */
    public Optional<MineIDEPlugin> getPlugin(String id)
    {
        return loadedPlugins.stream()
                .filter(p -> p.getID().equals(id))
                .findFirst();
    }

    private void loadPluginFromClasspath()
    {
        Reflections reflections = new Reflections(Thread.currentThread().getContextClassLoader());
        for(Class<? extends MineIDEPlugin> pluginClass : reflections.getSubTypesOf(MineIDEPlugin.class))
        {
            try
            {
                MineIDEPlugin plugin = pluginClass.newInstance();
                PluginInfos infos = new PluginInfos(plugin);
                infos.setID(plugin.getID());
                plugin.logger = temporaryLogger;

                Package pluginPackage = pluginClass.getPackage();
                if(pluginPackage != null)
                {
                    String path = "/"+pluginPackage.getName().replace(".", "/")+"/plugin_infos.json";
                    InputStream stream = PluginSystem.class.getResourceAsStream(path);
                    if(stream != null)
                    {
                        try
                        {
                            Gson gson = new GsonBuilder().setLenient().create();
                            JsonObject pluginInfos = gson.fromJson(new InputStreamReader(stream), JsonObject.class);

                            JsonArray authors = pluginInfos.getAsJsonArray("authors");
                            String[] authorList = new String[authors.size()];
                            for (int i = 0; i < authorList.length; i++)
                            {
                                authorList[i] = authors.get(i).getAsString();
                            }
                            infos.setAuthors(authorList);

                            JsonArray credits = pluginInfos.getAsJsonArray("credits");
                            String[] creditsList = new String[authors.size()];
                            for (int i = 0; i < creditsList.length; i++)
                            {
                                creditsList[i] = credits.get(i).getAsString();
                            }
                            infos.setCredits(creditsList);

                            infos.setName(pluginInfos.get("name").getAsString());
                            infos.setDescription(pluginInfos.get("description").getAsString());
                        }
                        catch (Exception e)
                        {
                            plugin.onPluginException(e);
                        }
                        finally
                        {
                            try
                            {
                                stream.close();
                            }
                            catch (IOException e)
                            {
                            }
                        }
                    }
                    else
                    {
                        temporaryLogger.info("Could not find plugin_infos.json for "+plugin.getClass().getCanonicalName()+
                                " with path: "+path+". Automatic info loading could not be performed for this plugin");
                    }
                }

                plugin.populateInfos(infos);
                plugin.logger = LogManager.getLogger(infos.getName());
                plugin.infos = infos;
                loadedPlugins.add(plugin);
            }
            catch (InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void loadPluginFromFolder()
    {
        if(pluginFolder == null)
            return;
        File[] subfiles = pluginFolder.listFiles(f -> f.getName().endsWith(".jar") || f.getName().endsWith(".zip"));
        if(subfiles != null && subfiles.length > 0)
        {
            URLClassLoader loader = (URLClassLoader)Thread.currentThread().getContextClassLoader();
            try
            {
                Method method = loader.getClass().getDeclaredMethod("addURL", URL.class);
                for(File f : subfiles)
                {
                    try
                    {
                        method.invoke(loader, f.toURI().toURL());
                    }
                    catch (IOException | InvocationTargetException | IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Launches the initialization process of plugins
     */
    public void initPlugins()
    {
        forAllPlugins(p -> p.infos.setState(PluginInfos.PluginState.UNINITIALIZED));
        forAllPlugins(MineIDEPlugin::onPrePluginInit);
        forAllPlugins(MineIDEPlugin::onPluginInit);
        forAllPlugins(p -> p.infos.setState(PluginInfos.PluginState.INITIALIZED));
        forAllPlugins(MineIDEPlugin::onPostPluginInit);
        forAllPlugins(p -> p.infos.setState(PluginInfos.PluginState.LOADED));
    }

    private void forAllPlugins(Consumer<MineIDEPlugin> action)
    {
        loadedPlugins.forEach(p -> {
            try
            {
                action.accept(p);
            }
            catch (Exception e)
            {
                p.onPluginException(e);
            }
        });
    }
}
