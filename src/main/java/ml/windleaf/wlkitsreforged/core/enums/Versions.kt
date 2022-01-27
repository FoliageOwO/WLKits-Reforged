package ml.windleaf.wlkitsreforged.core.enums

import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import ml.windleaf.wlkitsreforged.core.reflect.versions.*

enum class Versions(val reflector: Reflector) {
    V1_12_R1(V1_12_R1()),
    V1_16_R3(V1_16_R3()),
    V1_18_R1(V1_18_R1());
}