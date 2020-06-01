package com.appplatform.commons.utils;

import android.content.Context;

import com.appplatform.commons.R;

import java.text.DecimalFormat;
import java.util.Locale;

public class FormatterUtils {
    public static String formatFileSize(Context context, long number) {
        return formatFileSize(context, number, false);
    }


    public static String formatPercent(float percent) {
        DecimalFormat df = new DecimalFormat("##.##");
        df.applyPattern("0.00");
        return "Bat: " + df.format(percent) + " %";
    }

    public static String formatShortFileSize(Context context, long number) {
        return formatFileSize(context, number, true);
    }

    private static String formatFileSize(Context context, long number, boolean shorter) {
        if (context == null) {
            return "";
        }
        String value;
        float result = (float) number;
        int suffix = R.string.byte_short;
        if (result > 900.0f) {
            suffix = R.string.kilobyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.megabyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.gigabyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.terabyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.petabyte_short;
            result /= 1024.0f;
        }
        if (result < 1.0f) {
            value = String.format(Locale.US, "Ram: %.2f", result);
        } else if (result < 10.0f) {
            if (shorter) {
                value = String.format(Locale.US, "Ram: %.1f", result);
            } else {
                value = String.format(Locale.US, "Ram: %.2f", result);
            }
        } else if (result >= 100.0f) {
            value = String.format(Locale.US, "Ram: %.0f", result);
        } else if (shorter) {
            value = String.format(Locale.US, "Ram: %.0f", result);
        } else {
            value = String.format(Locale.US, "Ram: %.2f", result);
        }
        return context.getResources().getString(R.string.fileSizeSuffix, value, context.getString(suffix));
    }
    public static String formatShortFileSize2(Context context, long number) {
        return formatFileSize2(context, number, true);
    }


    private static String formatFileSize2(Context context, long number, boolean shorter) {
        if (context == null) {
            return "";
        }
        String value;
        float result = (float) number;
        int suffix = R.string.byte_short;
        if (result > 900.0f) {
            suffix = R.string.kilobyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.megabyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.gigabyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.terabyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.petabyte_short;
            result /= 1024.0f;
        }
        if (result < 1.0f) {
            value = String.format(Locale.US, "%.2f", result);
        } else if (result < 10.0f) {
            if (shorter) {
                value = String.format(Locale.US, "%.1f", result);
            } else {
                value = String.format(Locale.US, "%.2f", result);
            }
        } else if (result >= 100.0f) {
            value = String.format(Locale.US, "%.0f", result);
        } else if (shorter) {
            value = String.format(Locale.US, "%.0f", result);
        } else {
            value = String.format(Locale.US, "%.2f", result);
        }
        return context.getResources().getString(R.string.fileSizeSuffix, value, context.getString(suffix));
    }

    public static String[] formatFileSizeArray(Context context, long number) {
        return formatFileSizeArray(context, number, true);
    }

    public static String[] formatShortFileSizeArray(Context context, long number) {
        return formatFileSizeArray(context, number, true);
    }

    private static String[] formatFileSizeArray(Context context, long number, boolean shorter) {
        if (context == null) {
            return null;
        }
        String value;
        String[] sizes = new String[2];
        float result = (float) number;
        int suffix = R.string.byte_short;
        if (result > 900.0f) {
            suffix = R.string.kilobyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.megabyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.gigabyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.terabyte_short;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.petabyte_short;
            result /= 1024.0f;
        }
        if (result < 1.0f) {
            value = String.format(Locale.US, "%.2f", result);
        } else if (result < 10.0f) {
            if (shorter) {
                value = String.format(Locale.US, "%.1f", result);
            } else {
                value = String.format(Locale.US, "%.2f", result);
            }
        } else if (result >= 100.0f) {
            value = String.format(Locale.US, "%.0f", result);
        } else if (shorter) {
            value = String.format(Locale.US, "%.0f", result);
        } else {
            value = String.format(Locale.US, "%.2f", result);
        }
        sizes[0] = value;
        sizes[1] = context.getString(suffix);
        return sizes;
    }
}
