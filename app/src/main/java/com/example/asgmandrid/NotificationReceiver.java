package com.example.asgmandrid;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the habit title from the intent
        String habitTitle = intent.getStringExtra("habit_title");

        // Get the NotificationManager system service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Define the notification channel ID
        String channelId = "habit_channel_id";

        // Check if the app is running on Android 8.0 (API level 26) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);

            // If the notification channel is disabled, prompt the user to enable it
            if (channel != null && channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                // The notification channel is disabled, prompt the user to enable it
                Intent intentToSettings = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intentToSettings.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                intentToSettings.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
                context.startActivity(intentToSettings);
                return; // Don't show the notification if the channel is disabled
            }
        }

        // Proceed with creating and showing the notification
        showNotification(context, habitTitle, channelId);
    }

    private void showNotification(Context context, String habitTitle, String channelId) {
        // Create the notification channel if not already created (for Android 8.0+)
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, // Channel ID
                    "Habit Notifications", // Channel Name
                    NotificationManager.IMPORTANCE_HIGH // Importance level
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Create an intent to open the activity when the notification is tapped
        Intent activityIntent = new Intent(context, main_daily_habit_tracker.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Habit Reminder")
                .setContentText("It's almost time to start: " + habitTitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // High priority for visibility
                .setContentIntent(contentIntent)  // Set the intent to open your activity
                .setAutoCancel(true);  // Automatically remove notification after tapping

        // Issue the notification with a unique ID (using habitTitle hash)
        int notificationId = habitTitle.hashCode();  // Use habitTitle hash code as notification ID
        Log.d("Notification", "Notification ID: " + notificationId);

        notificationManager.notify(notificationId, builder.build());
    }
}
