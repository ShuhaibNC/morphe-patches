package app.template.patches.mp3cutter

import app.morphe.patcher.patch.resourcePatch
import app.template.patches.shared.Constants.MP3CUTTER_COMPATIBILITY
import org.w3c.dom.Element

@Suppress("unused")
val removeAdsPatch = resourcePatch(
    name = "Remove Ads",
    description = "Removes AdActivities from the manifest.",
    default = true
) {
    compatibleWith(MP3CUTTER_COMPATIBILITY)

    execute {
        document("AndroidManifest.xml").use { document ->
            val activities = document.getElementsByTagName("activity")
            val adActivityName = "com.google.android.gms.ads.AdActivity"

            val activitiesToRemove = mutableListOf<Element>()
            for (i in 0 until activities.length) {
                val activity = activities.item(i) as? Element ?: continue
                val name = activity.getAttribute("android:name")
                if (name == adActivityName) {
                    activitiesToRemove.add(activity)
                }
            }

            activitiesToRemove.forEach { activity ->
                activity.parentNode?.removeChild(activity)
            }
        }
    }
}
