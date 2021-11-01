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
            uploaded_file.save('userFile.txt')
        f = open('userFile.txt', 'r')
        #subprocess.Popen("java mutation/src/mutation/Main", shell=True);
        subprocess.call(["java", "mutation/src/mutation/main.java", "arg1", "arg2", "arg3"])
        output = open('output.txt');
        return render_template("lab.html", text=f.read(), out=output.read())
    return render_template("lab.html")

#@app.route('/lab/userFile')
#def displayFile():
    #f = open('userFile.txt', 'r')
	#return render_template("lab.html", text=f.read())
    #return f.read()




@app.route('/about/')
def initAboutPage():
    return render_template("about.html")





#@app.route('/Help')
#def deploy():
# return "<h1>Help<h1>"

#@app.route('/Lab/')
#def deploy():
#  return "<h1>About<h1>"

#@app.route('/About/')
#def deploy():
#  return "<h1>About<h1>"
