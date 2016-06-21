package com.leviathanstudio.plugin;

import plugin.fr.scarex.obfuscatednamefinder.ui.ObfuscatedNameFinderFrame;

import java.io.File;
import java.util.Arrays;

public class RunnableTestPlugin extends MineIDEPlugin
{

    @Override
    public void populateInfos(PluginInfos infos)
    {
        infos.setName("Obfuscated Name Finder");
        infos.setAuthors("SCAREX");
        infos.setCredits("SCAREX");
        infos.setDescription("Simple plugin to find the properly name of an obfuscated field or method");
    }

    @Override
    public void onPrePluginInit()
    {
        logger.info("Name: "+infos.getName());
        logger.info("Authors:" + Arrays.toString(infos.getAuthors()));
        logger.info("Credits:" + Arrays.toString(infos.getCredits()));
        logger.info("Description: "+infos.getDescription());
    }

    @Override
    public void onPluginInit()
    {
        ObfuscatedNameFinderFrame.main(new String[0]);
    }

    @Override
    public void onPostPluginInit()
    {

    }

    @Override
    public String getID()
    {
        return "obfuscated_name_finder";
    }

    // Debug
    public static void main(String[] args)
    {
        PluginSystem pluginSystem = PluginSystem.kickoff(new File("./runtime"));
        pluginSystem.initPlugins();
    }
    
}
