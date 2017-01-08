package com.mattiamaestrini.monochromesearchbar;

import android.content.res.XResources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public class XposedMod implements IXposedHookInitPackageResources {

    private static final String PACKAGE_NAME = "com.google.android.googlequicksearchbox";
    private static final String RESOURCE_TYPE = "drawable";

    private static final String RES_IC_SEARCHBOX_GOOGLE = "ic_searchbox_google";
    private static final String RES_IC_MIC = "ic_mic";

    @Override
    public void handleInitPackageResources(final XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(PACKAGE_NAME)) {
            return;
        }

        int color = Color.GRAY;

        setTint(resparam, RES_IC_SEARCHBOX_GOOGLE, color);
        setTint(resparam, RES_IC_MIC, color);
    }

    private void setTint(final XC_InitPackageResources.InitPackageResourcesParam resparam, final String resourceName, final int color) {
        final int identifier = resparam.res.getIdentifier(resourceName, RESOURCE_TYPE, PACKAGE_NAME);

        if (identifier != 0) {
            final Drawable drawable = resparam.res.getDrawable(identifier);

            resparam.res.setReplacement(PACKAGE_NAME, RESOURCE_TYPE, resourceName, new XResources.DrawableLoader() {
                @Override
                public Drawable newDrawable(XResources xResources, int i) throws Throwable {
                    Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
                    Bitmap newBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);

                    Paint paint = new Paint();
                    ColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    paint.setColorFilter(colorFilter);

                    Canvas canvas = new Canvas(newBitmap);
                    canvas.drawBitmap(newBitmap, 0, 0, paint);

                    return new BitmapDrawable(resparam.res, newBitmap);
                }
            });
        }
    }
}
