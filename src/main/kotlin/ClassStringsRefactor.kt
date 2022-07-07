import java.io.File
import java.nio.file.Files
import java.util.regex.Pattern

fun main(args: Array<String>) {

    processAndModifyClass()

    // val baseResourcePathPre = "/Users/arpit/arpita/workspace/office/lenskart-android/android-consumer/lenskart-valyoo-android/%s/src/main/java/com/lenskart/%s"
    // val baseResourcePathPost = "/src/main/res/values/strings.xml"
    //
    // val packagePath = "/src/main/kotlin/"
    //
    // // val list = listOf("base","app","ar","store","framesize")
    // val list = listOf("store")
    //
    // list.forEach { module ->
    //     println(getModuleHeaderComment(module))
    //     val packageFile = File(packagePath+module)
    //     packageFile.walk().forEach { file ->
    //         processAndModifyClass(file)
    //     }
    // }
}

fun processAndModifyClass(file: File? = null) {
    var file = File("src/main/kotlin/AtHomeAddressFragment.kt")
    val lines: List<String> = file.readLines()
    val pendingFile = File("src/main/kotlin/storeerrors/${file.name}")
    var map = HashMap<String,String>()
    for (entry in lines){
        if(entry.contains(".string.")){
            processExtractedLine(entry,map)
        }
    }


}

fun processExtractedLine(inputString: String, map: HashMap<String, String>) {

    val syntaxMatcher = Pattern.compile("(getString\\(.*?\\))").matcher(inputString) //getString(R.string.abb_bbb)
    var subString = ""
    while (syntaxMatcher.find()) {
        subString = syntaxMatcher.group(1)
    }

    val stringMatcher = Pattern.compile(".string.(.*?)\\)").matcher(subString) //getString(R.string.abb_bbb)
    var stringId = ""
    while (stringMatcher.find()) {
        stringId = underscoreToCamelcase(stringMatcher.group(1))
        if (stringId.isNotBlank()){
            stringId = "languageStringsConfig.$stringId"
        }
    }

    syntaxMatcher.replaceAll(stringId)
}
