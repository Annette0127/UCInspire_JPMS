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
                        file_list.append(full_path.split("ballerina-lang-2.0.0-Preview4/")[1].split(".java")[0])
    return file_list


def update_cache(entity, cls):
    if entity not in cache.keys():
        file_list = find(entity.replace(".", "/"))
        if len(file_list) == 1:
            cache[entity] = file_list[0].replace("/", ".")
        elif len(file_list) == 0:
            cache[entity] = entity
        else:
            new_list = list()
            if len(new_list) == 1:
                cache[entity] = new_list[0].replace("/", ".")
            else:
                f_err.write("[ " + str(cls) + " ]\n")
                for a in file_list:
                    f_err.write(a + "\n")
                f_err.write("\n")


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

            cls_main = l.strip()
            update_cache(fr_en, cls_main)
            update_cache(to_en, cls_main)

            if cache.keys().__contains__(fr_en) and cache.keys().__contains__(to_en):
                f_out.write(arr[0] + " " + cache[fr_en] + " " + cache[to_en] + "\n")
