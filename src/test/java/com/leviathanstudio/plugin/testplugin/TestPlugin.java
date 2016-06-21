package com.leviathanstudio.plugin.testplugin;

import com.leviathanstudio.plugin.MineIDEPlugin;
import com.leviathanstudio.plugin.PluginInfos;

import java.util.Arrays;

public class TestPlugin extends MineIDEPlugin
{
    @Override
    public void populateInfos(PluginInfos infos)
    {
        ; // nop, done automatically via plugin_infos.json
    }

    @Override
    public void onPrePluginInit()
    {
        logger.debug("Hi from the pre-initialisation method!");
        logger.debug("Infos are:");
        logger.debug("\tName: "+infos.getName());
        logger.debug("\tDescription: "+infos.getDescription());
        logger.debug("\tAuthors: "+ Arrays.toString(infos.getAuthors()));
        logger.debug("\tCredits: "+ Arrays.toString(infos.getCredits()));
    }

    @Override
    public void onPluginInit()
    {
        logger.debug("Hi from the initialisation method!");
    }

    @Override
    public void onPostPluginInit()
    {
        logger.debug("Hi from the post-initialisation method!");
    }

    @Override
    public String getID()
    {
        return "test_plugin";
    }
}
