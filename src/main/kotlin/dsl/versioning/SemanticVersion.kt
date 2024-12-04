package io.github.frontrider.godle.dsl.versioning

data class SemanticVersion(
    val major: Int = 0,
    val minor: Int = 0,
    val patch: Int = 0,
    val suffix: String = "",
) : Comparable<SemanticVersion> {

    companion object {

        val MAX = SemanticVersion(
            major = Int.MAX_VALUE,
            minor = Int.MAX_VALUE,
            patch = Int.MAX_VALUE,
        )

        /**
         * Parses a semantic version.
         *
         * Groups:
         *
         * - 0: Entire match
         * - 1: Major (int, mandatory)
         * - 3: Minor (int, optional)
         * - 5: Patch (int, optional)
         * - 6: Suffix (string, optional)
         */
        private val REGEX = """(\d+)(\.(\d+)(\.(\d+)(.*))?)?""".toRegex()

        fun parseOrNull(string: String): SemanticVersion? {
            val matchResult = REGEX.matchEntire(string)
                ?: return null
            return SemanticVersion(
                major = matchResult.groupValues[1].toInt(),
                minor = matchResult.groupValues[3].toIntOrNull() ?: 0,
                patch = matchResult.groupValues[5].toIntOrNull() ?: 0,
                suffix = matchResult.groupValues[6]
            )
        }

    }

    override fun compareTo(other: SemanticVersion): Int {
        val majorCmp = this.major.compareTo(other.major)
        if (majorCmp != 0) {
            return majorCmp
        }
        val minorCmp = this.minor.compareTo(other.minor)
        if (minorCmp != 0) {
            return minorCmp
        }
        val patchCmp = this.patch.compareTo(other.patch)
        if (patchCmp != 0) {
            return patchCmp
        }
        return this.suffix.compareTo(other.suffix)
    }
}