package com.example.rescuesupport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeyHashGenerator {
    private static final String TAG = "KeyHashGenerator";

    public static String generateKeyHash(Context context) {
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES
            );

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                byte[] keyHashBytes = md.digest();

                // Base64 키 해시 값 생성
                String keyHash = Base64.encodeToString(keyHashBytes, Base64.NO_WRAP);

                Log.d(TAG, "Key Hash: " + keyHash);
                return keyHash;
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Log.e(TAG, "Error generating key hash: " + e.getMessage());
        }

        return null;
    }
}
