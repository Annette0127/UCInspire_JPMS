import os
import re

# Some utility functions...To process the data

if __name__ == '__main__':
    # ACDC结果分析（JUnit5.4.2 与 JUnit5.5.0）
    # Analyze the result of ACDC（JUnit5.4.2 and JUnit5.5.0）
    # clusters42 = set()
    # clusters50 = set()
    # with open("cluster-5.4.2-no_test.rsf") as f:
    #     for line in f:
    #         clusters42.add(line.split(" ")[1])
    # with open("cluster-5.5.0-no_test.rsf") as f:
    #     for line in f:
    #         clusters50.add(line.split(" ")[1])
    #
    # print(len(clusters42))
    #
    # print(len(clusters50))
    #
    # for c in iter(clusters42):
    #     if c not in clusters50:
    #         print("In JUnit 5.4.2 but not in JUnit 5.5.0:", c)
    # print("--------------------")
    #
    # for c in iter(clusters50):
    #     if c not in clusters42:
    #         print("In JUnit 5.5.0 but not in JUnit 5.4.2:",c)
    # ------------------------------------------------------------------------------
    # ACDC 的 Criteria-Indicator 分析（不同level的）
    # cnt = 0
    # strong = 0
    # very_strong = 0
    # with open("5.5.0-out.txt") as f:
    #     for line in f:
    #         num = float(line.split(",")[1])
    #         cnt += 1
    #         if num > 0.6:
    #             strong += 1
    #         if num > 0.8:
    #             very_strong += 1
    #
    # print(cnt)
    # print(strong)
    # print(very_strong)
    # ------------------------------------------------------------------------------
    # # ARC结果分析（JUnit5.4.2 与 JUnit5.5.0）
    # cluster_map_42 = dict()
    # cluster_map_50 = dict()
    #
    # topic_list_42 = dict()
    # topic_list_50 = dict()
    #
    # class_set_42 = set()
    # class_set_50 = set()
    # class_set = set()
    # with open("junit-5.4.2_108_topics_104_arc_clusters.rsf") as f:
    #     for line in f:
    #         arr = line.split(" ")
    #         cluster_map_42[arr[2].strip()] = arr[1]
    #         if not topic_list_42.__contains__(arr[1]):
    #             topic_list_42[arr[1]] = set()
    #         topic_list_42[arr[1]].add(arr[2].strip())
    #         class_set_42.add(arr[2].strip())
    #         class_set.add(arr[2].strip())
    #
    # with open("junit-5.5.0_188_topics_112_arc_clusters.rsf") as f:
    #     for line in f:
    #         arr = line.split(" ")
    #         cluster_map_50[arr[2].strip()] = arr[1]
    #         if not topic_list_50.__contains__(arr[1]):
    #             topic_list_50[arr[1]] = set()
    #         topic_list_50[arr[1]].add(arr[2].strip())
    #         class_set_50.add(arr[2].strip())
    #         class_set.add(arr[2].strip())
    #
    # # 统计class层级的不同
    # # print(len(class_set_42))
    # # print(len(class_set_50))
    # # print(len(class_set))
    # # for i in class_set:
    # #     if i in class_set_42 and i not in class_set_50:
    # #         print("In JUnit5.4.2 but not in JUnit5.5.0", i)
    # #
    # # for i in class_set:
    # #     if i in class_set_50 and i not in class_set_42:
    # #         print("In JUnit5.5.0 but not in JUnit5.4.2", i)
    #
    # # 比较每个cluster的差别
    # out = open("cluster-diff.txt", 'w')
    # out_general = open("cluster-diff-general.txt", 'w')
    # out_match = open("cluster-match.txt", 'w')
    #
    # compared_cluster = set()
    # match_cluster = set()
    # for cls in class_set:
    #     if cls in class_set_50 and cls in class_set_42:
    #         topic_num_42 = cluster_map_42.get(cls)
    #         topic_num_50 = cluster_map_50.get(cls)
    #
    #         diff_4250 = topic_list_42[topic_num_42] - topic_list_50[topic_num_50]
    #         diff_5042 = topic_list_50[topic_num_50] - topic_list_42[topic_num_42]
    #         if len(diff_5042) == 0 and len(diff_4250) == 0:
    #             if not match_cluster.__contains__((topic_num_42, topic_num_50)):
    #                 match_cluster.add((topic_num_42, topic_num_50))
    #                 out_match.write(
    #                     "Cluster num in JUnit-5.4.2:" + str(topic_num_42) + "  Cluster num in JUnit-5.5.0:" + str(
    #                         topic_num_50) + " number of classes in cluster:" + str(
    #                         topic_list_42[topic_num_42].__len__()) + "\n")
    #             continue
    #
    #         inter = topic_list_42[topic_num_42] & topic_list_50[topic_num_50]
    #         per_42 = float(len(inter)) / float(topic_list_42[topic_num_42].__len__())
    #         per_50 = float(len(inter)) / float(topic_list_50[topic_num_50].__len__())
    #
    #         out.write("[" + cls + "]\n")
    #         out.write("cluster num in 5.4.2:" + topic_num_42 + "\n")
    #         out.write("cluster num in 5.5.0:" + topic_num_50 + "\n")
    #         out.write("(42-50) " + str(round(per_42, 5)) + "\n")
    #         out.write(diff_4250.__str__() + "\n")
    #         out.write("(50-42) " + str(round(per_50, 5)) + "\n")
    #         out.write(diff_5042.__str__() + "\n")
    #         out.write("\n")
    #
    #         if not compared_cluster.__contains__((topic_num_42, topic_num_50)):
    #             compared_cluster.add((topic_num_42, topic_num_50))
    #             out_general.write(
    #                 "cluster number in JUnit5.4.2: " + str(topic_num_42) + "\ncluster number in JUnit5.5.0: " + str(
    #                     topic_num_50) + "\n")
    #             out_general.write("(intersection)/(number of clusters of JUnit5.4.2) " + str(round(per_42, 5)) + "\n")
    #             out_general.write("(intersection)/(number of clusters of JUnit5.5.0) " + str(round(per_50, 5)) + "\n")
    #             out_general.write("\n")
    # ------------------------------------------------------------------------------
    # # 处理docTopicsFile的结果，但是好像理解错了，所以这段代码实际上没啥用。。。
    # topic_file_Map = dict()
    # cnt = 0
    # with open("docTopicsFile.txt") as f:
    #     for line in f:
    #         arr = re.split('\s+', line.strip())
    #         file_name_on_server = arr[1]
    #         ver = re.findall(r"junit5-5\.(\d.\d)", file_name_on_server)
    #
    #         surfix = ''
    #         if file_name_on_server.__contains__("junit5-5.5.0"):
    #             surfix = file_name_on_server.replace("junit5-5.5.0",
    #                                                  "")
    #         elif file_name_on_server.__contains__("junit5-5.4.2"):
    #             surfix = file_name_on_server.replace("junit5-5.4.2",
    #                                                  "")
    #         surfix = "-noTest" + surfix
    #
    #         file_name_on_mac = ""
    #         file_name_on_mac = file_name_on_mac.replace("%20", " ")
    #         f2 = open(file_name_on_mac)  # 查文件存在不存在
    #
    #         # print(file_name_on_mac)
    #         # print(ver)
    #         # if cnt > 10:
    #         #     exit(0)
    #         cnt += 1
    #         # print(cnt)
    #
    #         for i in range(2, len(arr), 2):
    #             # print(arr[i], end=" ")
    #             if not topic_file_Map.__contains__(arr[i]):
    #                 topic_file_Map[arr[i]] = list()
    #             topic_file_Map[arr[i]].append(file_name_on_mac)
    #         # print()
    #     f_out = open("doc-topics-file.txt", 'w')
    #     print(len(topic_file_Map))
    #     file_num = 0
    #     for key in topic_file_Map.keys():
    #         f_out.write("-" + str(key) + "-")
    #         for item in topic_file_Map[key]:
    #             file_num += 1
    #             f_out.write("#@!" + item)
    #         f_out.write("\n")
    #     print(file_num)
    # ------------------------------------------------------------------------------
    # 处理docTopicsFile的结果
    # with open("docTopicsFile-right.txt") as f:
    #     f_out = open("/doc-topics-file-right.txt", 'w')
    #     for line in f:
    #         arr = line.split("\t")
    #         print(arr)
    #         for i in range(0, len(arr)):
    #             if i > 2 and arr[i].__contains__('E'):
    #                 f_out.write(str(eval(arr[i])) + " ")
    #                 print(str(eval(arr[i])))
    #             else:
    #                 f_out.write(arr[i].strip() + " ")
    #         f_out.write("\n")
    # ------------------------------------------------------------------------------
    # 处理5.5.0提取ground truth
    # entities_arc = set()
    # with open(
    #         "junit-5.5.0_188_topics_112_arc_clusters.rsf")as f:
    #     for line in f:
    #         entities_arc.add(line.split(" ")[2].strip())
    #         # print(line.split(" ")[2].strip())
    # entities_acdc = set()
    # with open(
    #         "cluster-5.5.0-no_test.rsf")as f:
    #     for line in f:
    #         entities_acdc.add(line.split(" ")[2].strip())
    #         # print(line.split(" ")[2].strip())
    #
    # print("Only ARC")
    # for item in entities_arc:
    #     if not entities_acdc.__contains__(item):
    #         print(item)
    #
    # print("Only ACDC")
    # for item in entities_acdc:
    #     if not entities_arc.__contains__(item):
    #         print(item)
    # ------------------------------------------------------------------------------
    # entities = set()
    # with open(
    #         "junit-5.4.2_deps-no_test.rsf")as f:
    #     for line in f:
    #         ar = line.split(" ")
    #         entities.add(ar[1].strip())
    #         entities.add(ar[2].strip())
    #
    # with open(
    #         "JUnit5.4.2-entities","w") as f:
    #     for item in entities:
    #         f.write(item + "\n")
    # ------------------------------------------------------------------------------
    # f_out = open("Bellerina-lang/java.txt", "w")
    # with open("project-materials/java.txt") as f:
    #     for l in f:
    #         if l.__contains__("src/main/java"):
    #             f_out.write(l)
    #
    # f_out = open("Bellerina-lang/class.txt", "w")
    # with open("project-materials/class.txt") as f:
    #     for l in f:
    #         if l.__contains__("build/classes/java/main") and not l.__contains__("$"):
    #             f_out.write(l)
    # # ------------------------------------------------------------------------------
    # j = list()
    # c = list()
    # with open("Bellerina-lang/java.txt") as f:
    #     for l in f:
    #         ar = l.split("/src/main/java/")
    #         j.append(ar[1].split(".")[0])
    #
    # with open("Bellerina-lang/class.txt") as f:
    #     for l in f:
    #         ar = l.split("build/classes/java/main/")
    #         c.append(ar[1].split(".")[0])
    #
    # diff = list()
    # for item in j:
    #     flag = False
    #     for i in c:
    #         if i == item:
    #             flag = True
    #             break
    #     if not flag:
    #         diff.append(item)
    #
    # for i in diff:
    #     print(i)
    # ------------------------------------------------------------------------------
    # prefix = list()
    # with open(
    #         "Ballerina-Lang_Project/newest-module-info.txt") as f:
    #     for line in f:
    #         if not line.__contains__("src/main/java/"):
    #             print(line)
    #             continue
    #         arr = line.split("src/main")
    #         prefix.append(arr[0])
    #
    #     for t1 in prefix:
    #         for t2 in prefix:
    #             if t1 == t2:
    #                 continue
    #             if t1.__contains__(t2):
    #                 print(t1, "---", t2)
    # ------------------------------------------------------------------------------
    # with open(
    #         "JDK_Project/module-111.txt") as f:
    #     for l in f:
    #         ar = l.split("module-info.java")
    #         # print("\"" + ar[0][2:].strip().replace("/", ".") + "\",")
    #         print(ar[0][2:].strip())
    # ------------------------------------------------------------------------------
    # with open(
    #         "ballerina-lang-newest_deps.rsf") as f:
    #     entity = set()
    #     for l in f:
    #         ar = l.split(" ")
    #         if not ar[1].__contains__("$"):
    #             entity.add(ar[1].strip())
    #         if not ar[2].__contains__("$"):
    #             entity.add(ar[2].strip())
    #     for e in entity:
    #         print(e)
    #
    # ------------------------------------------------------------------------------
    # export_map = dict()
    # cnt = 0
    # with open(
    #         "ground-truth/newest-clusters-path_from-folders_test-exclude.txt") as f:
    #     for module in f:
    #         module_name = ""
    #         # export_list = list()
    #         with open("ballerina-lang" + module.strip()[1:]) as module_f:
    #             for line in module_f:
    #                 if line.__contains__("module"):
    #                     module_name = line.strip().split("module")[1].split("{")[0].strip()
    #                     export_map[module_name] = list()
    #                     # print(module_name)
    #                 elif line.__contains__("exports") and not line.__contains__("//"):
    #                     export = line.strip().split("exports")[1].split(";")[0].strip()
    #                     # print(export)
    #                     # export_list.append(export)
    #                     export_map[module_name].append(export)
    #                     cnt += 1
    #                     # if  module_name.strip() == export.strip():
    #                     #     print("\"" + export.strip() + "\" to \""+module_name.strip() + "\",")
    #             # export_map[module_name] = export_list
    # ------------------------------------------------------------------------------
    # 处理JDK的相关信息（提取module-info）
    # with open(
    #          "ground-truth/newest-clusters-path_from-folders_test-exclude.txt"
    # ) as f:
    #     for line in f:
    #         with open("jdk-9+111/"
    #                   # "ballerina-lang"
    #                   + line.strip()[1:]
    #                   + "module-info.java"
    #                   ) as module_info:
    #             module = list()
    #             module_name = ""
    #             for l in module_info:
    #                 nl = l.strip()
    #                 if nl.__contains__("module"):
    #                     module_name = nl.strip().split("module")[1].split("{")[0].strip()
    #                 if not (nl.startswith("/*") or nl.startswith("*/") or nl.startswith(
    #                         "*") or nl == "" or nl.startswith("//")):
    #                     module.append(nl)
    #
    #         print(line.strip())
    #         print(module_name)
    #         for i in module:
    #             if not i.endswith("{") and not i.endswith(";") and not i.endswith("}"):
    #                 print(i, end=' ')
    #             else:
    #                 print(i)
    #         print("----------------------")
    # ------------------------------------------------------------------------------
    # with open(
    #         "ground-truth/111-module_rawinfo_test-exclude.txt"
    # ) as f:
    #     cnt = 0
    #     file_path, module_name = "", ""
    #     for l in f:
    #         if cnt == 0:
    #             file_path = l.strip()
    #             cnt += 1
    #         elif cnt == 1:
    #             cnt += 1
    #             module_name = l.strip()
    #             if module_name=='to':
    #                 print("\""+module_name+"\",")
    #         elif l.strip() == "----------------------":
    #             cnt = 0
    #         else:
    #             pass
    #             # ar = l.strip().split(" ")
    #             # if ar[0] == 'exports':
    #             #     if ar[1].__contains__(";"):
    #             #         print("\"" + ar[1].strip()[:-1] + "\" to \"" + module_name.strip() + "\",")
    #             #     else:
    #             #         print("\"" + ar[1].strip()+ "\" to \"" + module_name.strip() + "\",")
    #             # elif ar[0] == 'provides':
    #             #     print("\"" + ar[3].strip()[:-1] + "\" to \"" + module_name.strip() + "\",")
    # ------------------------------------------------------------------------------
    # with open(
    #         "Ballerina-Lang_Project/error_log-newest.rsf") as f:
    #     cnt = -1
    #     for l in f:
    #         cnt += 1
    #         if l.startswith('['):
    #             cnt = 0
    #             ar = l.split(" ")
    #             if l.__contains__("org.ballerinalang.test.expressions.builtinoperations.CloneOperationTest"):
    #                 if ar[2] == "org.ballerinalang.test.expressions.builtinoperations.CloneOperationTest":
    #                     print(
    #                         ar[1] + " "
    #                         + "tests.jballerina-bstring-unit-test.src.test.java.org.ballerinalang.test.expressions.builtinoperations.CloneOperationTest"
    #                         + " " + ar[3]
    #                     )
    #                 elif ar[3] == "org.ballerinalang.test.expressions.builtinoperations.CloneOperationTest":
    #                     print(
    #                         ar[1] + " "
    #                         + ar[2] + " "
    #                         + "tests.jballerina-bstring-unit-test.src.test.java.org.ballerinalang.test.expressions.builtinoperations.CloneOperationTest"
    #                     )
    #
    #         else:
    #             continue
    # ------------------------------------------------------------------------------
    # f_out = open(
    #     "fullDep_dotFile/full_entities-111.rsf"
    #     , "w"
    # )
    # with open(
    #         "fullDep_dotFile/full_entities-111-old.rsf") as f:
    #     n = 0
    #     for l in f:
    #         n += 1
    #         if n % 100:
    #             print(n)
    #         ar = l.split()
    #         from_en, to_en = "", ""
    #         if ar[1].startswith("."):
    #             from_en = ar[1].strip()[1:]
    #         else:
    #             from_en = ar[1].strip()
    #
    #         if ar[2].startswith("."):
    #             to_en = ar[2].strip()[1:]
    #         else:
    #             to_en = ar[2].strip()
    #         f_out.write("depends " + from_en + " " + to_en + "\n")
    # ------------------------------------------------------------------------------
    # 整理export
    # export_map = dict()
    # cnt = 0
    # with open(
    #         "ground-truth/preview5-module-expand_all.txt"
    # ) as f:
    #     module_name = ""
    #     for line in f:
    #         if line.strip().startswith("module "):
    #             module_name = line.strip().split("module")[1].split("{")[0].strip()
    #             export_map[module_name] = list()
    #             # print(module_name)
    #         elif line.__contains__("exports") and not line.__contains__("//"):
    #             export = line.strip().split("exports")[1].split(";")[0].strip()
    #             # print(export)
    #             export_map[module_name].append(export)
    #             cnt += 1
    #             print("\"" + export.strip() + "\" to \"" + module_name.strip() + "\",")
    # ------------------------------------------------------------------------------
    # # 找到module的绝对路径
    # with open(
    #         "ground-truth/preview5_module_tests-exclude.txt"
    # ) as f:
    #     for l in f:
    #         print(l[2:-18])
    # ------------------------------------------------------------------------------
    # path = list()
    # with open(
    #         "ground-truth/preview5_module_tests-exclude.txt"
    # ) as f:
    #     for l in f:
    #         path.append(l[2:-18])
    # export_map = dict()
    # cnt = -1
    # with open(
    #         "ground-truth/preview5-module-expand_all.txt"
    # ) as f:
    #     module_name = ""
    #     for line in f:
    #         if line.strip().startswith("module"):
    #             cnt += 1
    #             module_name = line.strip()[7:].split("{")[0].strip()
    #             print("\"" + module_name + "\",")
    #             # print("\"" + path[cnt].replace("/", ".") + "\"", "to", "\"" + module_name + "\",")
    # ------------------------------------------------------------------------------
    # entity = set()
    # with open(
    #         "full-path_preview4.rsf"
    # ) as f:
    #     for l in f:
    #         arr = l.split()
    #         entity.add(arr[1].strip())
    #         entity.add(arr[2].strip())
    #
    # with open(
    #         "ground-truth/preview4_entities.txt"
    #         , 'w'
    # ) as f:
    #     for en in entity:
    #         f.write(en + "\n")
    # ------------------------------------------------------------------------------
    # f_out = open(
    #     "out/base/docTopicsFile.txt"
    #     , 'w'
    # )
    # with open(
    #         "out/base/docTopicsFile-old.txt"
    # ) as f:
    #     for l in f:
    #         l1 = l.replace("ballerina-lang-2.0.0-Preview4/", "")
    #         l2 = l1.replace("ballerina-lang-2.0.0-Preview5/", "")
    #         f_out.write(l2)
    # ------------------------------------------------------------------------------
    # 把full path的转换成不是full path的
    # f_out = open(
    #     "ground-truth/preview5_ground-truth_clusters.rsf"
    #     , 'w'
    # )
    # with open(
    #         "ground-truth/preview5_ground-truth_clusters-full-path.rsf"
    # ) as f:
    #     for l in f:
    #         ar = l.split()
    #         if ar[2].__contains__("src.main.java"):
    #             f_out.write(ar[0] + " " + ar[1] + " " + ar[2].split("src.main.java.")[1]+"\n")
    #
    # ------------------------------------------------------------------------------
    # 过滤ACDC和ARC结果
    # f_out = open(
    #     "out-acdc/preview5-cluster-filter.rsf"
    #     , 'w'
    # )
    # with open("out-acdc/preview5-cluster.rsf") as f:
    #     for l in f:
    #         if not l.__contains__("$"):
    #             f_out.write(l)
    # ------------------------------------------------------------------------------
    # # 提取PKG prefix
    # # Find PKG prefix
    # pkg = set()
    # with open(
    #         "ballerina-lang-2.0.0-Preview4_deps.rsf") as f:
    #     for l in f:
    #         arr = l.split()
    #         # print(arr)
    #         if len(arr[1].split(".")) > 2:
    #             pkg.add(arr[1].split(".")[0] + "." + arr[1].split(".")[1])
    #         else:
    #             print("no pkg:" + arr[1])
    #         if len(arr[2].split(".")) > 2:
    #             pkg.add(arr[2].split(".")[0] + "." + arr[2].split(".")[1])
    #         else:
    #             print("no pkg:" + arr[2])
    # with open(
    #         "ballerina-lang-2.0.0-Preview5_deps.rsf") as f:
    #     for l in f:
    #         arr = l.split()
    #         # print(arr)
    #         if len(arr[1].split(".")) > 2:
    #             pkg.add(arr[1].split(".")[0] + "." + arr[1].split(".")[1])
    #         else:
    #             print("no pkg:" + arr[1])
    #         if len(arr[2].split(".")) > 2:
    #             pkg.add(arr[2].split(".")[0] + "." + arr[2].split(".")[1])
    #         else:
    #             print("no pkg:" + arr[2])
    #
    # for i in pkg:
    #     print(i, end=" ")
    # ------------------------------------------------------------------------------
    # construct the dependency relationship of modules as dotfile input
    # For ballerina!!!
    # module_map_pkg_exports = dict()
    # pkg_map_module = dict()
    # module_map_pkg_requires = dict()
    # module_transitive = dict()
    # with open(
    #         "ground-truth/preview5-module-expand_all.txt"
    # ) as f:
    #     for l in f:
    #         ls = l.strip()
    #         if ls.startswith("module"):
    #             module_name = l.split()[1]
    #             module_map_pkg_exports[module_name] = list()
    #             module_map_pkg_requires[module_name] = list()
    #         elif ls.startswith("/*") or ls.startswith("*") or len(ls) == 0 or ls.startswith("\\\\"):
    #             continue
    #         else:
    #             if ls.startswith("exports"):
    #                 pkg_name = l.split()[1]
    #                 module_map_pkg_exports[module_name].append(pkg_name[:-1])
    #                 pkg_map_module[pkg_name[:-1]] = module_name
    #             elif ls.startswith("requires"):
    #                 if ls.split()[1].strip() == "transitive":
    #                     if not module_transitive.keys().__contains__(module_name):
    #                         module_transitive[module_name] = list()
    #                     module_transitive[module_name].append(ls.split()[2][:-1])
    #                 else:
    #                     pkg_name = l.split()[1]
    #                     # print(pkg_name[:-1])
    #                     module_map_pkg_requires[module_name].append(pkg_name[:-1])
    #
    # dot_map = set()
    # for module in module_map_pkg_requires.keys():
    #     for pkg in module_map_pkg_requires[module]:
    #         if pkg_map_module.keys().__contains__(pkg):  # is pkg
    #             dot_map.add(module + "\t->\t" + pkg_map_module[pkg] + ";")
    #         elif module_map_pkg_exports.keys().__contains__(pkg):  # is module
    #             dot_map.add(module + "\t->\t" + pkg + ";")
    #
    # for i in dot_map:
    #     print(i.replace(".", "_"))
    # ------------------------------------------------------------------------------
    # construct the dependency relationship of modules as dotfile input
    # For JUnit
    module_map_pkg_exports = dict()
    pkg_map_module = dict()
    module_map_pkg_requires = dict()
    transitive = dict()
    with open(
            "junit5-5.5.0_moduleinfo-expand_all.txt"
    ) as f:
        for l in f:
            ls = l.strip()
            if ls.startswith("module"):
                module_name = l.split()[1]
                # print(module_name)
                module_map_pkg_exports[module_name] = list()
                module_map_pkg_requires[module_name] = list()
            elif ls.startswith("/*") or ls.startswith("*") or len(ls) == 0 or ls.startswith("\\\\"):
                continue
            else:
                if ls.startswith("exports"):
                    pkg_name = l.split()[1]
                    module_map_pkg_exports[module_name].append(pkg_name[:-1])
                    pkg_map_module[pkg_name[:-1]] = module_name
                elif ls.startswith("requires"):
                    if ls.split()[1].strip() == "transitive":
                        pkg_name = l.split()[2]
                        # print("requires transitive:",module_name,pkg_name)
                        module_map_pkg_requires[module_name].append(pkg_name[:-1])

                        if not transitive.keys().__contains__(module_name):
                            transitive[module_name] = list()
                        if pkg_name[:-1] != 'org.opentest4j' and pkg_name[:-1] != 'org.apiguardian.api' \
                                and pkg_name[:-1] != 'junit' and pkg_name[:-1] != 'org.assertj.core':
                            transitive[module_name].append(pkg_name[:-1])

                    else:
                        pkg_name = l.split()[1]
                        module_map_pkg_requires[module_name].append(pkg_name[:-1])

    dot_map = set()
    for module in module_map_pkg_requires.keys():
        for pkg in module_map_pkg_requires[module]:
            if pkg_map_module.keys().__contains__(pkg):  # is pkg
                dot_map.add(module + "\t->\t" + pkg_map_module[pkg] + ";")
            elif module_map_pkg_exports.keys().__contains__(pkg):  # is module
                dot_map.add(module + "\t->\t" + pkg + ";")

    # for i in dot_map:
    #      print(i.replace(".", "_"))
    select = set()
    zero_transitive = set()
    dep = transitive.copy()

    for i in transitive:
        if len(transitive[i]) == 0:
            zero_transitive.add(i)
        for j in transitive[i]:
            print(i.replace(".", "_") + "\t->\t" + j.replace(".", "_") + ";")
            # print(i, transitive[i])
