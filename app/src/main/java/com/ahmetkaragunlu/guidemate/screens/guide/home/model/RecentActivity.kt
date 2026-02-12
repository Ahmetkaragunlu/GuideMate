package com.ahmetkaragunlu.guidemate.screens.guide.home.model

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Message
import compose.icons.tablericons.Star
import compose.icons.tablericons.Ticket

data class RecentActivity(
    val icon: ImageVector,
    val description: String,
    val time: String,
    )



val recentActivities = listOf(
    RecentActivity(TablerIcons.Ticket, "Ahmet tur satın aldı.", "10 dk önce"),
    RecentActivity(TablerIcons.Star, "Ayşe 5 yıldız verdi.", "1 saat önce" ),
    RecentActivity(TablerIcons.CreditCard, "Paran hesabına aktarıldı.", "Dün"),
    RecentActivity(TablerIcons.Message, "Mehmet yorum yaptı", "Dün"),
    RecentActivity(TablerIcons.Message, "Mehmet yorum yaptı", "Dün"),
    RecentActivity(TablerIcons.Message, "Mehmet yorum yaptı", "Dün"),

)
