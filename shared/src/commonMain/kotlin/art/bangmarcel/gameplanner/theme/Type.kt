package art.bangmarcel.gameplanner.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import gameplanner.shared.generated.resources.Inter
import gameplanner.shared.generated.resources.Res
import org.jetbrains.compose.resources.FontResource

@Composable
fun GamePlannerTypography(): Typography {
    val interFamily = FontFamily(
        org.jetbrains.compose.resources.Font(Res.font.Inter)
    )

    return with(MaterialTheme.typography) {
        copy(
            displayLarge = TextStyle(
                fontFamily = interFamily,
                fontWeight = FontWeight.W700,
                fontSize = 36.sp,
                lineHeight = 43.2.sp,   // 36 * 1.2
                letterSpacing = (-0.02).sp,
            ),
            headlineLarge = TextStyle(
                fontFamily = interFamily,
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                lineHeight = 31.2.sp,   // 24 * 1.3
                letterSpacing = (-0.01).sp,
            ),
            headlineMedium = TextStyle(
                fontFamily = interFamily,
                fontWeight = FontWeight.W600,
                fontSize = 20.sp,
                lineHeight = 28.sp,     // 20 * 1.4
            ),
            bodyLarge = TextStyle(
                fontFamily = interFamily,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                lineHeight = 24.sp,     // 16 * 1.5
            ),
            bodySmall = TextStyle(
                fontFamily = interFamily,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                lineHeight = 21.sp,     // 14 * 1.5
            ),
            labelMedium = TextStyle(
                fontFamily = interFamily,
                fontWeight = FontWeight.W600,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                letterSpacing = 0.06.sp,  // ~0.05em at 12sp
            ),
        )
    }
}
