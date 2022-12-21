package dsl.publishing

/**
 * Versions of godot that are used to mark compatibility.
 * */
enum class CompatVersion(val version:String){
    `2_0`("2.0"),
    `2_1`("2.1"),
    `2_2`("2.2"),
    `3_0`("3.0"),
    `3_1`("3.1"),
    `3_2`("3.2"),
    `3_3`("3.3"),
    `3_4`("3.4"),
    `3_5`("3.5"),
    `4_0`("4.0")
}

enum class AddonType(val typeName:String) {
    ADDON("addon"),PROJECT("project")
}
enum class AssetCategories(val categoryName:String){
    `2DTools`("2D Tools"),
    `3DTools`("3D Tools"),
    Tools("Tools"),
    Shaders("Shaders"),
    Materials("Materials"),
    Scripts("Scripts"),
    Misc("Misc"),
    Templates("Templates"),
    Projects("Projects"),
    Demos("Demos"),
}