from flask import Flask

app = Flask(__name__)

@app_route('/')
def MASC_web():
  return "<h1>MASC Home Page<h1>"

@app_route('/Deploy')
def deploy():
  return "<h1>Deploy Page<h1>"

@app_route('/Help')
def deploy():
  return "<h1>Help<h1>"

@app_route('/Lab')
def deploy():
  return "<h1>About<h1>"

@app_route('/About')
def deploy():
  return "<h1>About<h1>"
