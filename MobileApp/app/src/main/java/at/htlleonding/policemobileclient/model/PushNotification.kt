package at.htlleonding.policemobileclient.model

data class PushNotification (
    val data: NotificationData,
    val to: String
)