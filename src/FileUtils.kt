import java.io.File
import java.nio.file.Paths

object FileUtils
{
    fun readLines(path: String): List<String>
    {
        val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
        val filePath = Paths.get(projectDirAbsolutePath, "resources", path)
        return File(filePath.toString()).readLines()
    }

    fun readText(path: String): String
    {
        val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
        val filePath = Paths.get(projectDirAbsolutePath, "resources", path)
        return File(filePath.toString()).readText()
    }

    fun saveText(path: String, text: String)
    {
        val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
        val filePath = Paths.get(projectDirAbsolutePath, "resources", path)
        File(filePath.toString()).writeText(text)
    }
}