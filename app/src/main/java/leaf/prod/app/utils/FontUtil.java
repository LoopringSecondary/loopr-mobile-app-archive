package leaf.prod.app.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

/**
 * 标题:    FontUtil
 * 版本:    V-1.0.0
 */
public class FontUtil {

    public static Typeface getTypeface(Context context, int type) {
        Typeface TEXT_TYPE = null;
        try {
            switch (type) {
                case 1:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Black.ttf");
                    break;
                case 2:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-BlackItalic.ttf");
                    break;
                case 3:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Bold.ttf");
                    break;
                case 4:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-BoldItalic.ttf");
                    break;
                case 5:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Italic.ttf");
                    break;
                case 6:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Light.ttf");
                    break;
                case 7:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-LightItalic.ttf");
                    break;
                case 8:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Medium.ttf");
                    break;
                case 9:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-MediumItalic.ttf");
                    break;
                case 10:
                    TEXT_TYPE = Typeface.createFromAsset(context.getAssets(), "fonts/Rubik-Regular.ttf");
                    break;
            }
        } catch (Exception e) {
            Log.e("FontUtil", "加载第三方字体失败 ");
            TEXT_TYPE = null;
        }
        return TEXT_TYPE;
    }
}
