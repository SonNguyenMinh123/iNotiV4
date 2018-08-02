/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.notification.inotythemehhhhh.mycenterutils.logs;

import android.util.Log;

import com.notification.inotythemehhhhh.BuildConfig;


/**
 * Helper class to redirect {@link LogUtil#logger} to {@link Log}
 */
public class LoggerDefault implements Logger {
    // true => enable Debug
    // false => disable Debug
    private boolean DEBUG = true;

    @Override
    public int v(String tag, String msg) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.v(tag, msg);
    }

    @Override
    public int v(String tag, String msg, Throwable tr) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.v(tag, msg, tr);
    }

    @Override
    public int d(String tag, String msg) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.d(tag, msg);
    }

    @Override
    public int d(String tag, String msg, Throwable tr) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.d(tag, msg, tr);
    }

    @Override
    public int i(String tag, String msg) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.i(tag, msg);
    }

    @Override
    public int i(String tag, String msg, Throwable tr) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.i(tag, msg, tr);
    }

    @Override
    public int w(String tag, String msg) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.w(tag, msg);
    }

    @Override
    public int w(String tag, String msg, Throwable tr) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.w(tag, msg, tr);
    }

    @Override
    public int e(String tag, String msg) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.e(tag, msg);
    }

    @Override
    public int e(String tag, String msg, Throwable tr) {
        if (!BuildConfig.DEBUG) {
            return 0;
        }
        return Log.e(tag, msg, tr);
    }
}
