/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * <p>
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.ldroid.kwei.retrofit;

import android.os.Build;
import android.util.Log;

import com.ldroid.kwei.App;
import com.ldroid.kwei.cookie.PersistentCookieJar;
import com.ldroid.kwei.cookie.cache.SetCookieCache;
import com.ldroid.kwei.cookie.persistence.SharedPrefsCookiePersistor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.Nullable;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

/**
 * Helper class that provides the same OkHttpClient instance that will be used for all networking
 * requests.
 */
public class OkHttpClientProvider {

    private static @Nullable
    OkHttpClient sClient;

    private static @Nullable
    OkHttpClientFactory sFactory;

    public static void setOkHttpClientFactory(OkHttpClientFactory factory) {
        sFactory = factory;
    }

    public static OkHttpClient getOkHttpClient() {
        if (sClient == null) {
            sClient = createClient();
        }
        return sClient;
    }


    public static void replaceOkHttpClient(OkHttpClient client) {
        sClient = client;
    }

    public static OkHttpClient createClient() {
        if (sFactory != null) {
            return sFactory.createNewNetworkModuleClient();
        }


        // No timeouts by default
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.INSTANCE))

                );

        return enableTls12OnPreLollipop(client).build();
    }

    /*
      On Android 4.1-4.4 (API level 16 to 19) TLS 1.1 and 1.2 are
      available but not enabled by default. The following method
      enables it.
     */
    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            try {
                client.sslSocketFactory(new TLSSocketFactory());

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpClientProvider", "Error while enabling TLS 1.2", exc);
            }
        }

        return client;
    }

}
