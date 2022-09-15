from asyncio import subprocess
from django.shortcuts import render

# Create your views here.
import asyncio

from modules.CipherManager.models import PropertiesList


async def run(cmd):
    print(cmd)
    proc = await asyncio.create_subprocess_shell(
        cmd,
        stdout=asyncio.subprocess.PIPE,
        stderr=asyncio.subprocess.PIPE)
    stdout, stderr = await proc.communicate()
    print(f'[{cmd!r} exited with {proc.returncode}]')
    if stdout:
        print(f'[stdout]\n{stdout.decode()}')
        return stdout.decode()
    if stderr:
        print(f'[stderr]\n{stderr.decode()}')
        return stderr.decode()

def index(request):
    # operator
    # make any.properties file
    return render(request, "masc-lab/lab.html", {
        "input_code": "NUll",
        "output_code": "Null"
    })


def read_selected_file(f):
    with open('./modules/static/properties/'+f, 'r') as destination:
        contents = destination.read()
    return contents


def get_final_output(paths):
    return paths

def read_logs():
    outputPaths = []
    with open('./MainScope.log', 'r') as destination:
        contents = destination.readlines()
    for line in contents:
        if "[OutputPath]" in line:
            x = line.split('#')
            outputPaths.append(x[len(x)-1])
    return outputPaths

def input_Form(request):
    print(request.method)
    if request.method == "POST":
        properties = request.POST['properties']
        fileinput = read_selected_file(properties)
        p = asyncio.run(
            run('java -jar ./modules/static/properties/app-all.jar '+properties))
        # read the output file
        input_code = fileinput
        output_code = read_logs()
        stdOut = p
        return render(request, "masc-lab/lab.html", {
            "input_code": input_code,
            "output_code": output_code,
            "stdOut": stdOut
        })
    list_of_operators = ["StringOperator","ByteOperator", "InterprocOperator", "Flexible", "IntOperator"]
    records = PropertiesList.objects.all().values()
    return render(request, "masc-lab/input-form.html",{
        "list_of_operators": list_of_operators,
        "properties_file": records
    })