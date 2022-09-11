import json
from django.shortcuts import render
from modules.CipherManager.models import PropertiesList


def index(request):
    uploaded_files_header = ["Id", "Name", "File Name", "Path", "Type", "Actions"]
    records = PropertiesList.objects.all().values()
    return render(request, "CipherManager/index.html", {
        "uploaded_files_header": uploaded_files_header,
        "records": records
    })


def editProperties(request, id):
    if request.method == "POST":
        return
        # edit requested
    record = PropertiesList.objects.get(id=id)
    property_name = record.name
    property_filename = record.filename
    property_operator = record.type
    return render(request, "CipherManager/edit.html", {
        "name" : property_name,
        "filename": property_filename,
        "type": property_operator
    })


def handle_uploaded_file(f):
    with open('./modules/static/properties/'+f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
    return destination.name

def uploadPropertyForm(request):
    if request.method == 'POST':
        name = request.POST['name']
        ptype = request.POST['type']
        print(ptype)
        filename = request.FILES['file'].name
        path = handle_uploaded_file(request.FILES['file']) # path from masc core shall be added
        data = PropertiesList(name = name,type = ptype, filename = filename, path = path);
        data.save()
        return render(request,'CipherManager/thanks.html')
    list_of_operators = ["StringOperator","ByteOperator", "InterprocOperator", "Flexible", "IntOperator"]
    return render(request, "CipherManager/uploadProperty.html", {
        "list_of_operators": list_of_operators
    })
# Create your views here.
