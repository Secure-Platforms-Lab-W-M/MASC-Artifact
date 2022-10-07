from django.shortcuts import render

from modules.CipherManager.models import PropertiesList
from modules.MascEngine.models import SourceCode
from zipfile import ZipFile


# Create your views here.

def read_selected_file(f):
    with open('./modules/static/properties/' + f, 'r') as destination:
        item = destination.read().split("\n")
    content = ''
    for line in item:
        if 'scope' in line.lower() or 'appsrc' in line.lower() or 'outputdir' in line.lower() or 'appname' in line.lower():
            continue
        else:
            content = content + line +'\n'
    return content


def handle_uploaded_file(f, app_name):
    with open('./modules/static/sourcecodes/' + f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
    with ZipFile('./modules/static/sourcecodes/' + f.name, 'r') as zipObj:
        zipObj.extractall('./modules/static/unzippedCodes/' + app_name)
    inputPath = './modules/static/unzippedCodes/' + app_name + f.name.split('.')[0]
    data = SourceCode(zip_file_name=f.name, zip_file_directory='./modules/static/sourcecodes/' + f.name,
                      input_path=inputPath, output_path='', appName=app_name);
    data.save()
    return data.id, inputPath


def build_properties(app_name, scope, input_path, contents):
    initial =  'appName=' + app_name + '\n' + 'scope=' + scope + '\n' + 'appSrc=' + input_path + '\n' + 'outputDir=./app/outputs\n'
    contents = initial + contents
    with open('./modules/static/properties/' + app_name + '.properties', 'w') as destination:
        destination.write(contents)
    print(contents)
    return './modules/static/properties/' + app_name + '.properties'


def runMASCEngine(request):
    if request.method == 'POST':
        scopes = request.POST['scope']
        properties_file = request.POST['file_name']
        app_name = request.POST['appName']
        contents = request.POST['content']
        source_code_id, input_path = handle_uploaded_file(request.FILES['sourcecode'], app_name)
        build_properties_path = build_properties(app_name, scopes, input_path, contents)
    custome_operator_headers = ["Uploaded File", "Selected Operator", "Status", "Actions"]
    return render(request, "masc-engine/history.html", {
        "custome_operator_headers": custome_operator_headers
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
    scopes = ['Similarity', 'Exhaustive']
    records = PropertiesList.objects.all().values()
    return render(request, "masc-engine/engine.html", {
        "scopes": scopes,
        "properties_file": records
    })

# Create your views here.
