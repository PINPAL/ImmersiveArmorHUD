import toni.blahaj.setup.modRuntimeOnly

plugins {
	id("toni.blahaj")
}

blahaj {
	config { }
	setup {
		txnilib("1.0.23")
		forgeConfig()

		when (mod.projectName) {
			"1.21.1-neoforge" -> "v21.1.1-1.21.1-NeoForge"
			"1.21.1-fabric" -> "v21.1.1-1.21.1-Fabric"
			"1.20.1-fabric" -> "v8.0.1-1.20.1-Fabric"
			"1.20.1-forge" -> "v8.0.1-1.20.1-Forge"
			else -> null
		}.let { v -> v?.run {
			deps.modRuntimeOnly(modrinth("overflowing-bars", this))
		}}

		when (mod.projectName) {
			"1.21.1-neoforge" -> "v21.1.33-1.21.1-NeoForge"
			"1.21.1-fabric" -> "v21.1.33-1.21.1-Fabric"
			"1.20.1-fabric" -> "v8.1.29-1.20.1-Fabric"
			"1.20.1-forge" -> "v8.1.29-1.20.1-Forge"
			else -> null
		}.let { v -> v?.run {
			deps.modRuntimeOnly(modrinth("puzzles-lib", this))
		}}

		when (mod.projectName) {
			"1.20.1-forge" -> {
				deps.modRuntimeOnly("fuzs.puzzlesaccessapi:puzzlesaccessapi-forge:20.1.1")

				deps.compileOnly(deps.annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")!!)
				deps.implementation(deps.include("io.github.llamalad7:mixinextras-forge:0.4.1")!!)
			}

			"1.20.1-fabric" -> {
				deps.modRuntimeOnly("fuzs.puzzlesaccessapi:puzzlesaccessapi-fabric:20.1.1")
			}
		}
	}
}