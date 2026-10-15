package app.template.patches.yatse

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.YATSE_COMPATIBILITY
import app.template.patches.shared.clearBody

object ProFingerprint : Fingerprint(
    definingClass = "Lsd/r;",
    name = "h",
    returnType = "Z",
    parameters = emptyList(),
)

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Yatse Pro",
    description = "Unlocks lifetime access to Yatse Pro Unlocker.",
    default = true
) {
    compatibleWith(YATSE_COMPATIBILITY)
    execute {
        ProFingerprint.method.apply {
            clearBody()
            addInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """
            )
        }
    }
}
