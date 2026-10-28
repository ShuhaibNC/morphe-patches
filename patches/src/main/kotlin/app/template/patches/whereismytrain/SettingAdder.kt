package app.template.patches.whereismytrain

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.resourcePatch
import app.template.patches.shared.Constants.WHEREISMYTRAIN_COMPATIBILITY
import org.w3c.dom.Document
import org.w3c.dom.Element

private const val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"
private const val APP_NAMESPACE = "http://schemas.android.com/apk/res-auto"
private const val CATEGORY_KEY = "pref_key_shuhaib_patches"

@Suppress("unused")
val showRunningDaysPreferencePatch = resourcePatch(
    name = "Show Running Days in Tracking",
    description = "Adds the preference to show running days on the tracking page."
) {
    compatibleWith(WHEREISMYTRAIN_COMPATIBILITY)

    execute {
        document("res/xml/location_preferences.xml").use { document ->
            addSwitchPreference(
                document,
                title = "Show running days in Tracking",
                key = "show_running_days_in_track",
                summary = "Shows running days in tracking page",
                defaultValue = false
            )
        }
    }
}

@Suppress("unused")
val rememberDateSelectionPreferencePatch = resourcePatch(
    name = "Remember Date Selection",
    description = "Adds the preference to remember the selected date."
) {
    compatibleWith(WHEREISMYTRAIN_COMPATIBILITY)

    execute {
        document("res/xml/location_preferences.xml").use { document ->
            addSwitchPreference(
                document,
                title = "Remember Date Selection",
                key = "saveDateSelection",
                summary = "Selection of date will be prefered",
                defaultValue = true,
                disableDependentsState = false
            )
        }
    }
}

@Suppress("unused")
val cloudTowerPreferencePatch = resourcePatch(
    name = "Cloud Tower",
    description = "Adds the preference to fetch tower information from the internet."
) {
    compatibleWith(WHEREISMYTRAIN_COMPATIBILITY)

    execute {
        document("res/xml/location_preferences.xml").use { document ->
            addSwitchPreference(
                document,
                title = "Cloud Tower",
                key = "towerFromInternet",
                summary = "Should I fetch the tower info from Internet?",
                defaultValue = true
            )
        }
    }
}

@Suppress("unused")
val mockTowerPreferencePatch = resourcePatch(
    name = "Mock Tower",
    description = "Adds the preference to simulate being on the tower."
) {
    compatibleWith(WHEREISMYTRAIN_COMPATIBILITY)

    execute {
        document("res/xml/location_preferences.xml").use { document ->
            addSwitchPreference(
                document,
                title = "Mock Tower",
                key = "mockTower",
                summary = "Simulate being on the tower",
                defaultValue = false,
                disableDependentsState = false
            )
        }
    }
}

@Suppress("unused")
val simulateCellTowerPreferencePatch = resourcePatch(
    name = "Simulate Cell Tower",
    description = "Adds the preference to simulate the selected train's cell tower."
) {
    compatibleWith(WHEREISMYTRAIN_COMPATIBILITY)

    execute {
        document("res/xml/location_preferences.xml").use { document ->
            addSwitchPreference(
                document,
                title = "Simulate Cell Tower",
                key = "simulateCellTower",
                summary = "Simulate cell tower for the selected train",
                defaultValue = false,
                disableDependentsState = false
            )
        }
    }
}

@Suppress("unused")
val debugModePreferencePatch = resourcePatch(
    name = "Debug Mode",
    description = "Adds the preference to enable detailed toast messages."
) {
    compatibleWith(WHEREISMYTRAIN_COMPATIBILITY)

    execute {
        document("res/xml/location_preferences.xml").use { document ->
            addSwitchPreference(
                document,
                title = "Debug Mode",
                key = "is_debug",
                summary = "You will get detailed Toast messages.",
                defaultValue = false
            )
        }
    }
}

private fun ensureCategory(document: Document): Element {
    val root = document.documentElement
        ?: throw PatchException("Could not find the root of res/xml/location_preferences.xml.")
    val category = findElementByKey(root, "PreferenceCategory", CATEGORY_KEY)
        ?: document.createElement("PreferenceCategory").also {
            it.setAttributeNS(ANDROID_NAMESPACE, "android:title", "Morphe Settings")
            it.setAttributeNS(ANDROID_NAMESPACE, "android:key", CATEGORY_KEY)
            it.setAttributeNS(APP_NAMESPACE, "app:iconSpaceReserved", "false")
            root.appendChild(it)
        }
    orderPreferenceCategories(root)
    return category
}

private fun orderPreferenceCategories(root: Element) {
    val category = findElementByKey(root, "PreferenceCategory", CATEGORY_KEY)
    if (category?.parentNode === root) {
        root.insertBefore(category, root.firstChild)
    }

    val aboutCategory = findElementByKey(root, "PreferenceCategory", "ABOUT_PREF")
    if (aboutCategory?.parentNode === root) {
        root.appendChild(aboutCategory)
    }
}

private fun addSwitchPreference(
    document: Document,
    title: String,
    key: String,
    summary: String,
    defaultValue: Boolean,
    disableDependentsState: Boolean? = null
) {
    val category = ensureCategory(document)
    if (findElementByKey(category, "SwitchPreferenceCompat", key) == null) {
        val preference = document.createElement("SwitchPreferenceCompat")
        preference.setAttributeNS(ANDROID_NAMESPACE, "android:ellipsize", "marquee")
        preference.setAttributeNS(ANDROID_NAMESPACE, "android:singleLine", "true")
        preference.setAttributeNS(ANDROID_NAMESPACE, "android:title", title)
        preference.setAttributeNS(ANDROID_NAMESPACE, "android:key", key)
        preference.setAttributeNS(ANDROID_NAMESPACE, "android:summary", summary)
        preference.setAttributeNS(ANDROID_NAMESPACE, "android:defaultValue", defaultValue.toString())
        disableDependentsState?.let {
            preference.setAttributeNS(
                ANDROID_NAMESPACE,
                "android:disableDependentsState",
                it.toString()
            )
        }
        preference.setAttributeNS(APP_NAMESPACE, "app:iconSpaceReserved", "false")
        category.appendChild(preference)
    }
    addPatchInfoPreference(document, category)
    orderMorphePreferences(category)
}

private fun addPatchInfoPreference(document: Document, category: Element) {
    val root = document.documentElement
        ?: throw PatchException("Could not find the root of res/xml/location_preferences.xml.")
    val preference = findElementByKey(root, "Preference", "patch_info")
        ?: document.createElement("Preference").apply {
            setAttributeNS(ANDROID_NAMESPACE, "android:ellipsize", "marquee")
            setAttributeNS(ANDROID_NAMESPACE, "android:singleLine", "true")
            setAttributeNS(ANDROID_NAMESPACE, "android:title", "Patch Info")
            setAttributeNS(ANDROID_NAMESPACE, "android:selectable", "true")
            setAttributeNS(ANDROID_NAMESPACE, "android:key", "patch_info")
            setAttributeNS(ANDROID_NAMESPACE, "android:summary", "Patched by Shuhaib")
            setAttributeNS(APP_NAMESPACE, "app:iconSpaceReserved", "false")
        }
    category.appendChild(preference)
}

private fun orderMorphePreferences(category: Element) {
    val orderedKeys = listOf(
        "show_running_days_in_track",
        "saveDateSelection",
        "is_debug",
        "mockTower",
        "simulateCellTower",
        "towerFromInternet",
        "patch_info"
    )
    orderedKeys.forEach { key ->
        val children = category.childNodes
        for (index in 0 until children.length) {
            val child = children.item(index) as? Element ?: continue
            val androidKey = child.getAttributeNS(ANDROID_NAMESPACE, "key")
                .ifEmpty { child.getAttribute("android:key") }
            if (androidKey == key) {
                category.appendChild(child)
                break
            }
        }
    }
}

private fun findElementByKey(parent: Element, tagName: String, key: String): Element? {
    val elements = parent.getElementsByTagName(tagName)
    for (index in 0 until elements.length) {
        val element = elements.item(index) as? Element ?: continue
        val androidKey = element.getAttributeNS(ANDROID_NAMESPACE, "key")
            .ifEmpty { element.getAttribute("android:key") }
        if (androidKey == key) return element
    }
    return null
}