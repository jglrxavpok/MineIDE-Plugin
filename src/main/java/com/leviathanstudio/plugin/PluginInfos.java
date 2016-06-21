package com.leviathanstudio.plugin;

import java.util.Arrays;

/**
 * Object storing all the informations about a plugin
 */
public class PluginInfos
{
    private final MineIDEPlugin plugin;
    private String id;
    private PluginState state;
    private String name;
    private String[] authors;
    private String[] credits;
    private String description;

    /**
     * The constructor, should not be called outside of {@link PluginSystem}
     * @param plugin
     */
    PluginInfos(MineIDEPlugin plugin)
    {
        state = PluginState.UNLOADED;
        this.plugin = plugin;
        authors = new String[] {"unknown"};
        credits = Arrays.copyOf(authors, authors.length);
        description = "<None>";
        name = plugin.getClass().getSimpleName();
        id = name.toLowerCase();
    }

    /**
     * Returns the instance of the represented mod
     * @return
     *      The instance of the mod
     */
    public MineIDEPlugin getInstance()
    {
        return plugin;
    }

    /**
     * Returns the ID of the mod
     * @return
     *      The ID of the mod
     */
    public String getID()
    {
        return id;
    }

    public void setID(String id)
    {
        this.id = id;
    }

    public String[] getAuthors()
    {
        return authors;
    }

    public String[] getCredits()
    {
        return credits;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public PluginState getState()
    {
        return state;
    }

    public void setName(String pluginName)
    {
        this.name = pluginName;
    }

    public void setAuthors(String... pluginAuthor)
    {
        this.authors = pluginAuthor;
    }

    public void setCredits(String... pluginCredits)
    {
        this.credits = pluginCredits;
    }

    public void setDescription(String pluginDescription)
    {
        this.description = pluginDescription;
    }

    public void setState(PluginState pluginState)
    {
        this.state = pluginState;
    }

    /**
     * Order: UNLOADED->UNINITIALIZED->INITIALIZED->LOADED
     */
    public enum PluginState
    {
        UNLOADED,
        UNINITIALIZED,
        INITIALIZED,
        LOADED
    }
}
