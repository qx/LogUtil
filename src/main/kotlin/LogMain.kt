import java.io.File

object LogMain {


    @JvmStatic
    fun main(args: Array<String>) {
//        val result = "Test      me".replace("\\s+".toRegex(), " ")
//        val file = File("gang.log")
//        val fileInputStream = FileInputStream(file)
//        println(fileInputStream)
        if (args.isEmpty()) {
            System.err.println("请输入日志目录...")
            return
        }
        File(args[0]).walkTopDown().forEach { f ->
            if (f.isFile) {
                var text = f.readText()
                text = text.replace(".*MESSAGE_RECEIVED.*\\s".toRegex(), "")
                text = text.replace(".* Firing a MESSAGE_SENT event for session.*\\s".toRegex(), "")
                text = text.replace(".*RECEIVED: \\{\"cmd\":\"SYNC\"\\}.*\\s+".toRegex(), "")
                text = text.replace(".*Event MESSAGE_SENT has been fired for session.*\\s+".toRegex(), "")
                text = text.replace(
                    "\\d+-\\d+-\\d+\\s\\d+\\D\\d+\\D\\d+\\DINFO\\D{2}LoggingFilter\\D-\\D".toRegex(),
                    ""
                )
                text = text.replace("SENT: \\{\"cmd\":\"SYNC_RES\".*}}}\\s{1}".toRegex(), "")
                text = text.replace(" DEBUG IoFilterEvent - ".toRegex(), "--->")
                text = text.replace("\\n+".toRegex(), "\n\n")
                f.writeText(text)
                val newFile = File(f.path.split(".").first() + ".json")
                if (f.renameTo(newFile))
                    println("${f.name} successfully renamed to ${newFile.name}")
                else
                    println("${f.name} could not be renamed")
            }
        }
    }

}