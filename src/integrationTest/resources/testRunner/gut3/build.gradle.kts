import io.github.frontrider.godle.dsl.testing.TestEngines
import io.github.frontrider.godle.dsl.versioning.asGodot

plugins {
    id("io.github.frontrider.godle")
}
godle{
    asGodot("3.5.1")
    addons{
        install{
            //install gut
            byURL("https://github.com/bitwes/Gut/archive/refs/tags/v7.4.1.zip"){
                isGitLike=true
            }
        }
    }
    testing{
        engine = TestEngines.GUT3
    }
}
