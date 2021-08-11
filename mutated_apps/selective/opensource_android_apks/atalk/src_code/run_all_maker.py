path = "/Users/amitseal/workspaces/mutationbackyard/mutations/"
pattern = "atalk-android-mutant"

for i in range(3, 29):
    current_folder = pattern + str(i)
    print("cd {} && chmod +x gradlew".format(current_folder))
    print("./gradlew assembleDebug > log_android_atalk_{}.txt".format(current_folder))
    print("cd ..")
