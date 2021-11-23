from flask import Flask, render_template, request, session, redirect, url_for
from os.path import exists
import subprocess

app = Flask(__name__)

@app.route('/')
def initMASC_web():
    return render_template("index.html")

@app.route('/deploy/')
def initDeployPage():
    return render_template("deploy.html")

@app.route('/help/')
def initHelpPage():
    return render_template("help.html")

@app.route('/lab/', methods=['GET', 'POST'])
def labPage():
    if request.method == 'POST':
        uploaded_file = request.files['file']
        if uploaded_file.filename != '':
            uploaded_file.save('input/userFile.java')
        f = open('input/userFile.java', 'r')
        createProperties("REACHABILITY", "hello")
        #subprocess.Popen("java mutation/src/mutation/Main", shell=True);
        e = subprocess.call(["java", "Users/scottmarsden/Documents/Github/csci435-Fall21-MASC/masc-core/app/src/main/java/masc/edu/wm/cs/masc/muse/Muse.java", "Users/scottmarsden/Documents/Github/CSci435-Fall21-MASC/masc-core/app/src/main/java/masc/edu/wm/cs/masc/muse/mdroid/prop.properties"])

        output = open('output/userFile.java')
        #formatOutput(output, False)
        #output = open('newOut.txt')
        return render_template("lab.html", text=f.read(), out=output.read(), error=e)
    return render_template("lab.html")

#@app.route('/lab/userFile')
#def displayFile():
    #f = open('userFile.txt', 'r')
	#return render_template("lab.html", text=f.read())
    #return f.read()




@app.route('/about/')
def initAboutPage():
    return render_template("about.html")


def formatOutput(file, userInput):
    doc = []
    for line in file:
        lines = line.split("/n")
        for newln in lines:
            doc.append(newln)
    newFile = open("newOut.txt","w")
    for line in doc:
        newFile.write(line)
        newFile.write("\n")
    file.close()
    newFile.close()

def createProperties(operator, mutation):
    doc = []
    doc.append("lib4ast: /Users/scottmarsden/Documents/Github/csci435-Fall21-MASC/masc-core/app/src/main/java/masc/edu/wm/cs/masc/muse/mdroid/")
    doc.append("appSrc: /Users/scottmarsden/Documents/Github/csci435-Fall21-MASC/masc-web/input")
    doc.append("operatorType: " + operator)
    # 4th mutator remove for now
    doc.append("appName: output")
    doc.append("output: /Users/scottmarsden/Documents/Github/csci435-Fall21-MASC/masc-web/")
    file = open("prop.properties","w")
    for line in doc:
        file.write(line)
        file.write("\n")
    file.close()
#@app.route('/Help')
#def deploy():
# return "<h1>Help<h1>"

#@app.route('/Lab/')
#def deploy():
#  return "<h1>About<h1>"

#@app.route('/About/')
#def deploy():
#  return "<h1>About<h1>"
