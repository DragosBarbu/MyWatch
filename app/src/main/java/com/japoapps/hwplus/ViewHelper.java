package com.japoapps.hwplus;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dragos on 2/28/18.
 */

public class ViewHelper {

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            Context ctx = v.getContext();
            p.setMargins(dpToPixel(l, ctx), dpToPixel(t, ctx), dpToPixel(r, ctx), dpToPixel(b, ctx));
            v.requestLayout();
        }
    }

    public static int dpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public static Snackbar getSnackbar(@NonNull View view, String message, SNACKBAR_TYPE type, int length) {
        Snackbar snackbar = Snackbar.make(view, message, length);
        if (type == SNACKBAR_TYPE.ERROR)
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorAccentDark));
        return snackbar;
    }

    public enum SNACKBAR_TYPE {
        NORMAL,
        ERROR
    }
}
