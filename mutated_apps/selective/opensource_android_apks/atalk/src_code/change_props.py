pattern = "atalk-android-mutant"
log = "log_android_atalk_atalk-android-mutant"
for i in range(2,29):
    current_folder = pattern + str(i)
    current_log = log+str(i)+".txt"
    print("echo {}".format(current_log))
    print("cd {} && tail {}".format(current_folder, current_log))
    print("cd ..")
    print("echo '=========='")
