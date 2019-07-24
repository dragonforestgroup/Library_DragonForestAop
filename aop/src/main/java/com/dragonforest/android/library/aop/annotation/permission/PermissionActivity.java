package com.dragonforest.android.library.aop.annotation.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * 请求权限activity
 * <br>
 * 进入此activity自动开始请求权限，必须由入口requestPermission（）进入，需要自己设置请求权限监听器
 *
 * @author 韩龙林
 * @date 2019/6/4 14:25
 */
public class PermissionActivity extends Activity {
    String[] permissions;
    int requestCode = 0;
    public static IPermission iPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "PermissionActivity onCreate()");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        requestCode = extras.getInt("requestCode");
        permissions = extras.getStringArray("permissions");
        startRequestPermissions();
    }

    private void finishWithNoAnim() {
        finish();
        overridePendingTransition(0, 0);
    }

    private void startRequestPermissions() {
        List<String> permissionNeedRequest = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionNeedRequest.add(permissions[i]);
            }
        }
        if (permissionNeedRequest.size() == 0) {
            // 不需要请求权限
            iPermission.onPermissionGrant(requestCode, permissions);
            finishWithNoAnim();
        } else {
            // 请求权限
            ActivityCompat.requestPermissions(this, permissionNeedRequest.toArray(new String[permissionNeedRequest.size()]), requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (this.requestCode == requestCode) {
            List<String> deniedList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permissions[i]);
                }
            }
            if (deniedList.size() == 0) {
                // 全部请求成功
                if (iPermission != null) {
                    iPermission.onPermissionGrant(requestCode, permissions);
                }
            } else {
                // 不是全部请求成功
                if (iPermission != null) {
                    iPermission.onPermissionDenied(requestCode, deniedList.toArray(new String[deniedList.size()]));
                }
            }
            finishWithNoAnim();
        }
    }

    public interface IPermission {
        void onPermissionGrant(int requestCode, String[] grantedPers);

        void onPermissionDenied(int requestCode, String[] deniedPers);

        void onPermissionCanceled(int requestCode, String[] canceledPers);
    }

    /**
     * 开始请求权限
     * 外部调用
     *
     * @param context
     * @param permissions
     * @param requestCode
     * @param iPermissionListener
     */
    public static void requestPermission(Context context, String[] permissions, int requestCode, IPermission iPermissionListener) {
        iPermission = iPermissionListener;
        Intent intent = new Intent(context, PermissionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("requestCode", requestCode);
        bundle.putStringArray("permissions", permissions);
        intent.putExtras(bundle);
        context.startActivity(intent);

        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }
}
