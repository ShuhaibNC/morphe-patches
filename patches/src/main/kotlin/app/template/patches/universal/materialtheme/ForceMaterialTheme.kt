package app.template.patches.universal.materialtheme


import app.morphe.patcher.patch.resourcePatch
import org.w3c.dom.Element

private const val THEME_ATTRIBUTE = "android:theme"
private const val MATERIAL_THEME = "@android:style/Theme.Material"

/**
 * Universal patch (no compatibleWith) that ports these two regex rules:
 *
 *   1. MATCH: android:theme="(.*?)"   REPLACE: (empty)
 *   2. MATCH: <application            REPLACE: <application android:theme="@android:style/Theme.Material"
 */
@Suppress("unused")
val forceMaterialThemePatch = resourcePatch(
    name = "Force Material theme",
    description = "Removes every android:theme attribute from AndroidManifest.xml " +
            "and applies the framework Material theme to the whole app.",
) {
    // No compatibleWith(...) call = works on any app.

    execute {
        document("AndroidManifest.xml").use { document ->
            // Rule 1: strip android:theme from <application>, <activity>, <service>, etc.
            val allElements = document.getElementsByTagName("*")
            for (i in 0 until allElements.length) {
                (allElements.item(i) as Element).removeAttribute(THEME_ATTRIBUTE)
            }

            // Rule 2: set the Material theme on <application>.
            val application = document.getElementsByTagName("application").item(0) as Element
            application.setAttribute(THEME_ATTRIBUTE, MATERIAL_THEME)
        }
    }
}