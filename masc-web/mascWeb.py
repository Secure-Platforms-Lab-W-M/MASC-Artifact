from flask import Flask, render_template, request, session
from os.path import exists

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

@app.route('/lab/', methods=['GET','POST'])
def labPage():
    if request.method == 'POST':
        file = request.files['file']
        file.save('userFile.java')

    if (exists('userFile.java')):
        with open('userFile.java', 'r') as userFile:
            return render_template("lab.html", output=f.userFile)
    else:
        return render_template("lab.html")

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
