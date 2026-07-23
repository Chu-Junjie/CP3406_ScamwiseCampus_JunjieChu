package com.chujunjie.scamwisecampus.data.remote.safebrowsing

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest
import okhttp3.Interceptor
import okhttp3.Response

class AndroidAppHeadersInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {
        val requestBuilder = chain.request()
            .newBuilder()
            .header(
                "X-Android-Package",
                context.packageName
            )

        signingCertificateSha1()
            ?.let { certificateSha1 ->
                requestBuilder.header(
                    "X-Android-Cert",
                    certificateSha1
                )
            }

        return chain.proceed(
            requestBuilder.build()
        )
    }

    @Suppress("DEPRECATION")
    private fun signingCertificateSha1(): String? {
        val packageInfo = runCatching {
            context.packageManager.getPackageInfo(
                context.packageName,
                if (
                    Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.P
                ) {
                    PackageManager
                        .GET_SIGNING_CERTIFICATES
                } else {
                    PackageManager.GET_SIGNATURES
                }
            )
        }.getOrNull() ?: return null

        val signatures =
            if (
                Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.P
            ) {
                packageInfo.signingInfo
                    ?.apkContentsSigners
            } else {
                packageInfo.signatures
            }

        val certificateBytes =
            signatures
                ?.firstOrNull()
                ?.toByteArray()
                ?: return null

        val digest = MessageDigest
            .getInstance("SHA-1")
            .digest(certificateBytes)

        return digest.joinToString(
            separator = ""
        ) { byte ->
            "%02X".format(
                byte.toInt() and 0xFF
            )
        }
    }
}
