package app.template.patches.worldmapquiz


import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.resourcePatch

import app.template.patches.shared.Constants.WORLD_MAP_COMPATIBILITY


private val bundleClassLoader: ClassLoader = object {}.javaClass.classLoader


@Suppress("unused")

val amoledDarkPatch = resourcePatch(

    name = "Amoled Dark Theme",

    description = "Adds amoled dark graphics to the app.",

    default = true

) {

    compatibleWith(WORLD_MAP_COMPATIBILITY)


    execute {

        val resource = bundleClassLoader.getResourceAsStream("graphics_base.png")
            ?: throw PatchException(
                "Resource 'graphics_base.png' not found in patch resources. " +
                "Ensure it is placed in 'patches/src/main/resources/'."
            )

        val targetFile = get("assets/graphics/graphics_base.png")
        resource.use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

    }

}
