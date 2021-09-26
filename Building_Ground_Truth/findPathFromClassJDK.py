import os

project_path = "[TODO]"  # The root directory of the project

cache = dict()


def find(name):
    file_list = list()
    for root, dirs, files in os.walk(project_path):
        for f in files:
            full_path = os.path.join(str(root), str(f))
            if (full_path).__contains__(name + ".java"):
                if not full_path.__contains__(".class"):
                    if open(full_path):
                        file_list.append(full_path.split("jdk-9+110/")[1].split(".java")[0])
                        # file_list.append(full_path.split("jdk-9+111/")[1].split(".java")[0])

    return file_list


if __name__ == "__main__":

    cnt = 0
    f_err = open("[TODO]", "w")  # Error log file
    f_out = open("[TODO]", "w")  # Entity with full path
    with open("[TODO]") as f:  # Dependency file generated by CLASSYCLE
        for l in f:
            cnt += 1
            if cnt % 100 == 0:
                print(cnt)
            arr = l.split(" ")
            fr_en = arr[1]
            to_en = arr[2].strip()

            cls = l.strip()
            if fr_en not in cache.keys():
                file_list1 = find(fr_en.replace(".", "/"))
                if len(file_list1) == 1:
                    cache[fr_en] = file_list1[0].replace("/", ".")
                elif len(file_list1) == 0:
                    cache[fr_en] = fr_en
                else:
                    new_list = list()
                    if len(new_list) == 1:
                        cache[fr_en] = new_list[0].replace("/", ".")
                    else:
                        f_err.write("[ " + str(cls) + " ]\n")
                        for a in file_list1:
                            f_err.write(a + "\n")
                        f_err.write("\n")

            if to_en not in cache.keys():
                file_list2 = find(to_en.replace(".", "/"))
                if len(file_list2) == 1:
                    cache[to_en] = file_list2[0].replace("/", ".")
                elif len(file_list2) == 0:
                    cache[to_en] = to_en
                else:
                    new_list = list()
                    if len(new_list) == 1:
                        cache[to_en] = new_list[0].replace("/", ".")
                    else:
                        f_err.write("[ " + str(cls) + " ]\n")
                        for a in file_list2:
                            f_err.write(a + "\n")
                        f_err.write("\n")

            if cache.keys().__contains__(fr_en) and cache.keys().__contains__(to_en):
                f_out.write(arr[0] + " " + cache[fr_en] + " " + cache[to_en]+"\n")
