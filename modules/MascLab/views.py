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
    if stderr:
        print(f'[stderr]\n{stderr.decode()}')

def index(request):
    # operator
    # make any.properties file 
    s = asyncio.run(run('node --version'))
    p = asyncio.run(run('java D:\8th\spl\masc\MASC-SFall2022\masc-core\\app\src\main\java\edu\wm\cs\masc\MASC.java cipher.properties'))
    # read the output file
    return render(request, "masc-lab/lab.html")