package app.template.patches.whereismytrain

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.colorOption
import app.morphe.patcher.patch.resourcePatch
import app.template.patches.shared.Constants.WHEREISMYTRAIN_COMPATIBILITY
import org.w3c.dom.Element

@Suppress("unused")
val changeAccentColorPatch = resourcePatch(
    name = "Change Accent Color",
    description = "Changes the accent color in the night theme."
) {
    compatibleWith(WHEREISMYTRAIN_COMPATIBILITY)

    val accentColor by colorOption(
        key = "accent_color",
        title = "Accent Color",
        description = "Choose the color to apply to the night theme."
    )

    execute {
        val colorNames = setOf(
            "md_green_600",
            "wimt_tag_green",
            "gm3_ref_palette_tertiary50",
            "md_theme_tertiary_container",
            "tool_tip"
        )
        val changedColors = mutableSetOf<String>()

        listOf("res/values/colors.xml", "res/values-night/colors.xml").forEach { path ->
            document(path).use { document ->
                val colors = document.getElementsByTagName("color")
                for (i in 0 until colors.length) {
                    val color = colors.item(i) as? Element ?: continue
                    val name = color.getAttribute("name")
                    if (name in colorNames) {
                        color.textContent = accentColor
                        changedColors.add(name)
                    }
                }
            }
        }

        val missingColors = colorNames - changedColors
        if (missingColors.isNotEmpty()) {
            throw PatchException(
                "Could not find color resource(s): ${missingColors.sorted().joinToString()}."
            )
        }
    }
}
