package app.template.patches.worldmapquiz


import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.resourcePatch

import app.template.patches.shared.Constants.WORLD_MAP_COMPATIBILITY
private val bundleClassLoader: ClassLoader = object {}.javaClass.classLoader


@Suppress("unused")

val removeUnwantedPatch = resourcePatch(

    name = "Remove Unwanted Promotional Icons and Ads",

    description = "Removes bloated promotional icons and ads from the home screen.",

    default = true

) {

    compatibleWith(WORLD_MAP_COMPATIBILITY)


    execute {
        val resource = bundleClassLoader.getResourceAsStream("graphics_other.png")
            ?: throw PatchException(
                "Resource 'graphics_other.png' not found in patch resources. " +
                "Ensure it is placed in 'patches/src/main/resources/'."
            )

        val targetFile = get("assets/graphics/graphics_other.png")

        resource.use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

    }

}
