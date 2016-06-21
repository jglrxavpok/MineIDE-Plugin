package com.leviathanstudio.plugin;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class MineIDEPlugin
{
    protected PluginInfos infos;
    protected Logger logger;

    public MineIDEPlugin()
    {

    }

    public abstract void populateInfos(PluginInfos infos);

    public abstract void onPrePluginInit();

    public abstract void onPluginInit();

    public abstract void onPostPluginInit();

    public abstract String getID();

    public void onPluginException(Exception e)
    {
        logger.error("Plugin crashed, ask the developer(s):", e);
    }
}