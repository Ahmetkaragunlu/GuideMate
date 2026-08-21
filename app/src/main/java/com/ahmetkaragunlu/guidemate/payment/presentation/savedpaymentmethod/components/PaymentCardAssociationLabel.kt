package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentCardAssociation

@Composable
fun PaymentCardAssociationLabel(
    association: PaymentCardAssociation,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PaymentCardAssociationMark(association = association)
        Text(
            text = association.displayName,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun PaymentCardAssociationMark(
    association: PaymentCardAssociation,
) {
    when (association) {
        PaymentCardAssociation.MASTER_CARD -> {
            Canvas(modifier = Modifier.size(width = 32.dp, height = 20.dp)) {
                val radius = size.height * 0.42f
                drawCircle(
                    color = Color(0xFFEB001B),
                    radius = radius,
                    center = center.copy(x = center.x - radius * 0.45f),
                )
                drawCircle(
                    color = Color(0xFFF79E1B).copy(alpha = 0.92f),
                    radius = radius,
                    center = center.copy(x = center.x + radius * 0.45f),
                )
            }
        }

        PaymentCardAssociation.VISA -> {
            AssociationTextMark(
                text = "VISA",
                contentColor = Color(0xFF1434CB),
                isItalic = true,
            )
        }

        PaymentCardAssociation.TROY -> {
            AssociationTextMark(
                text = "TROY",
                contentColor = Color(0xFF005BAA),
            )
        }

        PaymentCardAssociation.AMERICAN_EXPRESS -> {
            AssociationTextMark(
                text = "AMEX",
                contentColor = Color(0xFF006FCF),
            )
        }
    }
}

@Composable
private fun AssociationTextMark(
    text: String,
    contentColor: Color,
    isItalic: Boolean = false,
) {
    Box(
        modifier =
            Modifier
                .size(width = 36.dp, height = 22.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp),
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5E7EB),
                    shape = RoundedCornerShape(4.dp),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
        )
    }
}
