package com.szy.common.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.szy.common.Constant.MainColor;
import com.szy.common.Constant.ViewType;
import com.szy.common.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zongren on 2016/3/15.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class CommonUtils {
    private static final String TAG = "CommonUtils";
    private static final String SHARED_PREFERENCE_FILE = "YiShopApp";

    public static ViewType commonGetViewTypeOfTag(View view) {
        int viewType = CommonUtils.getViewTypeIntegerOfTag(view);

        return ViewType.valueOf(viewType);
    }

    public static void commonSetViewTypeForTag(View view, ViewType viewType) {
        CommonUtils.setViewTypeIntegerForTag(view, viewType.ordinal());
    }

    /**
     * Copy a field to the target object.
     *
     * @param target     The target object.
     * @param fieldName  The field name.
     * @param fieldValue The field value.
     */
    public static void copyField(Object target, String fieldName, Object fieldValue) {
        if (fieldValue == null | fieldName == null | target == null) {
            return;
        }
        Field[] fieldsTarget = target.getClass().getFields();
        for (Field fieldTarget : fieldsTarget) {
            if (fieldTarget.getName().equals(fieldName)) {
                try {
                    fieldTarget.set(target, fieldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Copy fields in source to target.
     *
     * @param source The source object.
     * @param target The target object.
     */
    public static void copyFields(Object source, Object target) {
        if (source == null | target == null) {
            return;
        }
        Field[] fieldsSource = source.getClass().getFields();
        Field[] fieldsTarget = target.getClass().getFields();

        for (Field fieldTarget : fieldsTarget) {
            for (Field fieldSource : fieldsSource) {
                if (fieldTarget.getName().equals(fieldSource.getName())) {
                    try {
                        fieldTarget.set(target, fieldSource.get(source));
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    /**
     * Create a target object and copy fields in source object to it.
     *
     * @param source      The source object.
     * @param targetClass The target object class.
     * @param <T>         The target object.
     * @return The target object.
     */
    public static <T> T copyFields(Object source, Class<T> targetClass) {
        T targetObject = null;
        try {
            targetObject = targetClass.newInstance();
            CommonUtils.copyFields(source, targetObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return targetObject;
    }

    public static void copyToClipboard(Context context, String label, String content) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(
                Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText(label, content);
        manager.setPrimaryClip(data);
    }

    /**
     * Create a new folder.
     *
     * @param dirPath The folder path.
     * @return isCreated
     */
    public static boolean createDir(String dirPath) {
        File folder = new File(dirPath);
        return !folder.exists() && ((folder.exists() && folder.isDirectory()) || folder.mkdirs());
    }

    /**
     * Create a new file.
     *
     * @param filePath The file path.
     * @param fileName The file name.
     * @return isCreated
     * @throws IOException
     */
    public static boolean createFile(String filePath, String fileName) throws IOException {
        File file = new File(filePath + "/" + fileName);
        return !file.exists() && file.createNewFile();
    }

    /**
     * Delete a file or a directory.
     *
     * @param filePath File path.
     * @return isDelete
     * @throws IOException
     */
    public static boolean deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        boolean isDelete = true;
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            isDelete = file.delete();
        } else if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    isDelete = isDelete && f.delete();
                }
                if (f.isDirectory()) {
                    isDelete = isDelete && deleteFile(f.getAbsolutePath());
                }
            }
        } else {
            return false;
        }

        return isDelete;
    }

    /**
     * Get a value in context's preferences file.
     *
     * @param context The context.
     * @param key     The key.
     * @return {String} value.
     */
    public static boolean getBoolFromSharedPreferences(Context context, String key) {
        if (context == null) {
            return false;
        }
        if (key == null) {
            return false;
        }
        return getBoolFromSharedPreferences(context, key, SHARED_PREFERENCE_FILE);
    }

    /**
     * Get a value in context's preferences file.
     *
     * @param context  The context.
     * @param key      The key.
     * @param fileName The filename.
     * @return {String} value.
     */
    public static boolean getBoolFromSharedPreferences(Context context, String key, String fileName) {
        if (context == null) {
            return false;
        }
        if (key == null) {
            return false;
        }
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    /**
     * Read inputstream and output byte array.
     *
     * @param inputStream The input stream.
     * @return Byte[].
     * @throws IOException
     */
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getDomain(String urlString) {
        Uri uri = Uri.parse(urlString);
        String host = uri.getHost();
        return host.startsWith("www.") ? host.substring(4) : host;
    }

    /**
     * Get the view's extra info.
     *
     * @param view The view.
     * @return The extra info.
     */
    public static int getExtraInfoOfTag(View view) {
        int tag;
        if (view.getTag(R.id.tag_extra_info) == null) {
            tag = 0;
        } else {
            tag = (int) view.getTag(R.id.tag_extra_info);
        }
        return tag;
    }

    /**
     * Get filepath of context.
     *
     * @param context The context.
     * @return The filepath.
     */
    public static String getFileDir(Context context) {
        File file = context.getFilesDir();
        return file.getAbsolutePath();
    }

    public static String getHost(String urlString) {
        Uri uri = Uri.parse(urlString);
        return uri.getHost();
    }

    /**
     * Get the real path of image uri.
     *
     * @param context  The context provide content resolver.
     * @param imageUri The image uri to be solved.
     * @return The real path of the image uri.
     */
    public static String getImagePath(Context context, Uri imageUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(imageUri, filePathColumn, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return picturePath;
        } else {
            return null;
        }
    }

    /**
     * Convert dp to pixel.
     *
     * @param context The context which provides resources.
     * @param dp      The dp value.
     * @return Pixel value equivalent to db.
     */
    public static int getPixel(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * Get the view's position of tag.
     *
     * @param view The view.
     * @return The position.
     */
    public static int getPositionOfTag(View view) {
        int tag;
        if (view.getTag(R.id.tag_position) == null) {
            tag = 0;
        } else {
            tag = (int) view.getTag(R.id.tag_position);
        }
        return tag;
    }

    /**
     * Get a value in context's preferences file.
     *
     * @param context The context.
     * @param key     The key.
     * @return {String} value.
     */
    public static String getSharedPreferences(Context context, String key) {
        if (context == null) {
            return null;
        }
        if (key == null) {
            return null;
        }
        return getSharedPreferences(context, key, SHARED_PREFERENCE_FILE);
    }

    /**
     * Get a value in context's preferences file.
     *
     * @param context  The context.
     * @param key      The key.
     * @param fileName The filename.
     * @return {String} value.
     */
    public static String getSharedPreferences(Context context, String key, String fileName) {
        if (context == null) {
            return null;
        }
        if (key == null) {
            return null;
        }
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getString(key, null);
    }

    /**
     * Get string by resourceId
     *
     * @param context    The context.
     * @param resourceId The resource id.
     * @return The string of the resource id.
     */
    public static String getString(Context context, int resourceId) {
        Log.i(TAG, resourceId + "");
        return context.getResources().getString(resourceId);
    }

    /**
     * Get string by resourceId
     *
     * @param context    The context.
     * @param resourceId The resource id.
     * @param formatArgs The arguments.
     * @return The string of the resource id.
     */
    public static String getString(Context context, int resourceId, Object... formatArgs) {
        return context.getResources().getString(resourceId, formatArgs);
    }

    /**
     * @return {String} timeStamp
     */
    public static String getTimestamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong.toString();
    }

    /**
     * Get app`s version code.
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int code = 1;
        try {
            code = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to get app version name.");
        }
        return code;
    }

    /**
     * Get app`s version name.
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version = "1.0.0";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to get app version name.");
        }
        return version;
    }

    /**
     * Get the type of view.
     *
     * @param view The view.
     * @return The view type.
     */
    public static int getViewTypeIntegerOfTag(View view) {
        int tag;
        if (view.getTag(R.id.tag_view_type) == null) {
            tag = 0;
        } else {
            tag = (int) view.getTag(R.id.tag_view_type);
        }
        return tag;
    }

    /**
     * Get the activity`s height
     *
     * @param activity The activity
     * @return height
     */
    public static int getWindowHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    /**
     * Get the activity`s width
     *
     * @param activity The activity
     * @return width
     */
    public static int getWindowWidth(Activity activity) {
        if (activity == null) {
            return 0;
        }
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static void instantiateFieldsOfObject(Object object, Set<String> packages) throws IllegalArgumentException, InstantiationException, IllegalAccessException {
        instantiateFieldsOfObject(object, packages, 0);
    }

    public static void instantiateFieldsOfObject(Object object, Set<String> packages, int depth) throws IllegalArgumentException, InstantiationException, IllegalAccessException {
        if (object == null) {
            return;
        }
        if (depth > 200) {
            return;
        }
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            Class<?> fieldClass = field.getType();

            if (fieldClass.isPrimitive()) {
                continue;
            }

            boolean inPackage = false;
            boolean isList = false;
            boolean isMap = false;
            boolean isString = false;
            if (fieldClass.equals(List.class)) {
                isList = true;
            } else if (fieldClass.equals(Map.class)) {
                isMap = true;
            } else if (fieldClass.equals(String.class)) {
                isString = true;
            } else {
                for (String pack : packages) {
                    if (fieldClass.getPackage() != null && fieldClass.getPackage().getName() != null && fieldClass.getPackage().getName().contains(
                            pack)) {
                        inPackage = true;
                        break;
                    }
                }
            }
            if (!inPackage && !isList && !isMap && !isString) {
                continue;
            }

            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);

            Object fieldValue = field.get(object);
            if (fieldValue == null) {
                if (isList) {
                    fieldValue = new ArrayList<>();
                } else if (isMap) {
                    fieldValue = new HashMap<>();
                } else if (isString) {
                    fieldValue = "";
                } else {
                    fieldValue = fieldClass.newInstance();
                }
                if (fieldValue != null) {
                    field.set(object, fieldValue);
                }
            }
            field.setAccessible(isAccessible);
            if (isList) {
                List list = (List) fieldValue;
                for (Object childObject : list) {
                    CommonUtils.instantiateFieldsOfObject(childObject, packages, depth + 1);
                }
            } else if (isMap) {
                Map map = (Map) fieldValue;
                for (Object childObject : map.values()) {
                    CommonUtils.instantiateFieldsOfObject(childObject, packages, depth + 1);
                }
            } else if (inPackage) {
                CommonUtils.instantiateFieldsOfObject(fieldValue, packages, depth + 1);
            }
        }
    }

    /**
     * Check if device is connected to internet.
     *
     * @param context The context.
     * @return isConnected
     */
    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }


    public static boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$";

        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    /**
     * Check if a file exists.
     *
     * @param filePath The filepath you want to check.
     * @return isExist.
     */
    public static boolean isFileExist(String filePath) {
        if (CommonUtils.isNull(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Check if a folder exists.
     *
     * @param dirPath The folder you want to check.
     * @return isExist.
     */
    public static boolean isFolderExist(String dirPath) {
        if (CommonUtils.isNull(dirPath)) {
            return false;
        }
        File dire = new File(dirPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * Check if mobile network is connected.
     *
     * @param context The context.
     * @return isMobileConnected
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null && networkInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a string is null or empty or equals 'null'.
     *
     * @param string The string to be checked.
     * @return isNull
     */
    public static boolean isNull(String string) {
        return string == null || string.length() == 0 || string.trim().length() == 0;
    }

    /**
     * Check if all string are null or empty or equal 'null'.
     *
     * @param strings The string list to be checked.
     * @return isNull
     */
    public static boolean isNull(String... strings) {
        for (String s : strings) {
            if (!CommonUtils.isNull(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a byte[] is null or empty.
     *
     * @param bytes The bytes to be checked.
     * @return isNull
     */
    public static boolean isNull(byte[] bytes) {
        return bytes == null || bytes.length <= 0;
    }

    /**
     * Check if a Map is null or empty.
     *
     * @param map The map to be checked.
     * @return isNull
     */
    public static boolean isNull(Map map) {
        return map == null || map.size() <= 0;
    }

    /**
     * Check if a List is null or empty.
     *
     * @param list The lists to be checked.
     * @return isNull
     */
    public static boolean isNull(List list) {
        return list == null || list.size() <= 0;
    }

    /**
     * Check if all list are null or empty.
     *
     * @param lists The list to be checked.
     * @return isNull
     */
    public static boolean isNull(List... lists) {
        for (List l : lists) {
            if (!CommonUtils.isNull(l)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if object is null.
     *
     * @param object The object to be checked.
     * @return isNull
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * Check if all objects are null.
     *
     * @param objects The objects to be checked.
     * @return isNull
     */
    public static boolean isNull(Object... objects) {
        for (Object o : objects) {
            if (!CommonUtils.isNull(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a string is phone number.
     *
     * @param phone The phone number you want to check.
     * @return isPhone.
     */
    public static boolean isPhone(String phone) {
        if (CommonUtils.isNull(phone)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^1(3|4|5|6|7|8|9)\\d{9}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }

    /**
     * Check if wifi is connected.
     *
     * @param context The context.
     * @return isWifiConnected
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null && networkInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public static String join(List<String> list, String glue) {
        String result = "";

        for (String item : list) {
            if (result.length() == 0) {
                result += item;
            } else {
                result += (glue + item);
            }
        }
        return result;
    }

    /**
     * @param sourceString
     * @param flag
     * @param targetLength
     * @return result string
     */
    public static String leftPadding(String sourceString, String flag, int targetLength) {
        if (sourceString == null) {
            sourceString = "";
        }

        if (flag == null) {
            flag = "0";
        }

        if (sourceString.length() >= targetLength) {
            return sourceString;
        }

        while (sourceString.length() < targetLength) {
            sourceString = flag + sourceString;
        }

        return sourceString;
    }

    /**
     * Make a toast.
     *
     * @param context The context.
     * @param content The content.
     */
    public static void makeToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * Make a toast.
     *
     * @param context    The context.
     * @param resourceId The content id.
     */
    public static void makeToast(Context context, int resourceId) {
        makeToast(context, context.getString(resourceId));
    }

    /**
     * MD5
     *
     * @param string Target.
     * @return Target's MD5 value.
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * Open activity with context.
     *
     * @param context    The context used to open activity.
     * @param className  The activity className,without package name.
     * @param parameters The parameters that will be added to intent.
     * @return If the activity is successfully opened.
     */
    public static boolean openActivity(Context context, String className, String packageName, Map<String, String> parameters) {
        String fullPackageName = context.getPackageName() + ".Activity." + className;
        if (!TextUtils.isEmpty(packageName)) {
            fullPackageName = packageName + ".Activity." + className;
        }

        try {
            Class clazz = Class.forName(fullPackageName);
            Intent intent = new Intent(context, clazz);
            PackageManager packageManager = context.getPackageManager();
            if (intent.resolveActivity(packageManager) != null) {
                Set<Map.Entry<String, String>> entries = parameters.entrySet();
                for (Map.Entry entry : entries) {
                    intent.putExtra((String) entry.getKey(), (String) entry.getValue());
                }
                context.startActivity(intent);
                return true;
            } else {
                Toast.makeText(context, "Cannot resolve activity:" + className,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ClassNotFoundException e) {
            Toast.makeText(context, "Cannot find class:" + className, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Open activity with context.
     *
     * @param context   The context used to open activity.
     * @param className The activity className,without package name.
     * @return If the activity is successfully opened.
     */
    public static boolean openActivity(Context context, String className, String packageName) {
        return CommonUtils.openActivity(context, className, packageName, new HashMap<String, String>());
    }

    /**
     * Open activity with context.
     *
     * @param context The context used to open activity.
     * @param intent  The intent.
     * @return If the activity is successfully opened.
     */
    public static boolean openActivity(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            context.startActivity(intent);
            return true;
        } else {
            Toast.makeText(context, "Cannot resolve activity", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Open url with other browsers.
     *
     * @param context The context.
     * @param url     The url.
     */
    public static void openBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * Read a file and return string
     *
     * @param filePath The file name.
     * @return fileContent
     * @throws IOException
     */
    public static String readFile(String filePath) throws IOException {
        return CommonUtils.readFile(new File(filePath));
    }

    /**
     * Read a file and return string
     *
     * @param filePath The file name.
     * @param charset  charset The charset.
     * @return fileContent
     * @throws IOException
     */
    public static String readFile(String filePath, String charset) throws IOException {
        return CommonUtils.readFile(new File(filePath), charset);
    }

    /**
     * Read a file and return string
     *
     * @param fileName The file name.
     * @return fileContent
     * @throws IOException
     */
    public static String readFile(File fileName) throws IOException {
        return CommonUtils.readFile(fileName, "UTF-8");
    }

    /**
     * Read a file and return string
     *
     * @param fileName The file name.
     * @param charset  The charset.
     * @return fileContent
     * @throws IOException
     */
    public static String readFile(File fileName, String charset) throws IOException {
        if (fileName == null || !fileName.isFile()) {
            return null;
        }
        StringBuilder builder = new StringBuilder("");
        BufferedReader reader = null;
        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(fileName), charset);
            reader = new BufferedReader(in);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return builder.toString();
    }

    /**
     * Remove extra slashes of url string.
     *
     * @param url The messed up url.
     * @return The clean url.
     */
    public static String removeExtraSlashOfUrl(String url) {
        if (url == null || url.length() == 0) {
            return url;
        }
        return url.replaceAll("(?<!(http:|https:))/+", "/");
    }

    /**
     * Add extra info to the view's tag.
     *
     * @param view      The view.
     * @param extraInfo The extra info.
     */
    public static void setExtraInfoForTag(View view, int extraInfo) {
        view.setTag(R.id.tag_extra_info, extraInfo);
    }

    /**
     * Add position info to the view's tag.
     *
     * @param view     The view.
     * @param position The position.
     */
    public static void setPositionForTag(View view, int position) {
        view.setTag(R.id.tag_position, position);
    }

    /**
     * Set or add a key-value pair in preferences file.
     *
     * @param context The context.
     * @param key     The key.
     * @param value   The value.
     */
    public static void setSharedPreferences(Context context, String key, boolean value) {
        if (context == null) {
            return;
        }
        if (key == null) {
            return;
        }
        setSharedPreferences(context, key, value, SHARED_PREFERENCE_FILE);
    }

    /**
     * Set or add a key-value pair in preferences file.
     *
     * @param context  The context.
     * @param key      The key.
     * @param value    The value.
     * @param fileName The filename.
     */
    public static void setSharedPreferences(Context context, String key, boolean value, String fileName) {
        if (context == null) {
            return;
        }
        if (key == null) {
            return;
        }
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().putBoolean(key,
                value).commit();
    }

    /**
     * Set or add a key-value pair in preferences file.
     *
     * @param context The context.
     * @param key     The key.
     * @param value   The value.
     */
    public static void setSharedPreferences(Context context, String key, String value) {
        if (context == null) {
            return;
        }
        if (key == null) {
            return;
        }
        setSharedPreferences(context, key, value, SHARED_PREFERENCE_FILE);
    }

    /**
     * Set or add a key-value pair in preferences file.
     *
     * @param context  The context.
     * @param key      The key.
     * @param value    The value.
     * @param fileName The filename.
     */
    public static void setSharedPreferences(Context context, String key, String value, String fileName) {
        if (context == null) {
            return;
        }
        if (key == null) {
            return;
        }
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().putString(key,
                value).commit();
    }

    /**
     * Set the type of view.
     *
     * @param view     The view.
     * @param viewType The view type {@link ViewType}.
     */
    public static void setViewTypeIntegerForTag(View view, int viewType) {
        view.setTag(R.id.tag_view_type, viewType);
    }

    /**
     * Shrink image.
     *
     * @param bitmap    The bitmap need to be scaled.
     * @param maxWidth  The max width.
     * @param maxHeight The max height.
     * @return new Bitmap.
     */
    public static Bitmap shrinkImage(Bitmap bitmap, int maxWidth, int maxHeight) {
        int photoWidth = bitmap.getWidth();
        int photoHeight = bitmap.getHeight();
        if (photoHeight <= maxHeight && photoWidth <= maxWidth) {
            return bitmap;
        }

        float scaleWidth = ((float) maxWidth) / photoWidth;
        float scaleHeight = ((float) maxHeight) / photoHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, photoWidth, photoHeight, matrix,
                false);
        return resizedBitmap;
    }

    public static String toDateString(String timestamp) {
        return CommonUtils.toDateString(timestamp, "yyyy-MM-dd");
    }

    @SuppressLint("SimpleDateFormat")
    public static String toDateString(String timestamp, String format) {
        Long timestampLong = Long.parseLong(timestamp);
        //        return new SimpleDateFormat(format).format(new java.util.Date(timestampLong));

        SimpleDateFormat formatTime = new SimpleDateFormat(format);
        String time = formatTime.format(new Date(timestampLong * 1000L));
        return time;
    }

    /**
     * Convert input stream to string.
     *
     * @param in The input stream.
     * @return result
     * @throws IOException
     */
    public static String toString(InputStream in) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    public static String toTimeString(String timestamp) {
        return CommonUtils.toDateString(timestamp, "yyyy-MM-dd HH:mm:ss");
    }

    public static long toTimestamp(String dateString) {
        //return CommonUtils.toDateString(dateString, "yyyy-MM-dd");
        return CommonUtils.toTimestamp(dateString, "yyyy-MM-dd");
    }

    @SuppressLint("SimpleDateFormat")
    public static long toTimestamp(String dateString, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Write content to a file.
     *
     * @param filePath File path.
     * @param content  File content.
     * @param append   If append to file.
     * @return isSuccess
     * @throws IOException
     */
    public static boolean writeFile(String filePath, String content, boolean append) throws IOException {
        if (CommonUtils.isNull(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            CommonUtils.createDir(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    /**
     * Write content to a file.
     *
     * @param filePath File path.
     * @param content  File content.
     * @return isSuccess.
     * @throws IOException
     */
    public static boolean writeFile(String filePath, String content) throws IOException {
        return writeFile(filePath, content, false);
    }

    public static class toastUtil {
        private static Toast toast;

        public static void showToast(Context mContext, String message) {

            String msg = message;

            if (toast == null) {
                toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
            } else {
                toast.cancel();
                toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
            }

            toast.show();
        }
    }


    public static int dpToPx(Context context, float dpValue) {
        if (dpValue <= 0) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 图片上色
     *
     * @param up
     * @return
     */
    public static Drawable drawableUp(Drawable up) {
//        Drawable.ConstantState state = up.getConstantState();
//        Drawable drawableUp = DrawableCompat.wrap(state == null ? up : state.newDrawable()).mutate();
        Drawable drawableUp = DrawableCompat.wrap(up);
        DrawableCompat.setTint(drawableUp, MainColor.getInstance().getColorPrimary());
        return drawableUp;
    }

    public static Drawable getDrawableUp(Context context, @DrawableRes int resid) {
        return getDrawableUp(context, resid, false);
    }

    public static Drawable getDrawableUp(Context context, @DrawableRes int resid, boolean flag) {
        Drawable drawable = flag ? ContextCompat.getDrawable(context, resid).mutate() : ContextCompat.getDrawable(context, resid);
        return drawableUp(drawable);
    }

}
