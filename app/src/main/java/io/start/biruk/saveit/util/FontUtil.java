package io.start.biruk.saveit.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by biruk on 02/07/18.
 */

public class FontUtil {
    public static void setFont(Context context, TextView textView, String fontName) {
        String fontPath = "fonts/" + fontName;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
        textView.setTypeface(typeface);
    }
}
