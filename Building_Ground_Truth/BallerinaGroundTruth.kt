import java.io.File

var B_clusters: HashMap<String, ArrayList<String>> = HashMap()
var B_addition_rule: HashMap<String, String> = HashMap()
var B_skip_print_set: HashSet<String> = HashSet()
var B_path_to_module: HashMap<String, String> = HashMap()
var file_cnt: Int = 0
fun main(args: Array<String>) {
    B_form_clusters()
    B_put_entities_into_clusters(fileName = "[TODO]")//The path of the file that contains all the entities
    B_print_clusters()
    B_print_summary_table()

    B_print_others()
}

//Initiate all clusters
fun B_form_clusters(): Unit {
//    Assuming each module as a cluster for Ballerina-newest
    val modules = arrayOf(
        "others",
        "io.ballerina.maven.resolver",
        "io.ballerina.observe.trace.extension.choreo",
        "io.ballerina.observe.trace.extension.jaeger",
        "io.ballerina.docerina",
        "io.ballerina.formatter",
        "io.ballerina.observe.metrics.extension.defaultimpl",
        "io.ballerina.formatter.cli",
        "io.ballerina.formatter.core",
        "ballerina.debug.adapter.core",
        "ballerina.debug.adapter.cli",
        "io.ballerina.datamapper",
        "io.ballerina.toml",
        "io.ballerina.testerina.runtime",
        "io.ballerina.testerina.core",
        "io.ballerina.observability",
        "io.ballerina.observability.test.utils",
        "org.ballerinalang.birspec",
        "io.ballerina.cli.utils",
        "io.ballerina.tool",
        "io.ballerina.packerina",
        "io.ballerina.cli.module",
        "io.ballerina.language.server.commons",
        "io.ballerina.language.server.compiler",
        "io.ballerina.language.server.stdio.launcher",
        "io.ballerina.language.server.cli",
        "io.ballerina.language.server.core",
        "io.ballerina.crypto",
        "io.ballerina.runtime.api",
        "io.ballerina.cache",
        "io.ballerina.file",
        "io.ballerina.auth",
        "io.ballerina.reflect",
        "io.ballerina.config.api",
        "io.ballerina.io",
        "io.ballerina.time",
        "io.ballerina.system",
        "io.ballerina.transactions",
        "io.ballerina.http",
        "io.ballerina.task",
        "io.ballerina.mime",
        "io.ballerina.log.api",
        "io.ballerina.lang.bool",
        "io.ballerina.lang.xml",
        "io.ballerina.lang.test",
        "io.ballerina.lang.array",
        "io.ballerina.lang.transaction",
        "io.ballerina.lang.internal",
        "io.ballerina.lang.string",
        "io.ballerina.java",
        "io.ballerina.lang.future",
        "io.ballerina.lang.value",
        "io.ballerina.lang.floatingpoint",
        "io.ballerina.lang.decimal",
        "io.ballerina.lang.table",
        "io.ballerina.lang.error",
        "io.ballerina.lang.map",
        "io.ballerina.lang.query",
        "io.ballerina.lang.integer",
        "io.ballerina.lang",
        "io.ballerina.tools.api",
        "io.ballerina.parser",
        "io.ballerina.treegen",
        "io.ballerina.logging",
        "io.ballerina.runtime",
        "io.ballerina.config",
        "io.ballerina.core",
    )

    B_path_to_module = HashMap(
        mapOf(
            "misc.maven-resolver.src.main.java" to "io.ballerina.maven.resolver",
            "misc.tracing-extensions.modules.ballerina-choreo-extension.src.main.java" to "io.ballerina.observe.trace.extension.choreo",
            "misc.tracing-extensions.modules.ballerina-jaeger-extension.src.main.java" to "io.ballerina.observe.trace.extension.jaeger",
            "misc.docerina.src.main.java" to "io.ballerina.docerina",
            "misc.ballerina-formatter.src.main.java" to "io.ballerina.formatter",
            "misc.metrics-extensions.modules.ballerina-metrics-extension.src.main.java" to "io.ballerina.observe.metrics.extension.defaultimpl",
            "misc.formatter.modules.formatter-cli.src.main.java" to "io.ballerina.formatter.cli",
            "misc.formatter.modules.formatter-core.src.main.java" to "io.ballerina.formatter.core",
            "misc.debug-adapter.modules.debug-adapter-core.src.main.java" to "ballerina.debug.adapter.core",
            "misc.debug-adapter.modules.debug-adapter-cli.src.main.java" to "ballerina.debug.adapter.cli",
            "misc.ballerinalang-data-mapper.src.main.java" to "io.ballerina.datamapper",
            "misc.toml-parser.src.main.java" to "io.ballerina.toml",
            "misc.testerina.modules.testerina-runtime.src.main.java" to "io.ballerina.testerina.runtime",
            "misc.testerina.modules.testerina-core.src.main.java" to "io.ballerina.testerina.core",
            "observelib.observe.src.main.java" to "io.ballerina.observability",
            "tests.observability-test-utils.src.main.java" to "io.ballerina.observability.test.utils",
            "docs.bir-spec.src.main.java" to "org.ballerinalang.birspec",
            "cli.ballerina-cli-utils.src.main.java" to "io.ballerina.cli.utils",
            "cli.ballerina-tool.src.main.java" to "io.ballerina.tool",
            "cli.ballerina-packerina.src.main.java" to "io.ballerina.packerina",
            "cli.ballerina-cli-module.src.main.java" to "io.ballerina.cli.module",
            "language-server.modules.langserver-commons.src.main.java" to "io.ballerina.language.server.commons",
            "language-server.modules.langserver-compiler.src.main.java" to "io.ballerina.language.server.compiler",
            "language-server.modules.launchers.stdio-launcher.src.main.java" to "io.ballerina.language.server.stdio.launcher",
            "language-server.modules.langserver-cli.src.main.java" to "io.ballerina.language.server.cli",
            "language-server.modules.langserver-core.src.main.java" to "io.ballerina.language.server.core",
            "stdlib.crypto.src.main.java" to "io.ballerina.crypto",
            "stdlib.runtime-api.src.main.java" to "io.ballerina.runtime.api",
            "stdlib.cache.src.main.java" to "io.ballerina.cache",
            "stdlib.file.src.main.java" to "io.ballerina.file",
            "stdlib.auth.src.main.java" to "io.ballerina.auth",
            "stdlib.reflect.src.main.java" to "io.ballerina.reflect",
            "stdlib.config-api.src.main.java" to "io.ballerina.config.api",
            "stdlib.io.src.main.java" to "io.ballerina.io",
            "stdlib.time.src.main.java" to "io.ballerina.time",
            "stdlib.system.src.main.java" to "io.ballerina.system",
            "stdlib.transactions.src.main.java" to "io.ballerina.transactions",
            "stdlib.http.src.main.java" to "io.ballerina.http",
            "stdlib.task.src.main.java" to "io.ballerina.task",
            "stdlib.mime.src.main.java" to "io.ballerina.mime",
            "stdlib.log-api.src.main.java" to "io.ballerina.log.api",
            "langlib.lang.boolean.src.main.java" to "io.ballerina.lang.bool",
            "langlib.lang.xml.src.main.java" to "io.ballerina.lang.xml",
            "langlib.lang.test.src.main.java" to "io.ballerina.lang.test",
            "langlib.lang.array.src.main.java" to "io.ballerina.lang.array",
            "langlib.lang.transaction.src.main.java" to "io.ballerina.lang.transaction",
            "langlib.lang.__internal.src.main.java" to "io.ballerina.lang.internal",
            "langlib.lang.string.src.main.java" to "io.ballerina.lang.string",
            "langlib.java.src.main.java" to "io.ballerina.java",
            "langlib.lang.future.src.main.java" to "io.ballerina.lang.future",
            "langlib.lang.value.src.main.java" to "io.ballerina.lang.value",
            "langlib.lang.float.src.main.java" to "io.ballerina.lang.floatingpoint",
            "langlib.lang.decimal.src.main.java" to "io.ballerina.lang.decimal",
            "langlib.lang.table.src.main.java" to "io.ballerina.lang.table",
            "langlib.lang.error.src.main.java" to "io.ballerina.lang.error",
            "langlib.lang.map.src.main.java" to "io.ballerina.lang.map",
            "langlib.lang.query.src.main.java" to "io.ballerina.lang.query",
            "langlib.lang.int.src.main.java" to "io.ballerina.lang.integer",
            "compiler.ballerina-lang.src.main.java" to "io.ballerina.lang",
            "compiler.ballerina-tools-api.src.main.java" to "io.ballerina.tools.api",
            "compiler.ballerina-parser.src.main.java" to "io.ballerina.parser",
            "compiler.ballerina-treegen.src.main.java" to "io.ballerina.treegen",
            "bvm.ballerina-logging.src.main.java" to "io.ballerina.logging",
            "bvm.ballerina-runtime.src.main.java" to "io.ballerina.runtime",
            "bvm.ballerina-config.src.main.java" to "io.ballerina.config",
            "bvm.ballerina-core.src.main.java" to "io.ballerina.core",
        )
    )

    for (m in modules) {
        B_clusters.put(m, ArrayList())
    }

    //Skip set
    skip_print_set = HashSet(
        setOf<String>(
            "others",
        )
    )
}

fun B_put_entities_into_clusters(fileName: String): Unit {
    var a: Int = 0
    var cnt: Int = 0
    File(fileName).forEachLine {
        file_cnt++
        cnt++
        var flag: Boolean = false

        var B_entity: String
        if (it.contains("$")) {
            B_entity = it.split("$")[0]
        } else {
            B_entity = it
        }


        for (str in B_path_to_module.keys) {
            if (B_entity.startsWith(str)) {
                flag = true
                B_clusters.get(B_path_to_module.get(str))?.add(B_entity)
                break
            }
        }

        if (!flag) {
            B_clusters.get("others")?.add(B_entity)
            a++
        }
    }
    println(a)
    println(cnt)
}

fun B_print_clusters(): Unit {
    val file_name = "[TODO]"//The ground truth file
    val f = File(file_name)
    if (!f.exists()) {
        f.createNewFile()
    } else {
        f.writeText("")
    }
    for (key in B_clusters.keys) {
        if (key in skip_print_set) continue
        for (entity in B_clusters.get(key)!!) {
            f.appendText("contain " + key + " " + entity + "\n")
        }
    }
}

fun B_print_summary_table(): Unit {
    for (key in B_clusters.keys) {
        println(key + " " + B_clusters.get(key)?.size)
    }
    println(file_cnt)
}


fun B_print_others(): Unit {
    var o_cnt = 0
    for (i in B_clusters.get("others")!!) {
        if (
            !i.contains("$")
        ) {
            println(i)
            o_cnt++
        }
    }
    println(o_cnt)
}