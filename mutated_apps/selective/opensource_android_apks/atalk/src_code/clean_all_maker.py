pattern = "atalk-android-mutant"

for i in range(1, 29):
    current_folder = pattern + str(i)
    print("cd {} && chmod +x gradlew".format(current_folder))
    print("./gradlew clean")
    print("cd ..")
