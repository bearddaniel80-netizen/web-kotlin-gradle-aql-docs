package example.service

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path

@Service
class MarkdownService {

    private val link = Regex("""\[([^\]]+)\]\(([^)]+)\.md\)""")

    /** Rewrites [label](slug.md) links to <b>Label</b>, in place. */
    fun mdLinksToHtml(file: Path) {
        val text = Files.readString(file)
        Files.writeString(file, link.replace(text) { "<b>${title(it.groupValues[1])}</b>" })
    }

    /** Mimics Python's str.title(): capitalise each run of letters, lowercase the rest. */
    internal fun title(s: String): String {
        val sb = StringBuilder(s.length)
        var startOfWord = true
        for (c in s) {
            if (c.isLetter()) {
                sb.append(if (startOfWord) c.uppercaseChar() else c.lowercaseChar())
                startOfWord = false
            } else {
                sb.append(c)
                startOfWord = true
            }
        }
        return sb.toString()
    }
}
