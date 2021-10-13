from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def MASC_web():
    return render_template("index.html")

@app.route('/deploy/')
def deploy():
    return "<h1>Deploy Page<h1>"

#@app.route('/Help')
#def deploy():
# return "<h1>Help<h1>"

#@app.route('/Lab/')
#def deploy():
#  return "<h1>About<h1>"

#@app.route('/About/')
#def deploy():
#  return "<h1>About<h1>"
