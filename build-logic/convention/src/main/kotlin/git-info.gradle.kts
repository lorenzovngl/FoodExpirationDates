fun runGit(vararg args: String): String {
    return try {
        val process = ProcessBuilder(listOf("git", *args))
            .directory(rootDir)
            .redirectErrorStream(true)
            .start()

        val out = process.inputStream.readBytes().toString(Charsets.UTF_8).trim()
        val code = process.waitFor()
        if (code == 0) out else "nogit"
    } catch (_: Exception) {
        "nogit"
    }
}

val p = gradle.rootProject

val branch = runGit("rev-parse", "--abbrev-ref", "HEAD")
val commitHash = runGit("rev-parse", "--short", "HEAD")
val commitDate = runGit("show", "-s", "--format=%cd", "--date=short", "HEAD")

p.extensions.extraProperties["gitBranch"] = branch
p.extensions.extraProperties["gitCommitHash"] = commitHash
p.extensions.extraProperties["gitCommitDate"] = commitDate
