from flask import Flask, render_template, request, session

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

@app.route('/lab/')
def initLabPage():
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
