// Directory buildSrc is treated as part of composition build.
// Classes under this directory are automatically added to class path used by build.

repositories {
    // Current Kotlin DSL needs JCenter to resolve stdlib for buildSrc compilation
    jcenter()
}

plugins {
    `kotlin-dsl`
}
