import json
import os
from django.shortcuts import render
from modules.CipherManager.models import PropertiesList
from django.shortcuts import render, redirect
from django.http import HttpResponse

def index(request):
    uploaded_files_header = ["Id", "Name", "File Name", "Path", "Type", "Actions"]
    records = PropertiesList.objects.all().values()
    return render(request, "CipherManager/index.html", {
        "uploaded_files_header": uploaded_files_header,
        "records": records
    })


def editProperties(request, id):
    record = PropertiesList.objects.get(id=id)
    if request.method == "POST":
        filename = record.filename
        file_content = request.POST["content"]
        update_file_content(filename,file_content)
        return redirect(index)
        # edit requested
    property_name = record.name
    property_filename = record.filename
    property_operator = record.type
    content = read_data_from_uploaded_file(property_filename)
    return render(request, "CipherManager/edit.html", {
        "name" : property_name,
        "filename": property_filename,
        "type": property_operator,
        "content" : content
    })


def read_data_from_uploaded_file(f):
    with open('./modules/static/properties/'+f, 'r') as destination:
        contents = destination.read()
    return contents


def update_file_content(filename, content):
    arr = bytes(content, 'utf-8')
    with open('./modules/static/properties/'+filename, 'wb') as destination:
        destination.write(arr)


def handle_uploaded_file(f):
    with open('./modules/static/properties/'+f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
    return destination.name


def delete_uploaded_file(f):
    path = './modules/static/properties/'+f
    if os.path.isfile(path):
        os.remove(path)

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


def deleteProperties(request,id):
    record = PropertiesList.objects.get(id=id)
    delete_uploaded_file(record.filename)
    PropertiesList.objects.get(id=id).delete()
    return redirect(index)
