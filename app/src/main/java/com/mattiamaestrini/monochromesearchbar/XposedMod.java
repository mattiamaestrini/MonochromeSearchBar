package com.mattiamaestrini.monochromesearchbar;

import android.content.res.XModuleResources;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public class XposedMod implements IXposedHookZygoteInit, IXposedHookInitPackageResources {

    public static final String PACKAGE_NAME = "com.google.android.googlequicksearchbox";

    private static String MODULE_PATH = null;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(final XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(PACKAGE_NAME))
            return;

        final XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        resparam.res.setReplacement(PACKAGE_NAME, "drawable", "ic_mic", modRes.fwd(R.drawable.ic_mic));
        resparam.res.setReplacement(PACKAGE_NAME, "drawable", "ic_mic_widget", modRes.fwd(R.drawable.ic_mic_widget));
        resparam.res.setReplacement(PACKAGE_NAME, "drawable", "ic_searchbox_google", modRes.fwd(R.drawable.ic_searchbox_google));
    }
}
