import java.io.File

var clusters: HashMap<String, ArrayList<String>> = HashMap()
var addition_rule: HashMap<String, String> = HashMap()
var skip_print_set: HashSet<String> = HashSet()
fun main(args: Array<String>) {
    form_clusters()
    create_addition_rule_set()
    put_entities_into_clusters(fileName = "[TODO]") //The path of the file that contains all the entities
    print_clusters()

}

//Initiate all clusters
fun form_clusters(): Unit {

//Assuming each module as a cluster for JUnit5-5.5.0
//    var modules = arrayOf(
//        "junit-jupiter",
//        "junit-jupiter-api",
//        "junit-jupiter-engine",
//        "junit-jupiter-migrationsupport",
//        "junit-jupiter-params",
//        "junit-platform-commons",
//        "junit-platform-console",
//        "junit-platform-engine",
//        "junit-platform-launcher",
//        "junit-platform-reporting",
//        "junit-platform-runner",
//        "junit-platform-suite-api",
//        "junit-platform-testkit",
//        "junit-vintage-engine",
//        "others",
//        "opentest4j",
//        "gradle",
//        "java",
//    )

//    Assuming each module as a cluster for JUnit5-5.4.2
    var modules = arrayOf(
        "junit-jupiter",
        "junit-jupiter-api",
        "junit-jupiter-engine",
        "junit-jupiter-migrationsupport",
        "junit-jupiter-params",
        "junit-platform-commons",
        "junit-platform-console",
        "junit-platform-engine",
        "junit-platform-launcher",
        "junit-platform-reporting",
        "junit-platform-runner",
        "junit-platform-suite-api",
        "junit-platform-testkit",
        "junit-vintage-engine",
        "others",
        "wired_param",
    )
    for (m in modules) {
        clusters.put(m.replace("-", "."), ArrayList())
    }

    //Skip set
    skip_print_set = HashSet(
        setOf<String>(
            "others",
            "wired_param",
        )
    )
}

fun create_addition_rule_set(): Unit {
    addition_rule = HashMap(
        mapOf<String, String>(
            "org.junit.jupiter.jmh" to "others",
            "$" to "wired_param",
        )
    )
}

fun put_entities_into_clusters(fileName: String): Unit {
    var a: Int = 0
    var cnt: Int = 0
    File(fileName).forEachLine {
        cnt++
        var flag: Boolean = false

        for (from in addition_rule.keys) {
            if (it.contains(from)) {
                flag = true
                clusters.get(addition_rule.get(from))?.add(it)
                break
            }
        }
        if (!flag)
            for (str in clusters.keys) {
                if (it.contains(str)) {
                    flag = true
                    clusters.get(str)?.add(it)
                    break
                }
            }

        if (!flag) {
            clusters.get("others")?.add(it)
            a++
        }
    }
    println(a)
    println(cnt)
}

fun print_clusters(): Unit {
    val file_name = "[TODO]"
    val f = File(file_name)
    if (!f.exists()) {
        f.createNewFile()
    } else {
        f.writeText("")
    }
    for (key in clusters.keys) {
        if (key in skip_print_set) continue
        for (entity in clusters.get(key)!!) {
            f.appendText("contain " + key + " " + entity + "\n")
        }
    }
}

// Filter the kotlin part in the dependency file
fun read_file_and_filter_and_write(inFileName: String, outFileName: String) = File(inFileName).forEachLine {
    var arr: List<String> = it.split("\\s".toRegex())
    val f = File(outFileName)
    if (!arr[1].contains("gradle.kotlin.") && !arr[1].contains("Kotlin_library_conventions")
        && !arr[1].contains("Java_library_conventions")
    ) {
        if (!f.exists()) {
            f.createNewFile()
        } else {
            f.appendText(it + "\n")
        }
    }
}
