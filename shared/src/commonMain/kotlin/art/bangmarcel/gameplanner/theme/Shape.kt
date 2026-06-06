package art.bangmarcel.gameplanner.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val GamePlannerShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // tooltips, tags
    small = RoundedCornerShape(8.dp),         // buttons, inputs
    medium = RoundedCornerShape(8.dp),        // cards
    large = RoundedCornerShape(12.dp),        // dialogs
)
