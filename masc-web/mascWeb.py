from flask import Flask

app = Flask(__name__)

@app_route('/MASC-Web')
def MASC_web():
  return "<h1>MASC Home Page<h1>"

@app_route('/Deploy')
def deploy():
  return "<h1>Deploy Page<h1>"

@app_route('/Deploy')
def deploy():
  return "<h1><h1>"
