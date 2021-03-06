/*
 * Copyright 2017 Pranav Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Helper class to perform {@link Bitmap} operations.
 */
public class DynamicBitmapUtils {

    /**
     * Get bitmap from the supplied drawable.
     *
     * @param drawable The drawable to get the bitmap.
     *
     * @return The bitmap from the supplied drawable.
     */
    public @Nullable static Bitmap getBitmapFormDrawable(@Nullable Drawable drawable) {
        if (drawable != null) {
            try {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                return bitmap;
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    /**
     * Resize bitmap to the new width and height.
     *
     * @param bitmap The bitmap to resize.
     * @param newWidth The new width for the bitmap.
     * @param newHeight The new height for the bitmap.
     *
     * @return The resized bitmap with new width and height.
     */
    public static @NonNull Bitmap resizeBitmap(@NonNull Bitmap bitmap, int newWidth,
            int newHeight) {
        Bitmap resizedBitmap = Bitmap.createBitmap(
                newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);

        Canvas canvas = new Canvas(resizedBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return resizedBitmap;
    }

    /**
     * Apply color filter on the supplied bitmap.
     *
     * @param bitmap The bitmap to apply color filter.
     * @param colorFilter The color filter to be applied on the bitmap.
     *
     * @return The new bitmap with applied color filter.
     */
    public static @NonNull Bitmap applyColorFilter(@NonNull Bitmap bitmap,
            @NonNull ColorFilter colorFilter) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(colorFilter);
        paint.setFilterBitmap(true);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return bitmap;
    }

    /**
     * Apply monochrome color filter on the supplied bitmap.
     *
     * @param bitmap The bitmap to apply color filter.
     * @param color The color to generate color filter.
     *
     * @return The new bitmap with applied color filter.
     */
    public static @NonNull Bitmap applyColorFilter(@NonNull Bitmap bitmap, @ColorInt int color) {
        return applyColorFilter(bitmap, new PorterDuffColorFilter(
                color, PorterDuff.Mode.SRC_ATOP));
    }

    /**
     * Extract the dominant color form the supplied bitmap.
     *
     * @param bitmap The bitmap to extract the dominant color.
     *
     * @return The dominant color extracted from the bitmap.
     */
    public static @ColorInt int getDominantColor(@NonNull Bitmap bitmap) {
        Bitmap newBitmap = resizeBitmap(bitmap, 1, 1);
        final @ColorInt int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();

        return color;
    }
}
