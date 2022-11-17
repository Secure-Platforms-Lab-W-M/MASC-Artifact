import os

from django.shortcuts import render, redirect

from modules.plugins.models import PluginsList
import asyncio


# Create your views here.
async def run(cmd):
    print(cmd)
    proc = await asyncio.create_subprocess_shell(
        cmd,
        stdout=asyncio.subprocess.PIPE,
        stderr=asyncio.subprocess.PIPE)
    stdout, stderr = await proc.communicate()
    print(f'[{cmd!r} exited with {proc.returncode}]')
    utf = 'utf-8'
    status = 'ignore'
    if stdout:
        # print(f'[stdout]\n{stdout.decode(utf,status)}')
        return proc.returncode
    if stderr:
        # print(f'[stderr]\n{stderr.decode(utf, status)}')
        return proc.returncode


def index(request):
    # operator
    # make any.properties file
    files_header = ["Name", "File Name", "Status", "Compilation", "Actions"]
    records = PluginsList.objects.all().values()
    return render(request, "plugins/index.html", {
        "headers": files_header,
        "records": records
    })


def handle_uploaded_file(f):
    with open('./plugins/' + f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
    return destination.name


def delete_uploaded_file(f,c):
    path = './plugins/' + f
    class_path = './plugins/' + c
    if os.path.isfile(path):
        os.remove(path)
    if os.path.isfile(class_path):
        os.remove(class_path)


def delete_plugin(request, id):
    record = PluginsList.objects.get(id=id)
    delete_uploaded_file(record.filename, record.classfile)
    PluginsList.objects.get(id=id).delete()
    return redirect(index)


def uploadPlugins(request):
    if request.method == 'POST':
        name = request.POST['name']
        filename = request.FILES['file'].name
        path = handle_uploaded_file(request.FILES['file'])  # path from masc core shall be added
        data = PluginsList(name=name, filename=filename, path=path);
        data.save()
        return render(request, 'plugins/thanks.html')
    # list_of_operators = ["StringOperator","ByteOperator", "InterprocOperator", "Flexible", "IntOperator"]
    return render(request, "plugins/upload.html")


def compile_class(request, id):
    record = PluginsList.objects.get(id=id)
    status_code = asyncio.run(
         run('javac -cp ./modules/static/properties/app-all.jar ./plugins/'+record.filename))
    print(status_code)
    if status_code == None:
        record.compilation = 'true'
        record.classfile = record.filename.split('.')[0]+'.class'
    record.save()
    return redirect(index)

