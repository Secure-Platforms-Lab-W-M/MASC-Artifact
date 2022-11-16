import sys
import os
from asgiref.sync import sync_to_async
from django.http import HttpResponse
from django.shortcuts import render

from modules.CipherManager.models import PropertiesList
from modules.MascEngine.models import SourceCode
from modules.MascEngine.models import ProcessLog
from zipfile import ZipFile
from datetime import datetime
import asyncio
from shutil import make_archive
from wsgiref.util import FileWrapper
import time

# Create your views here.

import threading

class thread(threading.Thread):
    def __init__(self, app_name, build_properties_path,log_id):
        threading.Thread.__init__(self)
        self.app_name = app_name
        self.build_properties_path = build_properties_path
        self.log_id = log_id
        # helper function to execute the threads

    def run(self):
        print(str(self.app_name) + " " + str(self.build_properties_path))
        p, status_code = asyncio.run(
            run('java -jar D:/8th/spl/masc-web-client-django/MascWebCore/modules/static/properties/app-all.jar ' + self.build_properties_path))
        record =ProcessLog.objects.get(id=self.log_id)
        print('Hello =>'+record.status)
        if status_code == 0:
            record.status = 'completed'
        else:
            record.status = 'failed'
        record.save()
        sys.exit()
        print(self.is_alive)

async def run(cmd):
    print(cmd)
    proc = await asyncio.create_subprocess_shell(
        cmd,
        stdout=asyncio.subprocess.PIPE,
        stderr=asyncio.subprocess.PIPE)
    stdout, stderr = await proc.communicate()
    print(await proc.communicate())
    print(f'[{cmd!r} exited with {proc.returncode}]')
    utf = 'utf-8'
    status = 'ignore'
    if stdout:
        print(f'[stdout]\n{stdout.decode(utf,status)}')
        return stdout.decode(utf,status), proc.returncode
    if stderr:
        print(f'[stderr]\n{stderr.decode(utf,status)}')
        return stderr.decode(utf,status), proc.returncode


def read_selected_file(f):
    with open('./modules/static/properties/' + f, 'r') as destination:
        item = destination.read().split("\n")
    content = ''
    for line in item:
        if 'scope' in line.lower() or 'appsrc' in line.lower() or 'outputdir' in line.lower() or 'appname' in line.lower():
            continue
        else:
            content = content + line + '\n'
    return content


def handle_uploaded_file(f, app_name):
    with open('./modules/static/sourcecodes/' + f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
    with ZipFile('./modules/static/sourcecodes/' + f.name, 'r') as zipObj:
        zipObj.extractall('./modules/static/unzippedCodes/' + app_name)
    inputPath = 'D:/8th/spl/masc-web-client-django/MascWebCore/modules/static/unzippedCodes/' + app_name
    data = SourceCode(zip_file_name=f.name, zip_file_directory='./modules/static/sourcecodes/' + f.name,
                      input_path=inputPath, output_path='', appName=app_name);
    data.save()
    return data.id, inputPath


async def build_properties(app_name, scope, input_path, contents):
    initial = 'appName=' + app_name + '\n' + 'scope=' + scope + '\n' + 'appSrc=' + input_path + '\n' + 'outputDir=./app/outputs/'+app_name+'\n'
    contents = initial + contents
    with open('./modules/static/properties/' + app_name + '.properties', 'w') as destination:
        destination.write(contents)
    print(contents)
    return 'D:/8th/spl/masc-web-client-django/MascWebCore/modules/static/properties/' + app_name + '.properties'


def run_sub_process_masc_engine(build_properties_path, source_code_id, scope):
    source = SourceCode.objects.get(id=source_code_id)
    data = ProcessLog(properties=build_properties_path, scope=scope, status='running', source_code=source, start_time=datetime.now())
    data.save()
    threadMASC = thread(source.appName,build_properties_path,data.id)
    threadMASC.start()


def runMASCEngine(request):
    custome_operator_headers = ["Id", "Scope", "Properties", "App Name","Status","Actions"]
    if request.method == 'POST':
        scopes = request.POST['scope']
        properties_file = request.POST['file_name']
        app_name = request.POST['appName']
        contents = request.POST['content']
        source_code_id, input_path = handle_uploaded_file(request.FILES['sourcecode'], app_name)
        build_properties_path = asyncio.run(build_properties(app_name, scopes, input_path, contents))
        run_sub_process_masc_engine(build_properties_path,source_code_id,scopes)
    data = ProcessLog.objects.all().values()
    records = []
    for x in data:
        source = SourceCode.objects.get(id=x['source_code_id'])
        x['source_code'] = source
        arr = x['properties'].split('/')
        x['properties_name'] = arr[len(arr)-1]
        records.append(x)
    return render(request, "masc-engine/history.html", {
        "custome_operator_headers": custome_operator_headers,
        "records": records
    })


def index(request):
    if request.method == 'POST':
        scope = request.POST['scopes']
        properties = request.POST['properties']
        contents = read_selected_file(properties)
        return render(request, "masc-engine/engine-details.html", {
            "scope": scope,
            "filename": properties,
            "content": contents,
        })
    scopes = ['SELECTIVE', 'EXHAUSTIVE']
    records = PropertiesList.objects.all().values()
    return render(request, "masc-engine/engine.html", {
        "scopes": scopes,
        "properties_file": records
    })


def delete_uploaded_file(f):
    path = './app/outputs/'+f
    if os.path.isdir(path):
        os.system("rm -rf "+path)
    if os.path.isfile('./app/outputs/'+f+'./zip'):
        os.remove('./app/outputs/'+f+'./zip')


def delete_source_code(request,id,name):
    SourceCode.objects.get(id=id).delete()
    delete_uploaded_file(name)
    data = ProcessLog.objects.all().values()
    records = []
    for x in data:
        source = SourceCode.objects.get(id=x['source_code_id'])
        x['source_code'] = source
        arr = x['properties'].split('/')
        x['properties_name'] = arr[len(arr) - 1]
        records.append(x)
    custome_operator_headers = ["Id", "Scope", "Properties", "App Name","Status","Actions"]
    return render(request, "masc-engine/history.html", {
        "custome_operator_headers": custome_operator_headers,
        "records": records
    })


def download(request, app_name):
    files_path = "./app/outputs/"+app_name
    path_to_zip = make_archive(files_path, "zip", files_path)
    response = HttpResponse(FileWrapper(open(path_to_zip,'rb')), content_type='application/zip')
    response['Content-Disposition'] = 'attachment; filename="{filename}.zip"'.format(
        filename = app_name.replace(" ", "_")
    )
    return response
# Create your views here.

