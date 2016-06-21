package com.leviathanstudio.plugin;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a MineIDE plugin. Loaded at the launch of the {@link PluginSystem Plugin System}.<br/>
 * Starting point of any plugin.<br/><br/>
 * <b>WARNING:</b> In order for your plugin to be loaded, its core class must extend with one.
 *
 */
public abstract class MineIDEPlugin
{
    /**
     * The informations about the plugin, filled at pre-initialization and by {@link #populateInfos(PluginInfos)}
     */
    protected PluginInfos infos;

    /**
     * The logger dedicated to this plugin, assigned at loading
     */
    protected Logger logger;

    public MineIDEPlugin()
    {

    }

    /**
     * Populate the <code>infos</code> object with informations about this mod.
     * @param infos
     *      The object storing the informations
     */
    public abstract void populateInfos(PluginInfos infos);

    /**
     * Called before all plugins are initialized
     */
    public abstract void onPrePluginInit();

    /**
     * Called when the plugin needs to initialize
     */
    public abstract void onPluginInit();

    /**
     * Called after all plugins have been initialized
     */
    public abstract void onPostPluginInit();

    /**
     * Returns the ID of this mod.<br/>
     * To be consistent across plugins, follow the snake case naming convention (e.g. "test_plugin", "my_super_duper_awesome_plugin", etc.)
     * @return
     *      The ID of this mod
     */
    public abstract String getID();

    /**
     * Called when an exception is raised because of this mod, prints the error message by default
     * @param e
     *      The exception
     */
    public void onPluginException(Exception e)
    {
        logger.error("Plugin crashed, ask the developer(s):", e);
    }
}