package app.template.patches.worldmapquiz

import app.morphe.patcher.patch.resourcePatch
import app.template.patches.shared.Constants.WORLD_MAP_COMPATIBILITY
import java.io.File

@Suppress("unused")
val amoledDarkPatch = resourcePatch(
    name = "Amoled Dark Theme",
    description = "Adds amoled dark graphics to the app.",
    default = true
) {
    compatibleWith(WORLD_MAP_COMPATIBILITY)

    execute {
        // Path in the APK where the file should be placed
        val targetFile = this["assets/graphics/graphics_base.png"]
        
        // Ensure the assets/graphics directory exists in the decoded APK
        targetFile.parentFile.mkdirs()

        // Load the image from the patch's resources (src/main/resources)
        val resource = javaClass.classLoader.getResourceAsStream("graphics_base.png")
            ?: throw IllegalStateException("Resource 'graphics_base.png' not found in patch resources. Ensure it is placed in 'patches/src/main/resources/'.")
        
        // Copy the resource content to the target file in the APK
        resource.use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}
