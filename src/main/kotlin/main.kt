import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern


val set = mutableSetOf<String>() //for removing duplicate keys

fun main(args: Array<String>) {

    val baseResourcePathPre = "/Users/arpit/arpita/workspace/office/lenskart-android/android-consumer/lenskart-valyoo-android/"
    val baseResourcePathPost = "/src/main/res/values/strings.xml"

    val xmlPendingPackage = "src/main/resources/"
    val pendingFileName = "pendingString.xml"

    val dataClassPackage = "src/main/kotlin/"
    // CAREFULL !!   : THIS WILL CHANGE ANDROID LanguageStringsConfig.kt FILE
    // val dataClassPackage = "/Users/arpit/arpita/workspace/office/lenskart-android/android-consumer/lenskart-valyoo-android/base/src/main/java/com/lenskart/baselayer/model/config/"
    val dataClassFileName = "LanguageStringsConfig.kt"

    val list = listOf("base","app","ar","store","framesize")

    val dataFile = File("$dataClassFileName$dataClassFileName")
    dataFile.writeText("""
        package com.lenskart.baselayer.model.config

        /**
         * @author Arpit Agarwal <arpit.agarwal@lenskart.in>
         * @version         $\Revision         1.0 $\{'$'},         $\Date         2021/04/25 00:26 $\{'$'}
         * @since 0.1.0
         */
        class LanguageStringsConfig {
    """.trimIndent())

    list.forEach { module ->
        println(getModuleHeaderComment(module))
        processAndGenerateFiles(
            File(baseResourcePathPre+module+baseResourcePathPost),
            File(xmlPendingPackage+module+pendingFileName),
            dataFile,
            getModuleHeaderComment(module)
        )
    }

    dataFile.appendText("\t}")
}

fun processAndGenerateFiles(stringsFile:File,pendingStringFile:File,dataFile:File,moduleHeaderComment:String){
    val lines: List<String> = stringsFile.readLines()
    dataFile.appendText(moduleHeaderComment)
    pendingStringFile.writeText("")
    for (index in lines.indices) {
        if (!processExtractedString(lines[index],dataFile)) {
            //print current line
            if (lines[index].isNotBlank()) {
                pendingStringFile.appendText("\n" + lines[index])
            }
        }
    }
}

fun processExtractedString(inputString: String?, dataClassFile: File): Boolean {
    if (inputString.isNullOrBlank()) {
        return false
    }

    if (inputString.contains("<!--")) {
        var comment = ""
        val commentMatcher: Matcher = Pattern.compile("<!--(.*?)-->").matcher(inputString)
        while (commentMatcher.find()) {
            comment = commentMatcher.group(1)
        }
        dataClassFile.appendText("\n\n\t// $comment")
        return true
    } else if (inputString.contains("<string name=")) {
        var key = ""
        var value = ""

        val keyMatcher: Matcher = Pattern.compile("name=\"(.*?)\"").matcher(inputString)
        while (keyMatcher.find()) {
            key = keyMatcher.group(1)
        }

        val valueMatcher = Pattern.compile("\">(.*?)</string>").matcher(inputString)
        while (valueMatcher.find()) {
            value = valueMatcher.group(1)
        }

        if (value.isBlank()) {
            //print("Value is blank for key : $key")
            return false
        }

        // generateJson(key, value)
        generateDataClass(key, value,dataClassFile)
        return true
    }

    return false

}

fun generateDataClass(key: String, value: String, dataClassFile: File) {
    val modifiedKey = underscoreToCamelcase(key)
    if(set.add(modifiedKey)){
        dataClassFile.appendText("\n\tval $modifiedKey = \"$value\"")
    }
}

fun underscoreToCamelcase(key: String): String {
    val list = key.split("_");
    var resultString = list[0]
    for (i in 1 until list.size) {
        resultString += list[i].capitalize()
    }
    return resultString
}

fun getModuleHeaderComment(moduleName:String) : String {
    val comment = "\n\n /********************************${moduleName.capitalize()}*********************************/\n"
    println(comment)
    return comment
}


















//
// fun generateJson(key: String, value: String) {
//     println("\t\"$key\":\"$value\",")
// }
