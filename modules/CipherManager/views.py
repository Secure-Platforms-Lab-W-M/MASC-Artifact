import json
from django.shortcuts import render
from modules.CipherManager.models import PropertiesList


def index(request):
    uploaded_files_header = ["Id", "File Name", "Path", "Type"]
    records = PropertiesList.objects.all().values()
    print(records)
    return render(request, "CipherManager/index.html", {
        "uploaded_files_header": uploaded_files_header
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
        filename = request.FILES['file'].name
        path = handle_uploaded_file(request.FILES['file']) # path from masc core shall be added
        data = PropertiesList(name = name,type = ptype, filename = filename, path = path);
        data.save()
        return render(request,'CipherManager/thanks.html')
    return render(request, "CipherManager/uploadProperty.html")
# Create your views here.
