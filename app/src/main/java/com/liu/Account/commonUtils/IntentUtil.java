package com.liu.Account.commonUtils;

import java.io.File;
import java.util.List;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Telephony;
import android.text.TextUtils;

public class IntentUtil {


    /**
     * 检查是否有已安装的应用程序能够处理给定的Intent/数据。
     *
     * @param context  上下文
     * @param action   要检查的action
     * @param uri      要检查的data的uri(可为空)
     * @param mimeType 要检查的类型 如（audio/*) (may be null)
     * @return true
     */
    public static boolean isIntentAvailable(Context context, String action, Uri uri, String mimeType) {
        final Intent intent = (uri != null) ? new Intent(action, uri) : new Intent(action);
        if (mimeType != null) {
            intent.setType(mimeType);
        }
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    /**
     * 检查是否有应用程序能够处理给定的Intent/类型。
     * Checks whether there are applications installed which are able to handle the given action/type.
     *
     * @param context  the current context
     * @param action   the action to check
     * @param mimeType the MIME type of the content (may be null)
     * @return true if there are apps which will respond to this action/type
     */
    public static boolean isIntentAvailable(Context context, String action, String mimeType) {
        final Intent intent = new Intent(action);
        if (mimeType != null) {
            intent.setType(mimeType);
        }
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    /**
     * 检查是否有程序可处理所给的intent
     *
     * @param context the current context
     * @param intent  the intent to check
     * @return true if there are apps which will respond to this intent
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }


    public static final String AUDIO_TYPE = "audio/*";
    public static final String VIDEO_TYPE = "video/*";
    public static final String IMAGE_TYPE = "image/*";

    /**
     * 打开音乐播放器播放所给文件
     *
     * @param file The file path of the media to play.
     * @return the intent
     */
    public static Intent newPlayAudioFileIntent(File file) {
        return newPlayMediaFileIntent(file, AUDIO_TYPE);
    }

    /**
     * 打开音乐播放器播放所给文件
     *
     * @param path The file path of the media to play.
     * @return the intent
     */
    public static Intent newPlayAudioFileIntent(String path) {
        return newPlayMediaFileIntent(path, AUDIO_TYPE);
    }

    /**
     * 打开音乐播放器播放所给文件
     *
     * @param url The URL of the media to play.
     * @return the intent
     */
    public static Intent newPlayAudioIntent(String url) {
        return newPlayMediaIntent(url, AUDIO_TYPE);
    }

    /**
     * 打开图片管理器展示所给文件
     *
     * @param file The file path of the media to play.
     * @return the intent
     */
    public static Intent newPlayImageFileIntent(File file) {
        return newPlayMediaFileIntent(file, IMAGE_TYPE);
    }

    /**
     * 打开图片管理器展示所给文件
     *
     * @param path The file path of the media to play.
     * @return the intent
     */
    public static Intent newPlayImageFileIntent(String path) {
        return newPlayMediaFileIntent(path, IMAGE_TYPE);
    }

    /**
     * 打开图片管理器展示所给文件
     *
     * @param url The URL of the media to play.
     * @return the intent
     */
    public static Intent newPlayImageIntent(String url) {
        return newPlayMediaIntent(url, IMAGE_TYPE);
    }

    /**
     * 打开视频播放器播放所给文件
     *
     * @param file The file path of the media to play.
     * @return the intent
     */
    public static Intent newPlayVideoFileIntent(File file) {
        return newPlayMediaFileIntent(file, VIDEO_TYPE);
    }

    /**
     * 打开视频播放器播放所给文件
     *
     * @param path The file path of the media to play.
     * @return the intent
     */
    public static Intent newPlayVideoFileIntent(String path) {
        return newPlayMediaFileIntent(path, VIDEO_TYPE);
    }

    /**
     * 打开视频播放器播放所给文件
     *
     * @param url The URL of the media to play.
     * @return the intent
     */
    public static Intent newPlayVideoIntent(String url) {
        return newPlayMediaIntent(url, VIDEO_TYPE);
    }

    /**
     * 打开媒体播放器播放给的媒体文件
     *
     * @param url  The URL of the media to play.
     * @param type The mime type
     * @return the intent
     */
    public static Intent newPlayMediaIntent(String url, String type) {
        return newPlayMediaIntent(Uri.parse(url), type);
    }

    /**
     * 打开媒体播放器播放给的媒体文件
     *
     * @param file The file path of the media to play.
     * @param type The mime type
     * @return the intent
     */
    public static Intent newPlayMediaFileIntent(File file, String type) {
        return newPlayMediaIntent(Uri.fromFile(file), type);
    }

    /**
     * 打开媒体播放器播放给的媒体文件
     *
     * @param path The file path of the media to play.
     * @param type The mime type
     * @return the intent
     */
    public static Intent newPlayMediaFileIntent(String path, String type) {
        return newPlayMediaIntent(Uri.fromFile(new File(path)), type);
    }

    /**
     * 打开媒体播放器播放给的媒体文件链接
     *
     * @param uri  The Uri of the media to play.
     * @param type The mime type
     * @return the intent
     */
    public static Intent newPlayMediaIntent(Uri uri, String type) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        return intent;
    }

    /**
     * 打开浏览器展示所给链接
     *
     * @param url the URL to open
     * @return the intent
     */
    public static Intent newOpenWebBrowserIntent(String url) {
        if(url==null)
            return null;
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        return intent;
    }

    /**
     * 创建一个拍照Intent,并把拍的照片储存在所给File对象中
     *
     * @param tempFile 将要储存所拍照片的文件
     * @return the intent
     */
    public static Intent newTakePictureIntent(File tempFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        return intent;
    }

    /**
     * 创建一个拍照Intent,并把拍的照片储存在所给路径
     *
     * @param tempFile 所拍图片的储存路径
     * @return the intent
     */
    public static Intent newTakePictureIntent(String tempFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempFile)));
        return intent;
    }

    /**
     * 创建一个用来打开图片管理器以选择图片的的Intent
     *
     * @return the intent
     */
    public static Intent newSelectPictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 创建一个打开发短信界面的Intent
     *
     * @return the intent
     */
    public static Intent newEmptySmsIntent(Context context) {
        return newSmsIntent(context, null, (String[]) null);
    }

    /**
     * 创建一个打开发短信界面的Intent
     *
     * @param phoneNumber 短信的收信号码
     * @return the intent
     */
    public static Intent newEmptySmsIntent(Context context, String phoneNumber) {
        return newSmsIntent(context, null, new String[]{phoneNumber});
    }

    /**
     * 创建一个打开发短信界面的Intent
     *
     * @param phoneNumbers 短信的收信人
     * @return the intent
     */
    public static Intent newEmptySmsIntent(Context context, String[] phoneNumbers) {
        return newSmsIntent(context, null, phoneNumbers);
    }

    /**
     * 创建一个发短信的Intent
     *
     * @param body 短信的内容
     * @return the intent
     */
    public static Intent newSmsIntent(Context context, String body) {
        return newSmsIntent(context, body, (String[]) null);
    }

    /**
     * 创建一个发短信的Intent
     *
     * @param body 短信的内容
     * @param phoneNumber 短信的收件人
     * @return the intent
     */
    public static Intent newSmsIntent(Context context, String body, String phoneNumber) {
        return newSmsIntent(context, body, new String[]{phoneNumber});
    }


    /**
     * 创建一个发短信的Intent
     *
     * @param body         短信内容
     * @param phoneNumbers 短信的收件人(可为空)
     * @return the intent
     */
    public static Intent newSmsIntent(Context context, String body, String[] phoneNumbers) {
        Uri smsUri;
        if (phoneNumbers == null || phoneNumbers.length==0) {
            smsUri = Uri.parse("smsto:");
        } else {
            smsUri = Uri.parse("smsto:" + Uri.encode(TextUtils.join(",", phoneNumbers)));
        }

        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_SENDTO, smsUri);
            intent.setPackage(Telephony.Sms.getDefaultSmsPackage(context));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, smsUri);
        }

        if (body!=null) {
            intent.putExtra("sms_body", body);
        }

        return intent;
    }

    /**
     * 打开电话界面并输入给定的号码
     * 并不会直接拨出
     *
     * @param phoneNumber the number to dial
     * @return the intent
     */
    @TargetApi(19) @SuppressLint("NewApi")
    public static Intent newDialNumberIntent(String phoneNumber) {
        final Intent intent;
        if (phoneNumber == null || phoneNumber.trim().length() <= 0) {
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
        } else {
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber.replace(" ", "")));
        }
        return intent;
    }

    /**
     * 创建一个打电话的Intent.
     * 将立即播出  
     * 请求权限 android.Manifest.permission#CALL_PHONE
     *
     * @param phoneNumber the number to call
     * @return the intent
     */
    public static Intent newCallNumberIntent(String phoneNumber) {
        final Intent intent;
        if (phoneNumber == null || phoneNumber.trim().length() <= 0) {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"));
        } else {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber.replace(" ", "")));
        }
        return intent;
    }

    /**
     * 从电话簿中选择联系人
     */
    public static Intent newPickContactIntent() {
        return newPickContactIntent(null);
    }

    /**
     * 从电话簿中选择联系人
     * <p/>
     * Examples:
     * <p/>
     * <code><pre>
     *     // 只选择有email的联系人
     *     IntentUtils.pickContact(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
     * <p/>
     *     // 只选择有电话号码的联系人
     *     IntentUtils.pickContact(Contacts.Phones.CONTENT_TYPE);
     * <p/>
     *     // Select only from users with phone numbers on devices with Eclair and higher
     *     IntentUtils.pickContact(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
     * </pre></code>
     *
     * @param scope You can restrict selection by passing required content type.
     */
    @SuppressWarnings("deprecation")
    public static Intent newPickContactIntent(String scope) {
        Intent intent;
        if (isContacts2ApiSupported()) {
            intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://com.android.contacts/contacts"));
        } else {
            intent = new Intent(Intent.ACTION_PICK, Contacts.People.CONTENT_URI);
        }

        if (!TextUtils.isEmpty(scope)) {
            intent.setType(scope);
        }

        return intent;
    }
    /**
     * Does the current device support the Post eclair contacts API?
     */
    private static boolean isContacts2ApiSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
    }
    /**
     * 创建一个分享的Intent
     *
     * @param subject            分享标题 The subject to share (might be discarded, for instance if the user picks an SMS app)
     * @param message            分享内容  The message to share
     * @param chooserDialogTitle 选择对话框的标题  The title for the chooser dialog
     * @return the intent
     */
    public static Intent newShareTextIntent(String subject, String message, String chooserDialogTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.setType(MIME_TYPE_TEXT);
        return Intent.createChooser(shareIntent, chooserDialogTitle);
    }

    private static final String MIME_TYPE_TEXT = "text/*";

    /**
     * 可打开当前设备上的应用商店上本程序的界面
     *
     * @param context The context associated to the application
     * @return the intent
     */
    public static Intent newMarketForAppIntent(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        return newMarketForAppIntent(context, packageName);
    }

    /**
     * 可打开当前设备上的应用商店上给定程序的界面
     * @param context     The context associated to the application
     * @param packageName 需要在应用商店找的包名
     * @return the intent 如果没有应用商店则返回空
     */
    public static Intent newMarketForAppIntent(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));

        if (!IntentUtil.isIntentAvailable(context, intent)) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?p=" + packageName));
        }

        if (!IntentUtil.isIntentAvailable(context, intent)) {
            intent = null;
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        return intent;
    }

    /**
     * 从文件管理器中选取文件，可在onActivityResult处理得到的数据
     * Pick file from sdcard with file manager. Chosen file can be obtained from Intent in onActivityResult.
     * See code below for example:
     * <p/>
     * <pre><code>
     *     @Override
     *     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     *         Uri file = data.getData();
     *     }
     * </code></pre>
     */
    public static Intent newPickFileIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        return intent;
    }

    /**
     * 创建一个只有一个收件人的email发送Intent
     *
     * @param address 收件人邮箱地址(可为空) (or null if not specified)
     * @param subject 邮件主题(可为空)(or null if not specified)
     * @param body    邮件内容(可为空)The body of the email (or null if not specified)
     * @return the intent
     */
    public static Intent newEmailIntent(String address, String subject, String body) {
        return newEmailIntent(address, subject, body, null);
    }

    /**
     * 创建一个只有一个收件人的email发送Intent(带附件)
     *
     * @param address 收件人邮箱地址(可为空) (or null if not specified)
     * @param subject 邮件主题(可为空)(or null if not specified)
     * @param body    邮件内容(可为空)The body of the email (or null if not specified)
     * @param attachment 附件的URI  必须是邮箱程序可读的地址 
     * The URI of a file to attach to the email. Note that the URI must point to a location the email
     *                   application is allowed to read and has permissions to access.
     * @return the intent
     */
    public static Intent newEmailIntent(String address, String subject, String body, Uri attachment) {
        return newEmailIntent(address == null ? null : new String[]{address}, subject, body, attachment);
    }

    /**
     * 创建一个带附件的邮件Intent (多个收件人)
     *
     * @param addresses  The recipients addresses (or null if not specified)
     * @param subject    The subject of the email (or null if not specified)
     * @param body       The body of the email (or null if not specified)
     * @param attachment The URI of a file to attach to the email. Note that the URI must point to a location the email
     *                   application is allowed to read and has permissions to access.
     * @return the intent
     */
    public static Intent newEmailIntent(String[] addresses, String subject, String body, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (addresses != null) intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        if (body != null) intent.putExtra(Intent.EXTRA_TEXT, body);
        if (subject != null) intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (attachment != null) intent.putExtra(Intent.EXTRA_STREAM, attachment);
        intent.setType(MIME_TYPE_EMAIL);

        return intent;
    }

    private static final String MIME_TYPE_EMAIL = "message/rfc822";
    /**
     * 打开地图并显示给定地址(如果地址存在) (if it exists)
     *
     * @param address    需要在地图上搜寻的地址
     * @param placeTitle 地图上显示点的名字
     * @return the intent
     */
    public static Intent newMapsIntent(String address, String placeTitle) {
        StringBuilder sb = new StringBuilder();
        sb.append("geo:0,0?q=");

        String addressEncoded = Uri.encode(address);
        sb.append(addressEncoded);

        // pass text for the info window
        String titleEncoded = Uri.encode(" (" + placeTitle + ")");
        sb.append(titleEncoded);

        return new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
    }

    /**
     * 打开地图并显示给定的经纬度 (如果经纬度合法) (if it exists)
     *
     * @param latitude   纬度
     * @param longitude  经度 
     * @return the intent
     */
    public static Intent newMapsIntent(float longitude,float latitude) {
        return newMapsIntent(latitude, longitude, null);
    }

    /**
     * 打开地图并显示给定的经纬度 (如果经纬度合法)
     *
     * @param latitude   纬度
     * @param longitude  经度 
     * @param placeName 地图上显示点的名字
     * @return the intent
     */
    public static Intent newMapsIntent( float longitude,float latitude, String placeName) {
        StringBuilder sb = new StringBuilder();
        sb.append("geo:");

        sb.append(latitude);
        sb.append(",");
        sb.append(longitude);

        if (!TextUtils.isEmpty(placeName)) {
            sb.append("?q=");
            sb.append(latitude);
            sb.append(",");
            sb.append(longitude);
            sb.append("(");
            sb.append(Uri.encode(placeName));
            sb.append(")");
        }

        return new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
    }
    /**
     * 打开其他应用程序
     * @param context 上下文对象
     * @param packageName 其他程序的包名
     * @return
     * **/
    public static Intent newApplication(Context context,String packageName){
        Intent intent = new Intent();
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        return intent;
    }
    /***
     * 打开到设置WiFi界面的Intent
     * */
    public static Intent newWiFSettingiIntent(){
        return new  Intent(Settings.ACTION_WIFI_SETTINGS)
                ;
    }
    /***
     * 打开设置数据链接界面的Intent
     * */
    public static Intent newWirelessSettingIntent(){
        return new Intent(Settings.ACTION_WIRELESS_SETTINGS);
    }
    /***
     * 打开设置GPS界面的Intent
     * */
    public static Intent newGPSSettingIntent(){
        return new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }

}