/*
 * Copyright 2015 Erkan Molla
 * Copyright 2015 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.acbattery;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class BatteryWidgetProvider extends AppWidgetProvider {

    /**
     * Get the number of widgets that have been enabled and placed on the home screen.
     *
     * @param context The application context.
     */
    public static int getNumberOfWidgets(final Context context) {
        ComponentName componentName = new ComponentName(context, BatteryWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] activeWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        return activeWidgetIds == null ? 0 : activeWidgetIds.length;
    }

    @Override
    public void onEnabled(Context context) {
        context.startService(new Intent(context, MonitorService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] widgetIds) {
        // ensure service is running
        context.startService(new Intent(context, MonitorService.class));
        // update the widgets
        Intent updateIntent = new Intent(context, UpdateService.class);
        updateIntent.setAction(UpdateService.ACTION_WIDGET_UPDATE);
        updateIntent.putExtra(UpdateService.EXTRA_WIDGET_IDS, widgetIds);
        context.startService(updateIntent);
    }

    @Override
    public void onDeleted(Context context, int[] widgetIds) {
        if (getNumberOfWidgets(context) == 0) {
            // stop monitoring if there are no more widgets on screen
            context.stopService(new Intent(context, MonitorService.class));
        }
    }
}
