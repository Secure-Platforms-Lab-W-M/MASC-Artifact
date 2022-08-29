from asyncio import subprocess
from django.shortcuts import render

# Create your views here.
import asyncio

async def run(cmd):
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

def input_Form(request):
    print(request.method)
    if request.method == "POST":
        print('her')
        s = asyncio.run(run('node --version'))
        p = asyncio.run(
            run('java D:\8th\spl\masc\MASC-SFall2022\masc-core\\app\src\main\java\edu\wm\cs\masc\MASC.java cipher.properties'))
        # read the output file
        input_code = "public static main"
        output_code = p
        return render(request, "masc-lab/lab.html", {
            "input_code": input_code,
            "output_code": output_code
        })
    list_of_operators = ["StringOperator","ByteOperator", "InterprocOperator", "Flexible", "IntOperator"]
    properties_file = ["a","b","upload"]
    return render(request, "masc-lab/input-form.html",{
        "list_of_operators": list_of_operators,
        "properties_file": properties_file
    })