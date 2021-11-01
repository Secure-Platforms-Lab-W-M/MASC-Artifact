# MASC Web

This is the web component of MASC. It is built using Flask in python.

## How to Run

To start create a virtual environment to work in on your personal machine. If
you are unsure how to do this refer to this page: https://docs.python.org/3/library/venv.html

Once you have created your virtual environment activate it. Once it is active
run the following command (replace <venv name> with the name of your virtual environment:
 
><venv name>/bin/python -m pip install -r requirements.txt  
 
This command installs the Flask Python libraries to run MASC Web.

Next navigate to the masc-web directory within your virtual environment. Run the following commands to activate the web page:

>export FLASK_APP=mascWeb  
>flask run  

  
If you plan to work on the file I would reccomend running the program in development
mode to enable the debugger. To do this run the following command prior to "flask run" :

>export FLASK_ENV=development  
>flask run  

For further information go to: https://flask.palletsprojects.com/en/2.0.x/quickstart/

On your personal machine open a web browser and go to "localhost:5000"
You should now be able to see MASC Web!
