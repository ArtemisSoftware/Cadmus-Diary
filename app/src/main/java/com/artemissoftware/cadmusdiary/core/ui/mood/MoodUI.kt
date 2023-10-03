package com.artemissoftware.cadmusdiary.core.ui.mood

import androidx.compose.ui.graphics.Color
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.domain.model.Mood
import com.artemissoftware.cadmusdiary.ui.theme.AngryColor
import com.artemissoftware.cadmusdiary.ui.theme.AwfulColor
import com.artemissoftware.cadmusdiary.ui.theme.BoredColor
import com.artemissoftware.cadmusdiary.ui.theme.CalmColor
import com.artemissoftware.cadmusdiary.ui.theme.DepressedColor
import com.artemissoftware.cadmusdiary.ui.theme.DisappointedColor
import com.artemissoftware.cadmusdiary.ui.theme.HappyColor
import com.artemissoftware.cadmusdiary.ui.theme.HumorousColor
import com.artemissoftware.cadmusdiary.ui.theme.LonelyColor
import com.artemissoftware.cadmusdiary.ui.theme.MysteriousColor
import com.artemissoftware.cadmusdiary.ui.theme.NeutralColor
import com.artemissoftware.cadmusdiary.ui.theme.RomanticColor
import com.artemissoftware.cadmusdiary.ui.theme.ShamefulColor
import com.artemissoftware.cadmusdiary.ui.theme.SurprisedColor
import com.artemissoftware.cadmusdiary.ui.theme.SuspiciousColor
import com.artemissoftware.cadmusdiary.ui.theme.TenseColor

// TODO: isto é uma classe de UI. No multi module mudar isto para o package correto
enum class MoodUI(
    val icon: Int,
    val contentColor: Color,
    val containerColor: Color,
) {
    Neutral(
        icon = R.drawable.neutral,
        contentColor = Color.Black,
        containerColor = NeutralColor,
    ),
    Happy(
        icon = R.drawable.happy,
        contentColor = Color.Black,
        containerColor = HappyColor,
    ),
    Angry(
        icon = R.drawable.angry,
        contentColor = Color.White,
        containerColor = AngryColor,
    ),
    Bored(
        icon = R.drawable.bored,
        contentColor = Color.Black,
        containerColor = BoredColor,
    ),
    Calm(
        icon = R.drawable.calm,
        contentColor = Color.Black,
        containerColor = CalmColor,
    ),
    Depressed(
        icon = R.drawable.depressed,
        contentColor = Color.Black,
        containerColor = DepressedColor,
    ),
    Disappointed(
        icon = R.drawable.disappointed,
        contentColor = Color.White,
        containerColor = DisappointedColor,
    ),
    Humorous(
        icon = R.drawable.humorous,
        contentColor = Color.Black,
        containerColor = HumorousColor,
    ),
    Lonely(
        icon = R.drawable.lonely,
        contentColor = Color.White,
        containerColor = LonelyColor,
    ),
    Mysterious(
        icon = R.drawable.mysterious,
        contentColor = Color.Black,
        containerColor = MysteriousColor,
    ),
    Romantic(
        icon = R.drawable.romantic,
        contentColor = Color.White,
        containerColor = RomanticColor,
    ),
    Shameful(
        icon = R.drawable.shameful,
        contentColor = Color.White,
        containerColor = ShamefulColor,
    ),
    Awful(
        icon = R.drawable.awful,
        contentColor = Color.Black,
        containerColor = AwfulColor,
    ),
    Surprised(
        icon = R.drawable.surprised,
        contentColor = Color.Black,
        containerColor = SurprisedColor,
    ),
    Suspicious(
        icon = R.drawable.suspicious,
        contentColor = Color.Black,
        containerColor = SuspiciousColor,
    ),
    Tense(
        icon = R.drawable.tense,
        contentColor = Color.Black,
        containerColor = TenseColor,
    ),
}

fun String.toMoodUi(): MoodUI{
    return MoodUI.values().find { it.name == this } ?: MoodUI.Neutral
}