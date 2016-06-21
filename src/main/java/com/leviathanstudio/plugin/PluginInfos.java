package com.leviathanstudio.plugin;

import java.util.Arrays;

public class PluginInfos
{
    private final MineIDEPlugin plugin;
    private String id;
    private PluginState state;
    private String name;
    private String[] authors;
    private String[] credits;
    private String description;

    public PluginInfos(MineIDEPlugin plugin)
    {
        state = PluginState.UNLOADED;
        this.plugin = plugin;
        authors = new String[] {"unknown"};
        credits = Arrays.copyOf(authors, authors.length);
        description = "<None>";
        name = plugin.getClass().getSimpleName();
        id = name.toLowerCase();
    }

    public MineIDEPlugin getInstance()
    {
        return plugin;
    }

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

    public enum PluginState
    {
        UNLOADED, UNINITIALIZED, PluginState, INITIALIZED, LOADED
    }
}
