from django.shortcuts import render

from modules.CipherManager.models import PropertiesList
from modules.MascEngine.models import SourceCode
from zipfile import ZipFile

# Create your views here.

def read_selected_file(f):
    with open('./modules/static/properties/'+f, 'r') as destination:
        item = destination.read().split("\n")
    content = ''
    for line in item:
        if 'scope' in line.lower() or 'appsrc' in line.lower() or 'outputdir' in line.lower() or 'appName' in line.lower():
            continue
        else:
            content = content + line + '\n'
    return content


def handle_uploaded_file(f,app_name):
    with open('./modules/static/sourcecodes/'+f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
    with ZipFile('./modules/static/sourcecodes/'+f.name, 'r') as zipObj:
        zipObj.extractall('./modules/static/unzippedCodes/'+app_name)
    # data = SourceCode(zip_file_name=f.name, zip_file_directory='./modules/static/sourcecodes/'+f.name, input_path=filename, output_path=path, appName='');
    # data.save()
    return destination.name


def runMASCEngine(request):
    if request.method == 'POST':
        scopes = request.POST['scope']
        properties_file = request.POST['file_name']
        app_name = request.POST['appName']
        contents = request.POST['content']
        input_path = handle_uploaded_file(request.FILES['sourcecode'],app_name) # path from masc core shall be added
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
            "content" : contents,
        })
    scopes = ['Similarity', 'Exhaustive']
    records = PropertiesList.objects.all().values()
    return render(request, "masc-engine/engine.html", {
        "scopes": scopes,
        "properties_file": records
    })


# Create your views here.
