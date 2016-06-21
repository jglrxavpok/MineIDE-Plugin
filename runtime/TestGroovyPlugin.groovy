import com.leviathanstudio.plugin.MineIDEPlugin
import com.leviathanstudio.plugin.PluginInfos

class TestGroovyPlugin extends MineIDEPlugin {
    @Override
    void populateInfos(PluginInfos infos) {
        infos.authors = {"jglrxavpok"}
        infos.credits = {"jglrxavpok"}
        infos.description = "A test plugin made in Groovy"
        infos.name = {"Groovy Test Plugin"}
    }

    @Override
    void onPrePluginInit() {
        logger.info("Hi from pre-init in Groovy!")
    }

    @Override
    void onPluginInit() {
        logger.info("Hi from init in Groovy!")
    }

    @Override
    void onPostPluginInit() {
        logger.info("Hi from post-init in Groovy!")
    }

    @Override
    String getID() {
        return "test_groovy"
    }
}
